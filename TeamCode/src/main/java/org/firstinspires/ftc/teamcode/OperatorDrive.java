package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp(name = "move-y teleop")
public class OperatorDrive extends OpMode {

    private DcMotor left;
    private DcMotor right;

    @Override
    public void init() {
        left = hardwareMap.get(DcMotor.class, "right_drive");
        right = hardwareMap.get(DcMotor.class, "left_drive");

        right.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    @Override
    public void loop() {
        /*

        double ABCD = gamepad1.right_trigger;

        left.setPower(ABCD);
        right.setPower(ABCD);
        // left.setPower(ABCD);
        // double WXYZ = gamepad1.right_trigger;
        // right.setPower(WXYZ);
        */

        double speed = gamepad1.right_trigger - gamepad1.left_trigger;

        double left_stick = gamepad1.left_stick_x;


        if (left_stick != 0) {
            double turningRate = (speed + left_stick) / 2;


            if (turningRate > 0) {
                right.setPower(turningRate);
                left.setPower(turningRate * -1);
            } else {
                right.setPower(turningRate * -1);
                left.setPower(turningRate);
            }
        }
        else {
            left.setPower(speed);
            right.setPower(speed);
        }
    }
}
