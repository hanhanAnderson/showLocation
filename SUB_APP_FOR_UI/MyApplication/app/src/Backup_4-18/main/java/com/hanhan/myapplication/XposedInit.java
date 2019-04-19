package com.hanhan.myapplication;


import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.content.res.XResources;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import de.robv.android.xposed.IXposedHookInitPackageResources;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_InitPackageResources;
import de.robv.android.xposed.callbacks.XC_LayoutInflated;
import de.robv.android.xposed.callbacks.XC_LoadPackage;


public class XposedInit implements IXposedHookLoadPackage {
    String packageName = "com.hanhan.maptest";
    String className = "com.hanhan.maptest.MapsActivity";
    private SharedPreferences sharedPreferences;


//    private static SharedPreferences getPrefs(Context context) {
//        return context.getSharedPreferences("prog", Context.MODE_PRIVATE);
//    }
//
//    public static String getProg(Context context) {
//        return getPrefs(context).getString("prog", "");
//    }


    Integer mode = 1;
    Integer prog = 50;
//    Integer para[] = new Integer[]{0, 50};
//    case "ZipCode": mode = 0;
//    case "Random" : mode = 1;
//    case "Shifted" : mode = 2;
//    default: mode = 0;


    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        Log.d("Xdbug:::::", "handleLoadPackage");


        if (!lpparam.packageName.equals(packageName)) {
            return;
        }
        XposedHelpers.findAndHookMethod(className, lpparam.classLoader, "getLat", Location.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
//                double fuzzLat = Math.random()*11.1;
                Log.d("Xdbug::B4HookLat::", param.toString());

            }

            //            @Override
//            protected Object replaceHookedMethod(MethodHookParam param)throws Throwable {
//                return null;
//            }
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                Object temp = param.getResult();
                double newLat = 0.0;
                Integer para[] = MainActivity.getPara();
                prog = para[0];
                Log.i("Xdbug:Para:",para[0].toString()+"  "+para[1].toString());
                mode = para[1];

//                prog = (int) SharedPreferencesUtils.getParam(MainActivity.getActivity(),"int", 0);
//                sharedPreferences.getInt("prog", 0);
                Log.d("Xdbug:AfHookLAT:PROG::", Integer.toString(prog));
                switch (mode) {
                    case 0 : newLat = (double) temp + (Math.random() - 0.5) * 0.003 * prog; break;// ZipCode (0-4.5km)
                    case 1 : newLat = (double) temp + (Math.random() - 0.5) * 0.3 * prog; break;//Random (0-450km)
                    case 2 :  newLat= (double) temp +  0.1 * prog; break;//Shifted (0-300Km)
                    default: newLat = (double) temp + (Math.random() - 0.5) * 0.003 * prog;
                }


                Log.d("Xdbug:AfHookLAT:temp::", temp.toString());
                Log.d("Xdbug:AfHookLAT:mode::", Integer.toString(mode));
                Log.d("Xdbug:AfHookLAT:prog::", Integer.toString(prog));
                Log.d("Xdbug:NEWLAT::", Double.toString(newLat));
                param.setResult(newLat);



            }
        });
        XposedHelpers.findAndHookMethod(className, lpparam.classLoader, "getLon", Location.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                Log.d("Xdbug::param::", param.toString());
            }

            @Override
            protected void afterHookedMethod
                    (MethodHookParam param) throws Throwable {
                Object temp = param.getResult();
                double newLON = 0.0;


                switch (mode) {
                    case 0 : newLON = (double) temp + (Math.random() - 0.5) * 0.003 * prog;  break;// ZipCode (0-4.5km)
                    case 1 : newLON = (double) temp + (Math.random() - 0.5) * 0.3 * prog; break;//Random (0-450km)
                    case 2 :  newLON= (double) temp +  0.1 * prog; break;//Shifted (0-300Km)
                    default: newLON = (double) temp + (Math.random() - 0.5) * 0.003 * prog;
                }

//                Log.d("Xdbug:LON:temp::", temp.toString());
                param.setResult(newLON);
                // Add random number -5.0 to 5.0
//                double newLON = (double) temp + (Math.random() - 0.5) * 0.01;
            }
        });
    }
}
