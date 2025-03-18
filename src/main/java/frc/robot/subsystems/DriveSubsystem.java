// DriveSubsystem.java
package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.InvertType;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;


import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class DriveSubsystem extends SubsystemBase {
  // Define CAN IDs for motors
  private static final int FRONT_LEFT_MOTOR_ID = 20;
  private static final int BACK_LEFT_MOTOR_ID = 21;
  private static final int FRONT_RIGHT_MOTOR_ID = 22;
  private static final int BACK_RIGHT_MOTOR_ID = 23;
  
  // Define DIO ports for encoders
  private static final int[] FRONT_LEFT_ENCODER_PORTS = {0, 1};
  private static final int[] BACK_LEFT_ENCODER_PORTS = {2, 3};
  private static final int[] FRONT_RIGHT_ENCODER_PORTS = {4, 5};
  private static final int[] BACK_RIGHT_ENCODER_PORTS = {6, 7};
  
  // Motor controllers
  private final WPI_TalonSRX m_frontLeftMotor = new WPI_TalonSRX(FRONT_LEFT_MOTOR_ID);
  private final WPI_VictorSPX m_backLeftMotor = new WPI_VictorSPX(BACK_LEFT_MOTOR_ID);
  private final WPI_TalonSRX m_frontRightMotor = new WPI_TalonSRX(FRONT_RIGHT_MOTOR_ID);
  private final WPI_VictorSPX m_backRightMotor = new WPI_VictorSPX(BACK_RIGHT_MOTOR_ID);
  
  // Motor controller groups
  private final MotorControllerGroup m_leftMotors = 
      new MotorControllerGroup(m_frontLeftMotor, m_backLeftMotor);
  private final MotorControllerGroup m_rightMotors = 
      new MotorControllerGroup(m_frontRightMotor, m_backRightMotor);
  
  // The robot's drive
  private final DifferentialDrive m_drive = new DifferentialDrive(m_leftMotors, m_rightMotors);
  
  // Encoders
  private final Encoder m_frontLeftEncoder = new Encoder(
      FRONT_LEFT_ENCODER_PORTS[0], FRONT_LEFT_ENCODER_PORTS[1], false);
  private final Encoder m_backLeftEncoder = new Encoder(
      BACK_LEFT_ENCODER_PORTS[0], BACK_LEFT_ENCODER_PORTS[1], false);
  private final Encoder m_frontRightEncoder = new Encoder(
      FRONT_RIGHT_ENCODER_PORTS[0], FRONT_RIGHT_ENCODER_PORTS[1], true);
  private final Encoder m_backRightEncoder = new Encoder(
      BACK_RIGHT_ENCODER_PORTS[0], BACK_RIGHT_ENCODER_PORTS[1], true);
  
  // NetworkTable entries
  private final NetworkTableEntry m_leftEncoderEntry;
  private final NetworkTableEntry m_rightEncoderEntry;
  private final NetworkTableEntry m_leftSpeedEntry;
  private final NetworkTableEntry m_rightSpeedEntry;
  
  /**
   * Creates a new DriveSubsystem.
   */
  public DriveSubsystem() {
    // Configure motors
    configureMotors();
    
    // Configure encoders
    configureEncoders();
    
    // Configure NetworkTables
    var table = NetworkTableInstance.getDefault().getTable("Drivetrain");
    m_leftEncoderEntry = table.getEntry("LeftEncoderPosition");
    m_rightEncoderEntry = table.getEntry("RightEncoderPosition");
    m_leftSpeedEntry = table.getEntry("LeftSpeed");
    m_rightSpeedEntry = table.getEntry("RightSpeed");
    
    // Add some encoder data to SmartDashboard
    SmartDashboard.putData("Front Left Encoder", m_frontLeftEncoder);
    SmartDashboard.putData("Front Right Encoder", m_frontRightEncoder);
  }
  
  /**
   * Configure all motor controllers.
   */
  private void configureMotors() {
    // Reset all motor controllers to factory defaults
    m_frontLeftMotor.configFactoryDefault();
    m_backLeftMotor.configFactoryDefault();
    m_frontRightMotor.configFactoryDefault();
    m_backRightMotor.configFactoryDefault();
    
    // Set neutral mode to brake
    m_frontLeftMotor.setNeutralMode(NeutralMode.Brake);
    m_backLeftMotor.setNeutralMode(NeutralMode.Brake);
    m_frontRightMotor.setNeutralMode(NeutralMode.Brake);
    m_backRightMotor.setNeutralMode(NeutralMode.Brake);
    
    // Invert right side motors
    m_rightMotors.setInverted(true);
    
    // Configure voltage compensation
    m_frontLeftMotor.configVoltageCompSaturation(12);
    m_backLeftMotor.configVoltageCompSaturation(12);
    m_frontRightMotor.configVoltageCompSaturation(12);
    m_backRightMotor.configVoltageCompSaturation(12);
    
    m_frontLeftMotor.enableVoltageCompensation(true);
    m_backLeftMotor.enableVoltageCompensation(true);
    m_frontRightMotor.enableVoltageCompensation(true);
    m_backRightMotor.enableVoltageCompensation(true);
    
    // Set timeout for commands
    m_frontLeftMotor.configOpenloopRamp(0.2);
    m_backLeftMotor.configOpenloopRamp(0.2);
    m_frontRightMotor.configOpenloopRamp(0.2);
    m_backRightMotor.configOpenloopRamp(0.2);
  }
  
  /**
   * Configure all encoders.
   */
  private void configureEncoders() {
    // CIMcoder: 20 pulses per revolution, 4x encoding = 80 counts per revolution
    // Assuming 6-inch wheels and 10.71:1 gearing
    final double wheelDiameterInches = 6.0;
    final double pulsesPerRevolution = 80.0;
    final double gearRatio = 10.71;
    
    final double distancePerPulse = (Math.PI * wheelDiameterInches) / (pulsesPerRevolution * gearRatio);
    
    m_frontLeftEncoder.setDistancePerPulse(distancePerPulse);
    m_backLeftEncoder.setDistancePerPulse(distancePerPulse);
    m_frontRightEncoder.setDistancePerPulse(distancePerPulse);
    m_backRightEncoder.setDistancePerPulse(distancePerPulse);
    
    // Reset encoders
    resetEncoders();
  }
  
  /**
   * Reset all encoders to zero.
   */
  public void resetEncoders() {
    m_frontLeftEncoder.reset();
    m_backLeftEncoder.reset();
    m_frontRightEncoder.reset();
    m_backRightEncoder.reset();
  }
  
  /**
   * Drives the robot using tank drive controls.
   *
   * @param leftSpeed the left motor speed
   * @param rightSpeed the right motor speed
   */
  public void tankDrive(double leftSpeed, double rightSpeed) {
    m_drive.tankDrive(leftSpeed, rightSpeed, true);
    
    // Update the NetworkTable entries
    m_leftSpeedEntry.setDouble(leftSpeed);
    m_rightSpeedEntry.setDouble(rightSpeed);
  }
  
  /**
   * Drives the robot using arcade drive controls.
   *
   * @param speed the speed value between -1.0 and 1.0
   * @param rotation the rotation value between -1.0 and 1.0
   */
  public void arcadeDrive(double speed, double rotation) {
    m_drive.arcadeDrive(speed, rotation, true);
    
    // We don't have direct access to left/right speeds in arcade mode,
    // so we'll use the output of the motor controller groups
    m_leftSpeedEntry.setDouble(m_leftMotors.get());
    m_rightSpeedEntry.setDouble(m_rightMotors.get());
  }
  
  /**
   * Stops the drive motors.
   */
  public void stopMotors() {
    m_drive.stopMotor();
    m_leftSpeedEntry.setDouble(0);
    m_rightSpeedEntry.setDouble(0);
  }
  
  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    
    // Update NetworkTables with encoder data
    double leftPosition = (m_frontLeftEncoder.getDistance() + m_backLeftEncoder.getDistance()) / 2.0;
    double rightPosition = (m_frontRightEncoder.getDistance() + m_backRightEncoder.getDistance()) / 2.0;
    
    m_leftEncoderEntry.setDouble(leftPosition);
    m_rightEncoderEntry.setDouble(rightPosition);
    
    // Update SmartDashboard with more detailed encoder information
    SmartDashboard.putNumber("Front Left Encoder Position", m_frontLeftEncoder.getDistance());
    SmartDashboard.putNumber("Back Left Encoder Position", m_backLeftEncoder.getDistance());
    SmartDashboard.putNumber("Front Right Encoder Position", m_frontRightEncoder.getDistance());
    SmartDashboard.putNumber("Back Right Encoder Position", m_backRightEncoder.getDistance());
    
    SmartDashboard.putNumber("Front Left Encoder Rate", m_frontLeftEncoder.getRate());
    SmartDashboard.putNumber("Back Left Encoder Rate", m_backLeftEncoder.getRate());
    SmartDashboard.putNumber("Front Right Encoder Rate", m_frontRightEncoder.getRate());
    SmartDashboard.putNumber("Back Right Encoder Rate", m_backRightEncoder.getRate());
  }
}