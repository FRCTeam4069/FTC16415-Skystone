package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "OperatorDrive")
public class OperatorDrive extends OpMode {

    private DcMotor left;
    private DcMotor right;
    private DcMotor elevator;
    private DcMotor intakeHinge;
    private CRServo VexMotor0;
    private CRServo VexMotor1;
    private Servo latch;

    private boolean savedPosHinge;
    private boolean oldButtonHinge;
    private boolean savedPosIntake;
    private boolean oldButtonIntake;
    private boolean savedPosLatch;
    private boolean oldButtonLatch;

    @Override
    public void init() {
        left = hardwareMap.get(DcMotor.class, "left_drive");
        right = hardwareMap.get(DcMotor.class, "right_drive");
        elevator = hardwareMap.get(DcMotor.class, "elevator");
        intakeHinge = hardwareMap.get(DcMotor.class, "attachment");
        VexMotor0 = hardwareMap.get(CRServo.class, "VexMotor0");
        VexMotor1 = hardwareMap.get(CRServo.class, "VexMotor1");
        latch = hardwareMap.get(Servo.class, "latch");
        right.setDirection(DcMotorSimple.Direction.REVERSE);

        intakeHinge.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        intakeHinge.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        savedPosHinge = false;
        oldButtonHinge = false;
        savedPosIntake = false;
        oldButtonIntake = false;
        savedPosLatch = false;
        oldButtonLatch = false;
    }

    @Override
    public void loop() {
        double speed = gamepad1.right_trigger - gamepad1.left_trigger;
        double turn = gamepad1.left_stick_x;
        boolean buttonHinge = gamepad1.b;
        boolean buttonIntake = gamepad1.a;
        boolean buttonLacth = gamepad1.x;
        double elevatorSpeed = gamepad1.right_stick_y;

        telemetry.addData("elevator pos", elevator.getCurrentPosition());

        if(elevator.getCurrentPosition()>=0) {
            elevator.setPower(elevatorSpeed * -1);
        }
        else if(elevator.getCurrentPosition()<0 && elevatorSpeed<0) {
            elevator.setPower(elevatorSpeed * -1);
        }
        else{
            elevator.setPower(0);
        }

        if(buttonLacth && !oldButtonLatch) {
            savedPosLatch = !savedPosLatch;
            latch.setPosition((savedPosLatch) ? 0 : 0.5);
        }

        if(buttonIntake && !oldButtonIntake) {
            savedPosIntake = !savedPosIntake;
            VexMotor0.setPower((savedPosIntake) ? -0.75 : 0);
            VexMotor1.setPower((savedPosIntake) ? 0.75 : 0);
        }
        /*
        if(buttonHinge && !oldButtonHinge) {
            savedPosHinge = !savedPosHinge;

            if(savedPosHinge) {
                intakeHinge.setTargetPosition(0);
            }
            else {
                intakeHinge.setTargetPosition(-12600);
            }
            intakeHinge.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            intakeHinge.setPower(-0.1);
        }
        */
        left.setPower(speed + turn);
        right.setPower(speed - turn);

        oldButtonHinge = buttonHinge;
    }
}
