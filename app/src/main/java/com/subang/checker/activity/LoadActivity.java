package com.subang.checker.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.subang.api.MiscAPI;
import com.subang.bean.AppInfo;
import com.subang.bean.Result;
import com.subang.checker.util.AppConst;
import com.subang.checker.util.AppUtil;
import com.umeng.update.UmengDialogButtonListener;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UpdateStatus;


public class LoadActivity extends Activity {

    private static final int WHAT_CHECK = 1;
    private static final int WHAT_MAIN = 2;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case AppConst.WHAT_NETWORK_ERR: {
                    AppUtil.networkTip(LoadActivity.this);
                    break;
                }
                case WHAT_CHECK: {
                    UmengUpdateAgent.setDialogListener(umengDialogButtonListener);
                    UmengUpdateAgent.forceUpdate(LoadActivity.this);
                    break;
                }
                case WHAT_MAIN: {
                    Intent intent = new Intent(LoadActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                }
            }
        }
    };

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (!AppUtil.checkNetwork(LoadActivity.this)) {
                handler.sendEmptyMessage(AppConst.WHAT_NETWORK_ERR);    //提示用户，停留此界面
                return;
            }

            //检查app版本
            AppInfo appInfo = AppUtil.getAppInfo(LoadActivity.this);
            Result result = MiscAPI.checkApp(appInfo);
            if (result == null) {
                handler.sendEmptyMessage(AppConst.WHAT_NETWORK_ERR);    //提示用户，停留此界面
                return;
            }
            if (!result.getCode().equals(Result.OK)) {
                handler.sendEmptyMessage(WHAT_CHECK);    //提示更新应用
                return;
            }

            AppUtil.conf(LoadActivity.this);
            AppUtil.confApi(LoadActivity.this);
            handler.sendEmptyMessage(WHAT_MAIN);        //转主界面
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);
        new Thread(runnable).start();
    }

    private UmengDialogButtonListener umengDialogButtonListener = new UmengDialogButtonListener() {

        @Override
        public void onClick(int status) {
            switch (status) {
                case UpdateStatus.Update:
                    break;
                case UpdateStatus.Ignore:
                case UpdateStatus.NotNow:
                    AppUtil.tip(LoadActivity.this, "由于后台的升级，您当前的应用版本已不被支持。");
                    break;
            }
        }
    };
}
