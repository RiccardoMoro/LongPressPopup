package rm.com.longpresspopup;

import android.support.annotation.NonNull;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Riccardo on 14/11/16.
 */

public class LongPressPopupUtils {

    public static boolean isTouchInsideView(@NonNull View view, MotionEvent motionEvent) {
        int motionEventX = (int) motionEvent.getRawX();
        int motionEventY = (int) motionEvent.getRawY();

        int[] l = new int[2];
        view.getLocationOnScreen(l);
        int x = l[0];
        int y = l[1];
        int w = view.getWidth();
        int h = view.getHeight();

        if (motionEventX < x || motionEventX > x + w || motionEventY < y || motionEventY > y + h) {
            return false;
        }
        return true;
    }
}
