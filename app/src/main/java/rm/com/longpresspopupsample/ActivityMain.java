package rm.com.longpresspopupsample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.Toast;

import rm.com.longpresspopup.LongPressPopupBuilder;
import rm.com.longpresspopup.PopupListener;

public class ActivityMain extends AppCompatActivity implements PopupListener {

    private Button mBtn;
    private Button mBtn2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBtn = (Button) findViewById(R.id.btn_long_clickable);
        mBtn2 = (Button) findViewById(R.id.btn_long_clickable_2);

        Button button = new Button(this);
        button.setText("Hello, World!");

        Button button2 = new Button(this);
        button2.setText("Hello, World2!");

        new LongPressPopupBuilder(this)
                .setTarget(mBtn)
                .setPopupView(button)
                .setLongPressDuration(200)
                .setDismissOnLongPressStop(false)
                .setDismissOnTouchOutside(false)
                .setDismissOnBackPressed(true)
                .setListener(this)
                .build()
                .register();

        new LongPressPopupBuilder(this)
                .setTarget(mBtn2)
                .setPopupView(button2)
                .setDismissOnLongPressStop(true)
                .setListener(this)
                .build()
                .register();
    }


    @Override
    public void onShow(@Nullable String popupTag) {
        Toast.makeText(this, "show: " + popupTag, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDismiss(@Nullable String popupTag) {
        Toast.makeText(this, "dismiss: " + popupTag, Toast.LENGTH_SHORT).show();
    }
}
