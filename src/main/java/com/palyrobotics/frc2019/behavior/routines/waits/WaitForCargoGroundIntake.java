package com.palyrobotics.frc2019.behavior.routines.waits;

import com.palyrobotics.frc2019.behavior.Routine;
import com.palyrobotics.frc2019.config.Commands;
import com.palyrobotics.frc2019.subsystems.Subsystem;

public class WaitForCargoGroundIntake extends Routine {
    private boolean done;

    public WaitForCargoGroundIntake() {
    }

    @Override
    public void start() {

    }

    @Override
    public Commands update(Commands commands) {
        done = commands.hasCargo;
        return commands;
    }

    @Override
    public Commands cancel(Commands commands) {
        return commands;
    }

    @Override
    public boolean finished() {
        return done;
    }

    @Override
    public Subsystem[] getRequiredSubsystems() {
        return new Subsystem[] { pusher };
    }

    @Override
    public String getName() {
        return "PusherInRoutine";
    }

}
