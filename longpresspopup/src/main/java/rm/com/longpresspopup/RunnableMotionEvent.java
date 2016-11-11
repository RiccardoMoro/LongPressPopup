package rm.com.longpresspopup;

import android.view.MotionEvent;

/**
 * Created by Riccardo on 11/11/16.
 */

public abstract class RunnableMotionEvent implements Runnable {

    protected MotionEvent mLastMotionEvent;

    public MotionEvent getLastMotionEvent() {
        return mLastMotionEvent;
    }

    public void setLastMotionEvent(MotionEvent motionEvent) {
        mLastMotionEvent = motionEvent;
    }
}
