package com.palyrobotics.frc2019.auto.modes;

import com.palyrobotics.frc2019.auto.AutoModeBase;
import com.palyrobotics.frc2019.behavior.Routine;
import com.palyrobotics.frc2019.behavior.SequentialRoutine;
import com.palyrobotics.frc2019.behavior.routines.drive.DrivePathRoutine;
import com.palyrobotics.frc2019.config.Constants;
import com.palyrobotics.frc2019.util.trajectory.Path;
import com.palyrobotics.frc2019.util.trajectory.Path.Waypoint;
import com.palyrobotics.frc2019.util.trajectory.Translation2d;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("Duplicates")

public class LeftStartHatchInCloseFarCargoInMid extends AutoModeBase { //Left start > rocket ship close > loading station > rocket ship far > depot > rocket ship mid

    //TODO: tune the code - I haven't tested yet

    public static int SPEED = 70;
    public static double kOffsetX = -Constants.kLowerPlatformLength - Constants.kRobotLengthInches;
    public static double kOffsetY = -Constants.kLevel3Width * .5 - Constants.kLevel2Width * .5;
    public static double kCargoShipLeftFrontX = mDistances.kLevel1CargoX + Constants.kLowerPlatformLength + Constants.kUpperPlatformLength;
    public static double kCargoShipLeftFrontY = mDistances.kFieldWidth * .5 - (mDistances.kCargoLeftY + mDistances.kCargoOffsetY);
    public static double kHabLineX = Constants.kUpperPlatformLength + Constants.kLowerPlatformLength;
    public static double kLeftLoadingStationX = 0;
    public static double kLeftLoadingStationY = mDistances.kFieldWidth * .5 - mDistances.kLeftLoadingY;
    public static double kLeftDepotX = Constants.kUpperPlatformLength;
    public static double kLeftDepotY = mDistances.kFieldWidth * .5 - mDistances.kDepotFromLeftY;
    public static double kLeftRocketShipCloseX = mDistances.kHabLeftRocketCloseX + kHabLineX;
    public static double kLeftRocketShipCloseY = mDistances.kFieldWidth * .5 - mDistances.kLeftRocketCloseY;
    public static double kLeftRocketShipMidX = kHabLineX + mDistances.kHabLeftRocketMidX;
    public static double kLeftRocketShipMidY = mDistances.kFieldWidth * .5 - mDistances.kLeftRocketMidY;
    public static double kLeftRocketShipFarX = mDistances.kFieldWidth - mDistances.kMidlineLeftRocketFarX;
    public static double kLeftRocketShipFarY = mDistances.kFieldWidth * .5 - mDistances.kLeftRocketFarY;

    public Translation2d kCargoShipLeftFront = new Translation2d(-(kCargoShipLeftFrontX + Constants.kRobotWidthInches * .2 + kOffsetX), -(kCargoShipLeftFrontY - Constants.kRobotLengthInches * .05 + kOffsetY));
    public Translation2d kLeftLoadingStation = new Translation2d(-(kLeftLoadingStationX + Constants.kRobotLengthInches + kOffsetX), -(kLeftLoadingStationY - Constants.kRobotLengthInches * .2 + kOffsetY));
    public Translation2d kLeftRocketShipFar = new Translation2d(-(kLeftRocketShipFarX + Constants.kRobotLengthInches * 1 + kOffsetX), -(kLeftRocketShipFarY - Constants.kRobotLengthInches * .05 + kOffsetY));
    public Translation2d kLeftDepot = new Translation2d(-(kLeftDepotX + Constants.kRobotLengthInches * 1.1 + kOffsetX), -(kLeftDepotY - Constants.kRobotLengthInches * .25 + kOffsetY));
    public Translation2d kLeftRocketShipClose = new Translation2d(-(kLeftRocketShipCloseX + Constants.kRobotLengthInches * .3 + kOffsetX), -(kLeftRocketShipCloseY - Constants.kRobotLengthInches * .5 + kOffsetY));

    @Override
    public String toString() {
        return mAlliance + this.getClass().toString();
    }

