package com.subang.checker.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.StringRes;
import android.widget.Toast;

import com.subang.api.SubangAPI;
import com.subang.bean.AppInfo;
import com.subang.checker.activity.R;
import com.subang.util.WebConst;


/**
 * Created by Qiang on 2015/10/31.
 */
public class AppUtil {

    //配置app，使用AppConf前调用此函数。如果没有配置，则配置
    public static boolean conf(Context context) {
        if (AppConf.isConfed()) {
            return true;
        }
        String basePath = context.getFilesDir().getAbsolutePath() + "/";
        AppConf.basePath = basePath;
        AppConf.cellnum = "";
        AppConf.password = "";
        return true;
    }

    //配置api，使用api前调用此函数。如果没有配置，则配置
    public static void confApi(Context context) {
        if (SubangAPI.isConfed()) {
            return;
        }
        conf(context);
        SubangAPI.conf(WebConst.OTHER, AppConf.cellnum, AppConf.password, AppConf.basePath);
    }

    public static boolean checkNetwork(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null) {
            return false;
        }
        return networkInfo.isAvailable();
    }

    public static void networkTip(Context context) {
        Toast toast = Toast.makeText(context, R.string.err_network, Toast.LENGTH_SHORT);
        toast.show();
    }

    public static void tip(Context context, String info) {
        Toast toast = Toast.makeText(context, info, Toast.LENGTH_SHORT);
        toast.show();
    }

    public static void tip(Context context, @StringRes int resId) {
        Toast toast = Toast.makeText(context, resId, Toast.LENGTH_SHORT);
        toast.show();
    }

    public static AppInfo getAppInfo(Context context) {
        PackageInfo packageInfo = null;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        Integer version = packageInfo.versionCode;
        AppInfo appInfo = new AppInfo(AppInfo.USER_CHECKER, AppInfo.OS_ANDROID, version);
        return appInfo;
    }
}
