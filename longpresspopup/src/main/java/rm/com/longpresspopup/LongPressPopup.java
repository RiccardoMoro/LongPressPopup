package rm.com.longpresspopup;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.IntDef;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Riccardo on 11/11/16.
 */

public class LongPressPopup implements LongPressPopupInterface, DialogInterface.OnDismissListener {

    private static final String TAG = LongPressPopup.class.getSimpleName();

    private Context mContext;
    private View mViewTarget;
    private ViewGroup mViewRootPopup;
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
    private DialogPopup mDialogPopup;
    @AnimationType
    private int mAnimationType;

    private boolean mRegistered;

    LongPressPopup(@NonNull LongPressPopupBuilder builder) {
        if (builder != null) {
            mContext = builder.getContext();
            mViewTarget = builder.getViewTarget();
            mViewRootPopup = new FrameLayout(mContext);
            mViewPopup = builder.getPopupView();
            // Get the layout and inflater listener only if no popup view set
            if (mViewPopup == null) {
                mViewPopupRes = builder.getPopupViewRes();
                mInflaterListener = builder.getInflaterListener();
            }
            mLongPressDuration = builder.getLongPressDuration();
            mDismissOnLongPressStop = builder.isDismissOnLongPressStop();
            mDismissOnTouchOutside = builder.isDismissOnTouchOutside();
            mDismissOnBackPressed = builder.isDismissOnBackPressed();
            mDispatchTouchEventOnRelease = builder.isDispatchTouchEventOnRelease();
            mLongPressReleaseClickListener = builder.getLongPressReleaseClickListener();

            // Hover only on ice cream sandwich or later
            mOnHoverListener = builder.getOnHoverListener();
            mPopupListener = builder.getPopupListener();
            mTag = builder.getTag();

            mDialogPopup = null;

            mAnimationType = builder.getAnimationType();

            mRegistered = false;
        } else {
            throw new IllegalArgumentException("Cannot create from null builder");
        }
    }

    public void register() {
        checkFieldsAndThrow();

        mViewTarget.setOnTouchListener(new PopupTouchListener(this, mLongPressDuration));
        mRegistered = true;
    }

    public void showNow() {
        if (!mRegistered) {
            register();
        }

        showPopupDialog();
    }

    public void dismissNow() {
        if (!mRegistered) {
            register();
        }

        dismissPopupDialog();
    }

    private void showPopupDialog() {
        if (mDialogPopup == null) {
            createDialog();
        } else if (mDialogPopup.isShowing()) {
            mDialogPopup.dismiss();
        }
        mViewRootPopup.removeAllViews();// Clear previous views

        mDialogPopup.setCancelable(mDismissOnTouchOutside);

        // Create the root view if only given the layout resource
        if (mViewPopup == null && mViewPopupRes != 0) {

            // Inflate manually from xml
            mViewPopup = LayoutInflater.from(mContext)
                    .inflate(mViewPopupRes, mViewRootPopup, false);
        }
        mViewRootPopup.addView(mViewPopup);// Add the popupView to the rootView
        mDialogPopup.setView(mViewRootPopup);// Set the root view as the popup view

        if (mInflaterListener != null) {
            mInflaterListener.onViewInflated(mViewRootPopup);
        }

        mDialogPopup.setOnDismissListener(this);
        mDialogPopup.show();

        if (mPopupListener != null) {
            mPopupListener.onShow(mTag);
        }
    }

    private void dismissPopupDialog() {
        if (mDialogPopup != null && mDialogPopup.isShowing())
            mDialogPopup.dismiss();

        if (mPopupListener != null) {
            mPopupListener.onDismiss(mTag);
        }
    }

