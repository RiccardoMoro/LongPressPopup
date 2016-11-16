package rm.com.longpresspopupsample.utils;

import android.Manifest;
import android.content.Context;
import android.os.Build;
import android.os.Vibrator;
import android.support.annotation.RequiresPermission;

/**
 * Created by Riccardo on 16/11/16.
 */

public class VibratorUtils {

    @RequiresPermission(value = Manifest.permission.VIBRATE)
    public static void vibrate(Context context) {
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);

        if (vibrator != null &&
                (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB &&
                        vibrator.hasVibrator())) {

            vibrator.vibrate(200);
        }
    }
}
