package com.palyrobotics.frc2020.config;

import com.palyrobotics.frc2020.util.config.ConfigBase;

@SuppressWarnings ("squid:ClassVariableVisibilityCheck")
public class PortConstants extends ConfigBase {

	/**
	 * Climber
	 */
	public int nariClimberHorizontalId;
	public int nariClimberVerticalId;
	public int nariClimberSolenoidId;

	/**
	 * Drivetrain
	 */
	public int nariDriveLeftMasterId, nariDriveLeftSlaveId;
	public int nariDriveRightMasterId, nariDriveRightSlaveId;

	/**
	 * Indexer
	 */
	public int nariIndexerHorizontalId;
	public int nariIndexerVerticalId;
	public int nariIndexerSolenoidId;
	public int nariIndexerHopperSolenoidId;
	public int nariIndexerBlockingSolenoidId;
	public int nariIndexerBackInfraredDIO;
	public int nariIndexerFrontInfraredDIO;
	public int nariIndexerTopInfraredDIO;

	/**
	 * Intake
	 */
	public int nariIntakeId;
	public int nariIntakeSolenoidId;

	/**
	 * Shooter
	 */
	public int nariShooterMasterId, nariShooterSlaveId;
	public int nariShooterHoodSolenoid;
	public int nariShooterBlockingSolenoidId;

	/**
	 * Spinner
	 */
	public int nariSpinnerId;
}
