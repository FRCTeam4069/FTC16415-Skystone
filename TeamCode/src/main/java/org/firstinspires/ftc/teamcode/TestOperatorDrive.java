package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "TestOperatorDrive")
public class TestOperatorDrive extends OpMode {

    private DcMotor left;
    private DcMotor right;
    private DcMotor elevator;
    private Servo intake0;
    private Servo intake1;
    private Servo latch;

    private final double ELEVATOR_COUNTS = ((1440*1)/(1.25*Math.PI))/2;//377
    private final int ELEVATOR_POSITION_0 = 0;//(int)Math.round(ELEVATOR_COUNTS*0);
    private final int ELEVATOR_POSITION_1 = 942;//(int)Math.round(ELEVATOR_COUNTS*2.5);
    private final int ELEVATOR_POSITION_2 = 2047;//(int)Math.round(ELEVATOR_COUNTS*6.25);
    private final int ELEVATOR_POSITION_3 = 3125;//(int)Math.round(ELEVATOR_COUNTS*10.25);
    private final int ELEVATOR_POSITION_4 = 4183;//(int)Math.round(ELEVATOR_COUNTS*14.25);

    private double elevatorSpeed;

    private double latchZero;
    private boolean latchPos;
    private boolean drop;

    private boolean oldElevatorStage0;
    private boolean oldElevatorStage1;
    private boolean oldElevatorStage2;
    private boolean oldElevatorStage3;
    private boolean oldElevatorStage4;

    private int target;

    private boolean savedPosIntake0;
    private boolean oldButtonIntake0;
    private boolean savedPosIntake1;
    private boolean oldButtonIntake1;
    private boolean savedPosLatch;
    private boolean oldButtonLatch;

    @Override
    public void init() {
        left = hardwareMap.get(DcMotor.class, "left_drive");
        right = hardwareMap.get(DcMotor.class, "right_drive");
        elevator = hardwareMap.get(DcMotor.class, "elevator");
        //intake0 = hardwareMap.get(Servo.class, "intake0");
        //intake1 = hardwareMap.get(Servo.class, "intake1");
        latch = hardwareMap.get(Servo.class, "latch");
        right.setDirection(DcMotorSimple.Direction.REVERSE);

        elevator.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        elevator.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        left.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        right.setMode((DcMotor.RunMode.STOP_AND_RESET_ENCODER));

        elevatorSpeed = 0.5;

        latchZero = latch.getPosition();
        latchPos = false;
        drop = false;

        oldElevatorStage0 = false;
        oldElevatorStage1 = false;
        oldElevatorStage2 = false;
        oldElevatorStage3 = false;
        oldElevatorStage4 = false;
        target = 0;

        savedPosIntake0 = false;
        oldButtonIntake0 = false;
        savedPosIntake1 = false;
        oldButtonIntake1 = false;
        savedPosLatch = false;
        oldButtonLatch = false;
    }

    @Override
    public void loop() {
        double speed = gamepad1.right_trigger - gamepad1.left_trigger;
        double turn = gamepad1.left_stick_x;
        boolean buttonIntake0 = gamepad1.a;
        boolean buttonIntake1 = gamepad1.b;
        boolean buttonLatch = gamepad1.x;
        boolean elevatorStage0 = gamepad1.dpad_down;
        boolean elevatorStage1 = gamepad1.dpad_left;
        boolean elevatorStage2 = gamepad1.dpad_right;
        boolean elevatorStage3 = gamepad1.dpad_up;
        boolean elevatorStage4 = gamepad1.y;
        double elevatorManual = gamepad1.right_stick_y;
        double axleDifference = left.getCurrentPosition() - right.getCurrentPosition();

        telemetry.addData("elevator pos", elevator.getCurrentPosition());

        telemetry.addData("left stick pos", turn);

        if(elevatorStage0 && !oldElevatorStage0) target = ELEVATOR_POSITION_0;
        else if(elevatorStage1 && !oldElevatorStage1) target = ELEVATOR_POSITION_1;
        else if(elevatorStage2 && !oldElevatorStage2) target = ELEVATOR_POSITION_2;
        else if(elevatorStage3 && !oldElevatorStage3) target = ELEVATOR_POSITION_3;
        else if(elevatorStage4 && !oldElevatorStage4) target = ELEVATOR_POSITION_4;

//        if(elevatorManual != 0 && target != elevator.getCurrentPosition()) {
//            target = elevator.getCurrentPosition();
//        }
//        //elevator.setMode(DcMotor.RunMode.);
//        elevator.setPower(elevatorManual);

        if(buttonIntake0 && !oldButtonIntake0) {
            intake0.setPosition((savedPosIntake0) ? 0 : 0.25);
            savedPosIntake0 = !savedPosIntake0;
        }

        if(buttonIntake1 && !oldButtonIntake1) {
            intake1.setPosition((savedPosIntake1) ? 0 : -0.25);
            savedPosIntake1 = !savedPosIntake1;
        }

        if(buttonLatch && !oldButtonLatch) {
            drop = true;

            if(latchPos) {
                target = elevator.getCurrentPosition()-(int)ELEVATOR_COUNTS;
            }
            latchPos = !latchPos;
        }

        if(!elevator.isBusy()) {
            elevator.setTargetPosition(target);
            elevator.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            elevator.setPower(elevatorSpeed);
        }

        if(drop) {
            latch.setPosition(latchZero+(latchPos ? 0.5 : -0.5));
            drop = false;
        }

        if(turn != 0) {
            left.setPower(speed + turn);
            right.setPower(speed  - turn);
            left.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            right.setMode((DcMotor.RunMode.STOP_AND_RESET_ENCODER));
            left.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            right.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        }
        else if(speed != 0){
            left.setPower(speed - (axleDifference/100));
            right.setPower(speed  + (axleDifference/100));
        }
        else{
            left.setPower(0);
            right.setPower(0);
            left.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            right.setMode((DcMotor.RunMode.STOP_AND_RESET_ENCODER));
            left.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            right.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        }

        oldElevatorStage0 = elevatorStage0;
        oldElevatorStage1 = elevatorStage1;
        oldElevatorStage2 = elevatorStage2;
        oldElevatorStage3 = elevatorStage3;
        oldElevatorStage4 = elevatorStage4;
        oldButtonLatch = buttonLatch;
        oldButtonIntake0 = buttonIntake0;
        oldButtonIntake1 = buttonIntake1;
    }
}
