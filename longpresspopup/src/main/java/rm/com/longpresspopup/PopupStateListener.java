package rm.com.longpresspopup;

import android.support.annotation.Nullable;

/**
 * Created by Riccardo on 11/11/16.
 */

public interface PopupStateListener {
    void onPopupShow(@Nullable String popupTag);

    void onPopupDismiss(@Nullable String popupTag);
}
