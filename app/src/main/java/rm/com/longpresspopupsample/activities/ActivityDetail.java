package rm.com.longpresspopupsample.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import rm.com.longpresspopupsample.Data;
import rm.com.longpresspopupsample.R;
import rm.com.longpresspopupsample.utils.IntentUtils;

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

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView description = (TextView) findViewById(R.id.txt_detail_description);

        setSupportActionBar(toolbar);

        description.setText(Data.sDescriptions[position]);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(Data.sVersionNames[position]);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_github) {
            IntentUtils.openGithubProfile(this);
        }

        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }

        return true;
    }
}
