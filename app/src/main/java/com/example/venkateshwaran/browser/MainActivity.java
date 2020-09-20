package com.example.venkateshwaran.browser;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;


import android.app.Activity;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.view.WindowManager;
import android.webkit.DownloadListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;

public class MainActivity extends ActionBarActivity {
    EditText ed1;
    EditText mTitleTextView;
    private WebView wv1;
    public boolean birthSort = true;
    String ur = "https://www.google.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        android.support.v7.app.ActionBar mActionBar = getSupportActionBar();
        // mActionBar.setBackgroundDrawable(new ColorDrawable(0xFFFFFFFF));
        mActionBar.setDisplayShowHomeEnabled(true);
        mActionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        View mCustomView = mInflater.inflate(R.layout.customactionbar, null);
        mTitleTextView = (EditText) mCustomView.findViewById(R.id.editText);
        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);
        ed1 = (EditText) findViewById(R.id.editText);

        ed1.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_ENTER:
                            ur = mTitleTextView.getText().toString();
                            setup();
                            wv1.loadUrl(ur);
                            return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });

        wv1 = (WebView) findViewById(R.id.webView);
        wv1.setWebViewClient(new MyBrowser());
        setup();

        if (savedInstanceState == null) {
            wv1.loadUrl(ur);
        }

        wv1.setDownloadListener(new DownloadListener() {
            public void onDownloadStart(String url, String userAgent,
                                        String contentDisposition, String mimetype,
                                        long contentLength) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        wv1.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
        wv1.onPause();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    if (wv1.canGoBack()) {
                        wv1.goBack();
                    } else {
                        finish();
                    }
                    return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    public void setup() {
        wv1.getSettings().setLoadsImagesAutomatically(true);
        wv1.getSettings().setJavaScriptEnabled(true);
        wv1.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        wv1.getSettings().setJavaScriptCanOpenWindowsAutomatically(false);
        wv1.getSettings().setSupportMultipleWindows(false);
        if (birthSort) {
            wv1.getSettings().setSupportZoom(true);
            wv1.getSettings().setBuiltInZoomControls(true);
        } else {
            wv1.getSettings().setSupportZoom(false);
            wv1.getSettings().setBuiltInZoomControls(false);
        }
        wv1.getSettings().setDisplayZoomControls(false);
        wv1.getSettings().setDomStorageEnabled(true);
        wv1.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        wv1.setVerticalScrollBarEnabled(false);
        wv1.setHorizontalScrollBarEnabled(false);
    }

    @Override
    public void onPause() {
        super.onPause();
        wv1.onPause();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            //Intent intent=new Intent(this,Settings.class);
            //startActivity(intent);
            return true;
        }

        if (id == R.id.action_home) {
            ur = "https://www.google.com";
            mTitleTextView.setText(ur);
            setup();
            wv1.loadUrl(ur);
        }

        if (id == R.id.action_zoom) {
            if (birthSort) {
                item.setTitle("Force Zoom Not Enabled");
                birthSort = false;
            } else {
                item.setTitle("Force Zoom Enabled");
                birthSort = true;
            }
            setup();
            ur = mTitleTextView.getText().toString();
            wv1.loadUrl(ur);
            return true;
        }

        if (id == R.id.action_refresh) {
            ur = mTitleTextView.getText().toString();
            setup();
            wv1.loadUrl(ur);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.startsWith("https://www.youtube")) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                return true;
            } else if (url.startsWith("https://maps.google")) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                return true;
            } else {
                view.loadUrl(url);
                mTitleTextView.setText(url);
                ur = url;
                return true;
            }
        }
    }
}
