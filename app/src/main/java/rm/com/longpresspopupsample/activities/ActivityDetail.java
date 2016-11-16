package rm.com.longpresspopupsample.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import rm.com.longpresspopupsample.Data;
import rm.com.longpresspopupsample.R;

/**
 * Created by Riccardo on 15/11/16.
 */
public class ActivityDetail extends AppCompatActivity {

    public static final String BUNDLE_ELEMENT = "element";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_detail);

        if (getIntent() != null) {
            int position = getIntent().getIntExtra(BUNDLE_ELEMENT, 0);

            setupUI(position);
        } else {
            finish();
        }
    }

    private void setupUI(int position) {
        Glide.with(this)
                .load(Data.sImgResources[position])
                .into((ImageView) findViewById(R.id.img_detail));

        TextView title = (TextView) findViewById(R.id.txt_detail_title);
        TextView description = (TextView) findViewById(R.id.txt_detail_description);

        title.setText(Data.sVersionNames[position]);
        description.setText(Data.sDescriptions[position]);
    }
}