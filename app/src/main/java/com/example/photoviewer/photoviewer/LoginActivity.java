package com.example.photoviewer.photoviewer;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.ContentResolver;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends Activity {
    public String request_token;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        AuthIG();
    }

    public void AuthIG() {
        String url = "https://api.instagram.com/oauth/authorize/?client_id=3a738930c86844a8b505115a3c2427c0&redirect_uri=http://codepath.com&response_type=token";
        WebView webView = (WebView) findViewById(R.id.webView);
        webView.setVerticalScrollBarEnabled(false);
        webView.setHorizontalScrollBarEnabled(false);
        webView.setWebViewClient(new AuthWebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(url);
    }

    public void goToSo (View view) {
        goToUrl ( "http://9gag.com/");
    }

    public void goToSu (View view) {

    }


    public class AuthWebViewClient extends WebViewClient
    {
        //@Override
        public boolean shouldOverrideUrlLoading(WebView view, String url)
        {
            if (url.startsWith("http://codepath.com"))
            {
                Toast.makeText(getApplicationContext(), url, Toast.LENGTH_LONG).show();
                String parts[] = url.split("=");
                request_token = parts[1];  //This is your request token.
                //InstagramLoginDialog.this.dismiss();
                launchTimeline();
                return true;
            }
            return false;
        }
    }

    public void launchTimeline() {
        Intent i = new Intent(this, TimelineActivity.class);
        Toast.makeText(getApplicationContext(), request_token, Toast.LENGTH_SHORT).show();
        i.putExtra("request_token", request_token);
        startActivity(i);
    }

    //access token: 402526745.3a73893.9c0054ab116f443d9ea0e3820e5f8e2f

    private void goToUrl (String url) {
        Uri uriUrl = Uri.parse(url);
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
        startActivity(launchBrowser);
    }

}
