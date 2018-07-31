package com.yjkmust.lemon;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import com.yjkmust.fourleafclover.model.AdvertisingModel;
import com.yjkmust.fourleafclover.view.SplashDialog;

public class MainActivity extends AppCompatActivity {

    private Button btnAdver;
    private SplashDialog splashDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnAdver = (Button) findViewById(R.id.btn_adver);



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
    }
}
