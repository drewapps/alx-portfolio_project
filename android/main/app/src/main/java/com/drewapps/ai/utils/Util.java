package com.drewapps.ai.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.drewapps.ai.Config;
import com.drewapps.ai.R;

import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

public class Util {

    private static Typeface fromAsset;
    private static Fonts currentTypeface;

    public static void showLog(String message) {

        if (Config.IS_DEVELOPING) {
            Log.d("Team_iQueen", message);
        }
    }

    public static void showErrorLog(String s, JSONException e) {
        if (Config.IS_DEVELOPING) {
            try {
                StackTraceElement l = e.getStackTrace()[0];
                Log.d("Team_iQueen", s);
                Log.d("Team_iQueen", "Line : " + l.getLineNumber());
                Log.d("Team_iQueen", "Method : " + l.getMethodName());
                Log.d("Team_iQueen", "Class : " + l.getClassName());
            } catch (Exception ee) {
                Log.d("Team_iQueen", "Error in psErrorLogE");
            }
        }
    }

    public static void showErrorLog(String log, Object obj) {
        if (Config.IS_DEVELOPING) {
            try {
                Log.d("Team_iQueen", log);
                Log.d("Team_iQueen", "Line : " + getLineNumber());
                Log.d("Team_iQueen", "Class : " + getClassName(obj));
            } catch (Exception ee) {
                Log.d("Team_iQueen", "Error in psErrorLog");
            }
        }
    }

    public static int getLineNumber() {
        return Thread.currentThread().getStackTrace()[4].getLineNumber();
    }

    public static int getTotalMemory(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(memoryInfo);
        return (int) (memoryInfo.totalMem / 1000000);
    }

    public static String getClassName(Object obj) {
        return "" + ((Object) obj).getClass();
    }

    public static String[] strTOStrArray(String str, String str2) {
        if (str == null) {
            return null;
        }
        return str.split(str2);
    }

    public static void StatusBarColor(Window window, int color) {
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(color);
    }

    public static List<String> listAssetFiles(Context context, String path) {

        List<String> list = new ArrayList<String>();
        try {
            String[] plist = context.getAssets().list(path);
            if (plist.length > 0) {
                for (String file : plist) {
                    if (context.getAssets().open(path + "/" + file) != null) {
                        list.add(file);
                    }
                }
            }
        } catch (IOException e) {
            return list;
        }

        return list;
    }

    private static final NavigableMap<Long, String> suffixes = new TreeMap<>();

    static {
        suffixes.put(1_000L, "k");
        suffixes.put(1_000_000L, "M");
        suffixes.put(1_000_000_000L, "G");
        suffixes.put(1_000_000_000_000L, "T");
        suffixes.put(1_000_000_000_000_000L, "P");
        suffixes.put(1_000_000_000_000_000_000L, "E");
    }

    public static Shader generateGradient(String[] gradientColors, View view) {
        String[] gradient = gradientColors;
        Shader.TileMode tileMode = Shader.TileMode.MIRROR;
        int[] iArr = new int[gradient.length];
        for (int i = 0; i < gradient.length; i++) {
            iArr[i] = Color.parseColor(gradient[i]);
        }
        LinearGradient linearGradient = null;
        linearGradient = new LinearGradient(0.0f, 0.0f, (float) view.getWidth(), 0.0f, iArr, null, tileMode);
        return linearGradient;
    }


    public static String numberFormat(long value) {
        //Long.MIN_VALUE == -Long.MIN_VALUE so we need an adjustment here
        if (value == Long.MIN_VALUE) return numberFormat(Long.MIN_VALUE + 1);
        if (value < 0) return "-" + numberFormat(-value);
        if (value < 1000) return Long.toString(value); //deal with easy case

        Map.Entry<Long, String> e = suffixes.floorEntry(value);
        Long divideBy = e.getKey();
        String suffix = e.getValue();

        long truncated = value / (divideBy / 10); //the number part of the output times 10
        boolean hasDecimal = truncated < 100 && (truncated / 10d) != (truncated / 10);
        return hasDecimal ? (truncated / 10d) + suffix : (truncated / 10) + suffix;
    }

    public static Bitmap getBitmapFromAssets(Context context, String fileName) throws IOException {
        AssetManager assetManager = context.getAssets();

        InputStream istr = assetManager.open(fileName);
        Bitmap bitmap = BitmapFactory.decodeStream(istr);
        istr.close();

        return bitmap;
    }

    public static Typeface getTypeFace(Context context, Fonts fonts) {

        if (currentTypeface == fonts) {
            if (fromAsset == null) {
                if (fonts == Fonts.LATO_REGULAR) {
                    fromAsset = Typeface.createFromAsset(context.getAssets(), "font/Lato-Regular.ttf");
                } else if (fonts == Fonts.LATO_MEDIUM) {
                    fromAsset = Typeface.createFromAsset(context.getAssets(), "font/Lato-Bold.ttf");
                } else if (fonts == Fonts.LATO_BOLD) {
                    fromAsset = Typeface.createFromAsset(context.getAssets(), "font/Lato-Black.ttf");
                } else if (fonts == Fonts.LATO_LIGHT) {
                    fromAsset = Typeface.createFromAsset(context.getAssets(), "font/Lato-Light.ttf");
                }
            }
        } else {
            if (fonts == Fonts.LATO_REGULAR) {
                fromAsset = Typeface.createFromAsset(context.getAssets(), "font/Lato-Regular.ttf");
            } else if (fonts == Fonts.LATO_MEDIUM) {
                fromAsset = Typeface.createFromAsset(context.getAssets(), "font/Lato-Bold.ttf");
            } else if (fonts == Fonts.LATO_BOLD) {
                fromAsset = Typeface.createFromAsset(context.getAssets(), "font/Lato-Black.ttf");
            } else if (fonts == Fonts.LATO_LIGHT) {
                fromAsset = Typeface.createFromAsset(context.getAssets(), "font/Lato-Light.ttf");
            } else {
                fromAsset = Typeface.createFromAsset(context.getAssets(), "font/Lato-Regular.ttf");
            }
            //fromAsset = Typeface.createFromAsset(activity.getAssets(), "font/Roboto-Italic.ttf");
            currentTypeface = fonts;
        }
        return fromAsset;
    }

    public enum Fonts {
        LATO_REGULAR,
        LATO_LIGHT,
        LATO_MEDIUM,
        LATO_BOLD
    }

    public static int dpToPx(Context context, int i) {
        context.getResources();
        return (int) (Resources.getSystem().getDisplayMetrics().density * ((float) i));
    }

    public static void showToast(Context context, String message) {
        try {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            showErrorLog(e.getMessage(), e);
        }
    }

}
