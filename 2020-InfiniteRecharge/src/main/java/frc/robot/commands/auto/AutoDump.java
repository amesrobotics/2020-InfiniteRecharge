/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.auto;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;

import frc.robot.subsystems.DumperSubsystem;

public class AutoDump extends CommandBase {
  private DumperSubsystem shooter;
  double startTime = 10;
  double timeNow;

  public AutoDump(DumperSubsystem shooter) {
    addRequirements(shooter);
    this.shooter = shooter;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    timeNow = Timer.getFPGATimestamp();
    shooter.resetIndexer();
    shooter.setDumpLowSpeed();
    shooter.setDumpForward(true);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    shooter.stopDump();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    // Once this has run for 3 seconds, then stop the program
    return Timer.getFPGATimestamp() >= timeNow + startTime;
  }
}
