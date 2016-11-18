package rm.com.longpresspopupsample.adapters;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import rm.com.longpresspopup.LongPressPopup;
import rm.com.longpresspopup.PopupInflaterListener;
import rm.com.longpresspopup.LongPressPopupBuilder;
import rm.com.longpresspopup.PopupOnHoverListener;
import rm.com.longpresspopup.PopupStateListener;
import rm.com.longpresspopupsample.Data;
import rm.com.longpresspopupsample.R;
import rm.com.longpresspopupsample.activities.ActivityDetail;
import rm.com.longpresspopupsample.utils.ToastUtils;
import rm.com.longpresspopupsample.utils.VibratorUtils;

/**
 * Created by Riccardo on 15/11/16.
 */

public class GalleryViewHolder extends RecyclerView.ViewHolder
        implements PopupInflaterListener, View.OnClickListener, PopupStateListener,
        PopupOnHoverListener {

    private ImageView mImg;

    private LongPressPopup mPopup;
    private ImageView mPopupImg;
    private TextView mPopupTitle;

    public GalleryViewHolder(View itemView) {
        super(itemView);

        mImg = (ImageView) itemView.findViewById(R.id.list_img);
    }

    public void onBind() {
        if (mPopup != null && mPopup.isRegistered()) {
            mPopup.unregister();
        }

        Glide.with(itemView.getContext())
                .load(Data.sImgResources[getAdapterPosition()])
                .into(mImg);

        mPopup = new LongPressPopupBuilder(itemView.getContext())
                .setTarget(itemView)
                .setPopupView(R.layout.popup_layout, this)
                .setAnimationType(LongPressPopup.ANIMATION_TYPE_FROM_CENTER)
                .setPopupListener(this)
                .setOnHoverListener(this)
                .build();
        mPopup.register();

        itemView.setOnClickListener(this);
    }


    @Override
    public void onViewInflated(@Nullable String popupTag, View root) {
        mPopupImg = (ImageView) root.findViewById(R.id.popup_img);
        mPopupTitle = (TextView) root.findViewById(R.id.popup_title);

        mPopupTitle.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if ((mPopupTitle != null && view.getId() == mPopupTitle.getId()) ||
                view.getId() == itemView.getId()) {
            Intent startIntent = new Intent(itemView.getContext(), ActivityDetail.class);

            startIntent.putExtra(ActivityDetail.BUNDLE_ELEMENT, getAdapterPosition());

            itemView.getContext().startActivity(startIntent);
        }
    }


    @Override
    public void onPopupShow(@Nullable String popupTag) {
        if (mPopupImg != null) {
            Glide.with(itemView.getContext())
                    .load(Data.sImgResources[getAdapterPosition()])
                    .into(mPopupImg);
        }

        if (mPopupTitle != null) {
            mPopupTitle.setText(Data.sVersionNames[getAdapterPosition()]);
        }
    }

    @Override
    public void onPopupDismiss(@Nullable String popupTag) {

    }

    @Override
    public void onHoverChanged(View view, boolean isHovered) {
        if (isHovered) {

            if (view.getId() == mPopupTitle.getId()) {

                ToastUtils.showLocalizedToast(view.getContext(), "Go to details", view);

                VibratorUtils.vibrate(view.getContext());
            } else if (view.getId() == mPopupImg.getId()) {

                ToastUtils.showLocalizedToast(view.getContext(), view.getContext()
                        .getString(Data.sVersionNumbers[getAdapterPosition()]), view);

                VibratorUtils.vibrate(view.getContext());
            }
        }
    }
}
