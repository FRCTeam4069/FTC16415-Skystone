package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "move-y teleop")
public class OperatorDrive extends OpMode {

    private DcMotor left;
    private DcMotor right;

    @Override
    public void init() {
        left = hardwareMap.get(DcMotor.class, "right_drive");
        right = hardwareMap.get(DcMotor.class, "left_drive");

        left.setMode(DcMotor.RunMode.RESET_ENCODERS);
        right.setMode(DcMotor.RunMode.RESET_ENCODERS);

        right.setDirection(DcMotorSimple.Direction.REVERSE);


    }

    @Override
    public void loop() {
        int leftPosition = left.getCurrentPosition();
        int rightPosition = right.getCurrentPosition();

        double averagePosition = (leftPosition + rightPosition) / 2;
        double setpoint = 0;
        double error = setpoint - averagePosition;
        double kP = 0.01;

        left.setPower(error * kP);
        right.setPower(error * kP);

        telemetry.addData("Status", "Running");
        telemetry.addData("Left Motor Position", leftPosition);
        telemetry.addData("Right Motor Position", rightPosition);
        telemetry.update();
    }
}
