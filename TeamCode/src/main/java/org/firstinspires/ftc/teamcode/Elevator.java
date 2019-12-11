package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "elevator tele-op")
public class Elevator extends OpMode {
    /*
    Motor counts: 1440
    Gearing: 10
    Gear diameter: 1.25
    Elevator multiplier: 2
    ((1440*10)/(1.25*pi))/2
     */
    private final double ELEVATOR_COUNTS = ((1440*1)/(1.25*Math.PI))/2;
    private final int ELEVATOR_POSITION_0 = (int)Math.round(ELEVATOR_COUNTS*0);
    private final int ELEVATOR_POSITION_1 = (int)Math.round(ELEVATOR_COUNTS*2.5);
    private final int ELEVATOR_POSITION_2 = (int)Math.round(ELEVATOR_COUNTS*6.25);
    private final int ELEVATOR_POSITION_3 = (int)Math.round(ELEVATOR_COUNTS*10.25);
    private final int ELEVATOR_POSITION_4 = (int)Math.round(ELEVATOR_COUNTS*14.25);

    private DcMotor elevator;
    private Servo latch;
    private double elevatorSpeed;

    private double latchZero;
    private boolean latchPos;
    private boolean drop;

    private boolean oldElevatorStage0;
    private boolean oldElevatorStage1;
    private boolean oldElevatorStage2;
    private boolean oldElevatorStage3;
    private boolean oldElevatorStage4;
    private boolean oldRelease;

    private int target;

    @Override
    public void init() {
        elevator = hardwareMap.get(DcMotor.class, "elevator");
        latch = hardwareMap.get(Servo.class, "latch");

        elevator.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        elevator.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        elevatorSpeed = 0.5;

        latchZero = latch.getPosition();
        latchPos = false;
        drop = false;

        oldElevatorStage0 = false;
        oldElevatorStage1 = false;
        oldElevatorStage2 = false;
        oldElevatorStage3 = false;
        oldElevatorStage4 = false;
        oldRelease = false;
        target = 0;
    }

    @Override
    public void loop() {
        boolean elevatorStage0 = gamepad1.dpad_down;
        boolean elevatorStage1 = gamepad1.dpad_left;
        boolean elevatorStage2 = gamepad1.dpad_right;
        boolean elevatorStage3 = gamepad1.dpad_up;
        boolean elevatorStage4 = gamepad1.y;
        boolean release = gamepad1.x;

        if(elevatorStage0 && !oldElevatorStage0) target = ELEVATOR_POSITION_0;
        else if(elevatorStage1 && !oldElevatorStage1) target = ELEVATOR_POSITION_1;
        else if(elevatorStage2 && !oldElevatorStage2) target = ELEVATOR_POSITION_2;
        else if(elevatorStage3 && !oldElevatorStage3) target = ELEVATOR_POSITION_3;
        else if(elevatorStage4 && !oldElevatorStage4) target = ELEVATOR_POSITION_4;

        if(release && !oldRelease) {
            drop = true;

            if(latchPos) {
                target = elevator.getCurrentPosition()-(int)ELEVATOR_COUNTS;
            }
            latchPos = !latchPos;
        }

        if(elevator.isBusy()) {
            elevator.setTargetPosition(target);
            elevator.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            elevator.setPower(elevatorSpeed);
        }
        
        if(drop) {
            latch.setPosition(latchZero+(latchPos ? 0.5 : -0.5));
            drop = false;
        }

        oldElevatorStage0 = elevatorStage0;
        oldElevatorStage1 = elevatorStage1;
        oldElevatorStage2 = elevatorStage2;
        oldElevatorStage3 = elevatorStage3;
        oldElevatorStage4 = elevatorStage4;
        oldRelease = release;
    }
}
