package rm.com.longpresspopupsample.utils;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

/**
 * Created by Riccardo on 15/11/16.
 */

public class ToastUtils {

    // Unified toast messages
    private static Toast sToast = null;

    public static void showLocalizedToast(Context context, String msg, final View target) {
        cancelToast();

        createToast(context, msg);

        if (target != null) {

            sToast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, 56);

            showToast();
        }
    }

    public static void showToast(Context context, String msg) {
        cancelToast();

        createToast(context, msg);

        showToast();
    }

    private static void showToast() {
        sToast.show();
    }

    private static void createToast(Context context, String msg) {
        sToast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
    }

    private static void cancelToast() {
        if (sToast != null) {
            sToast.cancel();
        }
    }
}
