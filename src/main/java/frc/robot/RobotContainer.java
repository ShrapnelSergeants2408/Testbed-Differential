// RobotContainer.java
package frc.robot;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.cscore.UsbCamera;
import edu.wpi.first.cscore.VideoSource.ConnectionStrategy;
import frc.robot.commands.DriveWithJoystick;
import frc.robot.subsystems.DriveSubsystem;

public class RobotContainer {
  // The robot's subsystems
  private final DriveSubsystem m_driveSubsystem = new DriveSubsystem();
  
  // The driver's controller
  private final XboxController m_driverController = new XboxController(0);
  
  // Drive mode selection
  private boolean m_isArcadeDrive = false;

  // Camera
  private UsbCamera m_camera;
  
  /**
   * The container for the robot. Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {
    // Initialize camera and send to dashboard
    initializeCamera();
    
    // Configure the button bindings
    configureButtonBindings();
    
    // Set default command for drive subsystem
    m_driveSubsystem.setDefaultCommand(
      new DriveWithJoystick(
        m_driveSubsystem,
        () -> m_driverController.getLeftY(),
        () -> m_driverController.getRightY(),
        () -> m_driverController.getLeftY(),
        () -> m_driverController.getRightX(),
        () -> m_isArcadeDrive
      )
    );
  }

  /**
   * Initialize the USB camera and send to dashboard
   */
  private void initializeCamera() {
    // Start camera capture
    m_camera = CameraServer.startAutomaticCapture();
    
    // Configure camera settings
    m_camera.setResolution(320, 240);
    m_camera.setFPS(15);
    m_camera.setConnectionStrategy(ConnectionStrategy.kKeepOpen);
    
    // Send to dashboard
    SmartDashboard.putString("Camera Status", "Initialized");
  }
  
  /**
   * Use this method to define your button->command mappings.
   */
  private void configureButtonBindings() {
    // Toggle between tank and arcade drive when START button is pressed
    new JoystickButton(m_driverController, XboxController.Button.kStart.value)
      .onTrue(new edu.wpi.first.wpilibj2.command.InstantCommand(() -> {
          m_isArcadeDrive = !m_isArcadeDrive;
          SmartDashboard.putString("Drive Mode", m_isArcadeDrive ? "Arcade Drive" : "Tank Drive");
        })
      );
  }
  
  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    // No autonomous command for this example
    return null;
  }
}