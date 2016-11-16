package rm.com.longpresspopupsample.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import rm.com.longpresspopupsample.R;

/**
 * Created by Riccardo on 16/11/16.
 */

public class IntentUtils {

    public static void openGithubProfile(Context context) {
        openLink(context, context.getString(R.string.my_github_url));
    }

    static void openLink(Context context, String link) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
        context.startActivity(browserIntent);
    }
}
