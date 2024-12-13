package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

/*
 * This OpMode illustrates the concept of driving a path based on encoder counts.
 * The code is structured as a LinearOpMode
 *
 * The code REQUIRES that you DO have encoders on the wheels,
 *   otherwise you would use: RobotAutoDriveByTime;
 *
 *  This code ALSO requires that the drive Motors have been configured such that a positive
 *  power command moves them forward, and causes the encoders to count UP.
 *
 *
 *  The code is written using a method called: encoderDrive(speed, leftInches, rightInches, timeoutS)
 *  that performs the actual movement.
 * The code also has a method called : encoderTurn(speed, leftInches, rightInches, timeouts)
 * that performs the actual turns.
 *  This method assumes that each movement is relative to the last stopping place.
 *  There are other ways to perform encoder based moves, but this method is probably the simplest.
 *  This code uses the RUN_TO_POSITION mode to enable the Motor controllers to generate the run profile
 *
 * Use Android Studio to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this OpMode to the Driver Station OpMode list
 */
public class AutoFunctionsRhino extends Main002 {

    private ElapsedTime runtime = new ElapsedTime();
    public double COUNTS_PER_MOTOR_REV = 0;
    public  double DRIVE_GEAR_REDUCTION = 0;
    public  double WHEEL_DIAMETER_INCHES = 0;
    public  double COUNTS_PER_INCH = 0;
    public double COUNTS_PER_VIPERROT_ROTATION = 0;
    public double COUNTS_PER_DEGREE_ANGLE = 0;
    public double COUNTS_PER_VIPERBASE_ROTATION = 0;
    public double COUNTS_PER_INCH_EXTN = 0;
    public  double DRIVE_SPEED = 1;
    public double TURN_SPEED = 0.5;

    public int setParameters( String motorType,
                              double motorCountsPerRev,
                              double driveGrearReduction,
                              double wheelDiameter) {
        // Calculate the COUNTS_PER_INCH for your specific drive train.
        // Go to your motor vendor website to determine your motor's COUNTS_PER_MOTOR_REV
        // For external drive gearing, set DRIVE_GEAR_REDUCTION as needed.
        // For example, use a value of 2.0 for a 12-tooth spur gear driving a 24-tooth spur gear.
        // This is gearing DOWN for less speed and more torque.
        // For gearing UP, use a gear ratio less than 1.0. Note this will affect the direction of wheel rotation.
        // GoBuilda Motor Encoder 5203 Series RPM ??? xx:x ratio Series motor counts per rotation is 5281.1
        if (motorType.equals("WHEEL")) {
            COUNTS_PER_MOTOR_REV = motorCountsPerRev;
            DRIVE_GEAR_REDUCTION = driveGrearReduction;
            WHEEL_DIAMETER_INCHES = wheelDiameter;
            COUNTS_PER_INCH = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) / (WHEEL_DIAMETER_INCHES * 3.1415); // Pie value is 3.1415
        }
        if (motorType.equals("VIPERROT")) {
            COUNTS_PER_VIPERROT_ROTATION = motorCountsPerRev;
            DRIVE_GEAR_REDUCTION = driveGrearReduction;
            COUNTS_PER_DEGREE_ANGLE = ((COUNTS_PER_VIPERROT_ROTATION * DRIVE_GEAR_REDUCTION)/ 360); //
        }
        if (motorType.equals("VIPERBASE")) {
            COUNTS_PER_VIPERBASE_ROTATION = motorCountsPerRev;
            DRIVE_GEAR_REDUCTION = driveGrearReduction;
            COUNTS_PER_INCH_EXTN = ((COUNTS_PER_VIPERBASE_ROTATION * DRIVE_GEAR_REDUCTION) / 360);
        }

