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
    private final double COUNTS_PER_INCH = (1440/(3.1415*4));
    private final double SPEED = 0.5;
    private final double RADIUS = 6;
    private int error = 0;
    private final double ELEVATOR_COUNTS = ((1440*1)/(1.25*Math.PI))/2;

    private final int ELEVATOR_POSITION_0 = (int)Math.round(ELEVATOR_COUNTS*0);
    private final int ELEVATOR_POSITION_1 = (int)Math.round(ELEVATOR_COUNTS*2.5);
    private final int ELEVATOR_POSITION_2 = (int)Math.round(ELEVATOR_COUNTS*6.25);
    private final int ELEVATOR_POSITION_3 = (int)Math.round(ELEVATOR_COUNTS*10.25);
    private final int ELEVATOR_POSITION_4 = (int)Math.round(ELEVATOR_COUNTS*14.25);

    private Servo latch;
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

        right.setDirection(DcMotorSimple.Direction.REVERSE);

        elevatorSpeed = 0.5;

        latchZero = latch.getPosition();
        latchPos = false;
        drop = false;

        target = 0;

        waitForStart();
        runtime.reset();

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
    private void moveElevator(int pos, DcMotor elevator) {
        if(pos == 0) target = ELEVATOR_POSITION_0;
        else if(pos == 1) target = ELEVATOR_POSITION_1;
        else if(pos == 2) target = ELEVATOR_POSITION_2;
        else if(pos == 3) target = ELEVATOR_POSITION_3;
        else if(pos == 4) target = ELEVATOR_POSITION_4;


    }

}