package rm.com.longpresspopup;

/**
 * Created by Riccardo on 11/11/16.
 */

public interface LongPressPopupInterface {

    /**
     * Called when normal press register, still NOT long press
     */
    void onPressStart(float x, float y);

    /**
     * Called while continue to press, but still not for long enough
     *
     * @param progress The current progress towards the long press
     */
    void onPressContinue(int progress, float x, float y);

    /**
     * Called when a press event stops before reaching the long press needed time
     */
    void onPressStop(float x, float y);

    /**
     * Called when the button has been long pressed for long enough, passing the last touch
     * coordinates
     */
    void onLongPressStart(float x, float y);

    /**
     * Called when keep long pressing
     *
     * @param longPressTime The time the view has been long clicked for
     */
    void onLongPressContinue(int longPressTime, float x, float y);

    /**
     * Called when stopping the long press
     */
    void onLongPressEnd(float x, float y);
}
