package rm.com.longpresspopupsample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import rm.com.longpresspopup.LongPressPopup;
import rm.com.longpresspopup.LongPressPopupBuilder;
import rm.com.longpresspopup.PopupListener;

public class ActivityMain extends AppCompatActivity implements PopupListener {

    private Button mBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBtn = (Button) findViewById(R.id.btn_long_clickable);

        Button button = new Button(this);
        button.setText("Hello, World!");

        LongPressPopup popup = new LongPressPopupBuilder(this)
                .setTarget(mBtn)
                .setPopupView(button)
                .setDismissOnLongPressStop(false)
                .setListener(this)
                .build();
        popup.register();
    }


    @Override
    public void onShow(@Nullable String popupTag) {

    }

    @Override
    public void onDismiss(@Nullable String popupTag) {

    }
}
