package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;


@Autonomous(name = "AutoBasket")
//@Autonomous(name="Robot: Auto Drive By Encoder", group="Robot")
//@Disabled
public class AutoBasket extends AutoFunctionsRhino {


    private ElapsedTime runtime = new ElapsedTime();
    @Override

    public void runOpMode() {

        // Initialize the drive system variables.

        initHardware();


        left_front.setDirection(DcMotor.Direction.REVERSE);
        right_front.setDirection(DcMotor.Direction.FORWARD);
        left_back.setDirection(DcMotor.Direction.REVERSE);
        right_back.setDirection(DcMotor.Direction.FORWARD);

        resetMotorsAutonomous();

        // Set Motor parameters GoBuilda Motor Encoder 5203 312 RPM 19.2:1 ratio Series counts per rotation is 537.7 and wheel diameter is 3.77952 inches.
        // In our Robot, the drive Gear reduction is approx. 0.925 for the Rhino wheels connected to the gears for the right angle rotations of the wheels vs. motor shaft.
        // Consistently we are noticing that around 504 ticks of this motor is making the robot travel 12 inches. (537.7*0.925)/(3.77952*3.1415) = ~42 ticks per inch.
        int abc1=setParameters("WHEEL",537.7, 0.925, 3.77952);
        int abc2=setParameters("VIPERROT",5281.1, 1, 0);
        int abc3=setParameters("VIPERBASE",537.7, 1, 0);


        initManualModes();

        // Send telemetry message to indicate successful Encoder reset
        telemetry.addData("Starting at", "%7d :%7d",
                left_front.getCurrentPosition(),
                right_front.getCurrentPosition(),
                left_back.getCurrentPosition(),
                right_back.getCurrentPosition());

        telemetry.update();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        // Step through each leg of the path,
        // Note: Reverse movement is obtained by setting a negative distance (not speed)


        //putSampleInBasket();
        // Finger Positions : 0 is open full; 0.5 is open half; 1 is full close.
        // Wrist Positions : 0 is down; 0.5 is hover position; 1 is completely back to drop block in the basket.

        wrist.setPosition(0);
        fingers.setPosition(1);
        encoderTurn(TURN_SPEED, -45,.75);  // S2: Turn Left 30 Degrees with 5 Sec timeout
        encoderDrive(DRIVE_SPEED, 17, 17, .75);  // S1: Forward 18 Inches with 5 Sec timeout
        encoderTurn(TURN_SPEED, 85,.75);  // S2: Turn Left 30 Degrees with 5 Sec timeout
        encoderDrive(DRIVE_SPEED, -3, -3, .75);  // S1: Forward 18 Inches with 5 Sec timeout
        putSampleInBasket();
        encoderTurn(TURN_SPEED, -52,1);  // S2: Turn Left 30 Degrees with 5 Sec timeout
        encoderDrive(DRIVE_SPEED, -5, -5, .75);  // S1: Forward 18 Inches with 5 Sec timeout
        fingers.setPosition(0);
        encoderDriveViperBase(0.5,-1950,3);
        encoderTurn(TURN_SPEED, 7,1);  // S2: Turn Left 30 Degrees with 5 Sec timeout
        fingers.setPosition(1);
        sleep(250);
        encoderDriveViperBase(0.5,1950,3);
        encoderDrive(DRIVE_SPEED, 4, 4, .75);  // S1: Forward 18 Inches with 5 Sec timeout
        encoderTurn(TURN_SPEED, 43,.75);  // S2: Turn Left 30 Degrees with 5 Sec timeout
        putSampleInBasket();



        /*
        fingers.setPosition(1);
        encoderTurn(TURN_SPEED, -30,5.0);  // S2: Turn Left 30 Degrees with 5 Sec timeout
        encoderDrive(DRIVE_SPEED, 22, 22, 5.0);  // S1: Forward 18 Inches with 5 Sec timeout
        encoderTurn(TURN_SPEED, 20,5.0);  // S2: Turn Right 25 Degrees with 5 Sec timeout
        encoderDriveViperRot(0.5, 55, 5.0);
        wrist.setPosition(0);
        sleep(1000);
        encoderDriveViperBase(0.5,-1700,5.0);
        sleep(500);
        encoderDriveViperRot(0.5, -10, 5.0);
        sleep(1000);
        encoderDriveViperBase(0.5,800,5.0);
        sleep(200);
        fingers.setPosition(0.5);
        sleep(200);
        encoderDriveViperBase(0.5,900,5.0);
        encoderDriveViperRot(0.5, -45, 5.0);
        encoderDrive(DRIVE_SPEED, -25, -25, 5.0);  // S1: Back 22 Inches with 5 Sec timeout

        encoderTurn(TURN_SPEED, 50,5.0);  // S2: Turn Right 50 Degrees with 5 Sec timeout
        encoderDrive(DRIVE_SPEED, 63, 63, 5.0);  // S1: Forward 60 Inches with 5 Sec timeout
        encoderTurn(TURN_SPEED, 145,5.0);  // S2: Turn Right 90 Degrees with 5 Sec timeout
        encoderDrive(DRIVE_SPEED, 45, 45, 5.0);  // S1: Forward 60 Inches with 5 Sec timeout
        encoderDrive(DRIVE_SPEED, -10, -10, 5.0);  // S1: Forward 60 Inches with 5 Sec timeout
        encoderDriveViperRot(0.5, 15, 15);
        sleep(1000);
        encoderDriveViperBase(0.5,-1000,5.0);
        wrist.setPosition(0);
        fingers.setPosition(1);
        wrist.setPosition(0.5);
        fingers.setPosition(0.5);
        sleep(1000);
        encoderDriveViperBase(0.5,1000,5.0);
        telemetry.addData("Path", "Complete");
        telemetry.update();
        sleep(1000);  // pause to display final telemetry message.
         */
    }
}

