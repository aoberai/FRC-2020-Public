package com.palyrobotics.frc2020.auto.modes;

import com.palyrobotics.frc2020.auto.AutoModeBase;
import com.palyrobotics.frc2020.behavior.Routine;
import com.palyrobotics.frc2020.behavior.SequentialRoutine;
import com.palyrobotics.frc2020.behavior.routines.drive.DrivePathRoutine;
import com.palyrobotics.frc2020.behavior.routines.drive.ParallelDrivePathRoutine;
import com.palyrobotics.frc2020.behavior.routines.intake.IntakeBallRoutine;
import com.palyrobotics.frc2020.behavior.routines.shooter.ShootAllBallsRoutine;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Rotation2d;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("Duplicates")
public class RightRendezvousTwoShootFive extends AutoModeBase {

    @Override
    public Routine getRoutine() {
        return new SequentialRoutine(driveForward());
    }

    public Routine driveForward() {
        ArrayList<Routine> routines = new ArrayList<>();
        List<Pose2d> rendezvous1 = new ArrayList<>();
        // TODO: Update the positions
        rendezvous1.add(new Pose2d(120, -80, Rotation2d.fromDegrees(0)));
        rendezvous1.add(new Pose2d(110, -60, Rotation2d.fromDegrees(0)));

        List<Pose2d> shoot = new ArrayList<>();
        shoot.add(new Pose2d(0, 0, Rotation2d.fromDegrees(0)));

        routines.add(new ParallelDrivePathRoutine(new IntakeBallRoutine(0.0), new DrivePathRoutine(rendezvous1), 0.8));
        routines.add(new ParallelDrivePathRoutine(new ShootAllBallsRoutine(), new DrivePathRoutine(shoot), 0.8));
        // Shoot ball

        return new SequentialRoutine(routines);
    }
}