    private void checkFieldsAndThrow() {
        String errorMessage = null;

        if (mViewPopup == null && mViewPopupRes == 0) {
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
    public void onPressStart(MotionEvent motionEvent) {
    }

    @Override
    public void onPressContinue(int progress, MotionEvent motionEvent) {
    }

    @Override
    public void onPressStop(MotionEvent motionEvent) {
    }

    @Override
    public void onLongPressStart(MotionEvent motionEvent) {

        showPopupDialog();
    }

    @Override
    public void onLongPressContinue(int longPressTime, MotionEvent motionEvent) {

        if (mDispatchTouchEventOnRelease) {

            // Only if simulate click on release, hover effects on child views
            dispatchActiveFocusToLeafsOnly(mViewRootPopup, motionEvent);
        }
    }

    @Override
    public void onLongPressEnd(MotionEvent motionEvent) {

        // Dispatch touch event on the popup child which the finger has been released on
        if (mDispatchTouchEventOnRelease && mViewRootPopup != null) {

            dispatchClickToLeafsOnly(mViewRootPopup, motionEvent);
        }


        if (mDismissOnLongPressStop) {

            dismissPopupDialog();
        }
    }

    private void dispatchClickToLeafsOnly(ViewGroup view, MotionEvent motionEvent) {

        // If no view or no children, return
        if (view == null || view.getChildCount() == 0) {
            return;
        }
        for (int i = 0; i < view.getChildCount(); i++) {
            View child = view.getChildAt(i);

            if (child instanceof ViewGroup) {

                // If a ViewGroup, search for leafs inside
                dispatchClickToLeafsOnly((ViewGroup) child, motionEvent);
            } else {

                // Else check if touch event is inside it
                if (LongPressPopupUtils.isTouchInsideView(child, motionEvent)) {

                    if (mLongPressReleaseClickListener != null) {

                        // Notify the given listener
                        mLongPressReleaseClickListener.onClick(child);
                    } else {

                        // Else, dispatch the touch event on the view
                        child.performClick();
                    }
                }
            }
        }
    }

    private void dispatchActiveFocusToLeafsOnly(ViewGroup view, MotionEvent motionEvent) {

        // If no view or no children, return
        if (view == null || view.getChildCount() == 0) {
            return;
        }
        for (int i = 0; i < view.getChildCount(); i++) {
            View child = view.getChildAt(i);

            if (child instanceof ViewGroup) {

                // If a ViewGroup, search for leafs inside
                dispatchActiveFocusToLeafsOnly((ViewGroup) child, motionEvent);
            } else {

                // Else check if touch event is inside it
                if (LongPressPopupUtils.isTouchInsideView(child, motionEvent)) {

                    if (!isViewFocused(child)) {

                        setFocusOnView(child);
                    }
                } else {

                    if (isViewFocused(child)) {

                        removeFocusFromView(child);
                    }
                }
            }
        }
    }

    private boolean isViewFocused(View target) {

        // Check press state
        return target.isPressed();
    }

    private void setFocusOnView(View target) {

        // Set pressed state
        target.setPressed(true);

        if (mOnHoverListener != null) {
            mOnHoverListener.onHoverChanged(target, true);
        }
    }

    private void removeFocusFromView(View target) {

        // Remove pressed state
        target.setPressed(false);

        if (mOnHoverListener != null) {
            mOnHoverListener.onHoverChanged(target, false);
        }
    }

    @Override
    public void onDismiss(DialogInterface dialogInterface) {
        if (mPopupListener != null) {
            mPopupListener.onDismiss(mTag);
        }
    }

    private void createDialog() {
        mDialogPopup = new DialogPopup(mContext, mAnimationType) {
            @Override
            public void onBackPressed() {
                if (mDismissOnBackPressed) {
                    super.onBackPressed();
                    mDialogPopup.dismiss();
                }
            }
        };
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

    public int getAnimationType() {
        return mAnimationType;
    }

    public boolean isRegistered() {
        return mRegistered;
    }


    // Animation type
    public static final int ANIMATION_TYPE_NO_ANIMATION = 0;
    public static final int ANIMATION_TYPE_FROM_LEFT = 1;
    public static final int ANIMATION_TYPE_FROM_RIGHT = 2;
    public static final int ANIMATION_TYPE_FROM_TOP = 3;
    public static final int ANIMATION_TYPE_FROM_BOTTOM = 4;
    public static final int ANIMATION_TYPE_FROM_CENTER = 5;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({ANIMATION_TYPE_NO_ANIMATION,
            ANIMATION_TYPE_FROM_LEFT,
            ANIMATION_TYPE_FROM_RIGHT,
            ANIMATION_TYPE_FROM_TOP,
            ANIMATION_TYPE_FROM_BOTTOM,
            ANIMATION_TYPE_FROM_CENTER})
    public @interface AnimationType {
    }
}