    @Override
    public void prestart() {

    }

    @Override
    public Routine getRoutine() {
        return new SequentialRoutine(new RezeroSubAutoMode().Rezero(true), placeHatchClose(), takeHatch(), placeHatchFar(), takeCargo(), placeCargoMid());
    }

    public Routine placeHatchClose() { //start to rocket ship close
        ArrayList<Routine> routines = new ArrayList<>();

//        TODO: make super accurate (can only be at most 2 inches off)

        List<Path.Waypoint> DepotToRocketShip = new ArrayList<>();
        DepotToRocketShip.add(new Waypoint(new Translation2d(-(kHabLineX + Constants.kRobotLengthInches + kOffsetX), 0), 100)); //goes straight at the start so the robot doesn't get messed up over the ramp
        DepotToRocketShip.add(new Waypoint(new Translation2d(-(kLeftRocketShipCloseX * .8 + kOffsetX), -(findLineClose(kLeftRocketShipCloseX * .8) + kOffsetY)), 180)); //line up with rocket ship
        DepotToRocketShip.add(new Waypoint(kLeftRocketShipClose, 0));
        routines.add(new DrivePathRoutine(new Path(DepotToRocketShip), true));

//        TODO: add ReleaseHatchRoutine (not made yet)
//        routines.add(new TimeoutRoutine(1)); //placeholder

        return new SequentialRoutine(routines);
    }

    public Routine takeHatch() { //rocket ship close to loading station
        ArrayList<Routine> routines = new ArrayList<>();

        List<Path.Waypoint> CargoShipToLoadingStation = new ArrayList<>();
        CargoShipToLoadingStation.add(new Waypoint(new Translation2d(-(kHabLineX - Constants.kRobotLengthInches * .5 + kOffsetX), -(kLeftLoadingStationY - Constants.kRobotWidthInches * .4 + kOffsetY)), SPEED)); //lines up with loading station
        CargoShipToLoadingStation.add(new Waypoint(kLeftLoadingStation, 0));
        routines.add(new DrivePathRoutine(new Path(CargoShipToLoadingStation), false));

//        TODO: add IntakeHatchRoutine (not made yet)
//        routines.add(new TimeoutRoutine(1)); //placeholder

        return new SequentialRoutine(routines);
    }

    public Routine placeHatchFar() { //loading station to rocket ship close
        ArrayList<Routine> routines = new ArrayList<>();

        /*
        The robot starts backwards at the loading station after loading a hatch. It then goes around the rocket ship and over shoots it a bit. Then, it lines up with the rocket ship far and places the hatch.
         */

        List<Path.Waypoint> backLoadingStationToRocketShip = new ArrayList<>(); //robot starts going backwards
        backLoadingStationToRocketShip.add(new Waypoint(new Translation2d(-(kLeftLoadingStationX + Constants.kRobotLengthInches + kOffsetX), -(kLeftLoadingStationY + kOffsetY)), 180)); //backs up a bit
        backLoadingStationToRocketShip.add(new Waypoint(new Translation2d(-(kLeftRocketShipMidX + kOffsetX), -(findLineFar(kLeftRocketShipMidX + Constants.kRobotLengthInches * 1.8) + kOffsetY)), SPEED)); //goes around the rocket ship
        backLoadingStationToRocketShip.add(new Waypoint(new Translation2d(-(kLeftRocketShipFarX + Constants.kRobotLengthInches * 1.9 + kOffsetX), -(findLineFar(kLeftRocketShipMidX + Constants.kRobotLengthInches * 1.9) - Constants.kRobotLengthInches * .2 + kOffsetY)), 0));
        routines.add(new DrivePathRoutine(new Path(backLoadingStationToRocketShip), true)); //robot turns and then moves forward to the rocket ship

        List<Path.Waypoint> forwardLoadingStationToRocketShip = new ArrayList<>(); //robot turns and then moves forward
        forwardLoadingStationToRocketShip.add(new Waypoint(new Translation2d(-(kLeftRocketShipFarX + Constants.kRobotLengthInches * 1.6 + kOffsetX), -(findLineFar(kLeftRocketShipMidX + Constants.kRobotLengthInches * 1.6) - Constants.kRobotLengthInches * .3 + kOffsetY)), SPEED)); //line up with rocket ship far
        forwardLoadingStationToRocketShip.add(new Waypoint(kLeftRocketShipFar, 0)); //ends in front of the rocket ship far
        routines.add(new DrivePathRoutine(new Path(forwardLoadingStationToRocketShip), false));

//        TODO: add ReleaseHatchRoutine (not made yet)
//        routines.add(new TimeoutRoutine(1)); //placeholder

        return new SequentialRoutine(routines);
    }

