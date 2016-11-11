package rm.com.longpresspopup;

import android.content.Context;
import android.view.View;

/**
 * Created by Riccardo on 11/11/16.
 */

public class LongPressPopupBuilder {

    private Context mContext;
    private View mViewTarget;
    private View mViewPopup;
    private boolean mDismissOnLongPressStop;
    private boolean mDismissOnTouchOutside;
    private PopupListener mPopupListener;
    private String mTag;

    public LongPressPopupBuilder(Context context) {
        mContext = context;
        mViewTarget = null;
        mViewPopup = null;
        mDismissOnLongPressStop = true;
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

    public LongPressPopupBuilder setDismissOnLongPressStop(boolean dismissOnPressStop) {
        mDismissOnLongPressStop = dismissOnPressStop;
        return this;
    }

    public LongPressPopupBuilder setDismissOnTouchOutside(boolean dismissOnTouchOutside) {
        mDismissOnTouchOutside = dismissOnTouchOutside;
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

    public boolean isDismissOnLongPressStop() {
        return mDismissOnLongPressStop;
    }

    public boolean isDismissOnTouchOutside() {
        return mDismissOnTouchOutside;
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
