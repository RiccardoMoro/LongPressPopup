package rm.com.longpresspopup;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

/**
 * Created by Riccardo on 11/11/16.
 */

public class LongPressPopup implements LongPressPopupInterface, DialogInterface.OnDismissListener {

    private Context mContext;
    private View mViewTarget;
    private View mViewPopup;
    private int mLongPressDuration;
    private boolean mDismissOnLongPressStop;
    private boolean mDismissOnTouchOutside;
    private boolean mDismissOnBackPressed;
    private PopupListener mPopupListener;
    private String mTag;
    private DialogPopup mDialogPopup;

    LongPressPopup(@NonNull LongPressPopupBuilder builder) {
        if (builder != null) {
            mContext = builder.getContext();
            mViewTarget = builder.getViewTarget();
            mViewPopup = builder.getPopupView();
            mLongPressDuration = builder.getLongPressDuration();
            mDismissOnLongPressStop = builder.isDismissOnLongPressStop();
            mDismissOnTouchOutside = builder.isDismissOnTouchOutside();
            mDismissOnBackPressed = builder.isDismissOnBackPressed();
            mPopupListener = builder.getListener();
            mTag = builder.getTag();

            mDialogPopup = null;
        } else {
            throw new IllegalArgumentException("Cannot create from null builder");
        }
    }

    public void register() {
        checkFieldsAndThrow();

        mViewTarget.setOnTouchListener(new PopupTouchListener(this, mLongPressDuration));
    }

    private void checkFieldsAndThrow() {
        String errorMessage = null;

        if (mViewPopup == null) {
            errorMessage = "Cannot create with a null popup view";
        }

        if (mViewTarget == null) {
            errorMessage = "Cannot create with a null target view";
        }

        if (errorMessage != null) {
            throw new IllegalArgumentException(errorMessage);
        }
    }


    // LongPressPopupInterface methods
    @Override
    public void onPressStart(float x, float y) {
        Log.e("Test", "on press started");
    }

    @Override
    public void onPressContinue(int progress, float x, float y) {
        Log.e("Test", "on press continue: " + progress);
    }

    @Override
    public void onPressStop(float x, float y) {
        Log.e("Test", "on press stop");
    }

    @Override
    public void onLongPressStart(float x, float y) {
        Log.e("Test", "On long press started");

        if (mDialogPopup == null) {
            createDialog();
        }
        mDialogPopup.setCancelable(mDismissOnTouchOutside);
        mDialogPopup.setView(mViewPopup);
        mDialogPopup.setOnDismissListener(this);
        mDialogPopup.show();

        if (mPopupListener != null) {
            mPopupListener.onShow(mTag);
        }
    }

    @Override
    public void onLongPressContinue(int longPressTime, float x, float y) {
        Log.e("Test", "on long press continue: " + longPressTime);
    }

    @Override
    public void onLongPressEnd(float x, float y) {
        Log.e("Test", "on long press stop");

        if (mDismissOnLongPressStop && mPopupListener != null) {
            mDialogPopup.dismiss();
            mPopupListener.onDismiss(mTag);
        }
    }

    @Override
    public void onDismiss(DialogInterface dialogInterface) {
        if (mPopupListener != null) {
            mPopupListener.onDismiss(mTag);
        }
    }

    private void createDialog() {
        mDialogPopup = new DialogPopup(mContext) {
            @Override
            public void onBackPressed() {
                if (mDismissOnBackPressed) {
                    super.onBackPressed();
                    mDialogPopup.dismiss();
                }
            }
        };
    }


    // Static utility methods
    @Nullable
    public static LongPressPopup findPopupByTag(String tag) {
        // TODO Create a manager that has all the current instances
        return null;
    }
}
