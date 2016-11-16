package rm.com.longpresspopupsample.activities;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;

import rm.com.longpresspopupsample.R;
import rm.com.longpresspopupsample.adapters.GalleryAdapter;
import rm.com.longpresspopupsample.utils.IntentUtils;

public class ActivityMain extends AppCompatActivity {

    private static final String TAG = ActivityMain.class.getSimpleName();

    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.main_recycler);

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;

        // Choose between one, two or three elements per row
        if (dpWidth > 720 && getResources().getConfiguration().orientation ==
                ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            mRecyclerView.setLayoutManager(new GridLayoutManager(
                    this, 3, LinearLayoutManager.VERTICAL, false));
        } else if (dpWidth > 520) {
            mRecyclerView.setLayoutManager(new GridLayoutManager(
                    this, 2, LinearLayoutManager.VERTICAL, false));
        } else {
            mRecyclerView.setLayoutManager(new LinearLayoutManager(
                    this, LinearLayoutManager.VERTICAL, false));
        }
        mRecyclerView.addItemDecoration(
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mRecyclerView.setAdapter(new GalleryAdapter(this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_github) {
            IntentUtils.openGithubProfile(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
