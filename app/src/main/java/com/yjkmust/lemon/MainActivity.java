package com.yjkmust.lemon;

import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.yjkmust.fourleafclover.model.AdvertisingModel;
import com.yjkmust.fourleafclover.model.CommonData;
import com.yjkmust.fourleafclover.util.LiveDataBus;
import com.yjkmust.fourleafclover.util.UiUtils;
import com.yjkmust.fourleafclover.view.SplashDialog;
import com.yjkmust.fourleafclover.widget.LemonToolbar;

public class MainActivity extends AppCompatActivity {

    private Button btnAdver;
    private Button btnMessage;
    private SplashDialog splashDialog;
    private LemonToolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initLiveDataBus();
        btnAdver = (Button) findViewById(R.id.btn_adver);
        btnMessage = (Button) findViewById(R.id.btn_message);
        toolbar = (LemonToolbar) findViewById(R.id.toolbar);

        btnAdver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AdvertisingModel model = new AdvertisingModel(1, "http://bmob-cdn-10899.b0.upaiyun.com/2017/05/09/34b6d85c406894f3803d949a78c4546e.jpg");
                splashDialog = new SplashDialog(MainActivity.this, model);
                splashDialog.show();
                splashDialog.setCountDownTime();
//                splashDialog.setCountDownTime(10);
            }
        });
        btnMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AdvertisingModel model = new AdvertisingModel(1, "http://bmob-cdn-10899.b0.upaiyun.com/2017/05/09/34b6d85c406894f3803d949a78c4546e.jpg");
                LiveDataBus.getDefault().post(CommonData.LIVEDATA_TEST).setValue(model);
            }
        });
        toolbar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UiUtils.makeText(MainActivity.this,"点击左边菜单");
            }
        });
        toolbar.setRightClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UiUtils.makeText(MainActivity.this,"点击右边菜单");
            }
        });
    }

    private void initLiveDataBus() {
        LiveDataBus.getDefault().getMessage(CommonData.LIVEDATA_TEST, AdvertisingModel.class)
                .observe(MainActivity.this, new Observer<AdvertisingModel>() {
                    @Override
                    public void onChanged(@Nullable AdvertisingModel model) {
                        Log.d("yaojie", "onChanged: " + model);
                    }
                });
    }

}
