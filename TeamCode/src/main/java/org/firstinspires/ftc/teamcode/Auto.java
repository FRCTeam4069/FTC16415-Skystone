package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;

@TeleOp(name="Autonomous")
public class Auto extends LinearOpMode {
    private ElapsedTime runtime = new ElapsedTime();

    private DcMotor left;
    private DcMotor right;
    private DcMotor elevator;

    private final double COUNTS_PER_INCH = (1120/(Math.PI*4));
    private final double RADIUS = 6.25;

    private final double ELEVATOR_COUNTS = ((1440)/(1.25*Math.PI))/2;

    private final int ELEVATOR_POSITION_0 = (int)Math.round(ELEVATOR_COUNTS*0);
    private final int ELEVATOR_POSITION_1 = (int)Math.round(ELEVATOR_COUNTS*2.5);
    private final int ELEVATOR_POSITION_2 = (int)Math.round(ELEVATOR_COUNTS*6.25);
    private final int ELEVATOR_POSITION_3 = (int)Math.round(ELEVATOR_COUNTS*10.25);
    private final int ELEVATOR_POSITION_4 = (int)Math.round(ELEVATOR_COUNTS*14.25);

    private double speed;
    private int error;

    //private Servo latch;
    private double elevatorSpeed;

    private double latchZero;
    private boolean latchPos;
    private boolean drop;

    private int target;

    @Override
    public void runOpMode() {
        left = hardwareMap.get(DcMotor.class, "right_drive");
        right = hardwareMap.get(DcMotor.class, "left_drive");
        elevator = hardwareMap.get(DcMotor.class, "elevator");

        left.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        right.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        elevator.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        left.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        right.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        elevator.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        left.setDirection(DcMotorSimple.Direction.REVERSE);

        speed = 0.5;
        error = 0;

        elevatorSpeed = 0.5;

        //latchZero = latch.getPosition();
        latchPos = false;
        drop = false;

        target = 0;

        waitForStart();
        runtime.reset();

        telemetry.addData("Error", Integer.toString(error));
        telemetry.update();

        drive(3, error, left, right);
        turn(-72.47, error, left, right);
        // Move up to get above platform
        drive(39.85, error, left, right);
        //turn(72.47, error, left, right);
        //drive(12, error, left, right);
        // Move down to grab
        //drive(-16, error, left, right);
        // Lift up to drop
        //drive(-6, error, left, right);
        //turn(59.93, error, left, right);
//        drive(43.91, error, left, right);
//        turn(-30.07, error, left, right);
//        drive(3, error, left, right);
//        turn(-90, error, left, right);
//        drive(38, error, left, right);
//        turn(-90, error, left, right);
//        drive(24, error, left, right);
    }


    private void drive(double setpoint, int error, DcMotor left, DcMotor right) {
        telemetry.addData("State", Integer.toString(error));
        telemetry.update();
        int target = (int)(setpoint * COUNTS_PER_INCH);

        left.setTargetPosition(target);
        right.setTargetPosition(target);

        left.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        right.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        left.setPower(speed);
        right.setPower(speed);

        while(left.isBusy() && right.isBusy()) {
            telemetry.addData("Error", Integer.toString(error));
            telemetry.update();
        }

        error =  target-((left.getCurrentPosition()+right.getCurrentPosition())/2);
        left.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        right.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

    private void turn(double rotation, int error, DcMotor left, DcMotor right) {
        int target = (int) ((rotation / 180)*Math.PI*RADIUS*COUNTS_PER_INCH);

        left.setTargetPosition(target);
        right.setTargetPosition(-target);

        left.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        right.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        left.setPower(speed);
        right.setPower(-speed);

        while(left.isBusy() && right.isBusy()) {
            telemetry.addData("Error", target-((left.getCurrentPosition()+right.getCurrentPosition())/2));
            telemetry.update();
        }

        error = target-((left.getCurrentPosition()+right.getCurrentPosition())/2);
        left.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        right.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
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
}