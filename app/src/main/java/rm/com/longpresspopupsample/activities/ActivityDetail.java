package rm.com.longpresspopupsample.activities;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import rm.com.longpresspopup.LongPressPopup;
import rm.com.longpresspopup.LongPressPopupBuilder;
import rm.com.longpresspopup.PopupInflaterListener;
import rm.com.longpresspopup.PopupOnHoverListener;
import rm.com.longpresspopup.PopupStateListener;
import rm.com.longpresspopupsample.Data;
import rm.com.longpresspopupsample.R;
import rm.com.longpresspopupsample.utils.IntentUtils;

/**
 * Created by Riccardo on 15/11/16.
 */
public class ActivityDetail extends AppCompatActivity implements PopupInflaterListener,
        PopupStateListener, PopupOnHoverListener {

    public static final String BUNDLE_ELEMENT = "element";

    private static final String FAB_TAG = "Fab";
    private static final String TOOLBAR_TAG = "Toolbar";


    private int mPosition;

    private ImageView mImgPopup;
    private WebView mWebViewWikipedia;

    private FloatingActionButton mFabWikipedia;
    private CollapsingToolbarLayout mToolbarLayout;
    private ProgressBar mWebViewProgressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_detail);

        if (getIntent() != null) {
            mPosition = getIntent().getIntExtra(BUNDLE_ELEMENT, 0);

            setupUI();
        } else {
            finish();
        }
    }

    private void setupUI() {
        Glide.with(this)
                .load(Data.sImgResources[mPosition])
                .into((ImageView) findViewById(R.id.img_detail));

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView description = (TextView) findViewById(R.id.txt_detail_description);

        mToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        mFabWikipedia = (FloatingActionButton) findViewById(R.id.fab_wikipedia);

        new LongPressPopupBuilder(this)
                .setTarget(mToolbarLayout)
                .setPopupView(R.layout.popup_layout_image, this)
                .setPopupListener(this)
                .setAnimationType(LongPressPopup.ANIMATION_TYPE_FROM_TOP)
                .build(TOOLBAR_TAG)
                .register();

        new LongPressPopupBuilder(this)
                .setTarget(mFabWikipedia)
                .setPopupView(R.layout.popup_layout_webview, this)
                .setAnimationType(LongPressPopup.ANIMATION_TYPE_FROM_BOTTOM)
                .setDismissOnLongPressStop(false)
                .setDismissOnTouchOutside(true)
                .setPopupListener(this)
                .build(FAB_TAG)
                .register();


        mFabWikipedia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentUtils.openLink(
                        ActivityDetail.this, getString(Data.sUrls[mPosition]));
            }
        });


        setSupportActionBar(toolbar);

        description.setText(Data.sDescriptions[mPosition]);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(Data.sVersionNames[mPosition]);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mWebViewWikipedia != null) {
            mWebViewWikipedia.destroy();
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

    // Popup inflated callback
    @Override
    public void onViewInflated(@Nullable String tag, View root) {
        if (tag != null) {
            if (tag.equals(TOOLBAR_TAG)) {
                mImgPopup = (ImageView) root.findViewById(R.id.popup_detail_img);
            } else if (tag.equals(FAB_TAG)) {
                mWebViewWikipedia = (WebView) root.findViewById(R.id.popup_webview);
                mWebViewProgressBar = (ProgressBar) root.findViewById(R.id.popup_progress);
                mWebViewWikipedia.setWebViewClient(new WebViewClient() {
                    @Override
                    public void onPageStarted(WebView view, String url, Bitmap favicon) {
                        super.onPageStarted(view, url, favicon);

                        if (mWebViewProgressBar != null) {
                            if (mWebViewProgressBar.getVisibility() == View.GONE) {
                                mWebViewProgressBar.setVisibility(View.VISIBLE);
                            }

                            mWebViewProgressBar.setProgress(0);
                        }
                    }

                    @Override
                    public void onPageFinished(WebView view, String url) {
                        super.onPageFinished(view, url);

                        if (mWebViewProgressBar != null) {
                            if (mWebViewProgressBar.getVisibility() != View.GONE) {
                                mWebViewProgressBar.setVisibility(View.GONE);
                            }
                        }
                    }

                    @Override
                    public void onLoadResource(WebView view, String url) {
                        super.onLoadResource(view, url);
                    }
                });
                mWebViewWikipedia.setWebChromeClient(new WebChromeClient() {

                    @Override
                    public void onProgressChanged(WebView view, int newProgress) {
                        super.onProgressChanged(view, newProgress);
                        if (mWebViewProgressBar != null) {
                            if (mWebViewProgressBar.getVisibility() == View.GONE) {
                                mWebViewProgressBar.setVisibility(View.VISIBLE);
                            }

                            mWebViewProgressBar.setProgress(newProgress);
                        }
                    }
                });

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    mWebViewWikipedia.getSettings().setDisplayZoomControls(false);
                }
                mWebViewWikipedia.getSettings().setSupportZoom(true);
                mWebViewWikipedia.getSettings().setUseWideViewPort(true);
                mWebViewWikipedia.getSettings().setBuiltInZoomControls(true);
                mWebViewWikipedia.getSettings().setJavaScriptEnabled(true);
            }
        }
    }

    @Override
    public void onPopupShow(@Nullable String popupTag) {

        if (popupTag != null) {
            if (popupTag.equals(TOOLBAR_TAG)) {
                Glide.with(this)
                        .load(Data.sImgResources[mPosition])
                        .into(mImgPopup);
            } else if (popupTag.equals(FAB_TAG)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    mWebViewWikipedia.onResume();
                }
                mWebViewWikipedia.loadUrl(getString(Data.sUrls[mPosition]));
            }
        }
    }

    @Override
    public void onPopupDismiss(@Nullable String popupTag) {
        if (popupTag != null && popupTag.equals(FAB_TAG) && mWebViewWikipedia != null) {
            mWebViewWikipedia.onPause();
        }
    }

    @Override
    public void onHoverChanged(View view, boolean isHovered) {

    }
}
