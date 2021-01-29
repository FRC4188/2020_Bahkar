/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Turret extends SubsystemBase {

  Sensors sensors;

  CANSparkMax turretMotor = new CANSparkMax(23, MotorType.kBrushless);
  CANEncoder turretEncoder = new CANEncoder(turretMotor);
  CANPIDController pid = new CANPIDController(turretMotor);

  boolean isTracking;

  /**
   * Creates a new Turret.
   */
  public Turret(Sensors sensors) {
    this.sensors = sensors;
    controllerInit();
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  /**
  * Configures gains for Spark closed loop controller.
  */
  private void controllerInit() {
    pid.setP(4e-5);
    pid.setI(1e-6);
    pid.setD(0);
    pid.setIZone(0.0);
    pid.setFF(1.0 / Constants.Turret.MAX_VELOCITY);
    pid.setOutputRange(-1.0, 1.0);
    pid.setSmartMotionMaxVelocity(Constants.Turret.MAX_VELOCITY, 0);
    pid.setSmartMotionMaxAccel(Constants.Turret.MAX_ACCELERATION, 0);
    turretMotor.setClosedLoopRampRate(0.0);
    turretMotor.setOpenLoopRampRate(0.5);
    turretMotor.setIdleMode(IdleMode.kBrake);
  }

  /**
  * Resets turret encoder value to 0.
  */
  public void resetEncoders() {
    turretEncoder.setPosition(0);
  }

  /**
  * Sets turret motor to given percentage [-1.0, 1.0].
  */
  public void set(double percent) {
    turretMotor.set(percent);
  }

  /**
   * Turns turret to angle in degrees.
   */
  public void setAngle(double angle) {
      angle /= Constants.Turret.ENCODER_TO_DEGREES;
      pid.setReference(angle, ControlType.kSmartMotion);
  }

  public void trackTarget(boolean cont) {
    double angle = sensors.getTurretHorizontalAngle();
    double offset = sensors.getTurretOffset();
    double power = cont ? -(angle + offset) / 47 : 0;

    isTracking = cont;

    set(power);
  }

  
    /**
     * Returns turret encoder position in degrees.
     */
    public double getPosition() {
      return turretEncoder.getPosition() * Constants.Turret.ENCODER_TO_DEGREES;
  }

  /**
   * Returns turret encoder velocity in degrees per second.
   */
  public double getVelocity() {
      return turretEncoder.getVelocity() * Constants.Turret.ENCODER_TO_DEGREES / 60.0;
  }

  public double getTemperature() {
    return turretMotor.getMotorTemperature();
  }

  /**
   * Sets the isTracking variable (for SmartDashboard purposes).
   */
  public void setTracking(boolean track) {
      isTracking = track;
  }

  /**
   * Returns turret motor temperature in Celcius.
   */
  public double getTemp() {
      return turretMotor.getMotorTemperature();
  }

  public boolean getIsAimed() {
    double angle = sensors.getTurretHorizontalAngle() - sensors.getTurretOffset();
    boolean aimed = (angle < 1.0 && angle > 1.0) ? true : false;
    return aimed;
  }
}
