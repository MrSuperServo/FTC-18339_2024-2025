package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp
public class TankProtocol001 extends Main001 {
    private double left_front_power = 0;
    private double left_back_power = 0;
    private double right_front_power = 0;
    private double right_back_power = 0;

    private double moveMultiplier = 1f;

    @Override
    public void runOpMode() {
        initMaths();
        initHardware();
        initManualModes();

        waitForStart();

        if (opModeIsActive()) {
            while (opModeIsActive()) {
                SetMotorForces();
            }

            telemetry.update();
            idle();
        }
    }

    public void SetMotorForces() {
        if (!NoNullHardware()) return;

        //Algorithm determined wheel forces with the inputs
        double y = gamepad1.left_stick_y;

        //Right trigger right rotation
        //Right trigger is right rotation, left trigger is left. Can slow rotations by depressing trigger less.
        float rAxis = gamepad1.right_trigger - gamepad1.left_trigger;

        //find the speeds of each wheel
        left_front_power = math.GetWheelForceTank(y, 1, rAxis);
        left_back_power = math.GetWheelForceTank(y, 2, rAxis);
        right_front_power = math.GetWheelForceTank(y, 3, rAxis);
        right_back_power = math.GetWheelForceTank(y, 4, rAxis);

        //Show the powers on the telemetry screen
        telemetry.addData("y", gamepad1.left_stick_y);
        telemetry.addData("rot", gamepad1.right_trigger - gamepad1.left_trigger);
        telemetry.addData("lpb", left_back_power);
        telemetry.addData("lpf", left_front_power);
        telemetry.addData("rpb", right_back_power);
        telemetry.addData("rpf", right_front_power);

        //Set the velocities of each wheel motor
        left_front.setVelocity(-1 * left_front_power * MAX_NUM_TICKS_MOVEMENT * MOVEMENT_RPM);
        left_back.setVelocity(-1 * left_back_power * MAX_NUM_TICKS_MOVEMENT * MOVEMENT_RPM);
        right_front.setVelocity(right_front_power * MAX_NUM_TICKS_MOVEMENT * MOVEMENT_RPM);
        right_back.setVelocity(right_back_power * MAX_NUM_TICKS_MOVEMENT * MOVEMENT_RPM);
    }
}
