package frc.robot;

import edu.wpi.first.wpilibj.Joystick;

import frc.robot.Map;

import com.ctre.phoenix.motorcontrol.ControlMode;
import edu.wpi.first.wpilibj.Spark;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Compressor;

public class SubsystemCollection{
    public static void intake(Joystick joy, Spark in){
        if(joy.getTrigger()){
            in.set(Map.inSpeed);
        }
    }
    public static void turnIndexerOneTurn(Joystick joy, WPI_TalonSRX tal){
        if(joy.getRawButton(Map.revolver)){
            //turn 72
        }
    }
    public static void shootAllBall(Joystick joy, WPI_TalonSRX sho, Solenoid revUp, WPI_TalonSRX rev){
        if(joy.getRawButton(Map.shootAllBalls)){
            rev.set(ControlMode.PercentOutput, 0.2);
            sho.set(ControlMode.PercentOutput, 1);
        }
    }
    public static void raiseClimber(Joystick joy, WPI_VictorSPX vic){
        if(joy.getRawButton(Map.climbUp)){
            vic.set(ControlMode.PercentOutput, Map.climbSpeed);
        }
        else{
            vic.set(ControlMode.PercentOutput, 0);
        }
    }
    public static void climb(Joystick joy, WPI_VictorSPX vic){
        if(joy.getRawButton(Map.climbDown)){
            vic.set(ControlMode.PercentOutput, Map.climbSpeed);
        }
        else{
            vic.set(ControlMode.PercentOutput, 0);
        }
    }
    public static void dropTheGate(Solenoid sole){
        sole.set(true);
    }
    public static void raiseGate(Solenoid sole){
        sole.set(false);
    }
}