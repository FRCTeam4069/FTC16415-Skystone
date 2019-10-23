package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import java.util.concurrent.CountDownLatch;

@TeleOp(name = "PID")
public class PID extends OpMode {

    private DcMotor left;
    private DcMotor right;

    private int oldErrLeft;
    private int oldErrRight;

    private final double COUNTS_PER_INCH = (1440/(3.1415*4));

    @Override
    public void init() {
        left = hardwareMap.get(DcMotor.class, "right_drive");
        right = hardwareMap.get(DcMotor.class, "left_drive");

        left.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        right.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        left.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        right.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        right.setDirection(DcMotorSimple.Direction.REVERSE);

    }

    @Override
    public void loop() {
        int leftPosition = left.getCurrentPosition();
        int rightPosition = right.getCurrentPosition();

        double setpoint = 24;

//        left.setTargetPosition((int) (setpoint * COUNTS_PER_INCH));
//        right.setTargetPosition((int) (setpoint * COUNTS_PER_INCH));
//
//        left.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//        right.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//
//        left.setPower(1);
//        right.setPower(1);

        turning90cw();

//        oldErrLeft = (int)(((13 * 3.1415) / 4) * COUNTS_PER_INCH) - leftPosition;
//        oldErrRight = -(int)(((13 * 3.1415) / 4) * COUNTS_PER_INCH) - rightPosition;

        telemetry.addData("Status", "Running");
        telemetry.addData("Left Motor Position", leftPosition);
        telemetry.addData("Right Motor Position", rightPosition);
        telemetry.update();
    }

    private void turning90cw() {

        left.setTargetPosition((int)(((13 * 3.1415) / 4) * COUNTS_PER_INCH));
        right.setTargetPosition(-(int)(((13 * 3.1415) / 4) * COUNTS_PER_INCH));

        left.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        right.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        left.setPower(0.5);
        right.setPower(-0.5);
    }
}
