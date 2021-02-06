/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.hood;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Hood;

public class SetHood extends CommandBase {
  private Hood hood;
  private double position = 0.0;
  private DoubleSupplier dashRef = null;

  private boolean sup;

  /**
   * Creates a new MoveHood.
   */
  public SetHood(Hood hood, double position) {
    addRequirements(hood);
      this.hood = hood;
      this.position = position;

      sup = false;
  }

  public SetHood(Hood hood, DoubleSupplier dashRef) {
    this.hood = hood;
    this.dashRef = dashRef;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    if (sup) position = dashRef.getAsDouble();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    hood.setHoodPosition(position);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
