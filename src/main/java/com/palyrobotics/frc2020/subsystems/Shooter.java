package com.palyrobotics.frc2020.subsystems;

import static com.palyrobotics.frc2020.config.constants.ShooterConstants.kTargetDistanceToHoodState;
import static com.palyrobotics.frc2020.config.constants.ShooterConstants.kTargetDistanceToVelocity;
import static com.palyrobotics.frc2020.util.Util.kEpsilon;
import static com.palyrobotics.frc2020.util.Util.withinRange;

import com.palyrobotics.frc2020.config.subsystem.ShooterConfig;
import com.palyrobotics.frc2020.robot.Commands;
import com.palyrobotics.frc2020.robot.ReadOnly;
import com.palyrobotics.frc2020.robot.RobotState;
import com.palyrobotics.frc2020.util.Util;
import com.palyrobotics.frc2020.util.config.Configs;
import com.palyrobotics.frc2020.util.control.ControllerOutput;
import com.palyrobotics.frc2020.vision.Limelight;

import edu.wpi.first.wpilibj.MedianFilter;
import edu.wpi.first.wpilibj.Timer;

public class Shooter extends SubsystemBase {

	public enum ShooterState {
		IDLE, MANUAL_VELOCITY, VISION_VELOCITY
	}

	public enum HoodState {
		LOW, MIDDLE, HIGH
	}

	private static Shooter sInstance = new Shooter();
	private Limelight mLimelight = Limelight.getInstance();
	private ShooterConfig mConfig = Configs.get(ShooterConfig.class);
	private ControllerOutput mFlywheelOutput = new ControllerOutput();
	private boolean mHoodOutput, mBlockingOutput, mRumbleOutput;
	private Timer mRumbleTimer = new Timer();
	private boolean mIsReadyToShoot;
	// TODO: Change the size of the median filter to better or worse filter out values
	private MedianFilter distanceFilter = new MedianFilter(5);

	private Shooter() {
	}

	public static Shooter getInstance() {
		return sInstance;
	}

	@Override
	public void update(@ReadOnly Commands commands, @ReadOnly RobotState robotState) {
		/* Flywheel Velocity */
		ShooterState wantedState = commands.getShooterWantedState();
		double targetFlywheelVelocity;
		switch (wantedState) {
			case MANUAL_VELOCITY:
				targetFlywheelVelocity = commands.getShooterManualWantedFlywheelVelocity();
				break;
			case VISION_VELOCITY:
				targetFlywheelVelocity = kTargetDistanceToVelocity
						.getInterpolated(distanceFilter.calculate(mLimelight.getEstimatedDistanceZ()));
				break;
			default:
				targetFlywheelVelocity = 0.0;
				break;
		}
		targetFlywheelVelocity = Util.clamp(targetFlywheelVelocity, 0.0, mConfig.maxVelocity);

		/* Hood */
		HoodState targetHoodState = kTargetDistanceToHoodState.floorEntry(targetFlywheelVelocity).getValue();
		boolean isHoodExtended = robotState.shooterIsHoodExtended,
				isBlockingExtended = robotState.shooterIsBlockingExtended;
		switch (targetHoodState) {
			case LOW:
				/*
				When we are down, always make sure our locking piston is set to unblocking.
				This is how other states tell if we are down instead of just resting on top
				of the block, since the hood piston is retracted in case those two cases,
				meaning its extension state can't be used to determine physical position.
				*/
				mHoodOutput = mBlockingOutput = false;
				break;
			case MIDDLE:
				if (isBlockingExtended) {
					/* Hood is already at the top or middle state */
					mHoodOutput = false;
					mBlockingOutput = true;
				} else {
					/* We are at the low hood position. */
					mHoodOutput = true;
					/*
					Unblock until the hood reaches the top, then block.
					This moves to the first if condition and moves the
					hood down to rest on top of the blocking piston.
					*/
					mBlockingOutput = isHoodExtended;
				}
				break;
			case HIGH:
				/*
				This assumes that we will never be in the state where
				our blocking piston is extended and our hood is pushing
				upwards against it.
				*/
				mHoodOutput = mBlockingOutput = true;
				break;
		}

		mFlywheelOutput.setTargetVelocity(targetFlywheelVelocity, mConfig.velocityGains);

		/* Rumble */
		boolean inShootingVelocityRange = targetFlywheelVelocity > kEpsilon &&
				withinRange(targetFlywheelVelocity, robotState.shooterFlywheelVelocity, mConfig.velocityTolerance),
				justChangedReadyToShoot = mIsReadyToShoot != inShootingVelocityRange;
		mIsReadyToShoot = inShootingVelocityRange;
		switch (wantedState) {
			case MANUAL_VELOCITY:
			case VISION_VELOCITY:
				boolean justEnteredReadyToShoot = justChangedReadyToShoot && inShootingVelocityRange,
						justExitedReadyToShoot = justChangedReadyToShoot && !inShootingVelocityRange;
				if (justEnteredReadyToShoot) {
					mRumbleOutput = true;
					mRumbleTimer.reset();
					mRumbleTimer.start();
				} else if (mRumbleTimer.get() > mConfig.rumbleDurationSeconds || justExitedReadyToShoot) {
					mRumbleTimer.stop();
					mRumbleOutput = false;
				}
				break;
			default:
				mRumbleOutput = false;
		}
	}

	public ControllerOutput getFlywheelOutput() {
		return mFlywheelOutput;
	}

	public boolean getHoodOutput() {
		return mHoodOutput;
	}

	public boolean getBlockingOutput() {
		return mBlockingOutput;
	}

	public boolean getRumbleOutput() {
		return mRumbleOutput;
	}

	public boolean isReadyToShoot() {
		return mIsReadyToShoot;
	}
}
