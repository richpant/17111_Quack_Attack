/* Copyright (c) 2019 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import java.util.List;

/**
 * This 2022-2023 OpMode illustrates the basics of using the TensorFlow Object Detection API to
 * determine which image is being presented to the robot.
 *
 * Use Android Studio to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list.
 *
 * IMPORTANT: In order to use this OpMode, you need to obtain your own Vuforia license key as
 * is explained below.
 */
@Autonomous(name = "Camera Left")

public class Camera_Left extends LinearOpMode {

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
    /*
     * Specify the source for the Tensor Flow Model.
     * If the TensorFlowLite object model is included in the Robot Controller App as an "asset",
     * the OpMode must to load it using loadModelFromAsset().  However, if a team generated model
     * has been downloaded to the Robot Controller's SD FLASH memory, it must to be loaded using loadModelFromFile()
     * Here we assume it's an Asset.    Also see method initTfod() below .
     */
    private static final String TFOD_MODEL_ASSET = "quackAttack.tflite";
    // private static final String TFOD_MODEL_FILE  = "/sdcard/FIRST/tflitemodels/CustomTeamModel.tflite";


    private static final String[] LABELS = {
            "sideOne",
            "sideTwo",
            "sideThree"
    };

    /*
     * IMPORTANT: You need to obtain your own license key to use Vuforia. The string below with which
     * 'parameters.vuforiaLicenseKey' is initialized is for illustration only, and will not function.
     * A Vuforia 'Development' license key, can be obtained free of charge from the Vuforia developer
     * web site at https://developer.vuforia.com/license-manager.
     *
     * Vuforia license keys are always 380 characters long, and look as if they contain mostly
     * random data. As an example, here is a example of a fragment of a valid key:
     *      ... yIgIzTqZ4mWjk9wd3cZO9T1axEqzuhxoGlfOOI2dRzKS4T0hQ8kT ...
     * Once you've obtained a license key, copy the string from the Vuforia web site
     * and paste it in to your code on the next line, between the double quotes.
     */
    private static final String VUFORIA_KEY =
            "ATiCJRj/////AAABmUxnHHaXE0g3vqNekSp4n78Yc9HG5hvFRciORMSWK5TPVbCjSgtGh2uF6Af2gdKSYMRnSS1l/OAly1/MH1DNEwFRxH17CiFD4Y/9+l6vyaBKYKgmBbX5F0ssFRNVMGayAhs7f3dwzELymrTYxmbNLORCCLEMzIyrCM2H45xaM8DlOyRegC4WKBHCSmmn8eqQPJ40R29T4b9xnz3DmctDqsOsISuWACTrmSo6tgPbEGd2PqEFaoHATFXf81vRi/QgleIxacpCyw+RFmWsoXXeB2Vq9mwPjWOGsSAOQGQCk95sOzCnAoUSpsWX+Ge16vRVlpVK06TmRCxRA0e/QjuOCA0b+dwCVZEsm+dxtkMJUr1g";

    /**
     * {@link #vuforia} is the variable we will use to store our instance of the Vuforia
     * localization engine.
     */
    private VuforiaLocalizer vuforia;

    /**
     * {@link #tfod} is the variable we will use to store our instance of the TensorFlow Object
     * Detection engine.
     */
    private TFObjectDetector tfod;

    @Override
    public void runOpMode() {
        int zone = 2;
        int tree3 = 100000000; // tree(3) > 100000000
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
        // The TFObjectDetector uses the camera frames from the VuforiaLocalizer, so we create that
        // first.
        initVuforia();
        initTfod();

        /**
         * Activate TensorFlow Object Detection before we wait for the start command.
         * Do it here so that the Camera Stream window will have the TensorFlow annotations visible.
         **/
        if (tfod != null) {
            tfod.activate();

            // The TensorFlow software will scale the input images from the camera to a lower resolution.
            // This can result in lower detection accuracy at longer distances (> 55cm or 22").
            // If your target is at distance greater than 50 cm (20") you can increase the magnification value
            // to artificially zoom in to the center of image.  For best results, the "aspectRatio" argument
            // should be set to the value of the images used to create the TensorFlow Object Detection model
            // (typically 16/9).
            tfod.setZoom(1.0, 16.0/9.0);
        }

        /** Wait for the game to begin */
        telemetry.addData(">", "Press Play to start op mode");
        telemetry.update();
        waitForStart();


        if (opModeIsActive()) {
            while (opModeIsActive()) {
                if (tfod != null) {
                    // getUpdatedRecognitions() will return null if no new information is available since
                    // the last time that call was made.
                    List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
                    if (updatedRecognitions != null) {
                        telemetry.addData("# Objects Detected", updatedRecognitions.size());

                        // step through the list of recognitions and display image position/size information for each one
                        // Note: "Image number" refers to the randomized image orientation/number
                        for (Recognition recognition : updatedRecognitions) {
                            double col = (recognition.getLeft() + recognition.getRight()) / 2 ;
                            double row = (recognition.getTop()  + recognition.getBottom()) / 2 ;
                            double width  = Math.abs(recognition.getRight() - recognition.getLeft()) ;
                            double height = Math.abs(recognition.getTop()  - recognition.getBottom()) ;

                            telemetry.addData(""," ");
                            telemetry.addData("Image", "%s (%.0f %% Conf.)", recognition.getLabel(), recognition.getConfidence() * 100 );
                            telemetry.addData("- Position (Row/Col)","%.0f / %.0f", row, col);
                            telemetry.addData("- Size (Width/Height)","%.0f / %.0f", width, height);

                            //Shawn's code
                            if (recognition.getLabel().equals("sideOne")) {
                                zone = 1;
                                telemetry.addData("Side Detected", "sideOne");
                            }
                            if (recognition.getLabel().equals("sideTwo")) {
                                zone = 2;
                                telemetry.addData("Side Detected", "sideTwo");
                            }

                            if (recognition.getLabel().equals("sideThree")) {
                                zone = 3;
                                telemetry.addData("Side Detected", "sideThree");
                            }
                        }
                        telemetry.update();
                    }

                    if(getRuntime() > 3) {
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
                        move(400, 2);
                        Claw(.65);
                        sleep(200);
                        move(-400, 2);
                        move(-1050, 0);
                        TurretPosition(5);
                        ExtendPosition(5);
                        sleep(6000);

                        if (zone == 1) {
                            move(1300, 1);
                        }
                        if (zone == 2) {
                            move(100, 1);
                        }
                        if (zone == 3) {
                            move(-1200, 1);
                        }
                        sleep(tree3);
                    }
                }
            }
        }
    }

    /**
     * Initialize the Vuforia localization engine.
     */
    private void initVuforia() {
        /*
         * Configure Vuforia by creating a Parameter object, and passing it to the Vuforia engine.
         */
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraName = hardwareMap.get(WebcamName.class, "Webcam 1");

        //  Instantiate the Vuforia engine
        vuforia = ClassFactory.getInstance().createVuforia(parameters);
    }

    /**
     * Initialize the TensorFlow Object Detection engine.
     */
    private void initTfod() {
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
            "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfodParameters.minResultConfidence = 0.55f;
        tfodParameters.isModelTensorFlow2 = true;
        tfodParameters.inputSize = 300;
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);

        // Use loadModelFromAsset() if the TF Model is built in as an asset by Android Studio
        // Use loadModelFromFile() if you have downloaded a custom team model to the Robot Controller's FLASH.
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABELS);
        // tfod.loadModelFromFile(TFOD_MODEL_FILE, LABELS);
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
