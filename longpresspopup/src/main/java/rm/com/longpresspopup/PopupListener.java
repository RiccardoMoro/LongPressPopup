package rm.com.longpresspopup;

import android.support.annotation.Nullable;

/**
 * Created by Riccardo on 11/11/16.
 */

public interface PopupListener {
    void onShow(@Nullable String popupTag);

    void onDismiss(@Nullable String popupTag);
}
