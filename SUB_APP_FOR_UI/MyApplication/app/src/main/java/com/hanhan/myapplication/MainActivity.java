package com.hanhan.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    WebView mWebview;
    RadioGroup geoGroup;
    String GeoChoice;
    private SeekBar seekBar;
    static int para[] = new int[2];
    // mode to param[0]
    static int mode = 1;
    // prog to param[1]
    static int prog = 50;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        seekBar = (SeekBar) findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.i("SeekBar","Curr is:"+ progress);
                prog = progress;
                para[1] = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        geoGroup = (RadioGroup) findViewById(R.id.Geo_radioGroup);
        geoGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radbtn = (RadioButton) findViewById(checkedId);
                GeoChoice = (String)radbtn.getText();
//                Toast.makeText(getApplicationContext(), "Current choice:::" + GeoChoice, Toast.LENGTH_LONG).show();
                Log.i("SeekBar","Curr is:"+ GeoChoice);

                if(GeoChoice.equals("ZipCode")) {mode = 1; para[0] = 1;}
                if(GeoChoice.equals("Random")) {mode = 1; para[0] = 1; }
                if(GeoChoice.equals("Shifted")) {mode = 2; para[0] = 2;}
//                switch(GeoChoice) {
//                    case "ZipCode": mode = 0;
//                    case "Random" : mode = 1;
//                    case "Shifted" : mode = 2;
//                    default: mode = 0;
//                }
                Log.i("SeekBar","Curr mode is:"+ mode);
                Log.i("SeekBar","Curr GetMode is:"+ getMode());

            }
        });


        mWebview = (WebView) findViewById(R.id.webView);
        mWebview.loadUrl("https://www.google.com/");

        mWebview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });


    }

    public static int getProg() {
        return prog;
    }
    public static int getMode() {
        return mode;
    }


}
