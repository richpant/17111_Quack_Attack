package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;


@Autonomous(name = "Auto Right")
public class Auto_Right extends LinearOpMode {
    private DcMotor rightFront;
    private DcMotor rightRear;
    private DcMotor leftFront;
    private DcMotor leftRear;
    private DcMotor turretLeft;
    private DcMotor turretRight;
    private DcMotor turretExtend;
    private Servo clawL;
    private Servo clawR;
    private Servo clawT;

    @Override
    public void runOpMode() throws InterruptedException {
        leftFront = hardwareMap.get(DcMotor.class, "leftFront");
        leftRear = hardwareMap.get(DcMotor.class, "leftRear");
        rightFront = hardwareMap.get(DcMotor.class, "rightFront");
        rightRear = hardwareMap.get(DcMotor.class, "rightRear");
        turretLeft = hardwareMap.get(DcMotor.class, "turretLeft");
        turretRight = hardwareMap.get(DcMotor.class, "turretRight");
        turretExtend = hardwareMap.get(DcMotor.class, "turretExtend");
        clawL = hardwareMap.get(Servo.class, "clawL");
        clawR = hardwareMap.get(Servo.class, "clawR");
        clawT = hardwareMap.get(Servo.class, "clawT");
        leftFront.setDirection(DcMotorSimple.Direction.REVERSE);
        leftRear.setDirection(DcMotorSimple.Direction.FORWARD);
        rightFront.setDirection(DcMotorSimple.Direction.REVERSE);
        rightRear.setDirection(DcMotorSimple.Direction.FORWARD);
        turretLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        leftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftRear.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftRear.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightRear.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightRear.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        turretLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        turretLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        turretRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        turretRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        turretExtend.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        turretExtend.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftFront.setTargetPosition(0);
        leftRear.setTargetPosition(0);
        rightFront.setTargetPosition(0);
        rightRear.setTargetPosition(0);
        turretLeft.setTargetPosition(0);
        turretRight.setTargetPosition(0);
        turretExtend.setTargetPosition(50);
        clawL.setPosition(.65);
        clawR.setPosition(.35);
        clawT.setPosition(.78);

        waitForStart();

        turretLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        turretLeft.setPower(.3);
        turretRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        turretRight.setPower(.3);
        turretExtend.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        turretExtend.setPower(1);

        Claw(.25);
        sleep(300);
        TurretPosition(1910);
        ExtendPosition(3500);
        sleep(2000);
        move(2270, 0);
        move(-400,2);
        Claw(.65);
        sleep(200);
        move(400,2);
        move(-400,0);
        TurretPosition(5);
        ExtendPosition(50);
        sleep(4000);
        /*
        move(800,2);
        TurretPosition(500);
        ExtendPosition(50);
        move(300,1);
        move(400,0);
        Claw(.25);
         */

        /*
        leftFront.setPower(y - x - s);
        leftRear.setPower(y + x - s);
        rightFront.setPower(y + x + s);
        rightRear.setPower(y - x + s);
         */
    }

    public void move(int d, int mode) {
        leftFront.setPower(.4);
        leftRear.setPower(.4);
        rightFront.setPower(.4);
        rightRear.setPower(.4);
        /** mode is to determine if the robot is
         * driving straight, strafing, or turning
         * using 0, 1, or 2 respectively for each mode
         */
        if (mode == 0) {
            leftFront.setTargetPosition(d);
            leftRear.setTargetPosition(d);
            rightFront.setTargetPosition(d);
            rightRear.setTargetPosition(d);
        }
        if (mode == 1) {
            leftFront.setTargetPosition(-d);
            leftRear.setTargetPosition(d);
            rightFront.setTargetPosition(d);
            rightRear.setTargetPosition(-d);
        }
        if (mode == 2) {
            leftFront.setTargetPosition(d);
            leftRear.setTargetPosition(d);
            rightFront.setTargetPosition(-d);
            rightRear.setTargetPosition(-d);
        }
        leftFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftRear.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightRear.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        while ( leftRear.getCurrentPosition() != d) {
            sleep(10);
        }
        sleep(200);
        leftFront.setPower(0);
        leftRear.setPower(0);
        rightFront.setPower(0);
        rightRear.setPower(0);
        leftFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftRear.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightRear.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        sleep(200);
    }
    public void TurretPosition(int h) {
        turretLeft.setTargetPosition(h);
        turretRight.setTargetPosition(turretLeft.getTargetPosition());
        sleep(500);
        clawT.setPosition(.78 - (turretLeft.getTargetPosition() * 0.00025324));
    }
    public  void ExtendPosition(int e) {
        turretExtend.setTargetPosition(e);
    }
    public void Claw(double p) {
        clawL.setPosition(p);
        clawR.setPosition(1 - p);
    }
}
