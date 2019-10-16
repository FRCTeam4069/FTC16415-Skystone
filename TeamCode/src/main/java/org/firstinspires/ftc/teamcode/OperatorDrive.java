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
    private Servo servo0;

    private boolean savedPos;
    private boolean oldButton;

    @Override
    public void init() {
        left = hardwareMap.get(DcMotor.class, "right_drive");
        right = hardwareMap.get(DcMotor.class, "left_drive");
        servo0 = hardwareMap.get(Servo.class, "Servo0");

        right.setDirection(DcMotorSimple.Direction.REVERSE);

        savedPos = false;
        oldButton = false;
    }

    @Override
    public void loop() {

        double speed = gamepad1.right_trigger - gamepad1.left_trigger;
        double turn = gamepad1.left_stick_x;
        boolean button = gamepad1.a;

        if(button && !oldButton) {
            savedPos = !savedPos;
            servo0.setPosition((savedPos) ? 1 : 0);
        }

        left.setPower(speed - turn);
        right.setPower(speed + turn);

        oldButton = button;
    }
}
