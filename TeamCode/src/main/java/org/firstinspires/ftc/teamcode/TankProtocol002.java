package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp
public class TankProtocol002 extends Main002 {
    private double left_front_power = 0;
    private double left_back_power = 0;
    private double right_front_power = 0;
    private double right_back_power = 0;
    private double viper_base_power = 0;
    private double viper_rot_power = 0.5;
    double Finger_DPADUP_Count = 0;


    @Override
    public void runOpMode() {
        initMaths();
        initHardware();
        initManualModes();


        waitForStart();

        if (opModeIsActive()) {
            while (opModeIsActive()) {
                SetMotorForces();
                SetViperForces();
                SetServoForces();
                Viper_Functions();
                telemetry.update();

            }

            idle();
        }
    }


    public void SetMotorForces() {
        if (!NoNullHardware()) return;

        //Algorithm determined wheel forces with the inputs
        double yOneLeft = gamepad1.left_stick_y;


        //Right trigger right rotation
        //Right trigger is right rotation, left trigger is left. Can slow rotations by depressing trigger less.
        float rAxis = gamepad1.right_trigger - gamepad1.left_trigger;

        //find the speeds of each wheel
        left_front_power = math.GetWheelForceTank(yOneLeft, 1, rAxis);
        left_back_power = math.GetWheelForceTank(yOneLeft, 2, rAxis);
        right_front_power = math.GetWheelForceTank(yOneLeft, 3, rAxis);
        right_back_power = math.GetWheelForceTank(yOneLeft, 4, rAxis);


        //Show the powers on the telemetry screen
        telemetry.addData("y", gamepad1.left_stick_y);
        telemetry.addData("rot", gamepad1.right_trigger - gamepad1.left_trigger);
        telemetry.addData("lpb", left_back_power);
        telemetry.addData("lpf", left_front_power);
        telemetry.addData("rpb", right_back_power);
        telemetry.addData("rpf", right_front_power);

        telemetry.addData("Viper Extension", viper_base.getCurrentPosition());
        telemetry.addData("Viper Rotation", viper_rot.getCurrentPosition());


        //Set the velocities of each wheel motor
        left_front.setVelocity(-1 * left_front_power * MAX_NUM_TICKS_MOVEMENT * MOVEMENT_RPM);
        left_back.setVelocity(-1 * left_back_power * MAX_NUM_TICKS_MOVEMENT * MOVEMENT_RPM);
        right_front.setVelocity(right_front_power * MAX_NUM_TICKS_MOVEMENT * MOVEMENT_RPM);
        right_back.setVelocity(right_back_power * MAX_NUM_TICKS_MOVEMENT * MOVEMENT_RPM);

    }


    public void SetViperForces() {

        double yTwoLeft = gamepad2.left_stick_y;
        double yTwoRight = gamepad2.right_stick_y;

        viper_base_power = math.GetViperForce(yTwoLeft);
        viper_rot_power = math.GetViperRotation(yTwoRight);

        //Set the velocities for the viper slide components

        //viper_base.setVelocity(viper_base_power * MAX_NUM_TICKS_VIPER_BASE * VIPER_BASE_RPM);

        //Viper Rotation limits
        // If the viper rotation is in between a certain limit:
        if (viper_rot.getCurrentPosition() > -1500 && viper_rot.getCurrentPosition() < -50) {

            //automatically moves viper slide in relation two the y val of joystick
            viper_rot.setVelocity(viper_rot_power * MAX_NUM_TICKS_VIPER_BASE * VIPER_BASE_RPM * 0.1);

            /*if (viper_rot.getCurrentPosition() < -500 && viper_rot.getCurrentPosition() > -1500) {
                telemetry.addData("Viper slide slowed down", viper_rot.getVelocity());
                viper_rot.setVelocity((viper_rot_power * MAX_NUM_TICKS_VIPER_BASE * VIPER_BASE_RPM) * .005);
            }*/
            // Else statement referring to when the viper rotation is outside of the range
        } else if (viper_rot.getCurrentPosition() < -1500 || viper_rot.getCurrentPosition() > -50) {

            // Checking to see if it is too far up
            if (viper_rot.getCurrentPosition() < -1500) {
                // checks if the y val of joystick is continuing to attempt to bring it up
                if (yTwoRight <= 0) {
                    // sets velocity to 0
                    viper_rot.setVelocity(0);
                    telemetry.addData("Stop Motion", viper_rot.getVelocity());
                    // if the y val of joystick brings viper slide to the other direction
                } else {
                    // moves the viper slide
                    viper_rot.setVelocity(viper_rot_power * MAX_NUM_TICKS_VIPER_BASE * VIPER_BASE_RPM * 0.1);
                }
            }
            // Checking to see if it is too far down
            if (viper_rot.getCurrentPosition() > -50) {
                // checking to see if the y val of joystick is continuing to bring the viper slide down
                if (yTwoRight > 0.01) {
                    // sets velocity of viper slide to 0
                    viper_rot.setVelocity(0);
                    //moves the viper slide
                } else {
                    viper_rot.setVelocity(viper_rot_power * MAX_NUM_TICKS_VIPER_BASE * VIPER_BASE_RPM * 0.1);
                }
            }
            // otherwise the viper slide can move freely
        } else {
            viper_rot.setVelocity(viper_rot_power * MAX_NUM_TICKS_VIPER_BASE * VIPER_BASE_RPM * 0.1);
        }

        // -1500, 0
        telemetry.addData("Viper Velocity", viper_rot.getVelocity());

        if (viper_base.getCurrentPosition() > -3354 && viper_base.getCurrentPosition() < -400) {
            //When the viper slide is in the range of -3354 & -400 (inside range).
            viper_base.setVelocity(viper_base_power * MAX_NUM_TICKS_VIPER_BASE * VIPER_BASE_RPM);
        } else if (viper_base.getCurrentPosition() < -3354 || viper_base.getCurrentPosition() > -400) {
            //If the viper slide gets past the "limiter"
            if (viper_base.getCurrentPosition() < -3354) {
                //High limit is passed
                if (viper_rot.getCurrentPosition() > -100)
                    //Viper slide can only extend to max limit if viper slide not horizontal(42x20 RULE)
                    if (yTwoLeft < -0.1) {
                        //Can only contract after reaching limit
                        viper_base.setVelocity(0);
                    } else {
                        viper_base.setVelocity(viper_base_power * MAX_NUM_TICKS_VIPER_BASE * VIPER_BASE_RPM * .1);
                    }
                else {
                    if (viper_base.getCurrentPosition() < -4200) {
                        //Reaches max limit because viper slide is not horizontal
                        if (yTwoLeft < -0.1) {
                            //Can only extend in the direction away from the limit
                            viper_base.setVelocity(0);
                        } else {
                            viper_base.setVelocity(viper_base_power * MAX_NUM_TICKS_VIPER_BASE * VIPER_BASE_RPM * .1);
                        }
                    }
                }
            }
            if (viper_base.getCurrentPosition() > -400) {
                //Reaching limit where viper slide closing too far.
                if (yTwoLeft > 0.1) {
                    //Can only extend the viper slide
                    viper_base.setVelocity(0);
                } else {
                    viper_base.setVelocity(viper_base_power * MAX_NUM_TICKS_VIPER_BASE * VIPER_BASE_RPM * .1);
                }
            }
        } else {
            //Viper slide free from restrictions
            viper_base.setVelocity(viper_base_power * MAX_NUM_TICKS_VIPER_BASE * VIPER_BASE_RPM);
        }

    }


