package rm.com.longpresspopupsample.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import rm.com.longpresspopupsample.Data;
import rm.com.longpresspopupsample.R;

/**
 * Created by Riccardo on 15/11/16.
 */

public class GalleryAdapter extends RecyclerView.Adapter<GalleryViewHolder> {

    private Context mContext;
    private LayoutInflater mInflater;

    public GalleryAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public GalleryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new GalleryViewHolder(
                mInflater.inflate(R.layout.gallery_list_element, parent, false));
    }

    @Override
    public void onBindViewHolder(GalleryViewHolder holder, int position) {
        holder.onBind();
    }

    @Override
    public int getItemCount() {
        return Data.sImgResources.length;
    }
}
