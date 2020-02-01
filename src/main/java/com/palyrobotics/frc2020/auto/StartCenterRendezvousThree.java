package com.palyrobotics.frc2020.auto;

import static com.palyrobotics.frc2020.util.Util.newWaypoint;

import com.palyrobotics.frc2020.behavior.RoutineBase;
import com.palyrobotics.frc2020.behavior.SequentialRoutine;
import com.palyrobotics.frc2020.behavior.routines.drive.DrivePathRoutine;
import com.palyrobotics.frc2020.behavior.routines.drive.DriveSetOdometryRoutine;
import com.palyrobotics.frc2020.behavior.routines.drive.DriveYawRoutine;
import com.palyrobotics.frc2020.behavior.routines.vision.VisionAlignRoutine;

@SuppressWarnings ("Duplicates")
public class StartCenterRendezvousThree extends AutoBase {

	@Override
	public RoutineBase getRoutine() {
		var initialOdometry = new DriveSetOdometryRoutine(0, 0, 180);

		var turn = new DriveYawRoutine(0);

		var getTrenchBalls1 = new DrivePathRoutine(newWaypoint(70, -40, 0));
		var backup1 = new DrivePathRoutine(newWaypoint(50, -0, 0));

		var getTrenchBalls2 = new DrivePathRoutine(newWaypoint(80, -60, 0));

		var turn2 = new DriveYawRoutine(180);

		var goToShoot = new DrivePathRoutine(newWaypoint(0, 0, 180));

		// var turnAroundToShoot = new DriveYawRoutine(180.0);

		return new SequentialRoutine(initialOdometry, turn, getTrenchBalls1, backup1.driveInReverse(), getTrenchBalls2,
				turn2, goToShoot, new VisionAlignRoutine());
	}
}