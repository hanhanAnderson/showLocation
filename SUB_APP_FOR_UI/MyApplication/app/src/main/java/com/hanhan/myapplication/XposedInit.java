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
import android.text.format.Time;
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
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_InitPackageResources;
import de.robv.android.xposed.callbacks.XC_LayoutInflated;
import de.robv.android.xposed.callbacks.XC_LoadPackage;


public class XposedInit implements IXposedHookLoadPackage {
    String packageName = "com.hanhan.maptest";
    String className = "com.hanhan.maptest.MapsActivity";
    Time time = new Time();

    long prev = time.toMillis(false);
    long curr = 0;
    String demoPackage = "com.hanhan.showlocation";
//  private SharedPreferences sharedPreferences;


//  private static SharedPreferences getPrefs(Context context) {
//        return context.getSharedPreferences("prog", Context.MODE_PRIVATE);
//    }
//
//  public static String getProg(Context context) {
//        return getPrefs(context).getString("prog", "");
//    }


    Integer mode = 1;
    Integer prog = 50;
    Integer oldProg = 0;
    double lonRan = 0.0;
    double latRan = 0.0;

//    case "ZipCode": mode = 0;
//    case "Random" : mode = 1;
//    case "Shifted" : mode = 2;
//    default: mode = 0;


    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        Log.d("Xdbug:::::", "handleLoadPackage");

        if (lpparam.packageName.equals("com.hanhan.showlocation") ||
                lpparam.packageName.equals("com.yelp.android") ||
                lpparam.packageName.equals("com.dianping.v1") ||
                lpparam.packageName.equals("com.weather.Weather")
        ) {
            Log.d("Xdbug", "showlocation / yelp entered");

            XposedHelpers.findAndHookMethod("android.net.wifi.WifiManager", lpparam.classLoader, "getScanResults", new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    XposedBridge.log("wifi");
                    XposedBridge.log(param.getResult().toString());
                    param.setResult(null);
                }
            });
            XposedHelpers.findAndHookMethod("android.telephony.TelephonyManager", lpparam.classLoader, "getCellLocation", new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    XposedBridge.log("telephony1");
                    XposedBridge.log(param.getResult().toString());
                    param.setResult(null);
                }
            });
            XposedHelpers.findAndHookMethod("android.telephony.TelephonyManager", lpparam.classLoader, "getNeighboringCellInfo", new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    XposedBridge.log("telephony2");
                    XposedBridge.log(param.getResult().toString());
                    param.setResult(null);
                }
            });

            XposedHelpers.findAndHookMethod("android.location.Location", lpparam.classLoader, "getLatitude", new XC_MethodHook(){
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);

                    XSharedPreferences pre = new XSharedPreferences("com.hanhan.myapplication","para");
                    prog = pre.getInt("prog",0);
                    mode = pre.getInt("mode", 0);


                    Object temp = param.getResult();

//
//                    time.setToNow();
//
//                    curr = time.toMillis(false);
//                    Log.i("Xdbug time",Long.toString(curr));z
//                    Log.i("Xdbug time",Long.toString(prev));
//

//                    if(newLat != 0.0 && mode != 2 && curr - prev < 1000*5) {
//                        Log.i("Xdbug time", "less than 5 sec!!");
////                        param.setResult(newLat == 0.0 ? null : newLat);
//                        param.setResult(newLat);
//                        return;
//                    }
//
//                    prev = curr;





                    double newLat = 0.0;
//                    Log.d("Xdbug:AfHookLAT:PROG::", Integer.toString(prog));

                    if (oldProg != prog) {
                        Log.d("Xdbug::oldProg", "oldPrg != prog");
                        oldProg = prog;
                        latRan = (Math.random() - 0.5) * 0.0015 * prog;
                        lonRan = (Math.random() - 0.5) * 0.0015 * prog;
                    }

                    switch (mode) {
                        case 0:
                            newLat = (double) temp + latRan;
                            break;// ZipCode (0-3.75km)
                        case 1:
                            newLat = (double) temp + (Math.random() - 0.5) * 0.3 * prog;
                            break;//Random (0-750km)
                        case 2:
                            newLat = (double) temp + 0.1 * prog;
                            break;//Shifted (0-300Km)
                        default:
                            newLat = (double) temp + (Math.random() - 0.5) * 0.0015 * prog;
                    }

                    param.setResult(newLat);

                }
            });

            XposedHelpers.findAndHookMethod("android.location.Location", lpparam.classLoader, "getLongitude", new XC_MethodHook(){
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    Object temp = param.getResult();
                    double newLON = 0.0;

                    switch (mode) {
                        case 0:
                            newLON = (double) temp + latRan;
                            break;// ZipCode (0-3.75km)
                        case 1:
                            newLON = (double) temp + (Math.random() - 0.5) * 0.3 * prog;
                            break;//Random (0-750km)
                        case 2:
                            newLON = (double) temp + 0.1 * prog;
                            break;//Shifted (0-300Km)
                        default:
                            newLON = (double) temp + (Math.random() - 0.5) * 0.0015 * prog;
                    }
                    param.setResult(newLON);

                }
            });
        }
            /*
            XposedHelpers.findAndHookMethod("android.net.wifi.WifiManager", lpparam.classLoader, "getScanResults", new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    XposedBridge.log("wifi");
                    XposedBridge.log(param.getResult().toString());
                    param.setResult(null);
                }
            });
            XposedHelpers.findAndHookMethod("android.telephony.TelephonyManager", lpparam.classLoader, "getCellLocation", new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    XposedBridge.log("telephony1");
                    XposedBridge.log(param.getResult().toString());
                    param.setResult(null);
                }
            });
            XposedHelpers.findAndHookMethod("android.telephony.TelephonyManager", lpparam.classLoader, "getNeighboringCellInfo", new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    XposedBridge.log("telephony2");
                    XposedBridge.log(param.getResult().toString());
                    param.setResult(null);
                }
            });

            XposedHelpers.findAndHookMethod("android.location.LocationManager",lpparam.classLoader, "requestLocationUpdates", new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    super.beforeHookedMethod(param);
                    XposedBridge.log("LocationManager-before");
                    XposedBridge.log(param.getResult().toString());
                    if (param.args.length == 4 && (param.args[0] instanceof String)) {

                        LocationListener ll = (LocationListener)param.args[3];

                        Class<?> clazz = LocationListener.class;
                        Method m = null;
                        for (Method method : clazz.getDeclaredMethods()) {
                            if (method.getName().equals("onLocationChanged")) {
                                m = method;
                                break;
                            }
                        }

                        try {
                            if (m != null) {
                                Object[] args = new Object[1];
                                Location l = new Location(LocationManager.GPS_PROVIDER);

                                double la=121.53407;
                                double lo=25.077796;
                                l.setLatitude(la);
                                l.setLongitude(lo);
                                args[0] = l;
                                m.invoke(ll, args);
                                XposedBridge.log("fake location: " + la + ", " + lo);
                                XposedBridge.log("fake location:"+la+", "+lo);
                            }
                        } catch (Exception e) {
                            XposedBridge.log(e);
                        }
                    }
                }

                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                    XposedBridge.log("LocationManager-after");
                    XposedBridge.log(param.getResult().toString());
                    GpsStatus gss = (GpsStatus)param.getResult();
                    if (gss == null)
                        return;

                    Class<?> clazz = GpsStatus.class;
                    Method m = null;
                    for (Method method : clazz.getDeclaredMethods()) {
                        if (method.getName().equals("setStatus")) {
                            if (method.getParameterTypes().length > 1) {
                                m = method;
                                break;
                            }
                        }
                    }
                    m.setAccessible(true);
                    //make the apps belive GPS works fine now
                    int svCount = 5;
                    int[] prns = {1, 2, 3, 4, 5};
                    float[] snrs = {0, 0, 0, 0, 0};
                    float[] elevations = {0, 0, 0, 0, 0};
                    float[] azimuths = {0, 0, 0, 0, 0};
                    int ephemerisMask = 0x1f;
                    int almanacMask = 0x1f;
                    //5 satellites are fixed
                    int usedInFixMask = 0x1f;
                    try {
                        if (m != null) {
                            XposedBridge.log("invoke");
                            m.invoke(gss,svCount, prns, snrs, elevations, azimuths, ephemerisMask, almanacMask, usedInFixMask);
                            param.setResult(gss);
                        }
                    } catch (Exception e) {
                        Log.e("err:", e.toString());
                        XposedBridge.log(e);
                    }
                }
            });


        }
*/
        if (lpparam.packageName.equals(packageName)) {
            XposedHelpers.findAndHookMethod(className, lpparam.classLoader, "getLat", Location.class, new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    super.beforeHookedMethod(param);

                    Log.d("Xdbug::B4HookLat::", param.toString());

                }

                //            @Override
//            protected Object replaceHookedMethod(MethodHookParam param)throws Throwable {
//                return null;
//            }
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);

                    XSharedPreferences pre = new XSharedPreferences("com.hanhan.myapplication","para");
                    prog = pre.getInt("prog",0);
                    mode = pre.getInt("mode", 0);
                    Log.i("XSp::prog",Integer.toString(prog));


                    Object temp = param.getResult();
                    double newLat = 0.0;