        return 0;
    }


    public void encoderDrive(double speed,
                             double leftInches, double rightInches,
                             double timeoutS) {
        int newLeftTargetF;
        int newRightTargetF;
        int newLeftTargetB;
        int newRightTargetB;

        // Ensure that the OpMode is still active
        if (opModeIsActive()) {

            // Changes mode
            initManualModes();

            // Determine new target position, and pass to motor controller
            newLeftTargetF = left_front.getCurrentPosition() + (int) (leftInches * COUNTS_PER_INCH);
            newRightTargetF = right_front.getCurrentPosition() + (int) (rightInches * COUNTS_PER_INCH);
            newLeftTargetB = left_back.getCurrentPosition() + (int) (leftInches * COUNTS_PER_INCH);
            newRightTargetB = right_back.getCurrentPosition() + (int) (rightInches * COUNTS_PER_INCH);


            left_front.setTargetPosition(newLeftTargetF);
            right_front.setTargetPosition(newRightTargetF);
            left_back.setTargetPosition(newLeftTargetB);
            right_back.setTargetPosition(newRightTargetB);

            // Turn On RUN_TO_POSITION
            runToPosition();

            // reset the timeout time and start motion.
            runtime.reset();

            // Set the initial Power of all the motors
            left_front.setPower(Math.abs(speed));
            right_front.setPower(Math.abs(speed));
            left_back.setPower(Math.abs(speed));
            right_back.setPower(Math.abs(speed));

            while (opModeIsActive() &&
                        (runtime.seconds() < timeoutS) &&
                        (left_front.isBusy() &&
                         right_front.isBusy() &&
                         left_back.isBusy() &&
                         right_back.isBusy()
                        )
                  )
            {
                // keep looping while we are still active, and there is time left, and both motors are running.
                // Note: We use (isBusy() && isBusy()) in the loop test, which means that when EITHER motor hits
                // its target position, the motion will stop.  This is "safer" in the event that the robot will
                // always end the motion as soon as possible.
                // However, if you require that BOTH motors have finished their moves before the robot continues
                // onto the next step, use (isBusy() || isBusy()) in the loop test.
                // Display it for the driver.
                // Arya Added Code To Test Control System Logic
                if (Math.abs(left_front.getCurrentPosition()) < Math.abs(newLeftTargetF)) {
                    int error = newLeftTargetF - left_front.getCurrentPosition();
                    //int error_ratio = error/newLeftTargetF;
                    if (Math.abs(error) < Math.abs(newLeftTargetF * 0.1)) {
                        int adj_speed = (int) (speed / 2);
                        left_front.setPower(Math.abs(adj_speed));
                        telemetry.addData("IS RUNNING ", left_front.getCurrentPosition());
                        telemetry.update();
                    } else {
                        telemetry.addData("LEFT FRONT CURRENT:", left_front.getCurrentPosition());
                        telemetry.addData("TARGET LEFT FRONT:",newLeftTargetF);
                        telemetry.update();
                        sleep (200);
                    }
                }

                if (Math.abs(left_back.getCurrentPosition()) < Math.abs(newLeftTargetB)) {
                    int error = newLeftTargetB - left_back.getCurrentPosition();
                    //int error_ratio = error/newLeftTargetB;
                    if (Math.abs(error) < Math.abs(newLeftTargetB * 0.1)) {
                        int adj_speed = (int) (speed / 2);
                        left_back.setPower(Math.abs(adj_speed));
                        telemetry.addData("IS_RUNNING ", left_back.getCurrentPosition());
                        telemetry.update();
                    } else {
                        telemetry.addData("LEFT BACK CURRENT:", left_back.getCurrentPosition());
                        telemetry.addData("TARGET LEFT BACK:",newLeftTargetB);
                        telemetry.update();
                        sleep (200);
                    }
                }

                if (Math.abs(right_front.getCurrentPosition()) < Math.abs(newRightTargetF)) {
                    int error = newRightTargetF - right_front.getCurrentPosition();
                    //int error_ratio = error/newRightTargetF;
                    if (Math.abs(error) < Math.abs(newRightTargetF * 0.1)) {
                        int adj_speed = (int) (speed / 2);
                        right_front.setPower(Math.abs(adj_speed));
                        telemetry.addData("IS_RUNNING ", right_front.getCurrentPosition());
                        telemetry.update();
                    } else {
                        telemetry.addData("RIGHT FRONT CURRENT:", right_front.getCurrentPosition());
                        telemetry.addData("TARGET RIGHT FRONT:",newRightTargetF);
                        telemetry.update();
                        sleep (200);
                    }
                }

                if (Math.abs(right_back.getCurrentPosition()) < Math.abs(newRightTargetB)) {
                    int error = newRightTargetB - right_back.getCurrentPosition();
                    //int error_ratio = error/newRightTargetB;
                    if (Math.abs(error) < Math.abs(newRightTargetB * 0.1)) {
                        int adj_speed = (int) (speed / 2);
                        right_back.setPower(Math.abs(adj_speed));
                        telemetry.addData("IS_RUNNING ", right_back.getCurrentPosition());
                        telemetry.update();
                    } else {
                        telemetry.addData("RIGHT BACK CURRENT:", right_back.getCurrentPosition());
                        telemetry.addData("TARGET RIGHT BACK:",newRightTargetB);
                        telemetry.update();
                        sleep (200);
                    }
                }
                // End Arya Test Control System Logic. Uncomment the next Four lines.
                telemetry.addData("Running to", " %7d :%7d", newLeftTargetF, newRightTargetF);
                telemetry.addData("Currently at", " at %7d :%7d",
                        left_front.getCurrentPosition(), right_front.getCurrentPosition(),
                        left_back.getCurrentPosition(), right_back.getCurrentPosition());
                viper_rot.setPower(0.001);
                telemetry.update();
                // End of While Loop
            }

            // Stop all motors and motion
            left_front.setPower(0);
            right_front.setPower(0);
            left_back.setPower(0);
            right_back.setPower(0);

            // Turn off RUN_TO_POSITION
            initManualModes();
            viper_rot.setPower(0.001);
            sleep(250);   // optional pause after each move.
        }
    }

    public void encoderTurn(double speed,
                            double turnAngle,
                            double timeoutS) {
        int newLeftTargetF = 0;
        int newRightTargetF = 0;
        int newLeftTargetB = 0;
        int newRightTargetB = 0;
        // Consistently we are noticing that around 504 ticks of this motor is making the robot travel 12 inches. (537.7*0.925)/(3.77952*3.1415) = ~42 ticks per inch.
        // By repeated trails, it was observed that with the goBuilda motors used for wheels, full One Circle turn i.e., 360 degree turn at a motor speed of 0.5 was 3312 motor ticks.
        // Using this information it can be calculated that TURN_ANGLE_TICKS for each Degree of an Angle is approx. 9.25 ticks.


        double TURN_ANGLE_TICKS = 0;
        TURN_ANGLE_TICKS = turnAngle * 9.25;

        // Ensure that the OpMode is still active
        if (opModeIsActive()) {

            // Changes mode
            initManualModes();

            telemetry.addData("TURN ANGLE TICKS:", TURN_ANGLE_TICKS);
            telemetry.update();
            sleep (200);

            newLeftTargetF = left_front.getCurrentPosition() + (int) (TURN_ANGLE_TICKS);
            newLeftTargetB = left_back.getCurrentPosition() + (int) (TURN_ANGLE_TICKS);
            newRightTargetF = right_front.getCurrentPosition() - (int) (TURN_ANGLE_TICKS);
            newRightTargetB = right_back.getCurrentPosition() - (int) (TURN_ANGLE_TICKS);


            left_front.setTargetPosition(newLeftTargetF);
            right_front.setTargetPosition(newRightTargetF);
            left_back.setTargetPosition(newLeftTargetB);
            right_back.setTargetPosition(newRightTargetB);

            // Turn On RUN_TO_POSITION
            runToPosition();

            // reset the timeout time and start motion.
            runtime.reset();

            // Set the initial Power of all the motors
            left_front.setPower(Math.abs(speed));
            right_front.setPower(Math.abs(speed));
            left_back.setPower(Math.abs(speed));
            right_back.setPower(Math.abs(speed));

            // keep looping while we are still active, and there is time left, and both motors are running.
            // Note: We use (isBusy() && isBusy()) in the loop test, which means that when EITHER motor hits
            // its target position, the motion will stop.  This is "safer" in the event that the robot will
            // always end the motion as soon as possible.
            // However, if you require that BOTH motors have finished their moves before the robot continues
            // onto the next step, use (isBusy() || isBusy()) in the loop test.
            while (opModeIsActive() &&
                    (runtime.seconds() < timeoutS) &&
                    (left_front.isBusy() &&
                     right_front.isBusy() &&
                     left_back.isBusy() &&
                     right_back.isBusy()
                    )
                )
            {
                // Arya Added Code To Test Control System Logic
                if (Math.abs(left_front.getCurrentPosition()) < Math.abs(newLeftTargetF)) {
                    int error = newLeftTargetF - left_front.getCurrentPosition();
                    //int error_ratio = error/newLeftTargetF;
                    if (Math.abs(error) < Math.abs(newLeftTargetF * 0.1)) {
                        int adj_speed = (int) (speed / 2);
                        left_front.setPower(Math.abs(adj_speed));
                        telemetry.addData("IS_RUNNING ", left_front.getCurrentPosition());
                        telemetry.update();
                    } else {
                        telemetry.addData("LEFT FRONT CURRENT:", left_front.getCurrentPosition());
                        telemetry.addData("TARGET LEFT FRONT:",newLeftTargetF);
                        telemetry.update();
                        sleep (200);
                    }
                }

                if (Math.abs(left_back.getCurrentPosition()) < Math.abs(newLeftTargetB)) {
                    int error = newLeftTargetB - left_back.getCurrentPosition();
                    //int error_ratio = error/newLeftTargetB;
                    if (Math.abs(error) < Math.abs(newLeftTargetB * 0.1)) {
                        int adj_speed = (int) (speed / 2);
                        left_back.setPower(Math.abs(adj_speed));
                        telemetry.addData("IS_RUNNING ", left_back.getCurrentPosition());
                        telemetry.update();
                    } else {
                        telemetry.addData("LEFT BACK CURRENT:", left_back.getCurrentPosition());
                        telemetry.addData("TARGET LEFT BACK:",newLeftTargetB);
                        telemetry.update();
                        sleep (200);
                    }
                }

                if (Math.abs(right_front.getCurrentPosition()) < Math.abs(newRightTargetF)) {
                    int error = newRightTargetF - right_front.getCurrentPosition();
                    //int error_ratio = error/newRightTargetF;
                    if (Math.abs(error) < Math.abs(newRightTargetF * 0.1)) {
                        // Control System Logic to slow down the motor speed proportional to the remaining distance to the target
                        int adj_speed = (int) (speed / 2);
                        right_front.setPower(Math.abs(adj_speed));
                        telemetry.addData("IS_RUNNING ", right_front.getCurrentPosition());
                        telemetry.update();
                    } else {
                        telemetry.addData("RIGHT FRONT CURRENT:", right_front.getCurrentPosition());
                        telemetry.addData("TARGET RIGHT FRONT:",newRightTargetF);
                        telemetry.update();
                        sleep (200);
                    }
                }

                if (Math.abs(right_back.getCurrentPosition()) < Math.abs(newRightTargetB)) {
                    int error = newRightTargetB - right_back.getCurrentPosition();
                    //int error_ratio = error/newRightTargetB;
                    if (Math.abs(error) < Math.abs(newRightTargetB * 0.1)) {
                        int adj_speed = (int) (speed / 2);
                        right_back.setPower(Math.abs(adj_speed));
                        telemetry.addData("IS_RUNNING ", right_back.getCurrentPosition());
                        telemetry.update();
                    } else {
                        telemetry.addData("RIGHT BACK CURRENT:", right_back.getCurrentPosition());
                        telemetry.addData("TARGET RIGHT BACK:",newRightTargetB);
                        telemetry.update();
                        sleep (200);
                    }
                }
                // End Arya Test Control System Logic. Uncomment the next Four lines.

                // Display it for the driver.
                telemetry.addData("Running to", " %7d :%7d", newLeftTargetF, newRightTargetF);
                telemetry.addData("Currently at", " at %7d :%7d",
                        left_front.getCurrentPosition(), right_front.getCurrentPosition(),
                        left_back.getCurrentPosition(), right_back.getCurrentPosition());
                viper_rot.setPower(0.001);
                telemetry.update();
                // End of While Loop
            }

            // Stop all motion;
            left_front.setPower(0);
            right_front.setPower(0);
            left_back.setPower(0);
            right_back.setPower(0);

            // Turn off RUN_TO_POSITION
            initManualModes();
            viper_rot.setPower(0.001);
            sleep(250);   // optional pause after each move.
        }
    }

    public void encoderDriveViperRot(double speed, double degrees, double timeoutS) {
        int newRotTarget;

        if (opModeIsActive()) {
            // Sets mode
            initManualModes();
            // Gets curent position and adds certain amount of degrees to that position for new target position
            // goBuilda Motor 5203 Series 30 rpm 188:1 ratio.
            newRotTarget = viper_rot.getCurrentPosition() + (int) (degrees * COUNTS_PER_DEGREE_ANGLE);
            // Set target position
            viper_rot.setTargetPosition(newRotTarget);
            viper_rot.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            runtime.reset();
            viper_rot.setPower(Math.abs(speed));
            while (opModeIsActive() && (runtime.seconds() < timeoutS) && viper_rot.isBusy()) {
                // making sure that the viper slide is running to it's position before locking in
            }
            viper_rot.setTargetPosition(newRotTarget);
            viper_rot.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            viper_rot.setPower(0.001);
        }
    }

    public void encoderDriveViperBase(double speed, double ticks, double timeoutS) {
        int newBaseTarget;
        // goBuilda 5203 Series 312 RPM 19.2:1 ratio DC motor
        if (opModeIsActive()) {
            // Sets mode
            initManualModes();
            // Gets curent position and adds certain amount of degrees to that position for new target position
            newBaseTarget = viper_base.getCurrentPosition() + (int)(ticks);
            // Set target position
            viper_base.setTargetPosition(newBaseTarget);
            viper_base.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            runtime.reset();
            viper_base.setPower(Math.abs(speed));
            while (opModeIsActive() && (runtime.seconds() < timeoutS) && viper_base.isBusy()) {
                // making sure that the viper slide is running to it's position before locking in
            }
            viper_base.setTargetPosition(newBaseTarget);
            viper_base.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            viper_base.setPower(0.001);
            viper_rot.setPower(0.001);
        }


    }
    public void putSampleInBasket () {

        // Finger Positions : 0 is open full; 0.5 is open half; 1 is full close.
        // Wrist Positions : 0 is down; 0.5 is hover position; 1 is completely back to drop block in the basket.

        fingers.setPosition(1);

        sleep(500);

        while (viper_base.getCurrentPosition() < -400 && opModeIsActive()) {
            wrist.setPosition(0.5);
            viper_base.setPower(1);
        }
        while (viper_rot.getCurrentPosition() > -1424 && opModeIsActive()) {
            //Control system to prevent Viper Slide from hitting top limit.
            double viper_rot_position = viper_rot.getCurrentPosition();
            double error = viper_rot_position - (-1424);
            double power = 0.1 + (error * .001);
            viper_rot.setPower(-power);
        }

        wrist.setPosition(0);

        while (viper_base.getCurrentPosition() > -2950 && opModeIsActive()) {

            //Control system to prevent Viper Slide from hitting top limit.
            viper_rot.setPower(0.001);
            double viper_base_position = viper_base.getCurrentPosition();
            double error = viper_base_position - (-2950) + 50;
            double power = error * 0.001;

            viper_base.setPower(-power);
        }

        fingers.setPosition(1);

        sleep(500);

        wrist.setPosition(1);

        sleep(500);

        fingers.setPosition(0.5);

        sleep(500);

        wrist.setPosition(0);

        sleep(750);

        while (viper_base.getCurrentPosition() < -600 && opModeIsActive()) {
            viper_base.setPower(1);
        }
        while (viper_rot.getCurrentPosition() < -100) {

            //Control system to prevent Viper Rotation from hitting bottom too hard.
            double viper_rot_position = viper_rot.getCurrentPosition();
            double error = viper_rot_position - (-100);
            double power = error * 0.0025;

            viper_rot.setPower(-power);
        }
    }
}
