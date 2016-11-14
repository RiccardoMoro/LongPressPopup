package rm.com.longpresspopupsample;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import rm.com.longpresspopup.LongPressPopupBuilder;
import rm.com.longpresspopup.PopupInflaterListener;
import rm.com.longpresspopup.PopupOnHoverListener;
import rm.com.longpresspopup.PopupStateListener;

public class ActivityMain extends AppCompatActivity implements PopupStateListener,
        PopupInflaterListener {

    private static final String TAG = ActivityMain.class.getSimpleName();
    private Button mBtn;
    private Button mBtn2;
    private Button mBtn3;
    private Button mBtn4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBtn = (Button) findViewById(R.id.btn_long_clickable);
        mBtn2 = (Button) findViewById(R.id.btn_long_clickable_2);
        mBtn3 = (Button) findViewById(R.id.btn_long_clickable_3);
        mBtn4 = (Button) findViewById(R.id.btn_long_clickable_4);

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
                .setPopupListener(this)
                .build()
                .register();

        new LongPressPopupBuilder(this)
                .setTarget(mBtn2)
                .setPopupView(button2)
                .setLongPressDuration(500)
                .setDismissOnLongPressStop(true)
                .setPopupListener(this)
                .build()
                .register();

        new LongPressPopupBuilder(this)
                .setTarget(mBtn3)
                .setPopupView(R.layout.poup_layout, this)
                .setLongPressDuration(500)
                .setDismissOnLongPressStop(true)
                .build()
                .register();

        new LongPressPopupBuilder(this)
                .setTarget(mBtn4)
                .setPopupView(R.layout.poup_layout, null)
                .setLongPressDuration(500)
                .setDismissOnLongPressStop(true)
                .setOnHoverListener(new PopupOnHoverListener() {

                    @Override
                    public void onHoverChanged(View view, boolean isHovered) {

                        if (isHovered) {
                            showToast(ActivityMain.this, "Hover on " + view.getClass()
                                    .getSimpleName());
                        }
                    }
                })
                .setLongPressReleaseListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        showToast(ActivityMain.this, "ClickedFromBuilder: " +
                                view.getClass().getSimpleName());
                    }
                })
                .build()
                .register();
    }

    @Override
    public void onShow(@Nullable String popupTag) {
    }

    @Override
    public void onDismiss(@Nullable String popupTag) {
    }


    @Override
    public void onViewInflated(View root) {
        Button btnPopup = (Button) root.findViewById(R.id.btn_popup);
        btnPopup.setText(btnPopup.getText() + " number 3!");
        btnPopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showToast(ActivityMain.this, "ClickedStandard: " +
                        view.getClass().getSimpleName());
            }
        });
    }


    private static Toast sToast = null;

    private static void showToast(Context context, String msg) {
        if (sToast != null) {
            sToast.cancel();
        }

        sToast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        sToast.show();
    }
}
