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
    private DcMotor hinge;
    private Servo latch;
//1440*n*2.5=942
//    0.262
//    0.227
//    0.212
//    0.204
//    0.226
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

    private boolean savedPosHinge;
    private boolean oldButtonHinge;
    private boolean oldButtonLatch;

    @Override
    public void init() {
        left = hardwareMap.get(DcMotor.class, "left_drive");
        right = hardwareMap.get(DcMotor.class, "right_drive");
        elevator = hardwareMap.get(DcMotor.class, "elevator");
        hinge = hardwareMap.get(DcMotor.class, "hinge");
        //intake1 = hardwareMap.get(Servo.class, "intake1");
        latch = hardwareMap.get(Servo.class, "latch");
        right.setDirection(DcMotorSimple.Direction.REVERSE);

        hinge.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        hinge.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

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

        savedPosHinge = false;
        oldButtonHinge = false;
        oldButtonLatch = false;
    }

    @Override
    public void loop() {
        double speed = gamepad1.right_trigger - gamepad1.left_trigger;
        double turn = gamepad1.left_stick_x;
        boolean buttonHinge = gamepad1.a;
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

        if(buttonHinge && !oldButtonHinge) {
            savedPosHinge = !savedPosHinge;

            if(savedPosHinge) {
                hinge.setTargetPosition(0);
            }
            else {
                hinge.setTargetPosition(-12600);
            }

            while(hinge.isBusy()) {
                hinge.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                hinge.setPower(0.1);
            }
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
        oldButtonHinge = buttonHinge;
    }
}
