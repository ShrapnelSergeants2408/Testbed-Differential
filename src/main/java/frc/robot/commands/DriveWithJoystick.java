// DriveWithJoystick.java
package frc.robot.commands;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.DriveSubsystem;

public class DriveWithJoystick extends Command {
  private final DriveSubsystem m_drive;
  
  private final DoubleSupplier m_leftStickY;
  private final DoubleSupplier m_rightStickY;
  private final DoubleSupplier m_arcadeStickY;
  private final DoubleSupplier m_arcadeStickX;
  private final BooleanSupplier m_isArcadeDrive;

  /**
   * Creates a new DriveWithJoystick command.
   *
   * @param subsystem The drive subsystem this command will run on
   * @param leftStickY The DoubleSupplier for the left joystick Y axis for tank drive
   * @param rightStickY The DoubleSupplier for the right joystick Y axis for tank drive
   * @param arcadeStickY The DoubleSupplier for the arcade drive forward/reverse
   * @param arcadeStickX The DoubleSupplier for the arcade drive turning
   * @param isArcadeDrive A BooleanSupplier that indicates whether to use arcade drive
   */
  public DriveWithJoystick(
      DriveSubsystem subsystem, 
      DoubleSupplier leftStickY,
      DoubleSupplier rightStickY,
      DoubleSupplier arcadeStickY,
      DoubleSupplier arcadeStickX,
      BooleanSupplier isArcadeDrive) {
    m_drive = subsystem;
    m_leftStickY = leftStickY;
    m_rightStickY = rightStickY;
    m_arcadeStickY = arcadeStickY;
    m_arcadeStickX = arcadeStickX;
    m_isArcadeDrive = isArcadeDrive;
    
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(subsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    // Xbox controller inputs are inverted (pushing forward gives a negative value)
    // So we negate them for more intuitive control
    if (m_isArcadeDrive.getAsBoolean()) {
      // Use arcade drive with left stick
      m_drive.arcadeDrive(
          -applyDeadband(m_arcadeStickY.getAsDouble()),
          applyDeadband(m_arcadeStickX.getAsDouble()));
    } else {
      // Use tank drive with both sticks
      m_drive.tankDrive(
          -applyDeadband(m_leftStickY.getAsDouble()),
          -applyDeadband(m_rightStickY.getAsDouble()));
    }
  }

  // Apply deadband to joystick values to eliminate drift
  private double applyDeadband(double value) {
    final double deadband = 0.1;
    if (Math.abs(value) < deadband) {
      return 0.0;
    }
    // Scale the remaining values from deadband to 1.0 to the full range
    return Math.copySign((Math.abs(value) - deadband) / (1.0 - deadband), value);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_drive.stopMotors();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    // This should run until interrupted
    return false;
  }
}