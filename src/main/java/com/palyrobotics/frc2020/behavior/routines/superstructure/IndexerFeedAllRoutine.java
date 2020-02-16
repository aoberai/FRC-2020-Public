package com.palyrobotics.frc2020.behavior.routines.superstructure;

import java.util.Set;

import com.palyrobotics.frc2020.behavior.TimeoutRoutineBase;
import com.palyrobotics.frc2020.config.subsystem.IndexerConfig;
import com.palyrobotics.frc2020.robot.Commands;
import com.palyrobotics.frc2020.robot.ReadOnly;
import com.palyrobotics.frc2020.robot.RobotState;
import com.palyrobotics.frc2020.subsystems.Indexer;
import com.palyrobotics.frc2020.subsystems.SubsystemBase;
import com.palyrobotics.frc2020.util.config.Configs;

import edu.wpi.first.wpilibj.Timer;

public class IndexerFeedAllRoutine extends TimeoutRoutineBase {

	private Timer mReverseTimer = new Timer();
	private IndexerConfig mConfig = Configs.get(IndexerConfig.class);

	public IndexerFeedAllRoutine() {
		super(5.0);
	}

	public IndexerFeedAllRoutine(double timeoutSeconds) {
		super(timeoutSeconds);
	}

	@Override
	public void start(Commands commands, @ReadOnly RobotState state) {
		mReverseTimer.start();
	}

	@Override
	protected void update(Commands commands, @ReadOnly RobotState state) {
		if (state.shooterIsReadyToShoot) {
			commands.indexerWantedBeltState = mReverseTimer.get() > mConfig.reverseTime ? Indexer.BeltState.FEED_ALL : Indexer.BeltState.REVERSING;
		} else {
			commands.indexerWantedBeltState = mReverseTimer.get() > mConfig.reverseTime ? Indexer.BeltState.WAITING_TO_FEED : Indexer.BeltState.REVERSING;
			commands.indexerWantedHopperState = Indexer.HopperState.CLOSED;
		}
	}

	@Override
	protected void stop(Commands commands, @ReadOnly RobotState state) {
		commands.indexerWantedBeltState = Indexer.BeltState.IDLE;
	}

	@Override
	public boolean checkIfFinishedEarly(@ReadOnly RobotState state) {
		return false;
	}

	@Override
	public Set<SubsystemBase> getRequiredSubsystems() {
		return Set.of(mIndexer);
	}
}
