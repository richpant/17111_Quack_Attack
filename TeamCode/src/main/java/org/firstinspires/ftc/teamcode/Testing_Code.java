package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "Testing Code")
public class Testing_Code extends OpMode {
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
    public void init() {
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
        leftFront.setDirection(DcMotorSimple.Direction.FORWARD);
        leftRear.setDirection(DcMotorSimple.Direction.REVERSE);
        rightFront.setDirection(DcMotorSimple.Direction.FORWARD);
        rightRear.setDirection(DcMotorSimple.Direction.REVERSE);
        turretLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        turretLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        turretLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        turretRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        turretRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        turretExtend.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        turretExtend.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        turretLeft.setTargetPosition(0);
        turretRight.setTargetPosition(0);
        clawL.setPosition(.65);
        clawR.setPosition(.35);
        clawT.setPosition(.79);
        turretExtend.setTargetPosition(50);


        telemetry.addData("clawL", clawL.getPosition());
        telemetry.addData("clawR", clawR.getPosition());
        telemetry.addData("Say", "Hello Driver");
    }

    @Override
    public void loop() {

        double y = .6 * gamepad1.left_stick_y;
        double x = .6 * gamepad1.left_stick_x;
        double s = .6 * gamepad1.right_stick_x;
        if (gamepad1.left_trigger > .5) {
            y = .3 * gamepad1.left_stick_y;
            x = .3 * gamepad1.left_stick_x;
            s = .3 * gamepad1.right_stick_x;
        }

        turretLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        turretLeft.setPower(.6);
        turretRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        turretRight.setPower(.6);
        turretExtend.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        turretExtend.setPower(1);

        if (turretExtend.getCurrentPosition() < 50 || turretExtend.getTargetPosition() < 50) {
            turretExtend.setTargetPosition(50);
        }
        if (turretExtend.getCurrentPosition() > 3500 || turretExtend.getTargetPosition() > 3500) {
            turretExtend.setTargetPosition(3500);
        }

        leftFront.setPower(y - x - s);
        leftRear.setPower(y + x - s);
        rightFront.setPower(y + x + s);
        rightRear.setPower(y - x + s);


        if (gamepad1.dpad_up) {
            Turret(1);
        }
        if (gamepad1.dpad_down) {
            Turret(-1);
        }
        if (gamepad1.left_trigger > .1) {
            Extend(-1);
        }
        if (gamepad1.right_trigger >.1) {
            Extend(1);
        }
        //-------------HIGH--------------
        if (gamepad1.b) {
            TurretPosition(1910);
            ExtendPosition(3500);
        }
        //-------------MID---------------
        if (gamepad1.y) {
            TurretPosition(1550);
            ExtendPosition(50);
        }
        //-------------LOW---------------
        if (gamepad1.x) {
            TurretPosition(1020);
            ExtendPosition(50);
        }
        //------------RESET--------------
        if (gamepad1.a) {
            TurretPosition(5);
            ExtendPosition(50);
        }

        if (gamepad1.left_bumper) {
            clawL.setPosition(.25);
            clawR.setPosition(.75);
        }
        if (gamepad1.right_bumper) {
            clawL.setPosition(.65);
            clawR.setPosition(.35);
        }

        /*
        if (gamepad2.x && !gamepad2.y) {
            Tilt(.003);
        } else if (gamepad2.y && !gamepad2.x) {
            Tilt(-.003);
        } else if (gamepad2.x && gamepad2.y) {
            Tilt(0);
        } else {
            clawT.setPosition(.8 - (turretLeft.getCurrentPosition() * 0.00048));
        }
         */
        clawT.setPosition(.79 - (turretLeft.getCurrentPosition() * 0.00025324) - (gamepad2.left_stick_y / 4));

        telemetry.addData("turretLeft encoder", turretLeft.getCurrentPosition());
        telemetry.addData("turretLeft target", turretLeft.getTargetPosition());
        telemetry.addData("turretRight encoder", turretRight.getCurrentPosition());
        telemetry.addData("turretRight target", turretRight.getTargetPosition());
        telemetry.addData("turretExtend encoder", turretExtend.getCurrentPosition());
        telemetry.addData("turretExtend target", turretExtend.getTargetPosition());
        telemetry.addData("clawT position" , clawT.getPosition());
    }

    public void Turret(int s) {
        turretLeft.setTargetPosition(turretLeft.getCurrentPosition() + 114 * s);
        turretRight.setTargetPosition(turretLeft.getTargetPosition());
    }
    public void TurretPosition(int h) {
        turretLeft.setTargetPosition(h);
        turretRight.setTargetPosition(turretLeft.getTargetPosition());
    }
    public void Extend(int s) {
        turretExtend.setTargetPosition(turretExtend.getCurrentPosition() + 250 * s);
        if (turretExtend.getTargetPosition() < 50) {
            turretExtend.setTargetPosition(50);
        }
        if (turretExtend.getTargetPosition() > 3500) {
            turretExtend.setTargetPosition(3500);
        }
    }
    public  void ExtendPosition(int e) {
        turretExtend.setTargetPosition(e);
    }
    public void Tilt(double p) {
        clawT.setPosition(clawT.getPosition() + p);

    }
}
