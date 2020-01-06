package com.palyrobotics.frc2020.subsystems.controllers;

import com.palyrobotics.frc2020.config.RobotState;
import com.palyrobotics.frc2020.config.constants.DrivetrainConstants;
import com.palyrobotics.frc2020.config.subsystem.DriveConfig;
import com.palyrobotics.frc2020.subsystems.Drive;
import com.palyrobotics.frc2020.util.SparkDriveSignal;
import com.palyrobotics.frc2020.util.config.Configs;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.controller.RamseteController;
import edu.wpi.first.wpilibj.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveKinematics;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.wpilibj.trajectory.Trajectory;

public class DriveRamseteController implements Drive.DriveController {

    public static final double B = 2.0, ZETA = 0.7;

    private final RamseteController mController;
    private final Trajectory mTrajectory;
    private final SparkDriveSignal mOutput;
    private final DriveConfig mDriveConfig = Configs.get(DriveConfig.class);
    private final Timer mTimer = new Timer();
    private final DifferentialDriveKinematics mKinematics = new DifferentialDriveKinematics(DrivetrainConstants.kTrackWidthMeters);

    public DriveRamseteController(Trajectory trajectory) {
        mTrajectory = trajectory;
        mController = new RamseteController(B, ZETA);
        mTimer.reset();
        mTimer.start();
        mOutput = new SparkDriveSignal();
    }

    @Override
    public SparkDriveSignal update(RobotState state) {
        ChassisSpeeds speeds = mController.calculate(state.drivePose, mTrajectory.sample(mTimer.get()));
        DifferentialDriveWheelSpeeds wheelSpeeds = mKinematics.toWheelSpeeds(speeds);
        mOutput.leftOutput.setTargetVelocity(wheelSpeeds.leftMetersPerSecond, mDriveConfig.velocityGains);
        mOutput.rightOutput.setTargetVelocity(wheelSpeeds.rightMetersPerSecond, mDriveConfig.velocityGains);
        return mOutput;
    }

    @Override
    public boolean onTarget() {
        return false;
    }
}