/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import frc.robot.Map;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

//import com.ctre.phoenix.motorcontrol.*;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

import io.github.oblarg.oblog.Logger;
import io.github.oblarg.oblog.*;
//https://oblog-docs.readthedocs.io/en/latest/getting-started.html
//https://oblog-docs.readthedocs.io/en/latest/   https://github.com/Oblarg/Oblog

public class Robot extends TimedRobot {
  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();

  //Drive Train Motor Controllers 
  WPI_TalonSRX _rightFront = new WPI_TalonSRX(Map.frontRightTalon);
  WPI_TalonSRX _leftFront = new WPI_TalonSRX(Map.frontLeftTalon);
  WPI_TalonSRX _rightBack = new WPI_TalonSRX(Map.backRightTalon);
  WPI_TalonSRX _leftBack = new WPI_TalonSRX(Map.backLeftTalon);

  //Diffrential Drive setup for front motors 
  DifferentialDrive _diffDrive = new DifferentialDrive(_leftFront, _rightFront);

  //Init flight sticks
  Joystick _leftFL = new Joystick(Map.leftFLUsb);
  Joystick _rightFL = new Joystick(Map.rightFLUsb);

  @Override
  public void teleopPeriodic() {
    //Calculate angle of joystick y
    double left  = (Map.reverse ? 1 : -1) * _leftFL.getRawAxis(1)  * Map.speedMax; 
    double right = (Map.reverse ? 1 : -1) * _rightFL.getRawAxis(1) * Map.speedMax; 
    



    //Dead space in joy stick
    left  = Math.abs(left)  < 0.10 ? 0 : left;
    right = Math.abs(right) < 0.10 ? 0 : right;

    //Do diffrential Drive based on tank drive
    _diffDrive.tankDrive(left, right);
  }

  @Override
  public void robotInit() {
    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_chooser);

    /* factory default values */
    _rightFront.configFactoryDefault();
    _rightBack.configFactoryDefault();
    _leftFront.configFactoryDefault();
    _leftBack.configFactoryDefault();

    /* set up followers */
    _rightBack.follow(_rightFront);
    _leftBack.follow(_leftFront);

    /* flip values so robot moves forward when stick-forward/LEDs-green */
    _rightFront.setInverted(true); // !< Update this
    _leftFront.setInverted(false); // !< Update this

    /* set the invert of the followers to match their respective master controllers*/
    _rightBack.setInverted(true);
    _leftBack.setInverted(false);

    /* adjust sensor phase so sensor moves positive when Talon LEDs are green */
    _rightFront.setSensorPhase(true);
    _leftFront.setSensorPhase(true);

    /*
     * WPI drivetrain classes defaultly assume left and right are opposite. call
     * this so we can apply + to both sides when moving forward. DO NOT CHANGE
     */
    _diffDrive.setRightSideInverted(false);

    _rightFront.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative,/* Feedback*/ 
    Map.frontRightEncoder, Map.kTimeoutMs);
    
    _leftFront.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative,/* Feedback*/ 
    Map.frontLeftEncoder, Map.kTimeoutMs);

    //OBlog shuffleboard setup
    Logger.configureLoggingAndConfig(this, false);
  }

  @Override
  public void robotPeriodic() {

    Logger.updateEntries();

  }

  @Override
  public void autonomousInit() {
    m_autoSelected = m_chooser.getSelected();
    // m_autoSelected = SmartDashboard.getString("Auto Selector", kDefaultAuto);
    System.out.println("Auto selected: " + m_autoSelected);
  }

  
  @Override
  public void autonomousPeriodic() {
    switch (m_autoSelected) {
      case kCustomAuto:
        // Put custom auto code here
        break;
      case kDefaultAuto:
      default:
        // Put default auto code here
        break;
    }
  }

  
  @Override
  public void testPeriodic() {
  }
}
