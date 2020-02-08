package com.palyrobotics.frc2020.auto;

import static com.palyrobotics.frc2020.util.Util.newWaypoint;

import com.palyrobotics.frc2020.behavior.RoutineBase;
import com.palyrobotics.frc2020.behavior.SequentialRoutine;
import com.palyrobotics.frc2020.behavior.routines.drive.DriveAlignRoutine;
import com.palyrobotics.frc2020.behavior.routines.drive.DrivePathRoutine;
import com.palyrobotics.frc2020.behavior.routines.drive.DriveSetOdometryRoutine;
import com.palyrobotics.frc2020.behavior.routines.drive.DriveYawRoutine;

/**
 * @author Alexis
 */
@SuppressWarnings ("Duplicates")
public class StartRightTrenchStealTwoShootFive extends AutoBase {

	//TODO: test
	@Override
	public RoutineBase getRoutine() {
		var initialOdometry = new DriveSetOdometryRoutine(0, 0, 0);

		var getTrenchBalls = new DrivePathRoutine(newWaypoint(90, 0, 0));
		var backup = new DrivePathRoutine(newWaypoint(75, 0, 0));

		var turn = new DriveYawRoutine(120);

		var goToShoot = new DrivePathRoutine(newWaypoint(0, 100, 120));

		// var turnAroundToShoot = new DriveYawRoutine(180.0);

		return new SequentialRoutine(initialOdometry, getTrenchBalls, backup.driveInReverse(), turn, goToShoot,
				new DriveAlignRoutine(1));
	}
}
