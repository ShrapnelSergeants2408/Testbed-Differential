# FRC Testbed Robot Differential Drive Base

This repository contains the code for a basic FRC (FIRST Robotics Competition) robot drive base using WPILib's command-based framework. This project serves as a foundation for further development with additional subsystems and game-specific functionality.

## Hardware Configuration

### Motor Controllers
- **Left Side Drive Motors**:
  - Front Left: WPI_TalonSRX (CAN ID: 20)
  - Back Left: WPI_VictorSPX (CAN ID: 21)
- **Right Side Drive Motors**:
  - Front Right: WPI_TalonSRX (CAN ID: 22)
  - Back Right: WPI_VictorSPX (CAN ID: 23)

### Encoders (Digital I/O)
- **Front Left Encoder**: DIO ports 0, 1
- **Back Left Encoder**: DIO ports 2, 3
- **Front Right Encoder**: DIO ports 4, 5
- **Back Right Encoder**: DIO ports 6, 7

### Camera
- USB Camera with 320x240 resolution at 15 FPS

## Robot Functionality

### Drive System
- **Dual Drive Modes**:
  - Tank Drive: Direct control of left and right sides independently
  - Arcade Drive: Single-stick control (forward/backward and rotation)
  - Toggle between modes using the START button on the controller

### Camera Feed
- Provides video feedback to the driver station
- Configured for low-bandwidth operation (320x240 at 15 FPS)

### Telemetry and Feedback
- Encoder position and rate tracking for all four motors
- Robot status reporting (Initialized, Disabled, Autonomous, Teleop, Test)
- Drive mode indicator (Tank Drive or Arcade Drive)
- Detailed drivetrain data published to NetworkTables

### Motor Configuration
- Brake mode when stopped (not coast)
- Voltage compensation set to 12V
- Ramping (0.2 seconds) to prevent sudden changes in speed
- Right side motors are inverted for proper direction

### Encoder Configuration
- Configured for 6-inch wheels with 10.71:1 gear ratio
- Using CIMcoders (20 pulses per revolution, 4x encoding = 80 counts per revolution)

## Control System

### Controller Setup
- **Controller Type**: Xbox Controller (port 0)

### Controls
- **Tank Drive Mode**:
  - Left stick Y-axis: Controls left side of drivetrain
  - Right stick Y-axis: Controls right side of drivetrain
  
- **Arcade Drive Mode**:
  - Left stick Y-axis: Forward/backward movement
  - Right stick X-axis: Rotation/turning
  
- **Mode Toggle**: START button switches between Tank and Arcade drive modes

### Input Processing
- Inputs have a deadband of 0.1 to prevent drift
- Values are inverted (negated) to make pushing forward on the stick move the robot forward
- Values are scaled after deadband application

## Software Architecture

The code follows the Command-Based programming paradigm typical of FRC robots:

### Subsystems
- **DriveSubsystem**: Manages the drivetrain hardware and provides methods for different drive types
- **ExampleSubsystem**: Included but not used (template code)

### Commands
- **DriveWithJoystick**: Default command that controls the robot based on joystick input
- **ExampleCommand**: Included but not used (template code)

### Robot Container
- Configures button bindings
- Initializes subsystems
- Sets up default commands
- Manages the camera

### Autonomous
- No autonomous commands are implemented in this code
- The framework for autonomous is present but returns null

## Getting Started

1. Clone this repository to your local machine
2. Open the project in WPILib VS Code
3. Build and deploy to your robot using the WPILib deployment tools

## Future Enhancements

This code provides a solid foundation for a robot drive base. Possible enhancements include:

- Implementing autonomous routines
- Adding additional subsystems for game-specific mechanisms
- Implementing path following for complex autonomous trajectories
- Adding sensor-based alignment or assistance features
