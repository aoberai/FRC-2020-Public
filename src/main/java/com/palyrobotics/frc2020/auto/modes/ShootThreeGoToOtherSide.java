package com.palyrobotics.frc2020.auto.modes;

import java.util.ArrayList;
import java.util.List;

import com.palyrobotics.frc2020.auto.AutoModeBase;
import com.palyrobotics.frc2020.behavior.Routine;
import com.palyrobotics.frc2020.behavior.SequentialRoutine;
import com.palyrobotics.frc2020.behavior.routines.drive.DrivePathRoutine;
import com.palyrobotics.frc2020.behavior.routines.shooter.ShootAllBallsRoutine;

import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Rotation2d;

@SuppressWarnings ("Duplicates")
public class ShootThreeGoToOtherSide extends AutoModeBase {

	@Override
	public Routine getRoutine() {
		List<Routine> routines = new ArrayList<>();

		List<Pose2d> otherSide = new ArrayList<>();
		otherSide.add(new Pose2d(350, 0, Rotation2d.fromDegrees(0)));

		// shoot 3 balls
		// pick up 2 balls
		routines.add(new ShootAllBallsRoutine());
		routines.add(new DrivePathRoutine(otherSide));

		return new SequentialRoutine(routines);
	}
}
