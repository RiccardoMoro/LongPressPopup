package rm.com.longpresspopup;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;

/**
 * Created by Riccardo on 11/11/16.
 */

public class DialogPopup extends AlertDialog {

    protected DialogPopup(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            getWindow().getAttributes().windowAnimations = R.style.DialogAnimations;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
