package rm.com.longpresspopup;

import android.content.Context;
import android.support.annotation.IntRange;
import android.support.annotation.LayoutRes;
import android.view.View;

/**
 * Created by Riccardo on 11/11/16.
 */

public class LongPressPopupBuilder {

    private static final String TAG = LongPressPopupBuilder.class.getSimpleName();

    private Context mContext;
    private View mViewTarget;
    private View mViewPopup;
    @LayoutRes
    private int mViewPopupRes;
    private PopupInflaterListener mInflaterListener;
    private int mLongPressDuration;
    private boolean mDismissOnLongPressStop;
    private boolean mDismissOnTouchOutside;
    private boolean mDismissOnBackPressed;
    private boolean mDispatchTouchEventOnRelease;
    private View.OnClickListener mLongPressReleaseClickListener;
    private PopupOnHoverListener mOnHoverListener;
    private PopupStateListener mPopupListener;
    private String mTag;
    private int mAnimationType;

    public LongPressPopupBuilder(Context context) {
        mContext = context;
        mViewTarget = null;
        mViewPopup = null;
        mViewPopupRes = 0;
        mInflaterListener = null;
        mLongPressDuration = PopupTouchListener.LONG_CLICK_DURATION;
        mDismissOnLongPressStop = true;
        mDismissOnTouchOutside = true;
        mDismissOnBackPressed = true;
        mDispatchTouchEventOnRelease = true;
        mLongPressReleaseClickListener = null;
        mOnHoverListener = null;
        mPopupListener = null;
        mTag = null;
        mAnimationType = LongPressPopup.ANIMATION_TYPE_FROM_CENTER;
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

    public LongPressPopupBuilder setPopupView(@LayoutRes int popupViewRes,
                                              PopupInflaterListener inflaterListener) {
        mViewPopupRes = popupViewRes;
        mInflaterListener = inflaterListener;

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

        // Set dispatch touch on release only if the dialog is set to be dismissed after touch
        // release
        mDispatchTouchEventOnRelease = mDismissOnLongPressStop;
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

    public LongPressPopupBuilder setLongPressReleaseListener(View.OnClickListener listener) {
        mLongPressReleaseClickListener = listener;
        return this;
    }

    public LongPressPopupBuilder setOnHoverListener(PopupOnHoverListener listener) {
        mOnHoverListener = listener;
        return this;
    }

    public LongPressPopupBuilder setPopupListener(PopupStateListener popupListener) {
        mPopupListener = popupListener;
        return this;
    }

    public LongPressPopupBuilder setTag(String tag) {
        mTag = tag;
        return this;
    }

    public LongPressPopupBuilder setAnimationType(@LongPressPopup.AnimationType int animationType) {
        mAnimationType = animationType;
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

    @LayoutRes
    public int getPopupViewRes() {
        return mViewPopupRes;
    }

    public PopupInflaterListener getInflaterListener() {
        return mInflaterListener;
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

    public boolean isDispatchTouchEventOnRelease() {
        return mDispatchTouchEventOnRelease;
    }

    public View.OnClickListener getLongPressReleaseClickListener() {
        return mLongPressReleaseClickListener;
    }

    public PopupOnHoverListener getOnHoverListener() {
        return mOnHoverListener;
    }

    public PopupStateListener getPopupListener() {
        return mPopupListener;
    }

    public String getTag() {
        return mTag;
    }

    @LongPressPopup.AnimationType
    public int getAnimationType() {
        return mAnimationType;
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
