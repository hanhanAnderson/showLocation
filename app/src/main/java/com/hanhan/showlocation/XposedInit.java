package com.hanhan.showlocation;

import android.app.Activity;
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

public class XposedInit implements IXposedHookLoadPackage{
    String packageName = "com.hanhan.showlocation";
    String className = "com.hanhan.showlocation.MainActivity";

    String SMSPACKAGENAME = "com.android.mms";
    String SMSclassName = "com.android.internal.telephony.gsm.SmsMessage$PduParser";
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        Log.d("Xdbug:::::","handleLoadPackage");

//        Class clazz =  loadPackageParam.class.Loader.loadClass(className);
         //Check class Name
//        if (lpparam.appInfo != null && lpparam.isFirstApplication
//                && (SMSPACKAGENAME.equals(lpparam.packageName)))

        if (!lpparam.packageName.equals(packageName)) {
            return;
        }
        XposedHelpers.findAndHookMethod(className, lpparam.classLoader, "getLat",Location.class ,new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
//                double fuzzLat = Math.random()*11.1;
                Log.d("Xdbug::B4HookLat::",param.toString());

            }
            //            @Override
//            protected Object replaceHookedMethod(MethodHookParam param)throws Throwable {
//                return null;
//            }
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                Object temp = param.getResult();
                Log.d("Xdbug:AfHookLAT:temp::",temp.toString());
                param.setResult((double) temp + (Math.random()-0.5)*10);

            }
        });
        XposedHelpers.findAndHookMethod(className, lpparam.classLoader, "getLon",Location.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                Log.d("Xdbug::param::",param.toString());
            }
            //            @Override
//            protected Object replaceHookedMethod(MethodHookParam param)throws Throwable {
//                return null;
//            }
            @Override
            protected void afterHookedMethod
            (MethodHookParam param) throws Throwable {
                Object temp = param.getResult();
                Log.d("Xdbug:LON:temp::",temp.toString());
                
                // Add random number -5.0 to 5.0
                param.setResult((double) temp + (Math.random()-0.5)*10);


                //Shifting
                //param.setResult((double) temp + 0.5);

                //ZipCode level accurate
                //param.setResult((double) temp + (Math.random()*0.01);

            }
        });
//        if (lpparam.packageName.equals("com.hanhan.showlocation")) {
//            XposedHelpers.findAndHookMethod("com.hanhan.showlocation.MainActivity", lpparam.classLoader, "onCreate", Bundle.class, new XC_MethodHook() {
//                @Override
//                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                    //不能通过Class.forName()来获取Class ，在跨应用时会失效
//                    Class c = lpparam.classLoader.loadClass("com.hanhan.showlocation.MainActivity");
//                    Field field = c.getDeclaredField("textView");
//                    field.setAccessible(true);
//                    //param.thisObject 为执行该方法的对象，在这里指MainActivity
//                    TextView postionView = (TextView) field.get(param.thisObject);
//                    postionView.setText("Hello Xposed");
//                }
//            });
//        }
        Log.d("Xdbug",lpparam.packageName);
    }
}
