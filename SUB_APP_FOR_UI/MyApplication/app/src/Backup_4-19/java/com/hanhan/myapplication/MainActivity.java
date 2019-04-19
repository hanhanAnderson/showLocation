package com.hanhan.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
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

    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    Context mContext;
    static String className = "com.hanhan.myapplication";

    static Integer para[] = new Integer[]{0, 50};
    // mode to param[0]
    public static Integer mode = 1;
    // prog to param[1]
    public static Integer prog = 50;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_main);


        sp = getSharedPreferences("para", Activity.MODE_WORLD_READABLE);
        editor = sp.edit();
        editor.putInt("prog",1);
        editor.putInt("mode", 0);
//        final SharedPreferences.Editor editor = sharedPreferences.edit();


        mContext = this;
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
//                SharedPreferencesUtils.setParam(MainActivity.this, "int", prog);
//                editor.putInt("prog", prog);
//                Log.i("notXdbug:Para:",para[0].toString()+"  "+para[1].toString());
                editor.putInt("prog",prog);
                editor.commit();

                Log.i("sp Edit:",Integer.toString(sp.getInt("prog",-1)));
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

                if(GeoChoice.equals("ZipCode")) {mode = 1; para[0] = 0;}
                if(GeoChoice.equals("Random")) {mode = 1; para[0] = 1; }
                if(GeoChoice.equals("Shifted")) {mode = 2; para[0] = 2;}
//                switch(GeoChoice) {
//                    case "ZipCode": mode = 0;
//                    case "Random" : mode = 1;
//                    case "Shifted" : mode = 2;
//                    default: mode = 0;
//                }
                editor.putInt("mode", mode);
                editor.commit();
                Log.i("SeekBar","Curr mode is:"+ mode);
                Log.i("Sp edit mode:", Integer.toString(sp.getInt("mode",-1)));


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
    public Context getActivity() {
        return this;
    }
    public static Integer[] getPara() {
        return para;
    }
//    public static void setProg(Integer prog) {
//
////        return para[1];
//    }
//    public static int setMode() {
//        Log.i("not Xdbug gettingMode",Integer.toString(para[0]));
////        return para[0];
//
//    }


}
