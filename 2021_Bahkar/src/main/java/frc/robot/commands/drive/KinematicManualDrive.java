/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.drive;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

import com.ctre.phoenix.motorcontrol.NeutralMode;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Drivetrain;
import frc.robot.utils.CspController;

public class KinematicManualDrive extends CommandBase {

  Drivetrain drivetrain;
  DoubleSupplier speed;
  DoubleSupplier strafe;
  DoubleSupplier rotation;
  DoubleSupplier angle;
  BooleanSupplier noAngle;
  BooleanSupplier FO;

  /**
   * Creates a new KinematicManualDrive.
   */
  public KinematicManualDrive(Drivetrain drivetrain, DoubleSupplier speed, DoubleSupplier strafe, DoubleSupplier rotation, DoubleSupplier angle, BooleanSupplier noAngle, BooleanSupplier FO) {
    addRequirements(drivetrain);

    this.drivetrain = drivetrain;
    this.speed = speed;
    this.strafe = strafe;
    this.rotation = rotation;
    this.angle = angle;
    this.noAngle = noAngle;
    this.FO = FO;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    SmartDashboard.putBoolean("Manual Drive", true);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    drivetrain.drive(speed, strafe, rotation, angle, noAngle, FO);
    drivetrain.updateOdometry();
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    SmartDashboard.putBoolean("Manual Drive", false);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
