package com.yjkmust.lemon.base;

import android.app.Application;

import com.kk.taurus.playerbase.config.PlayerConfig;
import com.kk.taurus.playerbase.config.PlayerLibrary;
import com.kk.taurus.playerbase.entity.DecoderPlan;
import com.yjkmust.fourleafclover.videoplayer.play.ExoMediaPlayer;

/**
 * Created by 11432 on 2018/8/2.
 */

public class App extends Application {
    private static App instance;
    public static final int PLAN_ID_IJK = 1;
    public static final int PLAN_ID_EXO = 2;

    public static App get() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
//            PlayerConfig.addDecoderPlan(new DecoderPlan(PLAN_ID_IJK, IjkPlayer.class.getName(), "IjkPlayer"));
//            PlayerConfig.setDefaultPlanId(PLAN_ID_IJK);
        PlayerConfig.addDecoderPlan(new DecoderPlan(PLAN_ID_EXO, ExoMediaPlayer.class.getName(), "ExoPlayer"));
        PlayerConfig.setDefaultPlanId(PLAN_ID_EXO);
        PlayerConfig.setUseDefaultNetworkEventProducer(true);
        PlayerLibrary.init(this);
    }
}
