package com.palyrobotics.frc2020.robot;

import com.palyrobotics.frc2020.subsystems.Spinner;
import com.revrobotics.ColorMatch;

import edu.wpi.first.wpilibj.DriverStation;

public class HardwareReader {

	private static HardwareReader sInstance = new HardwareReader();

	public static HardwareReader getInstance() {
		return sInstance;
	}

	/**
	 * A REV Color Match object is used to register and detect known colors. This
	 * can be calibrated ahead of time or during operation.
	 * <p>
	 * This object uses euclidean distance to estimate the closest match with a
	 * given confidence range.
	 */
	public final ColorMatch mColorMatcher = new ColorMatch();

	private HardwareReader() {
		mColorMatcher.addColorMatch(Spinner.kCyanCPTarget);
		mColorMatcher.addColorMatch(Spinner.kGreenCPTarget);
		mColorMatcher.addColorMatch(Spinner.kRedCPTarget);
		mColorMatcher.addColorMatch(Spinner.kYellowCPTarget);
	}

	/**
	 * Takes all of the sensor data from the hardware, and unwraps it into the
	 * current {@link RobotState}.
	 */
	void updateState(RobotState robotState) {
		var drivetrain = HardwareAdapter.DrivetrainHardware.getInstance();

		robotState.driveHeading = drivetrain.gyro.getFusedHeading();

		robotState.driveLeftVelocity = drivetrain.leftMasterEncoder.getVelocity() / 60.0;
		robotState.driveRightVelocity = drivetrain.rightMasterEncoder.getVelocity() / 60.0;
		robotState.driveLeftPosition = drivetrain.leftMasterEncoder.getPosition();
		robotState.driveRightPosition = drivetrain.rightMasterEncoder.getPosition();

		// Updating color sensor data
		robotState.detectedRGBVals = HardwareAdapter.SpinnerHardware.getInstance().colorSensor.getColor();
		robotState.closestColorRGB = mColorMatcher.matchClosestColor(robotState.detectedRGBVals);
		if (robotState.closestColorRGB.color == Spinner.kCyanCPTarget) {
			robotState.closestColorString = "Cyan";
		} else if (robotState.closestColorRGB.color == Spinner.kYellowCPTarget) {
			robotState.closestColorString = "Yellow";
		} else if (robotState.closestColorRGB.color == Spinner.kGreenCPTarget) {
			robotState.closestColorString = "Green";
		} else if (robotState.closestColorRGB.color == Spinner.kRedCPTarget) {
			robotState.closestColorString = "Red";
		}
		robotState.closestColorConfidence = robotState.closestColorRGB.confidence;

		// For testing purposes
		// System.out.println(robotState.closestColorString + " with confidence level of
		// " + (robotState.closestColorConfidence * 100));
		// System.out.println(robotState.detectedRGBVals.red + ", " +
		// robotState.detectedRGBVals.green + ", " + robotState.detectedRGBVals.blue);

		robotState.gameData = DriverStation.getInstance().getGameSpecificMessage();
		// if (robotState.gameData.length() > 0) {
		// System.out.printf("Game data has been found, color is: %s%n",
		// robotState.gameData);
		// }

		robotState.updateOdometry(robotState.driveHeading, robotState.driveLeftPosition, robotState.driveRightPosition);
	}
}