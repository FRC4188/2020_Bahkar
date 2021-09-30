// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.sensors;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.subsystems.sensors.Sensors;
import frc.robot.subsystems.sensors.Limelight.CameraMode;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class SetCamera extends InstantCommand {

  Sensors sensors = Sensors.getInstance();
  CameraMode mode;

  public SetCamera(CameraMode mode) {
    this.mode = mode;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    sensors.setCameraMode(mode);
  }
}
