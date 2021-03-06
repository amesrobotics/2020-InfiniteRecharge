/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
// Wpilib imports
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

// SparkMAX imports
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

public class IntakeSubsystem extends SubsystemBase {

  static CANSparkMax intakeShaft;
  static CANSparkMax intakeActuator;
  static CANSparkMax polyLoop;
  static CANEncoder indexEncoder;
  public static Servo cameraIntake;

  public static boolean currentExtended = false;
  public static boolean currentRetracted = true;
  public boolean shouldSpin = false;
  static final double CURRENT_CONST = 19.0;
  private double lastTimeWasExtended = -100;

  public IntakeSubsystem() {
    intakeShaft = new CANSparkMax(Constants.BallCollectConstants.kSpinID, MotorType.kBrushless);
    intakeActuator = new CANSparkMax(Constants.BallCollectConstants.kActuateID, MotorType.kBrushed);
    cameraIntake = new Servo(Constants.BallCollectConstants.kCameraServo);
    
    intakeShaft.setSmartCurrentLimit(30);
    intakeActuator.setSmartCurrentLimit(28);
  }

  public void setExtend(){
      // This makes sense: if the output is less than the constant, it will continue to extend
      if (intakeActuator.getOutputCurrent() < CURRENT_CONST && !currentExtended) {
        currentRetracted = false; 
        intakeActuator.set(-0.65);
        cameraIntake.setAngle(40);
      } else {
        intakeActuator.set(0.0);
        currentExtended = true;
      }

  }

  public void setRetract(){
    
    if (intakeActuator.getOutputCurrent() < CURRENT_CONST && !currentRetracted) { 
      currentExtended = false;
      intakeActuator.set(0.65);
      cameraIntake.setAngle(100);
    } else{
      currentRetracted = true;
      intakeActuator.stopMotor();
    }
  }

  @Override
  public void periodic() {

    SmartDashboard.putNumber("intake amp draw", intakeActuator.getOutputCurrent());
    SmartDashboard.putNumber("Camera Angle", cameraIntake.getAngle());

    if(currentExtended)
      lastTimeWasExtended = Timer.getFPGATimestamp();

    if(Timer.getFPGATimestamp() < lastTimeWasExtended+1)
      intakeShaft.set(-0.5);
    else
      intakeShaft.stopMotor();

      SmartDashboard.putBoolean("currentExtended", currentExtended);
      SmartDashboard.putBoolean("currentRetracted", currentRetracted);
  }
}
