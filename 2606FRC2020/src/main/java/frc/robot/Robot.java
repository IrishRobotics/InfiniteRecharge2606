/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import frc.robot.Map;
import frc.robot.SubsystemCollection;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

//import com.ctre.phoenix.motorcontrol.*;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import edu.wpi.first.wpilibj.Spark;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.hal.sim.DriverStationSim;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Encoder;

import io.github.oblarg.oblog.Logger;
import io.github.oblarg.oblog.annotations.Log;
//import sun.awt.AWTAccessor.SystemColorAccessor;
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
  WPI_VictorSPX _rightBack = new WPI_VictorSPX(Map.backRightVictor);
  WPI_VictorSPX _leftBack = new WPI_VictorSPX(Map.backLeftVictor);

  WPI_VictorSPX _climbUp = new WPI_VictorSPX(Map.liftUp);
  Spark _climbDown = new Spark(Map.climbDown);
  
  Spark _shooterLoader = new Spark(Map.loader);
  WPI_TalonSRX _shooter = new WPI_TalonSRX(Map.flyMotor);

  WPI_TalonSRX _revolver = new WPI_TalonSRX(Map.revolver);
  WPI_VictorSPX _intakeVictor = new WPI_VictorSPX(Map.intake);

  Encoder revolverEncoder;
  
  //Diffrential Drive setup for front motors 
  DifferentialDrive _diffDrive = new DifferentialDrive(_leftFront, _rightFront);

  //Init flight sticks
  Joystick _leftFL = new Joystick(Map.leftFLUsb);
  Joystick _rightFL = new Joystick(Map.rightFLUsb);

  // //Logs
  // @Log
  // double distRight;
  // @Log
  // double distLeft;

  @Override
  public void teleopPeriodic() {
    //Calculate angle of joystick y
    double left  = (Map.reverse ? 1 : -1) * _leftFL.getRawAxis(1)  * Map.speedMax; 
    double right = (Map.reverse ? 1 : -1) * _rightFL.getRawAxis(1) * Map.speedMax; 
    
    //System.out.println(_climbDown.isAlive() + " "+ _shooterLoader.isAlive());
    DriverStation.reportError(_climbDown.get() + " "+
    _shooterLoader.get(), false);

    SubsystemCollection.warmUpWheel(_shooter);
    
    SubsystemCollection.offCheck(_revolver, _climbUp, _climbDown, _intakeVictor, _shooterLoader);
    
    //Right Trigger
    SubsystemCollection.intake(_rightFL, _intakeVictor);
    
    //Shoot all balls
    SubsystemCollection.shootAllBall(_leftFL, _shooter, _revolver, _shooterLoader); 

    //Right thumb button 2
    SubsystemCollection.turnIndexerOneTurn(_rightFL, _revolver, revolverEncoder);
    
    //Turn Right 10 degrees right 11
    // if(_rightFL.getRawButton(11)){
    //   SubsystemCollection.turnDegrees(_rightFront, _leftFront, rightEncoder, leftEncoder, -10);
    // }
    
    SubsystemCollection.turnTest(_rightFL, 6, _rightFront);
    SubsystemCollection.turnTest(_rightFL, 7, _rightFront);

    //SubsystemCollection.shootAllBall(joy, sho, rev);
    
    //left 11
    SubsystemCollection.lowerClimber(_leftFL, _climbUp);

    //Left button 9
    SubsystemCollection.climberUp(_leftFL, _climbDown);

    //Left button 8
    SubsystemCollection.raiseClimber(_leftFL, _climbUp);

    //Dead space in joy stick
    left  = Math.abs(left)  < 0.10 ? 0 : left;
    right = Math.abs(right) < 0.10 ? 0 : right;

    //Do diffrential Drive based on tank drive
    _diffDrive.tankDrive(left, right);
  }

  @Override
  public void robotInit() {
    revolverEncoder = new Encoder(/*Channel A*/0, /*Channel B*/1);
    revolverEncoder.setDistancePerPulse(84); // 7/4 is the ratio points per rotation 12 turn is 21 NOTE may need to be quaditure which is 84
    // 1024 is the 1 rotation ratio but again quaditure is 4096 // 1024 is the 1 rotation ratio but again quaditure is 4096

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

    _climbDown.checkMotors();
    _shooterLoader.checkMotors();

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
        //SubsystemCollection.turnDegrees(_rightFront, _leftFront, rightEncoder, leftEncoder, 20);
        break;
      case kDefaultAuto:
      default:
        //SubsystemCollection.moveDistance(_rightFront, _leftFront, rightEncoder, leftEncoder, 1);
        break;
    }
  }

  
  @Override
  public void testPeriodic() {
  }
}
