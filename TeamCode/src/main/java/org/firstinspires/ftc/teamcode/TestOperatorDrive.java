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
    private Servo leftHinge;
    private Servo rightHinge;

//1440*n*2.5=942
//    0.262
//    0.227
//    0.212
//    0.204
//    0.226
//    private final double ELEVATOR_COUNTS = ((1440*1)/(1.25*Math.PI))/2;//377
//    private final int ELEVATOR_POSITION_0 = 0;//(int)Math.round(ELEVATOR_COUNTS*0);
//    private final int ELEVATOR_POSITION_1 = 942;//(int)Math.round(ELEVATOR_COUNTS*2.5);
//    private final int ELEVATOR_POSITION_2 = 2047;//(int)Math.round(ELEVATOR_COUNTS*6.25);
//    private final int ELEVATOR_POSITION_3 = 3125;//(int)Math.round(ELEVATOR_COUNTS*10.25);
//    private final int ELEVATOR_POSITION_4 = 4183;//(int)Math.round(ELEVATOR_COUNTS*14.25);
//

    private double latchZero;
    private double hingeLeftZero;
    private double hingeRightZero;
    private boolean latchPos;
    private boolean hingeLeftPos;
    private boolean hingeRightPos;

    private boolean savedPosHinge;
    private boolean oldButtonHinge;
    private boolean oldButtonLatch;
    private boolean oldButtonHingeLeft;
    private boolean oldButtonHingeRight;
    private boolean oldCapStone;

    @Override
    public void init() {
        left = hardwareMap.get(DcMotor.class, "left_drive");
        right = hardwareMap.get(DcMotor.class, "right_drive");
        elevator = hardwareMap.get(DcMotor.class, "elevator");
        hinge = hardwareMap.get(DcMotor.class, "hinge");
        leftHinge = hardwareMap.get(Servo.class, "leftHinge");
        rightHinge = hardwareMap.get(Servo.class, "rightHinge");
        latch = hardwareMap.get(Servo.class, "latch");
        right.setDirection(DcMotorSimple.Direction.REVERSE);

        hinge.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        hinge.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        left.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        right.setMode((DcMotor.RunMode.STOP_AND_RESET_ENCODER));

        latchZero = latch.getPosition();
        hingeLeftZero = leftHinge.getPosition();
        hingeRightZero = rightHinge.getPosition();
        latchPos = false;
        hingeLeftPos = false;
        hingeRightPos = false;

        savedPosHinge = false;
        oldButtonHinge = false;
        oldButtonLatch = false;
        oldButtonHingeLeft = false;
        oldButtonHingeRight = false;
        oldCapStone = false;
    }

    @Override
    public void loop() {
        double speed = gamepad1.right_trigger - gamepad1.left_trigger;
        double turn = gamepad1.left_stick_x;
        //double elevatorUp = gamepad1.dpad_up ? 0.5 : 0;
        //double elevatorDown = gamepad1.dpad_down ? -0.5 : 0;
        double elevatorSpeed = gamepad1.right_stick_y;
        boolean buttonHingeLeft = gamepad2.left_bumper;
        boolean buttonHingeRight = gamepad2.right_bumper;
        boolean buttonHinge = gamepad2.a;
        boolean buttonLatch = gamepad2.x;
        boolean buttonLatchCapStone = gamepad2.b;

        double axleDifference = left.getCurrentPosition() - right.getCurrentPosition();

        if(buttonHinge && !oldButtonHinge) {
            savedPosHinge = !savedPosHinge;
            leftHinge.setPosition(1);
            rightHinge.setPosition(0.2);

            hinge.setTargetPosition(savedPosHinge ? -400 : 0);

            hinge.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            hinge.setPower(-0.1);
        }

        if(!hinge.isBusy()){
            hinge.setPower(0);
        }

        if(buttonLatch && !oldButtonLatch) {
            latch.setPosition(latchZero+(latchPos ? 0.7 : 0));
            latchPos = !latchPos;
        }

        if(buttonLatchCapStone && !oldCapStone) {
           latch.setPosition(latchZero+(0.8));
        }

        if(buttonHingeLeft && !oldButtonHingeLeft) {
            leftHinge.setPosition(hingeLeftPos ? 0.45 : 1);
            hingeLeftPos = !hingeLeftPos;
        }

        if(buttonHingeRight && !oldButtonHingeRight) {
            rightHinge.setPosition(hingeRightPos ? 0.7: 0.2);//0.4 : 0);
            hingeRightPos = !hingeRightPos;
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

        elevator.setPower(elevatorSpeed/-2);

        oldButtonLatch = buttonLatch;
        oldButtonHinge = buttonHinge;
        oldButtonHingeLeft = buttonHingeLeft;
        oldButtonHingeRight = buttonHingeRight;
        oldCapStone = buttonLatchCapStone;
    }
}
