package com.palyrobotics.frc2020.subsystems.controllers.lighting;

import com.palyrobotics.frc2020.robot.Commands;
import com.palyrobotics.frc2020.robot.RobotState;
import com.palyrobotics.frc2020.subsystems.Lighting;
import com.palyrobotics.frc2020.util.Color;

public class ConvergingBandsController extends Lighting.LEDController {

	private Color.HSV mBandColor;
	private Color.HSV mBackgroundColor;
	private int mBandLedCount;
	private int mCurrentBandPosition;
	private int mDuration = -1;

	/**
	 * Band color converges to center of strip
	 *
	 * @param startIndex      Initial index upon which led patterns should start
	 * @param lastIndex       End index upon which led patterns should stop
	 * @param bandColor       Color that should pulse through led strip
	 * @param backgroundColor Background color upon which converging effect will occur.
	 */

	public ConvergingBandsController(int startIndex, int lastIndex, Color.HSV bandColor, Color.HSV backgroundColor, int bandLedCount, double speed) {
		super(startIndex, lastIndex);
		mStartIndex = startIndex;
		mLastIndex = lastIndex;
		mBandColor = bandColor;
		mBackgroundColor = backgroundColor;
		mBandLedCount = bandLedCount;
		mSpeed = speed == 0 ? kZeroSpeed : speed;
		mTimer.start();
	}

	public ConvergingBandsController(int startIndex, int lastIndex, Color.HSV bandColor, Color.HSV backgroundColor, int bandLedCount, double speed, int duration) {
		super(startIndex, lastIndex);
		mStartIndex = startIndex;
		mLastIndex = lastIndex;
		mBandColor = bandColor;
		mBackgroundColor = backgroundColor;
		mBandLedCount = bandLedCount;
		mSpeed = speed == 0 ? kZeroSpeed : speed;
		mDuration = duration;
		mTimer.start();
	}

	@Override
	public void updateSignal(Commands commands, RobotState state) {
		if (Math.round(mTimer.get() / mSpeed) % 2 == 1) {
			mCurrentBandPosition += 1;
			mTimer.reset();
		}
		for (var i = 0; i < (mLastIndex - mStartIndex) / 2 - 1; i++) {
			if ((i + mCurrentBandPosition) / mBandLedCount % 2 == 0) {
				mOutputs.lightingOutput.get(i).setHSV(mBandColor.getH(), mBandColor.getS(), mBandColor.getV());
				mOutputs.lightingOutput.get(mLastIndex - mStartIndex - i - 1).setHSV(mBandColor.getH(),
						mBandColor.getS(), mBandColor.getV());
			} else {
				mOutputs.lightingOutput.get(i).setHSV(mBackgroundColor.getH(), mBackgroundColor.getS(),
						mBackgroundColor.getV());
				mOutputs.lightingOutput.get(mLastIndex - mStartIndex - i - 1).setHSV(mBackgroundColor.getH(),
						mBackgroundColor.getS(), mBackgroundColor.getV());
			}
		}
	}

	@Override

	public boolean checkFinished() {
		return mDuration != -1 && mTimer.hasElapsed(mDuration);
	}
}
