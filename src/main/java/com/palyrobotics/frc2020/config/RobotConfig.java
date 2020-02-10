package com.palyrobotics.frc2020.config;

import java.util.List;

import com.palyrobotics.frc2020.util.config.ConfigBase;

@SuppressWarnings ("squid:ClassVariableVisibilityCheck")
public class RobotConfig extends ConfigBase {

	public boolean coastDriveWhenDisabled, disableHardwareUpdates, enableVisionWhenDisabled;
	public int visionPipelineWhenDisabled;

	// Useful for testing at lower speeds
	public double motorOutputMultiplier;

	public List<String> enabledServices, enabledSubsystems;
}
