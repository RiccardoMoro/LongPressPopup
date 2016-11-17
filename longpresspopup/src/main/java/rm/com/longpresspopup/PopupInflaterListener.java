package rm.com.longpresspopup;

import android.support.annotation.Nullable;
import android.view.View;

/**
 * Created by Riccardo on 14/11/16.
 */

public interface PopupInflaterListener {
    /**
     * Called when the popup view has been inflated (not necessarily shown, I recommend using
     * {@link PopupStateListener} to know when the popup is show (to load images for example))
     *
     * @param popupTag The tag of the popup
     * @param root     The inflated root view, you can use findViewById method on it and look for
     *                 your views
     */
    void onViewInflated(@Nullable String popupTag, View root);
}
