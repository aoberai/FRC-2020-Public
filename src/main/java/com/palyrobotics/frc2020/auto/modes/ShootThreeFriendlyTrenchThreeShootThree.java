package com.palyrobotics.frc2020.auto.modes;

import java.util.ArrayList;
import java.util.List;

import com.palyrobotics.frc2020.auto.AutoModeBase;
import com.palyrobotics.frc2020.behavior.RoutineBase;
import com.palyrobotics.frc2020.behavior.SequentialRoutine;
import com.palyrobotics.frc2020.behavior.routines.drive.DriveParallelPathRoutine;
import com.palyrobotics.frc2020.behavior.routines.drive.DrivePathRoutine;
import com.palyrobotics.frc2020.behavior.routines.intake.IntakeBallRoutine;
import com.palyrobotics.frc2020.behavior.routines.shooter.ShootAllBallsRoutine;

import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.util.Units;

@SuppressWarnings ("Duplicates")
public class ShootThreeFriendlyTrenchThreeShootThree extends AutoModeBase {

	@Override
	public RoutineBase getRoutine() {
		List<RoutineBase> routines = new ArrayList<>();

		List<Pose2d> friendlyTrench = new ArrayList<>();

		friendlyTrench.add(new Pose2d(Units.inchesToMeters(0), Units.inchesToMeters(0), Rotation2d.fromDegrees(0)));
		// friendlyTrench.add(new Pose2d(Units.inchesToMeters(140),
		// Units.inchesToMeters(0), Rotation2d.fromDegrees(0)));
		// pick up ball
		// friendlyTrench.add(new Pose2d(Units.inchesToMeters(170),
		// Units.inchesToMeters(0), Rotation2d.fromDegrees(0)));
		// pick up ball
		friendlyTrench.add(new Pose2d(Units.inchesToMeters(200), Units.inchesToMeters(0), Rotation2d.fromDegrees(0)));
		// pick up ball

		routines.add(new ShootAllBallsRoutine());

		// shoot three balls
		routines.add(
				new DriveParallelPathRoutine(new IntakeBallRoutine(0.0), new DrivePathRoutine(friendlyTrench), 0.8));

		routines.add(new ShootAllBallsRoutine());
		// shoot three balls

		return new SequentialRoutine(routines);
	}
}
