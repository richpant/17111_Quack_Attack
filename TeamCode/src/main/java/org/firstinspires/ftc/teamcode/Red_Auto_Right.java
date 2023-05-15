package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@Disabled
@Autonomous(name = "Red Auto Right")
public class Red_Auto_Right extends LinearOpMode {
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
        turretLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        turretLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        turretRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        turretRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        turretExtend.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        turretExtend.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        turretLeft.setTargetPosition(0);
        turretRight.setTargetPosition(0);
        turretExtend.setTargetPosition(0);
        clawL.setPosition(.65);
        clawR.setPosition(.35);
        clawT.setPosition(.78);

        waitForStart();

        moveStraight(-.4, 6000);

        /*
        leftFront.setPower(y - x - s);
        leftRear.setPower(y + x - s);
        rightFront.setPower(y + x + s);
        rightRear.setPower(y - x + s);
         */
    }

    public void moveStraight(double p, int d) {
        leftFront.setPower(p);
        leftRear.setPower(p);
        rightFront.setPower(p);
        rightRear.setPower(p);
        sleep(d);
        leftFront.setPower(0);
        leftRear.setPower(0);
        rightFront.setPower(0);
        rightRear.setPower(0);
    }
}
