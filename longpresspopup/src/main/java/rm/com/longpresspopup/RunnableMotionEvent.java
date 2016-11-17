package rm.com.longpresspopup;

import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Riccardo on 11/11/16.
 */

public abstract class RunnableMotionEvent implements Runnable {

    protected MotionEvent mLastMotionEvent;
    protected View mStartView;

    public void setStartView(View view) {
        mStartView = view;
    }

    public View getStartView() {
        return mStartView;
    }

    public MotionEvent getLastMotionEvent() {
        return mLastMotionEvent;
    }

    public void setLastMotionEvent(MotionEvent motionEvent) {
        mLastMotionEvent = motionEvent;
    }
}
