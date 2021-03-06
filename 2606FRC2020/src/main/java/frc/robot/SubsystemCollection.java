package frc.robot;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Joystick;

import edu.wpi.first.wpilibj.Servo;

import frc.robot.Map;

import com.ctre.phoenix.motorcontrol.ControlMode;
import edu.wpi.first.wpilibj.Spark;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
//import edu.wpi.first.wpilibj.Encoder;
import com.fasterxml.jackson.databind.ObjectWriter.Prefetch;

public class SubsystemCollection{
    public static void warmUpWheel(WPI_TalonSRX sho){
        sho.set(ControlMode.PercentOutput, -0.7);
    }

    // tal  vic spark vic spark
    public static void offCheck(WPI_TalonSRX one, WPI_VictorSPX four, Spark three, WPI_VictorSPX five, Spark two){
        one.set(ControlMode.PercentOutput, 0);
        //two.set(0);
        //three.set(0);
        four.set(ControlMode.PercentOutput, 0);
        five.set(ControlMode.PercentOutput, 0);
    }

    public static void intake(Joystick joy, WPI_VictorSPX in){
        if(joy.getTrigger()){
            in.set(ControlMode.PercentOutput, Map.inSpeed);
        }
    }

    public static void turnIndexerOneTurn(Joystick joy, WPI_TalonSRX tal, Encoder revEnc){
        if(joy.getRawButton(Map.turnRevolverButton)){
            while(revEnc.getRaw() < 1){
                tal.set(ControlMode.PercentOutput, 0.3);
            }
            revEnc.reset();
        }
    }

    public static void shootAllBall(Joystick joy, WPI_TalonSRX sho, WPI_TalonSRX rev, Spark loader){
        if(joy.getRawButton(Map.shootAllBalls)){
            rev.set(ControlMode.PercentOutput, 0.2);
            sho.set(ControlMode.PercentOutput, -1);
            loader.set(1);
        }
    }

    public static void raiseClimber(Joystick joy, WPI_VictorSPX vic){
        if(joy.getRawButton(Map.climbUp)){
            vic.set(ControlMode.PercentOutput, Map.climbSpeed);
        }
    }
    public static void lowerClimber(Joystick joy, WPI_VictorSPX vic){
        if(joy.getRawButton(Map.climbUpDown)){
            vic.set(ControlMode.PercentOutput, -1 * Map.climbSpeed);
        }
    }

    public static void climberUp(Joystick joy, Spark spk){
        if(joy.getRawButton(Map.climbDown)){
            spk.set(-0.01);
        }
    }
    public static void moveDistance(WPI_TalonSRX right, WPI_TalonSRX left,Encoder rightEnc, Encoder leftEnc, double disInInches){
        double ratio = 1;
        while(leftEnc.getRaw() < disInInches/12 || rightEnc.getRaw() < disInInches/12){
            ratio = (leftEnc.getRaw()/rightEnc.getRaw());
            right.set(ControlMode.PercentOutput, 0.2* ratio);
            left.set(ControlMode.PercentOutput, 0.2);
        }
        leftEnc.reset();
        rightEnc.reset();
    }
    public static void turnDegrees(WPI_TalonSRX right, WPI_TalonSRX left,Encoder rightEnc, Encoder leftEnc, double degreesLeft){
        double rotationsToTurn = (((28.0*Math.PI)/360)*degreesLeft)/(6*Math.PI);
        double ratio = 1;
        while(leftEnc.getRaw() < rotationsToTurn || rightEnc.getRaw() > -rotationsToTurn){
            ratio = (leftEnc.getRaw()/rightEnc.getRaw());
            right.set(ControlMode.PercentOutput, 0.2* ratio);
            left.set(ControlMode.PercentOutput, -0.2);
        }
    }

    public static void altBallThing(Joystick joy, Spark load, WPI_TalonSRX fly, WPI_TalonSRX rev, Encoder revEnc){
        if(joy.getRawButton(Map.shootAllBalls)){
            load.set(0.5);
            fly.set(ControlMode.PercentOutput, 1);
            while(revEnc.getRaw() < 1){
                rev.set(ControlMode.PercentOutput, 0.3);
            }
            revEnc.reset();
        }
    }
    public static void turnMotor(Joystick joy, int button, WPI_TalonSRX tal){
        if(joy.getRawButton(button)){
            tal.set(ControlMode.PercentOutput, 0.1);
        }
    }
    public static void turnMotor(Joystick joy, int button, WPI_VictorSPX vic){
        if(joy.getRawButton(button)){
            vic.set(ControlMode.PercentOutput, 0.1);
        }
    }
    public static void turnMotor(Joystick joy, int button, Spark spk){
        if(joy.getRawButton(button)){
            spk.set(0.1);
        }
    }
    public static void turnTest(Joystick joy, int button, WPI_TalonSRX tal){
        if(joy.getRawButton(button)){
            tal.set(ControlMode.Position, 1024);
        }
        tal.clearMotionProfileTrajectories();
    }
}