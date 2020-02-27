package com.palyrobotics.frc2020.subsystems;

import java.util.*;

import com.palyrobotics.frc2020.config.subsystem.LightingConfig;
import com.palyrobotics.frc2020.robot.Commands;
import com.palyrobotics.frc2020.robot.ReadOnly;
import com.palyrobotics.frc2020.robot.RobotState;
import com.palyrobotics.frc2020.subsystems.controllers.lighting.*;
import com.palyrobotics.frc2020.util.Color;
import com.palyrobotics.frc2020.util.config.Configs;
import com.palyrobotics.frc2020.util.control.LightingOutputs;

import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.Timer;

public class Lighting extends SubsystemBase {

	public enum State {
		OFF, IDLE, INIT, DISABLE, TARGET_FOUND, SHOOTER_FULLRPM, ROBOT_ALIGNING, ROBOT_ALIGNED, CLIMBING, INTAKE_EXTENDED, BALL_ENTERED, SPINNER_DONE, BALL_SHOT
	}

	public abstract static class LEDController {

		protected static final double kZeroSpeed = 1e-4;

		protected LightingOutputs mOutputs = new LightingOutputs();
		protected Timer mTimer = new Timer();

		protected int mStartIndex;
		protected int mLastIndex;
		protected double mSpeed;

		protected LEDController(int startIndex, int lastIndex) {
			for (var i = startIndex; i <= lastIndex; i++) {
				mOutputs.lightingOutput.add(new Color.HSV());
			}
			mTimer.reset();
		}

		public final LightingOutputs update(@ReadOnly Commands commands, @ReadOnly RobotState state) {
			updateSignal(commands, state);
			return mOutputs;
		}

		public abstract void updateSignal(@ReadOnly Commands commands, @ReadOnly RobotState state);

		public boolean checkFinished() {
			return false;
		}
	}

	private static Lighting sInstance = new Lighting();
	private LightingConfig mConfig = Configs.get(LightingConfig.class);
	private AddressableLEDBuffer mOutputBuffer = new AddressableLEDBuffer(mConfig.ledCount);
	private State mState;
	private List<LEDController> mLEDControllers = new ArrayList<>(); //array of active led controllers

	private Lighting() {
	}

	public static Lighting getInstance() {
		return sInstance;
	}

	@Override
	public void update(@ReadOnly Commands commands, @ReadOnly RobotState state) {
		State wantedState = commands.lightingWantedState;
		if (RobotController.getBatteryVoltage() < mConfig.minVoltageToFunction) wantedState = State.OFF;
		boolean isNewState = mState != wantedState;
		mState = wantedState;
		if (isNewState) {
			switch (mState) {
				case OFF:
					resetLedStrip();
					mLEDControllers.clear();
					break;
				case IDLE:
					mLEDControllers.clear();
					addToControllers(new OneColorController(mConfig.totalSegmentFirstIndex,
							mConfig.totalSegmentLastIndex, Color.HSV.kWhite));
					break;
				case INIT:
				case DISABLE:
					resetLedStrip();
					mLEDControllers.clear();
					addToControllers(new OneColorController(mConfig.totalSegmentFirstIndex, mConfig.totalSegmentLastIndex, Color.HSV.kAqua));
					break;
				case TARGET_FOUND:
					addToControllers(new FlashingLightsController(mConfig.spinnerSegmentFirstIndex,
							mConfig.spinnerSegmentLastIndex, Color.HSV.kLime, 1));
					break;
				case SPINNER_DONE:
					addToControllers(new OneColorController(mConfig.frontLeftSegmentFirstIndex, mConfig.frontRightSegmentLastIndex, Color.HSV.kBlue, 2));
					break;
				case BALL_ENTERED:
					addToControllers(new PulseController(mConfig.frontLeftSegmentFirstIndex,
							mConfig.frontLeftSegmentLastIndex, List.of(Color.HSV.kOrange, Color.HSV.kOrange, Color.HSV.kOrange), 1));
					addToControllers(new PulseController(mConfig.frontRightSegmentFirstIndex,
							mConfig.frontRightSegmentLastIndex, List.of(Color.HSV.kOrange, Color.HSV.kOrange, Color.HSV.kOrange), 1));
					addToControllers(new DivergingBandsController(mConfig.spinnerSegmentFirstIndex, mConfig.spinnerSegmentLastIndex, Color.HSV.kOrange, Color.HSV.kWhite, 3, 1.0 / 6.0, 2));
					break;
				case CLIMBING:
					addToControllers(new FlashingLightsController(mConfig.totalSegmentFirstIndex,
							mConfig.totalSegmentLastIndex, Color.HSV.kOrange, 0.5, 3));
					break;
				case INTAKE_EXTENDED:
					addToControllers(new PulseController(mConfig.frontLeftSegmentFirstIndex,
							mConfig.frontLeftSegmentLastIndex, List.of(Color.HSV.kPurple, Color.HSV.kPurple, Color.HSV.kPurple), 1));
					addToControllers(new PulseController(mConfig.frontRightSegmentFirstIndex,
							mConfig.frontRightSegmentLastIndex, List.of(Color.HSV.kPurple, Color.HSV.kPurple, Color.HSV.kPurple), 1));
					addToControllers(new DivergingBandsController(mConfig.spinnerSegmentFirstIndex, mConfig.spinnerSegmentLastIndex, Color.HSV.kOrange, Color.HSV.kWhite, 3, 1.0 / 6.0, 2));
					break;
				case ROBOT_ALIGNING:
					addToControllers(new FlashingLightsController(mConfig.spinnerSegmentFirstIndex, mConfig.spinnerSegmentLastIndex, Color.HSV.kLime, 1, 2));
					break;
				case ROBOT_ALIGNED:
					addToControllers(new FlashingLightsController(mConfig.spinnerSegmentFirstIndex, mConfig.spinnerSegmentLastIndex, Color.HSV.kLime, 2, 2));
					break;
				case SHOOTER_FULLRPM:
					addToControllers(new FadeInFadeOutController(mConfig.spinnerSegmentFirstIndex, mConfig.spinnerSegmentLastIndex, Color.HSV.kLime, 0.5, 5));
					break;
				case BALL_SHOT:
					addToControllers(new OneColorController(mConfig.totalSegmentFirstIndex, mConfig.totalSegmentLastIndex, Color.HSV.kBlue, 1));
			}
		}
		resetLedStrip();
		mLEDControllers.removeIf(LEDController::checkFinished);
		for (LEDController ledController : mLEDControllers) {
			LightingOutputs controllerOutput = ledController.update(commands, state);
			for (int i = 0; i < controllerOutput.lightingOutput.size(); i++) {
				Color.HSV hsvValue = controllerOutput.lightingOutput.get(i);
				mOutputBuffer.setHSV(i + ledController.mStartIndex, hsvValue.getH(), hsvValue.getS(), Math.min(hsvValue.getV(), mConfig.maximumBrightness));
			}
		}
	}

	private void addToControllers(LEDController controller) {
		mLEDControllers.removeIf(controllers -> controllers.mStartIndex == controller.mStartIndex &&
				controllers.mLastIndex == controller.mLastIndex);
		mLEDControllers.add(controller);
	}

	private void resetLedStrip() {
		for (int i = 0; i < mOutputBuffer.getLength(); i++) {
			mOutputBuffer.setRGB(i, 0, 0, 0);
		}
	}

	public AddressableLEDBuffer getOutput() {
		return mOutputBuffer;
	}
}
