package rm.com.longpresspopup;

import android.os.Handler;
import android.support.annotation.IntDef;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Riccardo on 11/11/16.
 */

class PopupTouchListener implements View.OnTouchListener {

    // Long click duration in milliseconds
    static final int LONG_CLICK_DURATION = 1000;

    private LongPressPopupInterface mPressPopupInterface;

    @PopupTouchListener.PressStatus
    private int mCurrentPressStatus = STATUS_NOT_PRESSED;

    private int mLongClickDuration;
    private long mStartPressTimestamp;

    private Handler mLongPressHandler;

    PopupTouchListener(LongPressPopupInterface popupInterface) {
        this(popupInterface, 0);
    }

    PopupTouchListener(LongPressPopupInterface popup, int longClickDuration) {
        mPressPopupInterface = popup;
        mLongClickDuration = longClickDuration > 0 ? longClickDuration : LONG_CLICK_DURATION;

        mStartPressTimestamp = -1;
        mLongPressHandler = new Handler();
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        // If not popup passed, let others handle
        if (mPressPopupInterface == null) {
            return false;
        }

        // Press register
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN || motionEvent.getAction() ==
                MotionEvent.ACTION_MOVE) {

            switch (mCurrentPressStatus) {
                case STATUS_NOT_PRESSED:

                    // If register press and currently not pressed, register pressing
                    startPress(motionEvent);
                    break;
                case STATUS_PRESSING:

                    if (System.currentTimeMillis() - mStartPressTimestamp > mLongClickDuration) {

                        // Pressed enough, set as long pressing
                        startLongPress(motionEvent);
                    } else {

                        // Still not long enough, but press continue, send the current progress
                        continuePress(motionEvent, (int)
                                (((System.currentTimeMillis() - mStartPressTimestamp)) /
                                        mLongClickDuration) * 100);// Calculate the percentage
                    }
                    break;
                case STATUS_LONG_PRESSING:

                    // Already long pressing, nothing to do
                    continueLongPress(motionEvent,
                            (int) (System.currentTimeMillis() - mStartPressTimestamp -
                                    mLongClickDuration));
                    break;
            }
        } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {// Press finished

            switch (mCurrentPressStatus) {
                case STATUS_NOT_PRESSED:

                    // Already not pressing, nothing to do
                    break;
                case STATUS_PRESSING:

                    // Stop press before long press
                    stopPress(motionEvent);
                    break;
                case STATUS_LONG_PRESSING:

                    // Stop long pressing
                    stopLongPress(motionEvent);
                    break;
            }
        }

        return mCurrentPressStatus == STATUS_LONG_PRESSING;
    }


    // Standard press methods
    private void startPress(MotionEvent motionEvent) {

        // Add 10 milliseconds to avoid premature runnable calls
        mLongPressHandler.postDelayed(mLongPressRunnable, mLongClickDuration + 10);
        Log.e("Test", "Start postDelayed");

        updateLastMotionEventRunnable(motionEvent);

        mStartPressTimestamp = System.currentTimeMillis();
        mCurrentPressStatus = STATUS_PRESSING;

        mPressPopupInterface.onPressStart(motionEvent.getX(), motionEvent.getY());
    }

    private void continuePress(MotionEvent motionEvent, int pressStatus) {
        mPressPopupInterface.onPressContinue(pressStatus, motionEvent.getX(), motionEvent.getY());

        updateLastMotionEventRunnable(motionEvent);
    }

    private void stopPress(MotionEvent motionEvent) {
        mPressPopupInterface.onPressStop(motionEvent.getX(), motionEvent.getY());

        resetPressVariables();
    }


    // Long press methods
    private void startLongPress(MotionEvent motionEvent) {
        mCurrentPressStatus = STATUS_LONG_PRESSING;
        mPressPopupInterface.onLongPressStart(motionEvent.getX(), motionEvent.getY());
    }

    private void continueLongPress(MotionEvent motionEvent, int longPressDuration) {
        mPressPopupInterface.onLongPressContinue(longPressDuration,
                motionEvent.getX(), motionEvent.getY());
    }

    private void stopLongPress(MotionEvent motionEvent) {
        mPressPopupInterface.onLongPressEnd(motionEvent.getX(), motionEvent.getY());

        resetPressVariables();
    }


    private void updateLastMotionEventRunnable(MotionEvent motionEvent) {
        if (mLongPressRunnable != null) {
            mLongPressRunnable.setLastMotionEvent(motionEvent);
        }
    }


    // Clear press variables
    private void resetPressVariables() {
        mStartPressTimestamp = -1;
        mCurrentPressStatus = STATUS_NOT_PRESSED;

        if (mLongPressHandler != null && mLongPressRunnable != null) {
            mLongPressHandler.removeCallbacks(mLongPressRunnable);
            Log.e("Test", "Remove callback from handler");
        }
    }


    // Getters for current press status
    @PopupTouchListener.PressStatus
    public int getPressionStatus() {
        return mCurrentPressStatus;
    }

    public boolean isLongPressing() {
        return mCurrentPressStatus == STATUS_LONG_PRESSING;
    }

    public boolean isNotPressing() {
        return mCurrentPressStatus == STATUS_NOT_PRESSED;
    }

    public boolean isPressingStillNotLong() {
        return mCurrentPressStatus == STATUS_PRESSING;
    }


    // Press status
    private static final int STATUS_NOT_PRESSED = 0;
    private static final int STATUS_PRESSING = 1;
    private static final int STATUS_LONG_PRESSING = 2;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({STATUS_NOT_PRESSED, STATUS_PRESSING, STATUS_LONG_PRESSING})
    private @interface PressStatus {
    }


    private RunnableMotionEvent mLongPressRunnable = new RunnableMotionEvent() {
        @Override
        public void run() {

            // If pressing and time valid, register longPressing
            if (mCurrentPressStatus == STATUS_PRESSING &&
                    System.currentTimeMillis() - mStartPressTimestamp >= mLongClickDuration) {

                startLongPress(getLastMotionEvent());
            }
        }
    };
}
