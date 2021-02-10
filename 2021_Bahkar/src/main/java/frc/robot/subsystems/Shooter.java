package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.InvertType;
import com.ctre.phoenix.motorcontrol.NeutralMode;

import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.utils.components.CSPFalcon;

public class Shooter extends SubsystemBase {

    private final CSPFalcon upperShooterMotor = new CSPFalcon(10);
    private final CSPFalcon lowerShooterMotor = new CSPFalcon(11);

    Notifier shuffle;
    
    //https://www.omnicalculator.com/physics/projectile-motion

    public Shooter() {
        //set inversions
        lowerShooterMotor.setInverted(true);
        upperShooterMotor.setInverted(InvertType.FollowMaster);
        //set slave motor
        upperShooterMotor.follow(lowerShooterMotor);
        
        // configuration
        setCoast();
        PIDConfig();
        setRampRate();

        SmartDashboard.putNumber("Set Shooter Velocity", 0.0);

        shuffle = new Notifier(() -> updateShuffleboard());
    }

    @Override
    public void periodic() {
    }


    public void PIDConfig() {
        lowerShooterMotor.setPIDF(Constants.Shooter.kP, Constants.Shooter.kI, Constants.Shooter.kD, Constants.Shooter.kF);
    }

    private void updateShuffleboard() {
        SmartDashboard.putNumber("Shooter Speed", getUpperVelocity());
    }

    public void closeNotifier() {
        shuffle.close();
    }

    public void openNotifier() {
        shuffle.startPeriodic(0.1);
      }

    /**
     * Sets shooter motors to a given percentage [-1.0, 1.0].
     */
    public void setPercentage(double percent) {
        lowerShooterMotor.set(percent);
    }

    /**
     * Sets shooter motors to a given velocity in rpm.
     */
    public void setVelocity(double velocity) {
        lowerShooterMotor.setVelocity(velocity);
    }

    /**
     * Sets shooter motors to brake mode.
     */
    public void setBrake() {
        lowerShooterMotor.setNeutralMode(NeutralMode.Brake);
        upperShooterMotor.setNeutralMode(NeutralMode.Brake);
    }

    /**
     * Sets shooter motors to coast mode.
     */
    public void setCoast() {
        lowerShooterMotor.setNeutralMode(NeutralMode.Coast);
        upperShooterMotor.setNeutralMode(NeutralMode.Coast);
    }

    /**
     * Configures shooter motor ramp rates.
     */
    public void setRampRate() {
        lowerShooterMotor.configClosedloopRamp(Constants.Shooter.RAMP_RATE);
        lowerShooterMotor.configOpenloopRamp(Constants.Shooter.RAMP_RATE);
    }

    /**
     * Gets left shooter motor velocity in rpm.
     */
    public double getLowerVelocity() {
        return lowerShooterMotor.getVelocity();
    }

    /**
     * Gets right shooter motor velocity in rpm.
     */
    public double getUpperVelocity() {
        return upperShooterMotor.getVelocity();
    }

    /**
     * Returns left shooter motor temperature in Celcius.
     */
    public double getLowerTemp() {
        return lowerShooterMotor.getTemperature();
    }

    /**
     * Returns right shooter motor temperature in Celcius.
     */
    public double getUpperTemp() {
        return upperShooterMotor.getTemperature();
    }

    public boolean isReady() {
        return Math.abs(getLowerVelocity() - Constants.Shooter.SHOOTING_VEL) < Constants.Shooter.SHOOTING_TOLERANCE;
    }
}