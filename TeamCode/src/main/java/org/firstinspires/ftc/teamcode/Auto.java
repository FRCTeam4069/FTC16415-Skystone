package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;

@TeleOp(name="Autonomous")
public class Auto extends LinearOpMode {

    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor left;
    private DcMotor right;
    private final double COUNTS_PER_INCH = (1440/(3.1415*4));
    private final double SPEED = 0.5;
    private final double RADIUS = 6.5;
    private int error = 0;

    @Override
    public void runOpMode() {

        left = hardwareMap.get(DcMotor.class, "right_drive");
        right = hardwareMap.get(DcMotor.class, "left_drive");

        left.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        right.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        left.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        right.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        right.setDirection(DcMotorSimple.Direction.REVERSE);

        waitForStart();
        runtime.reset();

        error = turn(90.0, error, left, right);
        }


    private int drive(double setpoint, int error, DcMotor left, DcMotor right) {
        int target = (int)(setpoint * COUNTS_PER_INCH) + error;

        left.setTargetPosition(target);
        right.setTargetPosition(target);
        while(left.isBusy() && right.isBusy()) {
            left.setTargetPosition(target);
            right.setTargetPosition(target);

            left.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            right.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            left.setPower(SPEED);
            right.setPower(SPEED);
        }

        return target-((left.getCurrentPosition()+right.getCurrentPosition())/2);
    }

    private int turn(double rotation, int error, DcMotor left, DcMotor right) {
        double amount_to_move = (rotation / 180)*3.1416*RADIUS;

        int target = (int) (amount_to_move * COUNTS_PER_INCH);

        left.setTargetPosition(target);
        right.setTargetPosition(-target);

        left.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        right.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        left.setPower(SPEED);
        right.setPower(-SPEED);

        while(left.isBusy() && right.isBusy()) {
            left.setTargetPosition(target);
            right.setTargetPosition(-target);

            left.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            right.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            left.setPower(SPEED);
            right.setPower(-SPEED);

        }
        return target-((left.getCurrentPosition()+right.getCurrentPosition())/2);
    }

}