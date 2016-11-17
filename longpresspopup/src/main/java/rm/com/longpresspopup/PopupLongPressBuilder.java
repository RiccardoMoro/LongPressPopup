package rm.com.longpresspopup;

import android.content.Context;
import android.support.annotation.IntRange;
import android.support.annotation.LayoutRes;
import android.view.View;

/**
 * Created by Riccardo on 11/11/16.
 */

public class PopupLongPressBuilder {

    private static final String TAG = PopupLongPressBuilder.class.getSimpleName();

    /**
     * For variable meanings, check {@link PopupLongPress}
     */

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
    private boolean mCancelTouchOnDragOutsideView;
    private View.OnClickListener mLongPressReleaseClickListener;
    private PopupOnHoverListener mOnHoverListener;
    private PopupStateListener mPopupListener;
    private String mTag;
    private int mAnimationType;

    /**
     * Constructor that initialize all the parameters to default value
     */
    public PopupLongPressBuilder(Context context) {
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
        mCancelTouchOnDragOutsideView = true;
        mLongPressReleaseClickListener = null;
        mOnHoverListener = null;
        mPopupListener = null;
        mTag = null;
        mAnimationType = PopupLongPress.ANIMATION_TYPE_FROM_CENTER;
    }


    // Setters

    /**
     * Check them at {@link PopupLongPress} variable section
     */
    public PopupLongPressBuilder setTarget(View target) {
        mViewTarget = target;
        return this;
    }

    public PopupLongPressBuilder setPopupView(View popupView) {
        mViewPopup = popupView;
        return this;
    }

    public PopupLongPressBuilder setPopupView(@LayoutRes int popupViewRes,
                                              PopupInflaterListener inflaterListener) {
        mViewPopupRes = popupViewRes;
        mInflaterListener = inflaterListener;

        return this;
    }

    public PopupLongPressBuilder setLongPressDuration(@IntRange(from = 1) int duration) {
        if (duration > 0) {
            mLongPressDuration = duration;
        }

        return this;
    }

    public PopupLongPressBuilder setDismissOnLongPressStop(boolean dismissOnPressStop) {
        mDismissOnLongPressStop = dismissOnPressStop;

        // Set dispatch touch on release only if the dialog is set to be dismissed after touch
        // release
        mDispatchTouchEventOnRelease = mDismissOnLongPressStop;
        return this;
    }

    public PopupLongPressBuilder setDismissOnTouchOutside(boolean dismissOnTouchOutside) {
        mDismissOnTouchOutside = dismissOnTouchOutside;
        return this;
    }

    public PopupLongPressBuilder setDismissOnBackPressed(boolean dismissOnBackPressed) {
        mDismissOnBackPressed = dismissOnBackPressed;
        return this;
    }

    public PopupLongPressBuilder setCancelTouchOnDragOutsideView(boolean cancelOnDragOutside) {
        mCancelTouchOnDragOutsideView = cancelOnDragOutside;
        return this;
    }

    public PopupLongPressBuilder setLongPressReleaseListener(View.OnClickListener listener) {
        mLongPressReleaseClickListener = listener;
        return this;
    }

    public PopupLongPressBuilder setOnHoverListener(PopupOnHoverListener listener) {
        mOnHoverListener = listener;
        return this;
    }

    public PopupLongPressBuilder setPopupListener(PopupStateListener popupListener) {
        mPopupListener = popupListener;
        return this;
    }

    public PopupLongPressBuilder setTag(String tag) {
        mTag = tag;
        return this;
    }

    public PopupLongPressBuilder setAnimationType(@PopupLongPress.AnimationType int animationType) {
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

    public boolean isCancelOnDragOutsideView() {
        return mCancelTouchOnDragOutsideView;
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

    @PopupLongPress.AnimationType
    public int getAnimationType() {
        return mAnimationType;
    }


    // Build methods to obtain the PopupLongPress instance

    /**
     * Build with tag, returns a LongPressPopupIntance
     */
    public PopupLongPress build(String tag) {
        setTag(tag);
        return build();
    }

    /**
     * Build without tag, returns a LongPressInstance
     */
    public PopupLongPress build() {
        return new PopupLongPress(this);
    }
}
