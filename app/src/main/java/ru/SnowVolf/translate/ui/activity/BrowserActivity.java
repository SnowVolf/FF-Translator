package ru.SnowVolf.translate.ui.activity;

import android.Manifest;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebBackForwardList;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import ru.SnowVolf.translate.R;
import ru.SnowVolf.translate.api.yandex.YandexAPI;
import ru.SnowVolf.translate.ui.widget.ContextMenuTitleView;
import ru.SnowVolf.translate.ui.widget.ExtendedEditText;
import ru.SnowVolf.translate.ui.widget.InfoBar;
import ru.SnowVolf.translate.ui.widget.WebViewToolbar;
import ru.SnowVolf.translate.util.KeyboardUtil;
import ru.SnowVolf.translate.util.Utils;

public class BrowserActivity extends BaseActivity {

    public static final String EXTRA_URL = "intent.url";
    private WebView mWebView;
    private Context mContext;
    private WebViewToolbar mToolbar;
    private AppBarLayout mAppBarLayout;
    private CoordinatorLayout mCoordinatorLayout;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    public InfoBar mInfoBar;

    private AppBarLayout.Behavior mBehavior;
    private String baseUrl = YandexAPI.WEB_URL;
    private String htmlFilePath;
    private String mImageUrl;


    private boolean mNormalMode = true;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            setContentView(R.layout.activity_browser);
        } catch (UnsatisfiedLinkError linkError) {
            Toast.makeText(getApplicationContext(), R.string.webview_version_error, Toast.LENGTH_LONG).show();
            finish();
            return;
        } catch (RuntimeException runtime) {
            Toast.makeText(getApplicationContext(), R.string.no_webview, Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        mContext = this;

        mToolbar = (WebViewToolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_action_arrow_back);
        mToolbar.setNavigationOnClickListener(v -> finish());

        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.light_colorAccent));
        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            mToolbar.setProgress(0);
            mToolbar.setCanDrawProgress(true);
            mWebView.reload();
        });

        mAppBarLayout = (AppBarLayout) findViewById(R.id.view);

        mWebView = (WebView) findViewById(R.id.webView);
        WebSettings mWebSettings = mWebView.getSettings();

        mWebSettings.setJavaScriptEnabled(true);
        mWebSettings.setSupportZoom(true);
        mWebSettings.setBuiltInZoomControls(true);
        mWebSettings.setDisplayZoomControls(false);
        mWebView.setWebChromeClient(new MyWebChromeClient());
        mWebView.setWebViewClient(new MyWebViewClient());
        mWebView.setOnTouchListener(new MyOnTouchListener());

        registerForContextMenu(mWebView);

        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_URL)) {
            handleSendUrl(intent);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void handleSendUrl(Intent intent) {
        mToolbar.setTitle(" ");
        mToolbar.setSubtitle(intent.getStringExtra(EXTRA_URL));
        mWebView.loadUrl(baseUrl + intent.getStringExtra(EXTRA_URL));
        mNormalMode = true;
    }


    private boolean toolBarVisibility = true;

    private void setToolBarVisibility(boolean visible) {
        if (visible == toolBarVisibility)
            return;

        if (visible) {
            toolBarVisibility = true;
            mAppBarLayout.setExpanded(true, true);
        } else {
            toolBarVisibility = false;
            mAppBarLayout.setExpanded(false, true);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_browser, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                mToolbar.setProgress(0);
                mToolbar.setCanDrawProgress(true);
                mWebView.reload();
                return true;
            case R.id.action_insert:
                openWebLink();
                return true;
            case R.id.action_search:
                mWebView.showFindDialog("", true);
                return true;
            case R.id.menu_item_share:
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, mWebView.getUrl());
                Intent chooserIntent = Intent.createChooser(shareIntent, mContext.getString(R.string.share_url));
                chooserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(Intent.createChooser(chooserIntent, getString(R.string.share_url)));
                return true;
            case R.id.menu_item_copy_link:
                Utils.copyToClipboard(mWebView.getUrl());
                Snackbar.make(mCoordinatorLayout, mWebView.getUrl(), Snackbar.LENGTH_LONG).show();
                return true;
            case R.id.menu_item_open_in:
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(mWebView.getUrl()));
                startActivity(Intent.createChooser(intent, getString(R.string.open_with)));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onKeyDown(final int keyCode, final KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
            if (mNormalMode) {
                mWebView.goBack();
                return true;
            }

            WebBackForwardList webBackForwardList = mWebView.copyBackForwardList();
            String lastUrl = webBackForwardList.getItemAtIndex(webBackForwardList.getCurrentIndex() - 1).getUrl();
            if (lastUrl.equals(baseUrl)) {
                loadSearchResult(htmlFilePath, baseUrl);
            } else {
                mWebView.goBack();
            }

            return true;
        }
        finish();
        return true;
    }

    private static final int REQUEST_CODE = 0;

    private void getPermission(String permission) {
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{permission}, REQUEST_CODE);
        }
    }


    MenuItem.OnMenuItemClickListener handler = new MenuItem.OnMenuItemClickListener() {
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {
                case 0: {
                    Utils.copyToClipboard(mImageUrl);
                    Snackbar.make(mCoordinatorLayout, R.string.copied_to_clipboard, Snackbar.LENGTH_SHORT).show();
                    break;
                }
                case 1: {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                            ContextCompat.checkSelfPermission(BrowserActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        getPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                        break;
                    }
                    break;
                }
                case 2: {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(mImageUrl));
                    startActivity(Intent.createChooser(intent, ""));

                    break;
                }
                case 3: {
                    mWebView.loadUrl("https://www.google.com/searchbyimage?image_url=" + mImageUrl);

                    break;
                }
            }
            return true;
        }
    };

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, view, menuInfo);

        WebView.HitTestResult result = mWebView.getHitTestResult();

        if (result.getType() == WebView.HitTestResult.SRC_ANCHOR_TYPE) {
            mImageUrl = result.getExtra();
            menu.setHeaderView(new ContextMenuTitleView(this, mImageUrl));
            //menu.setHeaderTitle(mImageUrl);
            menu.add(Menu.NONE, 2, 0, R.string.open_with).setOnMenuItemClickListener(handler);
            menu.add(Menu.NONE, 0, 1, R.string.copy_url).setOnMenuItemClickListener(handler);
        }

        if (result.getType() == WebView.HitTestResult.IMAGE_TYPE ||
                result.getType() == WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE) {

            mImageUrl = result.getExtra();
            menu.setHeaderView(new ContextMenuTitleView(this, mImageUrl));
            menu.add(Menu.NONE, 0, 0, R.string.copy_url).setOnMenuItemClickListener(handler);
            menu.add(Menu.NONE, 1, 1, R.string.save_image).setOnMenuItemClickListener(handler);

        }
    }

    private void loadSearchResult(String path, String baseUrl) {
        htmlFilePath = path;

        File file = new File(htmlFilePath);

        if (!file.exists()) {
            return;
        }

        StringBuilder sb = new StringBuilder();
        BufferedReader br = null;

        try {
            br = new BufferedReader(new InputStreamReader(
                    new FileInputStream(file), "UTF-8"));
            int c = br.read();
            while (c != -1) {
                sb.append((char) c);
                c = br.read();
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (br != null)
                try {
                    br.close();
                } catch (IOException ignored) {
                }
        }

        mWebView.loadDataWithBaseURL(baseUrl,
                sb.toString(),
                "text/html",
                "utf-8",
                baseUrl);
    }

    private class MyWebChromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            mToolbar.setProgress(newProgress);
            mToolbar.setCanDrawProgress(true);

            if (view.getTitle() != mToolbar.getSubtitle()) {
                mToolbar.setTitle(view.getTitle());
            }
        }
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (mInfoBar != null) {
                mInfoBar.hide();
            }
                view.loadUrl(url);
                mToolbar.setTitle(" ");
                mToolbar.setSubtitle(url);
                setToolBarVisibility(true);

                return true;

        }

        @Override
        public void onLoadResource(WebView view, String url) {
            super.onLoadResource(view, url);
        }

        @Override
        public WebResourceResponse shouldInterceptRequest(final WebView view, String url) {
            if (url.contains("saucenao-new.css")) {
                return getCssWebResourceResponseFromAsset();
            } else {
                return super.shouldInterceptRequest(view, url);
            }
        }

        private WebResourceResponse getCssWebResourceResponseFromAsset() {
            try {
                return getUtf8EncodedCssWebResourceResponse(getAssets().open("saucenao-new.css"));
            } catch (IOException e) {
                return null;
            }
        }

        private WebResourceResponse getUtf8EncodedCssWebResourceResponse(InputStream data) {
            return new WebResourceResponse("text/css", "UTF-8", data);
        }


        @Override
        public void onPageFinished(WebView webView, String url) {
            super.onPageFinished(webView, url);

            if (!toolBarVisibility) {
                new Handler().postDelayed(() -> setToolBarVisibility(false), 1500);
            }

            mToolbar.setTitle(webView.getTitle());
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    private class MyOnTouchListener implements View.OnTouchListener {
        int DIRECTION_CHANGE = Utils.dpToPx(10);

        float mLocation;
        float mStart;
        int mState = 0;
        int mOldOffset = 0;
        int mNewOffset = 0;

        float mOldDistance;
        boolean mIsDown = false;

        ValueAnimator mAnimator;

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            int action = event.getAction();
            float y = event.getRawY();

            if (mBehavior == null) {
                mBehavior = (AppBarLayout.Behavior) ((CoordinatorLayout.LayoutParams) mAppBarLayout.getLayoutParams()).getBehavior();
            }

            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    mOldDistance = 0;
                    mStart = y;
                    mOldOffset = mBehavior.getTopAndBottomOffset();

                    if (mAnimator != null && mAnimator.isRunning()) {
                        mAnimator.cancel();
                    }

                    break;
                case MotionEvent.ACTION_MOVE:
                    mLocation = y;

                    float distance = (mStart - mLocation);
                    if (distance - mOldDistance > DIRECTION_CHANGE && !mIsDown) {
                        mStart = y + DIRECTION_CHANGE;
                        mIsDown = true;
                    }

                    if (distance - mOldDistance < -DIRECTION_CHANGE && mIsDown) {
                        mStart = y - DIRECTION_CHANGE;
                        mIsDown = false;
                    }

                    mNewOffset = mOldOffset - (int) distance;
                    if (mNewOffset <= 0 && mNewOffset >= -mAppBarLayout.getHeight()) {
                        mBehavior.setTopAndBottomOffset(mNewOffset);
                    }

                    mSwipeRefreshLayout.setEnabled(mNewOffset >= 0);

                    if (mInfoBar != null) {
                        if (distance > 0 && mInfoBar.getView().getTranslationY() < mInfoBar.getView().getHeight()) {
                            mInfoBar.getView().setTranslationY(distance);
                            mState = 0;
                        }

                        if (distance < 0 && mInfoBar.getView().getTranslationY() > 0) {
                            mInfoBar.getView().setTranslationY(mInfoBar.getView().getHeight() + distance);
                            mState = 1;
                        }
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    if (mInfoBar != null) {
                        mInfoBar.animateView(mState);
                    }

                    if (mAnimator != null && mAnimator.isRunning()) {
                        mAnimator.cancel();
                    }

                    int old = mBehavior.getTopAndBottomOffset();
                    if (old != 0) {
                        int target = (mOldOffset - mNewOffset < 0) ? 0 : -mAppBarLayout.getHeight();

                        mAnimator = ValueAnimator.ofInt(old, target);
                        mAnimator.setInterpolator(new LinearOutSlowInInterpolator());
                        mAnimator.setDuration(Math.round((float) Math.abs(target - old) / mAppBarLayout.getHeight() * 300));
                        mAnimator.addUpdateListener(animation -> mBehavior.setTopAndBottomOffset((int) animation.getAnimatedValue()));
                        mAnimator.start();
                    }

                    break;
            }

            return false;
        }
    }

    private void openWebLink() {
        View v = getLayoutInflater().inflate(R.layout.dialog_input, null);
        ExtendedEditText editText = (ExtendedEditText) v.findViewById(R.id.input);
        TextView check = (TextView) v.findViewById(R.id.check);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (editText.getText().toString().matches(Patterns.WEB_URL.toString())){
                    check.setVisibility(View.VISIBLE);
                } else {
                    check.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        new AlertDialog.Builder(this)
                .setView(v)
                .setPositiveButton(R.string.ok, (d, i) -> mWebView.loadUrl(baseUrl + editText.getText().toString()))
                .setNegativeButton(android.R.string.cancel, (d, i) -> d.dismiss())
                .setNeutralButton(R.string.paste, (d, i) -> {
                    editText.setText(Utils.getTextFromClipboard());
                    mWebView.loadUrl(baseUrl + editText.getText().toString());
                })
                .setOnDismissListener(dialogInterface -> KeyboardUtil.hideKeyboard(BrowserActivity.this))
                .show();
        //Принудительно показываем клавиатуру
        KeyboardUtil.showKeyboard();
    }
}