package org.firstinspires.ftc.teamcode;


public class Algorithms002 {
    //Really Slow 0.05f, Regular 0.1f
    float wheelControlMultiplier = 0.05f;
    float viperControlMultiplier = 0.1f;

    // NEED TO CHECK THIS VALUE AND MAKE IT CORRECT
    final float goBildaWheel = 289.03f;

    public static float wheelCircumferenceMm = 289.03f;

    //Manipulate values in here in force matrix
    public double GetWheelForceTank(double y, int i, float x2)
    {
        if(y == 0 && x2 == 0) {
            return 0;
        }

        double movePower = 0;

        //FORCE MATRIX i == 1 is left_front i == 2 is right_front i == 3 is left_back i == 4 is right_back
        //Uses unit circle math look it up if you need to
        //theta is just divided by pi so 2 is equal in angle to 2 pi
        if(y != 0)
        {
            movePower = y;
        }

        double rZ = Math.abs(x2);

        // something up with rotation, what we will likely need to do is control each wheel separately
        double rotationPower = 0;
        if(x2 != 0) {
            if(i == 1 || i == 2) {
                if(x2 < 0) {
                    rotationPower = -1;
                } else if(x2 > 0) {
                    rotationPower = 1;
                } else {
                    rotationPower = 0;
                }
            } else if (i == 3 || i == 4) {
                if(x2 < 0) {
                    rotationPower = 1;
                } else if(x2 > 0) {
                    rotationPower = -1;
                } else {
                    rotationPower = 0;
                }
            }
        }

        rotationPower *= rZ;

        double power;
        if(movePower != 0 && rotationPower == 0) {
            power = movePower;
        } else if(movePower == 0 && rotationPower != 0) {
            power = rotationPower;
        } else {
            power = (double)((movePower + rotationPower) / 2);
        }

        //power *= 0.5f;

        return power * wheelControlMultiplier;
    }

    public double GetViperForce(double y) {
        if ((y > 0) || (y < 0)) {
            return y * viperControlMultiplier;
        } else {
            return 0;
        }
    }
    public double GetViperRotation(double y) {
        if ((y > 0) || (y < 0)) {
            return y * viperControlMultiplier;
        } else {
            return 0;
        }
    }


    public void Initialize() {
        wheelCircumferenceMm = goBildaWheel;
    }
}
