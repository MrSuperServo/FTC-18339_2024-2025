package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

public class Main001 extends LinearOpMode {
    public DcMotorEx left_front;
    public DcMotorEx right_front;
    public DcMotorEx left_back;
    public DcMotorEx right_back;
    public DcMotorEx viper_base;
    public DcMotorEx viper_rot;

    public Algorithms001 math;

    public final float MAX_NUM_TICKS_MOVEMENT = 537.6f;
    public final float MAX_NUM_TICKS_VIPER_BASE = 753.2f;
    public final float MAX_NUM_TICKS_VIPER_ROT = 5281.1f;

    public final int MOVEMENT_RPM = 312;
    public final int VIPER_BASE_RPM = 223;
    public final int VIPER_ROT_RPM = 30;

    public void initHardware() {
        right_back = hardwareMap.get(DcMotorEx.class, "right_back");
        right_front = hardwareMap.get(DcMotorEx.class, "right_front");
        left_back = hardwareMap.get(DcMotorEx.class, "left_back");
        left_front = hardwareMap.get(DcMotorEx.class, "left_front");

        viper_base = hardwareMap.get(DcMotorEx.class, "viper_base");
        viper_rot = hardwareMap.get(DcMotorEx.class, "viper_rot");
    }

    public void initManualModes() {
        resetMotorsAutonomous();
        right_back.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        right_front.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        left_back.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        left_front.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        viper_base.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        viper_rot.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void resetMotorsAutonomous() {
        right_back.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        right_front.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        left_back.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        left_front.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        viper_base.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        viper_rot.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

    public void StopMotors() {
        right_back.setVelocity(0);
        left_back.setVelocity(0);
        right_front.setVelocity(0);
        left_front.setVelocity(0);

        viper_base.setVelocity(0);
        viper_rot.setVelocity(0);
    }

    public boolean NoNullHardware() { return (left_back != null && left_front != null && right_back != null && right_front != null && viper_base != null && viper_rot != null); }

    @Override
    public void runOpMode() { }

    public void initMaths() {

        //Initialize the maths program
        math = new Algorithms001();
        math.Initialize();
    }
}
