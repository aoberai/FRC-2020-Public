package com.palyrobotics.frc2020.behavior.routines.waits;

import com.palyrobotics.frc2020.behavior.routines.WaitRoutine;

public abstract class TimeoutRoutine extends WaitRoutine {

	public TimeoutRoutine(double timeout) {
		super(timeout);
	}

	@Override
	public final boolean checkFinished() {
		return super.checkFinished() || checkIfFinishedEarly();
	}

	public abstract boolean checkIfFinishedEarly();
}