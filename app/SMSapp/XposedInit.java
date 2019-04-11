package com.ooczc.spammess;

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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    String packageName = "com.ooczc.spammess";
    String className = "com.ooczc.spammess.MainActivity";
    @Override
    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        Log.d("Xdbug:::::","handleLoadPackage");
//        Class clazz =  loadPackageParam.class.Loader.loadClass(className);
        //Check class Name
        if (!lpparam.packageName.equals(packageName)) {
            return;
        }
        XposedHelpers.findAndHookMethod(className, lpparam.classLoader, "onCreate",Bundle.class ,new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
//                double fuzzLat = Math.random()*11.1;
                Log.d("Xdbug::B4HookSMS::",param.toString());

            }

            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                Class clazz = lpparam.classLoader.loadClass("com.ooczc.spammess.MainActivity");
                Field field = clazz.getDeclaredField("SMSlist");
                field.setAccessible(true);
                Log.d("Xdbug::SMSlist::", "ListHooked!");
                List<Map<String, Object>> slist = (List) field.get(param.thisObject);
                Log.d("Xdbug::smsinfo::", slist.toString());

                for(Map<String, Object> m : slist) {
                    Log.d("Xdbug::SMSlist Map::",m.toString());
                    if (m.get("Sender")!= null) m.put("Sender",m.get("Sender").toString().hashCode());
                    if (m.get("Message")!= null) m.put("Message", m.get("Message").toString().hashCode());
                }

//                List list = (List) field.get(param.thisObject);
                //List<Map<String,Object>>
//                List<Map<String,Object>> newList = new ArrayList<>();
//                HashMap<String, Object> map = new HashMap<>();
            }
        });

        Log.d("Xdbug",lpparam.packageName);
    }
}