//                Integer para[] = MainActivity.getPara();
//                prog = para[0];
//                Log.i("Xdbug:Para:",para[0].toString()+"  "+para[1].toString());
//                mode = para[1];

//                prog = (int) SharedPreferencesUtils.getParam(MainActivity.getActivity(),"int", 0);
//                sharedPreferences.getInt("prog", 0);


                    Log.d("Xdbug:AfHookLAT:PROG::", Integer.toString(prog));
                    switch (mode) {
                        case 0 : newLat = (double) temp + (Math.random() - 0.5) * 0.0015 * prog; break;// ZipCode (0-3.75km)
                        case 1 : newLat = (double) temp + (Math.random() - 0.5) * 0.3 * prog; break;//Random (0-750km)
                        case 2 :  newLat= (double) temp +  0.1 * prog; break;//Shifted (0-300Km)
                        default: newLat = (double) temp + (Math.random() - 0.5) * 0.0015 * prog;
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
                        case 0 : newLON = (double) temp + (Math.random() - 0.5) * 0.0015 * prog; break;// ZipCode (0-3.75km)
                        case 1 : newLON = (double) temp + (Math.random() - 0.5) * 0.3 * prog; break;//Random (0-750km)
                        case 2 :  newLON= (double) temp +  0.1 * prog; break;//Shifted (0-300Km)
                        default: newLON = (double) temp + (Math.random() - 0.5) * 0.0015 * prog;
                    }

//                Log.d("Xdbug:LON:temp::", temp.toString());
                    param.setResult(newLON);
                    // Add random number -5.0 to 5.0
//                double newLON = (double) temp + (Math.random() - 0.5) * 0.01;
                }
            });
        }
//----------------------------------------------------------------------------------------------------------------------------------------------------------------
//----------------------------------------------------------------------------------------------------------------------------------------------------------------
//----------------------------------------------------------------------------------------------------------------------------------------------------------------




    }



}