    public void SetServoForces() {
        if (gamepad2.a) {
            telemetry.addData("SHHHHHHHHHHHHHHHHH, u found me, ", 0110100.01101001);
        }
        wrist.setDirection(Servo.Direction.REVERSE);
        if (gamepad2.y) {
            wrist.setPosition(1);
        } else if (gamepad2.b) {
            wrist.setPosition(.5);
        } else if (gamepad2.a) {
            wrist.setPosition(0);
        }
        if (gamepad2.dpad_up) {
            Finger_DPADUP_Count += 1;
            sleep(250);
        }
        if (Finger_DPADUP_Count % 2 == 1) {
            fingers.setPosition(1);
            telemetry.addData("Open:", fingers.getPosition());
        } else {
            fingers.setPosition(.5);
            telemetry.addData("Closed:", fingers.getPosition());
        }
        telemetry.addData("DPADUP: ", Finger_DPADUP_Count);

        telemetry.addData("Wrist Position: ", wrist.getPosition());
        telemetry.addData("Fingers Position: ", fingers.getPosition());
    }


    public void Viper_Functions() {
        long sysTime;

        //brings out and sets servo to hover position
        if (gamepad2.right_bumper && viper_rot.getCurrentPosition() > -200) {
            while (viper_rot.getCurrentPosition() < -50 && opModeIsActive()) {
                while (viper_base.getCurrentPosition() < -400) {
                    viper_base.setPower(1);
                }
                viper_rot.setPower(0.5);
                SetMotorForces();
            }
            while (viper_base.getCurrentPosition() > -3000 && opModeIsActive()) {
                viper_base.setPower(-1);
                telemetry.addData("Viper_Base Ticks: ", viper_base.getCurrentPosition());
                telemetry.update();
                SetMotorForces();
                if (viper_base.getCurrentPosition() < -2800) {
                    wrist.setPosition(0.5);
                }
            }
        }

        //Rectracts viper slide and brings servo to hover position
        if (gamepad2.left_bumper) {
            telemetry.addData("THIS WORKS", fingers.getPosition());
            wrist.setPosition(0.5);
            while (viper_base.getCurrentPosition() < -400 && opModeIsActive()) {
                viper_base.setPower(1);
                SetMotorForces();
            }
        }

        //Brings viper_rot up and extends viper slide while also putting servo at hover position.
        if (gamepad2.right_trigger == 1) {
            while (viper_base.getCurrentPosition() < -400 && opModeIsActive()) {
                fingers.setPosition(1);
                wrist.setPosition(0.5);
                viper_base.setPower(1);
                SetMotorForces();
            }
            while (viper_rot.getCurrentPosition() > -1500 && opModeIsActive()) {
                viper_rot.setPower(-.5);
                SetMotorForces();
            }
            while (viper_base.getCurrentPosition() > -3300 && opModeIsActive()) {
                viper_base.setPower(-1);
                wrist.setPosition(0.5);
                SetMotorForces();
            }

            wrist.setPosition(0.5);
        }

        //Put sample in high basket, then bring servo back to hover. After that, retracts viper slide. Used after right trigger
        if (gamepad2.left_trigger == 1) {
            telemetry.addData("This is working! ", gamepad2.left_trigger);
            //sysTime = System.currentTimeMillis();
            fingers.setPosition(0.5);
            /*if (System.currentTimeMillis() == sysTime + 2500) {
                telemetry.addData("TRUE", sysTime);
                telemetry.addData("System   Time: ", System.currentTimeMillis());
            }*/
            sleep(1000);

            wrist.setPosition(0.5);
            while (viper_base.getCurrentPosition() < -600 && opModeIsActive()) {
                viper_base.setPower(1);
                SetMotorForces();
            }
            while (viper_rot.getCurrentPosition() < -50) {
                viper_rot.setPower(1);
            }
        }
    }
}
