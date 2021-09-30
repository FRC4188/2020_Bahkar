// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.drive;

import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.kinematics.SwerveDriveKinematics;
import edu.wpi.first.wpilibj.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.subsystems.sensors.Sensors;

public class Swerve extends SubsystemBase {

  private static Swerve instance = null;

  public static synchronized Swerve getInstance() {
    if (instance == null) instance = new Swerve();

    return instance;
  }

  private Module leftFront = new Module(1, 2, 21, Constants.drive.modules.M1_ZERO);
  private Module rightFront = new Module(3, 4, 22, Constants.drive.modules.M2_ZERO);
  private Module leftRear = new Module(5, 6, 23, Constants.drive.modules.M3_ZERO);
  private Module rightRear = new Module(7, 8, 24, Constants.drive.modules.M4_ZERO);

  private Odometry odometry = Odometry.getInstance();

  private Sensors sensors = Sensors.getInstance();

  //private Kinematics kinematics = Constants.drive.KINEMATICS;
  SwerveDriveKinematics kinematics = new SwerveDriveKinematics(Constants.drive.FrontLeftLocation, Constants.drive.FrontRightLocation, Constants.drive.BackLeftLocation, Constants.drive.BackRightLocation);

  private PIDController rotationPID = new PIDController(0.1, 0.0, 0.01);

  private Notifier dashboard = new Notifier(() -> smartDashboard());

  /** Creates a new Swerve. */
  private Swerve() {
    CommandScheduler.getInstance().registerSubsystem(this);
    rotationPID.enableContinuousInput(-180, 180);
    rotationPID.setTolerance(2.0);

    dashboard.startPeriodic(0.1);
  }

  @Override
  public void periodic() {
    odometry.update(sensors.getRotation(), getChassisSpeeds());
  }

  private void smartDashboard() {
    SmartDashboard.putNumber("M1 (LF) Angle", leftFront.getAbsoluteAngle());
    SmartDashboard.putNumber("M2 (RF) Angle", rightFront.getAbsoluteAngle());
    SmartDashboard.putNumber("M3 (LR) Angle", leftRear.getAbsoluteAngle());
    SmartDashboard.putNumber("M4 (RR) Angle", rightRear.getAbsoluteAngle());
    SmartDashboard.putString("Chassis Speeds", getChassisSpeeds().toString());

    SmartDashboard.putNumber("Falcon 1 Temp", leftFront.getAngleTemp());
    SmartDashboard.putNumber("Falcon 2 Temp", leftFront.getSpeedTemp());
    SmartDashboard.putNumber("Falcon 3 Temp", rightFront.getAngleTemp());
    SmartDashboard.putNumber("Falcon 4 Temp", rightFront.getSpeedTemp());
    SmartDashboard.putNumber("Falcon 5 Temp", leftRear.getAngleTemp());
    SmartDashboard.putNumber("Falcon 6 Temp", leftRear.getSpeedTemp());
    SmartDashboard.putNumber("Falcon 7 Temp", rightRear.getAngleTemp());
    SmartDashboard.putNumber("Falcon 8 Temp", rightRear.getSpeedTemp());
  }

  public void drive(double yInput, double xInput, double rotInput, boolean fieldOriented) {
    yInput *= Constants.drive.MAX_VELOCITY;
    xInput *= -Constants.drive.MAX_VELOCITY;
    rotInput *= 4.0 * Math.PI;

    if (rotInput != 0.0) {
      setRotSetpoint(-sensors.getRotation().getDegrees());
    } else {
      if (yInput != 0 || xInput != 0) {
        double correction = rotationPID.calculate(-sensors.getRotation().getDegrees());
        rotInput = /*rotationPID.atSetpoint() ? 0.0 : */correction;
      }
    }

    setChassisSpeeds(!fieldOriented ?
    ChassisSpeeds.fromFieldRelativeSpeeds(yInput, xInput, rotInput, sensors.getRotation()) :
    new ChassisSpeeds(yInput, xInput, rotInput));
  }

  public void setRotSetpoint(double setpoint) {
    rotationPID.setSetpoint(setpoint);
  }

  public void setChassisSpeeds(ChassisSpeeds speeds) {
    ChassisSpeeds newSpeeds = new ChassisSpeeds(speeds.vxMetersPerSecond, speeds.vyMetersPerSecond, speeds.omegaRadiansPerSecond);
    setModuleStates(kinematics.toSwerveModuleStates(newSpeeds));
  }

  public void setModuleStates(SwerveModuleState[] states) {
    SwerveDriveKinematics.normalizeWheelSpeeds(states, Constants.drive.MAX_VELOCITY);
    
    SmartDashboard.putNumber("Left Front Set", states[0].angle.getDegrees());
    leftFront.setModuleState(new SwerveModuleState(states[0].speedMetersPerSecond, states[0].angle));
    rightFront.setModuleState(new SwerveModuleState(states[1].speedMetersPerSecond, states[1].angle));
    leftRear.setModuleState(new SwerveModuleState(states[2].speedMetersPerSecond, states[2].angle));
    rightRear.setModuleState(new SwerveModuleState(states[3].speedMetersPerSecond, states[3].angle));
  }

  public ChassisSpeeds getChassisSpeeds() {
    return kinematics.toChassisSpeeds(getModuleStates());
  }

  public SwerveModuleState[] getModuleStates() {
    return new SwerveModuleState[] {
      leftFront.getModuleState(),
      rightFront.getModuleState(),
      leftRear.getModuleState(),
      rightRear.getModuleState()
    };
  }

  public double getFrontLeftAngleTemp() {
    return leftFront.getAngleTemp();
  }

  public double getFrontRightAngleTemp() {
    return rightFront.getAngleTemp();
  }

  public double getRearLeftAngleTemp() {
    return leftRear.getAngleTemp();
  }

  public double getRearRightAngleTemp() {
    return rightRear.getAngleTemp();
  }

  public double getFrontLeftDriveTemp() {
    return leftFront.getSpeedTemp();
  }

  public double getFrontRightDriveTemp() {
    return rightFront.getSpeedTemp();
  }

  public double getRearLeftDriveTemp() {
    return leftRear.getSpeedTemp();
  }

  public double getRearRightDriveTemp() {
    return rightRear.getSpeedTemp();
  }
}
