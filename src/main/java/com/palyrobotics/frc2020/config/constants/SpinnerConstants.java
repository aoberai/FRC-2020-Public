package com.palyrobotics.frc2020.config.constants;

import java.util.List;

import com.revrobotics.ColorMatch;

import edu.wpi.first.wpilibj.util.Color;

public class SpinnerConstants {

	// May need to be re-tuned based on lighting conditions
	public static final Color kCyanCPTarget = ColorMatch.makeColor(0.1, 0.4, 0.4);
	public static final Color kGreenCPTarget = ColorMatch.makeColor(0.1, 0.6, 0.25);
	public static final Color kRedCPTarget = ColorMatch.makeColor(0.5, 0.3, 0.1);
	public static final Color kYellowCPTarget = ColorMatch.makeColor(0.3, 0.5, 0.1);
	// scale ratio to allow for control panel 1/eighth rotation
	public static final double eighthCPMovementGearRatio = 1.5;

	public static List<String> controlPanelColorOrder = List.of("yellow", "blue", "green", "red");
}
