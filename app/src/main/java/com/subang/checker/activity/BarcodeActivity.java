package com.subang.checker.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.EditText;

import com.google.zxing.Result;
import com.subang.checker.util.AppUtil;
import com.subang.util.Validator;
import com.welcu.android.zxingfragmentlib.BarCodeScannerFragment;

public class BarcodeActivity extends FragmentActivity {

    private EditText tv_barcode;
    private BarCodeScannerFragment fm_scan;
    private boolean isStop = false;

    private BarCodeScannerFragment.IResultCallback resultCallback = new BarCodeScannerFragment.IResultCallback() {
        @Override
        public void result(Result lastResult) {
            if (!isStop) {
                tv_barcode.setText(lastResult.getText());
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode);
        tv_barcode = (EditText) findViewById(R.id.et_barcode);
        fm_scan = (BarCodeScannerFragment) getSupportFragmentManager().findFragmentById(R.id.fm_sacn);
        fm_scan.setmCallBack(resultCallback);
    }

    public void btn_stop_onClick(View view) {
        isStop = true;
    }

    public void btn_ok_onClick(View view) {
        String barcode = tv_barcode.getText().toString();
        com.subang.bean.Result result= Validator.validBarcode(barcode);
        if (!result.isOk()) {
            AppUtil.tip(BarcodeActivity.this, "条形码错误。");
            return;
        }
        Intent intent = getIntent();
        intent.putExtra("barcode", barcode);
        setResult(RESULT_OK, intent);
        finish();
    }
}
