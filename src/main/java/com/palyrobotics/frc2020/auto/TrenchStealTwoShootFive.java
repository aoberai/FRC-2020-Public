package com.palyrobotics.frc2020.auto;

import static com.palyrobotics.frc2020.util.Util.newWaypoint;

import com.palyrobotics.frc2020.behavior.ParallelRoutine;
import com.palyrobotics.frc2020.behavior.RoutineBase;
import com.palyrobotics.frc2020.behavior.SequentialRoutine;
import com.palyrobotics.frc2020.behavior.routines.ParallelRaceRoutine;
import com.palyrobotics.frc2020.behavior.routines.TimedRoutine;
import com.palyrobotics.frc2020.behavior.routines.drive.DriveAlignRoutine;
import com.palyrobotics.frc2020.behavior.routines.drive.DrivePathRoutine;
import com.palyrobotics.frc2020.behavior.routines.drive.DriveSetOdometryRoutine;
import com.palyrobotics.frc2020.behavior.routines.drive.DriveYawRoutine;
import com.palyrobotics.frc2020.behavior.routines.superstructure.*;
import com.palyrobotics.frc2020.subsystems.Indexer;

/**
 * Start by facing and aligning your left wheel to cover the trench. Pull the robot back until just
 * the back bumper covers the initiation line. The left ball should be centered in the intake from
 * this configuration.
 *
 * @author Jason
 */
@SuppressWarnings ("Duplicates")
public class TrenchStealTwoShootFive extends AutoBase {

	@Override
	public RoutineBase getRoutine() {
		var setInitialOdometry = new DriveSetOdometryRoutine(0, 0, 0);

		var getTrenchBalls = new ParallelRaceRoutine(
				new SequentialRoutine(
						new DrivePathRoutine(newWaypoint(92, 0, 0))
								.setMovement(1.5, 2.4),
						new DriveYawRoutine(15.0)),
//						new DrivePathRoutine(newWaypoint(76, 0, 0))
//								.setMovement(2.7, 2.4)
//								.driveInReverse(),
//						new DrivePathRoutine(newWaypoint(92, -10, -15))
//								.setMovement(2.7, 2.4)),
				new IndexerTimeRoutine(Double.POSITIVE_INFINITY),
				new IntakeBallRoutine(Double.POSITIVE_INFINITY));

		var goToShoot = new ParallelRoutine(
				new DrivePathRoutine(newWaypoint(50, 150, 180))
						.setMovement(5.0, 8.0)
						.driveInReverse(),
				new IndexerTimeRoutine(1.5),
				new SequentialRoutine(
						new IndexerHopperRoutine(Indexer.HopperState.OPEN),
						new IndexerHopperRoutine(Indexer.HopperState.CLOSED),
						new IndexerTimeRoutine(1.0)));

		var shootBalls = new SequentialRoutine(
				new DriveAlignRoutine(0),
				new ParallelRoutine(
						new ShooterVisionRoutine(6),
						new SequentialRoutine(
								new TimedRoutine(1.0),
								new IndexerFeedAllRoutine(5, false, true))));

//		var turnAndIntake = new SequentialRoutine(
//				new DriveYawRoutine(180.0),
//				new DriveSetOdometryRoutine(0, 0, 0.0),
//				new ParallelRoutine(
//						new DrivePathRoutine(newWaypoint(10, 0, 0.0)),
//						new IntakeBallRoutine(2),
//						new IndexerTimeRoutine(2)));

		return new SequentialRoutine(setInitialOdometry, getTrenchBalls, goToShoot, shootBalls);
	}
}
