package rm.com.longpresspopup;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.IntDef;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
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

public class PopupLongPress implements LongPressPopupInterface, DialogInterface.OnDismissListener {

    private static final String TAG = PopupLongPress.class.getSimpleName();

    /**
     * A valid context reference
     */
    private Context mContext;
    /**
     * The target view, the one that will make the popup appear on long click
     */
    private View mViewTarget;
    /**
     * Not exposed
     * It's the root view of the popup, needed for inflation
     */
    private ViewGroup mViewRootPopup;
    /**
     * Not exposed
     * The given popup view
     */
    private View mViewPopup;
    /**
     * The given popup layout resource
     */
    @LayoutRes
    private int mViewPopupRes;
    /**
     * The inflater listener, this will be called ONLY if a popup layout resource is passed as well
     */
    private PopupInflaterListener mInflaterListener;
    /**
     * The long press time needed to show the popup, default is 500
     */
    private int mLongPressDuration;
    /**
     * If the popup has to be dismissed on long press stop
     */
    private boolean mDismissOnLongPressStop;
    /**
     * If the popup has to be dismissed if the user touch outside
     * (valid only if {@link #mDismissOnLongPressStop} is set to false
     */
    private boolean mDismissOnTouchOutside;
    /**
     * If the popup has to be dismissed if the user clicks the back button
     * (valid only if {@link #mDismissOnLongPressStop} is set to false
     */
    private boolean mDismissOnBackPressed;
    /**
     * If dispatch the touch event has to be dispatcheed on the view on which the finger is over
     * when releasing the long touch
     */
    private boolean mDispatchTouchEventOnRelease;
    /**
     * When the parent is scrollable, the touch event is canceled on vertical scroll event
     * If you want to long press only the given view or if you want to let the user move around the
     * finger and show the popup anyway
     */
    private boolean mCancelTouchOnDragOutsideView;
    /**
     * Not exposed
     * The view on which the touch event starts
     */
    private View mInitialPressedView;
    /**
     * If this is not set, the view on which the fingers has been removed from will get the click
     * event
     * Called when releasing the long press on a specific view
     */
    private View.OnClickListener mLongPressReleaseClickListener;
    /**
     * The hover listener, it will be called every time the used drag his finger from one view to
     * another
     */
    private PopupOnHoverListener mOnHoverListener;
    /**
     * The popup state listener, will be called when popup is shown or dismissed
     */
    private PopupStateListener mPopupListener;
    /**
     * The Popup tag, used to distinguish it in callback methods
     */
    private String mTag;
    /**
     * Not exposed
     * The popup dialog
     */
    private DialogPopup mDialogPopup;
    /**
     * The animation type, may be one of {@link AnimationType}
     */
    @AnimationType
    private int mAnimationType;

    /**
     * Not exposed
     * The Popup touch listener, where all the touch-drag-click magic happens
     */
    private PopupTouchListener mPopupTouchListener;
    /**
     * If this long press popup has been registered and is ready to be shown
     */
    private boolean mRegistered;

