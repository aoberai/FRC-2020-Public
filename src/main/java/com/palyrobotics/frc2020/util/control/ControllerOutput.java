package com.palyrobotics.frc2020.util.control;

import java.util.Objects;

public class ControllerOutput {

	public enum Mode {
		PERCENT_OUTPUT, POSITION, VELOCITY, PROFILED_POSITION, PROFILED_VELOCITY,
	}

	private Mode mMode;
	private Gains mGains;
	private double mReference, mArbitraryDemand;

	public ControllerOutput() {
		this(Mode.PERCENT_OUTPUT);
	}

	public ControllerOutput(Mode controlMode) {
		mMode = controlMode;
	}

	public void setTargetVelocityProfiled(double targetVelocity, SmartGains gains) {
		setTargetVelocityProfiled(targetVelocity, 0.0, gains);
	}

	public void setTargetVelocityProfiled(double targetVelocity, double arbitraryDemand, SmartGains gains) {
		mMode = Mode.PROFILED_VELOCITY;
		mReference = targetVelocity;
		mArbitraryDemand = arbitraryDemand;
		mGains = gains;
	}

	public void setTargetVelocity(double targetVelocity, Gains gains) {
		setTargetVelocity(targetVelocity, 0.0, gains);
	}

	public void setTargetVelocity(double targetVelocity, double arbitraryDemand, Gains gains) {
		mMode = Mode.VELOCITY;
		mReference = targetVelocity;
		mArbitraryDemand = arbitraryDemand;
		mGains = gains;
	}

	public void setTargetPosition(double positionSetPoint, Gains gains) {
		setTargetPosition(positionSetPoint, 0.0, gains);
	}

	public void setTargetPosition(double positionSetPoint, double arbitraryDemand, Gains gains) {
		mMode = Mode.POSITION;
		mReference = positionSetPoint;
		mArbitraryDemand = arbitraryDemand;
		mGains = gains;
	}

	public void setTargetPositionProfiled(double positionSetPoint, SmartGains gains) {
		setTargetPositionProfiled(positionSetPoint, 0.0, gains);
	}

	public void setTargetPositionProfiled(double positionSetPoint, double arbitraryDemand, SmartGains gains) {
		mMode = Mode.PROFILED_POSITION;
		mReference = positionSetPoint;
		mArbitraryDemand = arbitraryDemand;
		mGains = gains;
	}

	public void setIdle() {
		setPercentOutput(0.0);
	}

	public void setPercentOutput(double output) {
		mMode = Mode.PERCENT_OUTPUT;
		mReference = output;
		mArbitraryDemand = 0.0;
		mGains = null;
	}

	public Gains getGains() {
		return mGains;
	}

	public double getReference() {
		return mReference;
	}

	public double getArbitraryDemand() {
		return mArbitraryDemand;
	}

	public Mode getControlMode() {
		return mMode;
	}

	@Override // Auto-generated
	public int hashCode() {
		return Objects.hash(mMode, mGains, mReference, mArbitraryDemand);
	}

	@Override // Auto-generated
	public boolean equals(Object other) {
		if (this == other)
			return true;
		if (other == null || getClass() != other.getClass())
			return false;
		ControllerOutput otherOutput = (ControllerOutput) other;
		return Double.compare(otherOutput.mReference, mReference) == 0
				&& Double.compare(otherOutput.mArbitraryDemand, mArbitraryDemand) == 0
				&& Objects.equals(mMode, otherOutput.mMode) && Objects.equals(mGains, otherOutput.mGains);
	}

	@Override // Auto-generated
	public String toString() {
		return String.format("Output{mMode=%s, mGains=%s, mReference=%s, mArbitraryDemand=%s}", mMode, mGains,
				mReference, mArbitraryDemand);
	}
}
