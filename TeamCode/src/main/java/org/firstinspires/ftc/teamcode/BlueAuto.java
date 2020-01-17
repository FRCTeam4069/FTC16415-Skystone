package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous
public class BlueAuto extends LinearOpMode {
    private ElapsedTime runtime = new ElapsedTime();

    private DcMotor left;
    private DcMotor right;
    private DcMotor elevator;
    private DcMotor hinge;
    private Servo leftHinge;
    private Servo rightHinge;

    private final double COUNTS_PER_INCH = (1120/(Math.PI*4));
    private final double RADIUS = 6.25;

    private final double ELEVATOR_COUNTS = ((1440)/(1.25*Math.PI))/2;

    private int leftTarget;
    private int rightTarget;

    private double speed;
    private int error;

    //private Servo latch;
    private double elevatorSpeed;

    private double leftHingeZero;
    private double rightHingeZero;

    private double latchZero;
    private boolean latchPos;
    private boolean drop;

    private int target;

    @Override
    public void runOpMode() {
        left = hardwareMap.get(DcMotor.class, "right_drive");
        right = hardwareMap.get(DcMotor.class, "left_drive");
        elevator = hardwareMap.get(DcMotor.class, "elevator");
        hinge = hardwareMap.get(DcMotor.class, "hinge");
        leftHinge = hardwareMap.get(Servo.class, "leftHinge");
        rightHinge = hardwareMap.get(Servo.class, "rightHinge");

        left.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        right.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        elevator.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        hinge.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        left.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        right.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        elevator.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        hinge.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        left.setDirection(DcMotorSimple.Direction.REVERSE);

        speed = 0.5;
        error = 0;

        leftTarget = 0;
        rightTarget = 0;

        elevatorSpeed = 0.5;

        //latchZero = latch.getPosition();
        latchPos = false;
        drop = false;

        leftHingeZero = leftHinge.getPosition();
        rightHingeZero = rightHinge.getPosition();

        target = 0;

        waitForStart();
        runtime.reset();

        telemetry.addData("Error", Integer.toString(error));
        telemetry.update();

        leftHinge.setPosition(0.725);
        rightHinge.setPosition(0.45);

        drive(3, left, right);
        moveHinge(200, hinge);

        turn(7.25, left, right);
        drive(24.19, left, right);
        turn(-7.10, left, right);

//        turn(-22.97, left, right);
//        drive(24.98, left, right);
//        turn(22.97, left, right);

        moveElevator(4, elevator);
        drive(8, left, right);
        moveHinge(450, hinge);
        //turn(-15, left, right);
        while(hinge.getCurrentPosition() < 400){
            telemetry.addData("Dropping", "...");
        }

        drive(-35, left, right);
        moveHinge(200, hinge);
        sleep(500);
        turn(-80, left, right);
        drive(24, left, right);
        turn(40, left, right);
        drive(10, left, right);
        turn(-45, left, right);
        drive(-15, left, right);
        moveElevator(0, elevator);
        drive(26, left, right);
        moveHinge(-10, hinge);
        //turn(-15, left, right);
        //drive(-12, left, right);
        //turn(-30, left, right);
        //drive(-18, left, right);
        //moveHinge(200, hinge);
        //drive(-30,left, right);
//        drive(-3, left, right);
//        drive(2, left, right);
//        //drive(, left, right);
//        turn(75, left, right);
//        drive(24, left, right);
//        moveElevator(0, elevator);
//        turn(-90, left, right);
//        drive(48, left, right);
//        turn(90, left, right);
//        drive(-36, left, right);
    }


    private void drive(double setpoint, DcMotor left, DcMotor right) {
        telemetry.addData("State", Integer.toString(error));
        telemetry.update();
        int target = (int)(setpoint * COUNTS_PER_INCH);

        leftTarget = leftTarget + target;
        rightTarget = rightTarget + target;

        left.setTargetPosition(leftTarget);
        right.setTargetPosition(rightTarget);

        left.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        right.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        left.setPower(speed);
        right.setPower(speed);

        while(left.isBusy() && right.isBusy()) {
            telemetry.addData("Error", Integer.toString(error));
            telemetry.update();
        }
    }

    private void turn(double rotation, DcMotor left, DcMotor right) {
        int target = (int) ((rotation / 180)*Math.PI*RADIUS*COUNTS_PER_INCH);

        leftTarget = leftTarget + target;
        rightTarget = rightTarget - target;

        left.setTargetPosition(leftTarget);
        right.setTargetPosition(rightTarget);

        left.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        right.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        left.setPower(speed);
        right.setPower(-speed);

        while(left.isBusy() && right.isBusy()) {
            telemetry.addData("Error", target-((left.getCurrentPosition()+right.getCurrentPosition())/2));
            telemetry.update();
        }
    }

    private void moveElevator(double pos, DcMotor elevator) {
        int target = (int)(pos*326);

        elevator.setTargetPosition(target);

        elevator.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        elevator.setPower(elevatorSpeed);

        while(elevator.isBusy()) {
            telemetry.addData("Elevator", "True");
            telemetry.update();
        }

    }

    private void moveHinge(int pos, DcMotor hinge) {
        hinge.setTargetPosition(pos);
        hinge.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        hinge.setPower(0.1);
        while(elevator.isBusy()){
            telemetry.addData("Hinge", "True");
            telemetry.update();
        }
        //hinge.setPower(0);
    }
}