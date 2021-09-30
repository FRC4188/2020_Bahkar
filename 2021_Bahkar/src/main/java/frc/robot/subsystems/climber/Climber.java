package frc.robot.subsystems.climber;

import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

/**
 * Class encapsulating climber function.
 */
public class Climber extends SubsystemBase {

    private static Climber instance;

    public synchronized static Climber getInstance() {
        if (instance == null) instance = new Climber();
        return instance;
    }

    private DualMotor motors = new DualMotor(13, 41);

    // pneumatics
    private Solenoid climberSolenoid = new Solenoid(2);// needs to change

    private Notifier shuffle;

    /**
     * Constructs a new Climber object and configures devices.
     */
    private Climber() {

        motors.setInverted(false);

        // init
        controllerInit();
        setBrake();
        setRampRate();

        // reset devices
        resetEncoders();

        shuffle = new Notifier(() -> updateShuffleboard());
        shuffle.startPeriodic(0.1);
    }

    /**
     * Runs every loop.
     */
    @Override
    public void periodic() {
        //updateShuffleboard();
    }

    /**
     * Writes values to the Shuffleboard.
     */
    public void updateShuffleboard() {
        SmartDashboard.putNumber("Left climber height", getPosition());
        SmartDashboard.putNumber("Left climber velocity", getVelocity());
        SmartDashboard.putBoolean("Climber brake", !climberSolenoid.get());
    }

    /**
     * Config Pid loop stuff. Have Locke explain.
     */
    public void controllerInit() {
    }

    /**
     * Sets both climber motors to a given percentage [-1.0, 1.0].
     */
    public void set(double percent) {
        motors.set(percent);
    }

    /**
     * Fires the break pistons to stop the climber.
     */
    public void engagePneuBrake(boolean output) {
        //climberSolenoid.set(output);
    }

    /**
     * Sets Climber motors to brake mode.
     */
    public void setBrake() {
        motors.brake(true);
    }

    /**
     * Sets climber ramp rates.
     */
    public void setRampRate() {
        motors.setRampRate(Constants.climber.RAMP_RATE);
    }

    /**
     * Resets encoder values to 0 for both sides of Climber.
     */
    public void resetEncoders() {
        motors.reset();
    }

    /**
     * Returns left encoder position in feet.
     */
    public double getPosition() {
        return motors.getPosition();
    }

    /**
     * Returns the left climber velocity in rpm.
     */
    public double getVelocity() {
        return motors.getVelocity();
    }

    /**
     * Returns the maximum position of the climber in raw encoder ticks.
     */
    public double getMaxPosition() {
        return Constants.climber.MAX_POSITION;
    }

    /**
     * Returns the minimum position of the climber in raw encoder ticks.
     */
    public double getMinPosition() {
        return Constants.climber.MIN_POSITION;
    }

    /**
     * Returns left climber motor temperature in Celcius.
     */
    public double getFalconTemp() {
        return motors.getFalconTemp();
    }

    /**
     * Returns right climber motor temperature in Celcius.
     */
    public double getNeoTemp() {
        return motors.getNeoTemp();
    }

}