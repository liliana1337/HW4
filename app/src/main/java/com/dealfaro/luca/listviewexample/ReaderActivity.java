package com.dealfaro.luca.listviewexample;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class ReaderActivity extends AppCompatActivity {

    static final public String MYPREFS = "myprefs";
    static final public String PREF_URL = "restore_url";
    static final public String WEBPAGE_NOTHING = "about:blank";

    WebView myWebView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reader);
        setTitle("MyWebView");
        myWebView = (WebView) findViewById(R.id.webView1);
        //myWebView.setWebViewClient(new WebViewClient());
        myWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                //Toast.makeText(getApplication(), url, Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        //webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        //myWebView.clearCache(true);


        //myWebView.clearHistory();

        // Binds the Javascript interface
        // myWebView.addJavascriptInterface(new JavaScriptInterface(this), "android");
        String url;
        Bundle extras = getIntent().getExtras();
        if(extras == null) {
            finish(); // No idea what else to do
        } else {
            url = extras.getString("URL");
            myWebView.loadUrl(url);
            Toast.makeText(getApplication(), url, Toast.LENGTH_SHORT).show();
            // myWebView.loadUrl("javascript:showToast(url);");
        }
    }

    //public class JavaScriptInterface {
        //Context mContext; // Having the context is useful for lots of things,
        // like accessing preferences.


        //JavaScriptInterface(Context c) {
            //mContext = c;
       // }
        /** Show a toast from the web page */
       // @JavascriptInterface
       // public void showToast(String toast) {
        //    Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
       // }

   // }
        @Override
        public void onBackPressed() {
            if(myWebView.canGoBack()) {
                myWebView.goBack();
            } else {
                super.onBackPressed();
            }
        }

    @Override
    public void onResume() {
        super.onResume();
        myWebView.reload();
    }








}