    /**
     * Construct from builder, get all the configured params
     */
    PopupLongPress(@NonNull PopupLongPressBuilder builder) {
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
            mCancelTouchOnDragOutsideView = builder.isCancelOnDragOutsideView();
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

    /**
     * Used to register a PopupLongPress, it will start listening to touch events only when you
     * call this method
     */
    public void register() {
        checkFieldsAndThrow();

        mPopupTouchListener = new PopupTouchListener(this, mLongPressDuration);
        mViewTarget.setOnTouchListener(mPopupTouchListener);
        mRegistered = true;
    }

    /**
     * Deregister, this will make the longpresspopup stop listening for touch events
     */
    public void unregister() {
        mPopupTouchListener.stopPress(null);
        dismissPopupDialog();
        mViewTarget.setOnTouchListener(null);
        mRegistered = false;
    }

    /**
     * Show manually the popup
     */
    public void showNow() {
        if (!mRegistered) {
            register();
        }

        showPopupDialog();
    }

    /**
     * Dismiss manually the popup
     */
    public void dismissNow() {
        if (!mRegistered) {
            register();
        }

        dismissPopupDialog();
    }

    /**
     * Show the popup, remove all previous views and re-inflate them to show changes
     */
    private void showPopupDialog() {

        // Create the popup dialog if null
        if (mDialogPopup == null) {
            createDialog();
        } else if (mDialogPopup.isShowing()) {// Dismiss if already showing, avoid duplicate popups
            mDialogPopup.dismiss();
        }
        mViewRootPopup.removeAllViews();// Clear previous views
        mViewRootPopup.setBackgroundColor(
                ContextCompat.getColor(mContext, android.R.color.transparent));

        mDialogPopup.setCanceledOnTouchOutside(mDismissOnTouchOutside);

        // Create the root view if only given the layout resource
        if (mViewPopup == null && mViewPopupRes != 0) {

            // Inflate manually from xml
            mViewPopup = LayoutInflater.from(mContext)
                    .inflate(mViewPopupRes, mViewRootPopup, false);
        }
        mViewRootPopup.addView(mViewPopup);// Add the popupView to the rootView
        mDialogPopup.setView(mViewRootPopup);// Set the root view as the popup view

        if (mInflaterListener != null) {// Notify the inflater listener if there is one
            mInflaterListener.onViewInflated(mTag, mViewRootPopup);
        }

        // Set the dismiss listener and show the dialog
        mDialogPopup.setOnDismissListener(this);
        mDialogPopup.show();

        // Notify the listener that the popup has been shown
        if (mPopupListener != null) {
            mPopupListener.onPopupShow(mTag);
        }
    }

    /**
     * Dismiss the popup
     */
    private void dismissPopupDialog() {
        if (mDialogPopup != null && mDialogPopup.isShowing()) {
            mDialogPopup.dismiss();

            // Notify the listener that the popup has been dismissed
            if (mPopupListener != null) {
                mPopupListener.onPopupDismiss(mTag);
            }
        }
    }

    /**
     * Check if some of the required fields are missing and throw an IllegalArgumentsException
     */
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
    public void onPressStart(View pressedView, MotionEvent motionEvent) {

        // Save the initial pressed view
        mInitialPressedView = pressedView;
    }

    @Override
    public void onPressContinue(int progress, MotionEvent motionEvent) {

        // If set to accept press only from initial view and outside, stop pressing
        if (mCancelTouchOnDragOutsideView && mInitialPressedView != null &&
                !LongPressPopupUtils.isTouchInsideView(mInitialPressedView, motionEvent)) {

            mPopupTouchListener.stopPress(motionEvent);
        }
    }

    @Override
    public void onPressStop(MotionEvent motionEvent) {

        // Clear the initial pressed view on press stop
        mInitialPressedView = null;
    }

    @Override
    public void onLongPressStart(MotionEvent motionEvent) {

        // When long press start, show the popup dialog
        showPopupDialog();
    }

    @Override
    public void onLongPressContinue(int longPressTime, MotionEvent motionEvent) {

        // If set an hover listener or if dispatcheventonrelease is true, pass focus event to
        // Selected view's leafs
        if (mDispatchTouchEventOnRelease || mOnHoverListener != null) {

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

        // If has been set that the popup is closed on long touch release, dismiss it
        if (mDismissOnLongPressStop) {

            dismissPopupDialog();
        }

        // Clear initial pressed view on touch end
        mInitialPressedView = null;
    }

    /**
     * Utility recursive method that dispatches the click event to only the given view's leafs
     */
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

    /**
     * Utility recursive method that dispatches the press state to leafs, to animate them
     */
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

                    if (!isViewFocused(child)) {// Avoid duplicated events

                        // Focus the view!
                        setFocusOnView(child);
                    }
                } else {

                    if (isViewFocused(child)) {// Avoid duplicated events

                        // Remove focus from the view!
                        removeFocusFromView(child);
                    }
                }
            }
        }
    }

    /**
     * Checks if the given view is focused
     */
    private boolean isViewFocused(View target) {

        // Check press state
        return target.isPressed();
    }

    /**
     * Set the focus state on the view
     */
    private void setFocusOnView(View target) {

        // Set pressed state
        target.setPressed(true);

        if (mOnHoverListener != null) {
            mOnHoverListener.onHoverChanged(target, true);
        }
    }

    /**
     * Remove the focus state from the view
     */
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
            mPopupListener.onPopupDismiss(mTag);
        }

        if (mPopupTouchListener != null) {
            mPopupTouchListener.stopPress(null);
        }
    }

    /**
     * Create the DialogPopup object
     */
    private void createDialog() {
        mDialogPopup = new DialogPopup(mContext, mAnimationType) {
            @Override
            public void onBackPressed() {

                // If has been set to be dismissed on back pressed, do it, else do nothing
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