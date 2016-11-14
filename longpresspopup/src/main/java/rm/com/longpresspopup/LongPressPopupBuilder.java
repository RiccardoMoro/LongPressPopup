package rm.com.longpresspopup;

import android.content.Context;
import android.support.annotation.IntRange;
import android.view.View;

/**
 * Created by Riccardo on 11/11/16.
 */

public class LongPressPopupBuilder {

    private Context mContext;
    private View mViewTarget;
    private View mViewPopup;
    private int mLongPressDuration;
    private boolean mDismissOnLongPressStop;
    private boolean mDismissOnTouchOutside;
    private boolean mDismissOnBackPressed;
    private PopupListener mPopupListener;
    private String mTag;

    public LongPressPopupBuilder(Context context) {
        mContext = context;
        mViewTarget = null;
        mViewPopup = null;
        mLongPressDuration = PopupTouchListener.LONG_CLICK_DURATION;
        mDismissOnLongPressStop = true;
        mDismissOnTouchOutside = true;
        mDismissOnBackPressed = true;
        mTag = null;
    }


    // Setters
    public LongPressPopupBuilder setTarget(View target) {
        mViewTarget = target;
        return this;
    }

    public LongPressPopupBuilder setPopupView(View popupView) {
        mViewPopup = popupView;
        return this;
    }

    public LongPressPopupBuilder setLongPressDuration(@IntRange(from = 1) int duration) {
        if (duration > 0) {
            mLongPressDuration = duration;
        }

        return this;
    }

    public LongPressPopupBuilder setDismissOnLongPressStop(boolean dismissOnPressStop) {
        mDismissOnLongPressStop = dismissOnPressStop;
        return this;
    }

    public LongPressPopupBuilder setDismissOnTouchOutside(boolean dismissOnTouchOutside) {
        mDismissOnTouchOutside = dismissOnTouchOutside;
        return this;
    }

    public LongPressPopupBuilder setDismissOnBackPressed(boolean dismissOnBackPressed) {
        mDismissOnBackPressed = dismissOnBackPressed;
        return this;
    }

    public LongPressPopupBuilder setListener(PopupListener popupListener) {
        mPopupListener = popupListener;
        return this;
    }

    public LongPressPopupBuilder setTag(String tag) {
        mTag = tag;
        return this;
    }


    // Getters
    public Context getContext() {
        return mContext;
    }

    public View getViewTarget() {
        return mViewTarget;
    }

    public View getPopupView() {
        return mViewPopup;
    }

    public int getLongPressDuration() {
        return mLongPressDuration;
    }

    public boolean isDismissOnLongPressStop() {
        return mDismissOnLongPressStop;
    }

    public boolean isDismissOnTouchOutside() {
        return mDismissOnTouchOutside;
    }

    public boolean isDismissOnBackPressed() {
        return mDismissOnBackPressed;
    }

    public PopupListener getListener() {
        return mPopupListener;
    }

    public String getTag() {
        return mTag;
    }


    // Build methods to obtain the LongPressPopup instance
    public LongPressPopup build(String tag) {
        setTag(tag);
        return build();
    }

    public LongPressPopup build() {
        return new LongPressPopup(this);
    }
}
