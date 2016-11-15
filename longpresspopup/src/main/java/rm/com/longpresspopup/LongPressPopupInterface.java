package rm.com.longpresspopup;

import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Riccardo on 11/11/16.
 */

public interface LongPressPopupInterface {

    /**
     * Called when normal press register, still NOT long press
     */
    void onPressStart(View pressedView, MotionEvent motionEvent);

    /**
     * Called while continue to press, but still not for long enough
     *
     * @param progress The current progress towards the long press
     */
    void onPressContinue(int progress, MotionEvent motionEvent);

    /**
     * Called when a press event stops before reaching the long press needed time
     */
    void onPressStop(MotionEvent motionEvent);

    /**
     * Called when the button has been long pressed for long enough, passing the last touch
     * coordinates
     */
    void onLongPressStart(MotionEvent motionEvent);

    /**
     * Called when keep long pressing
     *
     * @param longPressTime The time the view has been long clicked for
     */
    void onLongPressContinue(int longPressTime, MotionEvent motionEvent);

    /**
     * Called when stopping the long press
     */
    void onLongPressEnd(MotionEvent motionEvent);
}
