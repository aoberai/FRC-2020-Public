package com.palyrobotics.frc2020.auto;

import static com.palyrobotics.frc2020.util.Util.newWaypoint;

import com.palyrobotics.frc2020.behavior.RoutineBase;
import com.palyrobotics.frc2020.behavior.SequentialRoutine;
import com.palyrobotics.frc2020.behavior.routines.drive.DrivePathRoutine;
import com.palyrobotics.frc2020.behavior.routines.drive.DriveYawRoutine;
import com.palyrobotics.frc2020.behavior.routines.drive.SetOdometryRoutine;

@SuppressWarnings ("Duplicates")
public class StartCenterTwoPointTurnFriendlyTrenchThree extends AutoModeBase {

	@Override
	public RoutineBase getRoutine() {

		var initialOdometry = new SetOdometryRoutine(0, 0, 180);

		var point1 = new DrivePathRoutine(newWaypoint(25, -35, 90));

		var point2 = new DrivePathRoutine(newWaypoint(80, 45, 0));

		var getTrenchBalls = new DrivePathRoutine(newWaypoint(170, 45, 0));

		var turnAroundToShoot = new DriveYawRoutine(180.0);

		return new SequentialRoutine(initialOdometry, point1.driveInReverse(), point2, getTrenchBalls,
				turnAroundToShoot);
	}
}