    public Routine takeCargo() { //rocket ship close to depot - could be more accurate
        ArrayList<Routine> routines = new ArrayList<>();

        /*
        The robot starts at rocket ship far and goes around the rocket backwards. Ends at the depot and loads a cargo.
         */

        List<Path.Waypoint> RocketShipToDepot = new ArrayList<>();
        RocketShipToDepot.add(new Waypoint(new Translation2d(-(kLeftRocketShipFarX + Constants.kRobotLengthInches * 1.6 + kOffsetX), -(findLineFar(kLeftRocketShipMidX + Constants.kRobotLengthInches * 1.6) + Constants.kRobotLengthInches * 0.5 + kOffsetY)), 70));
        RocketShipToDepot.add(new Waypoint(new Translation2d(-(kCargoShipLeftFrontX + Constants.kRobotLengthInches + kOffsetX), -(kLeftDepotY + Constants.kRobotLengthInches * .2 + kOffsetY)), 180)); //turn around the rocket ship
        RocketShipToDepot.add(new Waypoint(kLeftDepot, 0));
        routines.add(new DrivePathRoutine(new Path(RocketShipToDepot), true));

//        TODO: add IntakeCargoRoutine (not made yet)
//        routines.add(new TimeoutRoutine(1)); //placeholder

        return new SequentialRoutine(routines);
    }

    public Routine placeCargoMid() { //depot to rocket ship mid
        ArrayList<Routine> routines = new ArrayList<>();

        List<Path.Waypoint> DepotToRocketShipMid = new ArrayList<>();
        DepotToRocketShipMid.add(new Waypoint(new Translation2d(-(kLeftDepotX + Constants.kRobotLengthInches * 2 + kOffsetX), -(kLeftDepotY + kOffsetY)), SPEED)); //go straight until near rocket ship
        DepotToRocketShipMid.add(new Waypoint(new Translation2d(-(kLeftRocketShipMidX - Constants.kRobotLengthInches * .3), -(kLeftDepotY + kOffsetY)), SPEED));
        DepotToRocketShipMid.add(new Waypoint(new Translation2d(-(kLeftRocketShipMidX), -(kLeftRocketShipMidY - Constants.kRobotLengthInches * .8)), SPEED)); //line up with rocket ship
        DepotToRocketShipMid.add(new Waypoint(new Translation2d(-(kLeftRocketShipMidX), -(kLeftRocketShipMidY)), 0));
        routines.add(new DrivePathRoutine(new Path(DepotToRocketShipMid), false));

//        TODO: add ReleaseCargoRoutine (not made yet)
//        routines.add(new TimeoutRoutine(1)); //placeholder

        return new SequentialRoutine(routines);
    }

    public double findLineClose(double cordX) {
        return 0.54862 * cordX - 0.54862 * kLeftRocketShipCloseX + kLeftRocketShipCloseY; //slope is derived from the angle of the rocket ship sides - constants derived from math
    } //the y cord of an invisible line extending from rocket ship close

    public double findLineFar(double cordX) {
        return -0.54862 * cordX + 0.54862 * kLeftRocketShipFarX + kLeftRocketShipFarY; //slope is derived from the angle of the rocket ship sides - constants derived from math
    } //the y cord of an invisible line extending from rocket ship close

    @Override
    public String getKey() {
        return mAlliance.toString();
    }
}