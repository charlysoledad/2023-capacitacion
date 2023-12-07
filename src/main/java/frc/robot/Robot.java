// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.ctre.phoenix.motorcontrol.InvertType;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {

  /* VARIABLES */
  /**
   * 
   *  MOTOR
   *  double - -1.0 -> 1.0 decimal
   *  Percentvoltage (0.0)
   * 
   *  DIGITALES
   *  0 - 1  (bool)
   *   * 
   *  ANALOGAS
   *  int
   *  double 0.0 - 1023.0
   * 
   *  PWM
   *  -255 -> 255
   * 
   *  String - cadenas texto
   *  int - numeros enteros -1??? 1????
   *  short -  
   *  long -
   *  double numeros con punto decimal
   *  char - caracteres
   * 
   *  Object -  SendableChooser
   * 
   */

  private static final String kDefaultAuto = "Default";
  public static final String kCustomAuto = "My Auto";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();


  // Declaraacion de variables para controlador de motores
  WPI_VictorSPX leftFront;
  WPI_VictorSPX leftRear;
  WPI_VictorSPX rightFront;
  WPI_VictorSPX rightRear;

  // Grupos de controladores
  MotorControllerGroup leftSide;
  MotorControllerGroup rightSide;

  // Drivebase
  DifferentialDrive drivetrain;

  // Controles
  Joystick driver;

  // Solenoides
  DoubleSolenoid brazo;
  DoubleSolenoid pinza;

  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  @Override
  public void robotInit() {
    leftFront = new WPI_VictorSPX(1);
    leftRear = new WPI_VictorSPX(2);
    rightFront = new WPI_VictorSPX(3);
    rightRear = new WPI_VictorSPX(4);

    leftFront.setInverted(true);
    leftRear.follow(leftFront);
    leftRear.setInverted(InvertType.FollowMaster);
    
    rightRear.follow(rightFront);

    leftSide = new MotorControllerGroup(leftFront, leftRear);
    rightSide = new MotorControllerGroup(rightFront, rightRear);

    driver = new Joystick(0);

    brazo = new DoubleSolenoid(PneumaticsModuleType.CTREPCM,0,1);
    pinza = new DoubleSolenoid(PneumaticsModuleType.CTREPCM,2,3);

    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    
    SmartDashboard.putData("Auto choices", m_chooser);
  }

  /**
   * This function is called every 20 ms, no matter the mode. Use this for items like diagnostics
   * that you want ran during disabled, autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before LiveWindow and
   * SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {}

  /**
   * This autonomous (along with the chooser code above) shows how to select between different
   * autonomous modes using the dashboard. The sendable chooser code works with the Java
   * SmartDashboard. If you prefer the LabVIEW Dashboard, remove all of the chooser code and
   * uncomment the getString line to get the auto name from the text box below the Gyro
   *
   * <p>You can add additional auto modes by adding additional comparisons to the switch structure
   * below with additional strings. If using the SendableChooser make sure to add them to the
   * chooser code above as well.
   */
  @Override
  public void autonomousInit() {
    m_autoSelected = m_chooser.getSelected();
    // m_autoSelected = SmartDashboard.getString("Auto Selector", kDefaultAuto);
    System.out.println("Auto selected: " + m_autoSelected);
  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {
    switch (m_autoSelected) {
      case kCustomAuto:
      drivetrain.tankDrive(0.5, 0.5);
      Timer.delay(2.0);
      drivetrain.tankDrive(0, 0);
      Timer.delay(0.2);
      drivetrain.tankDrive(0.5, 0);
      Timer.delay(1.5);
      drivetrain.tankDrive(0, 0);
      break;
      case kDefaultAuto:
      default:
        drivetrain.tankDrive(0.5, 0.5);
        Timer.delay(2.0);
        drivetrain.tankDrive(0, 0);
        break;
    }
  }

  /** This function is called once when teleop is enabled. */
  @Override
  public void teleopInit() {}

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {
    drivetrain.arcadeDrive(driver.getY(), driver.getX());

    if (driver.getRawButton(0)) { // cuando se presiona el boton 0
      pinza.set(Value.kForward);
    } else {
      pinza.set(Value.kReverse);
    }
    
    if (driver.getRawButton(1)) { // cuando se presiona el boton 1
      brazo.set(Value.kForward);
    } else {
      brazo.set(Value.kReverse);
    }
  }

  /** This function is called once when the robot is disabled. */
  @Override
  public void disabledInit() {}

  /** This function is called periodically when disabled. */
  @Override
  public void disabledPeriodic() {}

  /** This function is called once when test mode is enabled. */
  @Override
  public void testInit() {}

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {}

  /** This function is called once when the robot is first started up. */
  @Override
  public void simulationInit() {}

  /** This function is called periodically whilst in simulation. */
  @Override
  public void simulationPeriodic() {}
}
