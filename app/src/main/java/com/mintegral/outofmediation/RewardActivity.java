package com.mintegral.outofmediation;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.mintegral.mediation.common.CommonConst;
import com.mintegral.mediation.common.LifecycleListener;
import com.mintegral.mediation.common.bean.AdSource;
import com.mintegral.mediation.common.listener.MediationAdapterInitListener;
import com.mintegral.mediation.common.listener.MediationAdapterRewardListener;
import com.mintegral.mediation.out.handler.MediationRewardVideoHandler;


import java.util.HashMap;
import java.util.Map;

public class RewardActivity extends Activity implements View.OnClickListener {

    private Button showBtn, loadBtn, isReadyBtn, initBtn;

    private MediationRewardVideoHandler mediationRewardVideoHandler;
    private LifecycleListener lifecycleListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reward);
        initView();
        initListener();

    }

    private void initManager() {

        mediationRewardVideoHandler = new MediationRewardVideoHandler();

        mediationRewardVideoHandler.setMediationAdapterInitListener(new MediationAdapterInitListener() {
            @Override
            public void onInitSucceed() {
                Toast.makeText(RewardActivity.this, "onInitSucceed", Toast.LENGTH_LONG).show();
                Log.e("reward", "onInitSucceed");
            }

            @Override
            public void onInitFailed() {
                Toast.makeText(RewardActivity.this, "onInitFailed", Toast.LENGTH_LONG).show();
                Log.e("reward", "onInitFailed");
            }
        });
        Map<String, Object> paramsMap = new HashMap<>();
        //IronSource
        final AdSource adSource = new AdSource();
        Map<String,Object> ironsourceMap = new HashMap<>();
        ironsourceMap.put("local","88f1a7f5");
        adSource.setLocalParams(ironsourceMap);
        adSource.setTargetClass("com.mintegral.mediation.adapter.iron.IronRewardAdapter");
        adSource.setTimeOut(100000);
        paramsMap.put("2",adSource);
//        map.put("local","88f1a7f5");
//        map.put("targetClass","com.mintegral.mediation.adapter.iron.IronRewardAdapter");

        //MTG
        Map<String,Object> map = new HashMap<>();
        AdSource mtgAdSource = new AdSource();
        map.put(CommonConst.KEY_APP_ID, "118690");
        map.put(CommonConst.KEY_APP_KEY, "7c22942b749fe6a6e361b675e96b3ee9");
        map.put(CommonConst.KEY_USER_ID, "123");
        map.put(CommonConst.KEY_REWARD_ID, "12817");
        map.put(CommonConst.KEY_REWARD_UNIT_ID, "237149");
        map.put(CommonConst.KEY_MUTE, false);
        mtgAdSource.setLocalParams(map);
        mtgAdSource.setTargetClass("com.mintegral.mediation.adapter.mtg.MTGRewardAdapter");
        mtgAdSource.setTimeOut(20000);
        paramsMap.put("1",mtgAdSource);



        mediationRewardVideoHandler.setMediationAdapterRewardListener(new MediationAdapterRewardListener() {
            @Override
            public void loadSucceed() {
                Toast.makeText(RewardActivity.this, "loadSucceed", Toast.LENGTH_LONG).show();
            }

            @Override
            public void loadFailed(String msg) {
                Toast.makeText(RewardActivity.this, "loadFailed:" + msg, Toast.LENGTH_LONG).show();
            }

            @Override
            public void showSucceed() {
                Toast.makeText(RewardActivity.this, "showSucceed:", Toast.LENGTH_LONG).show();
            }

            @Override
            public void showFailed(String msg) {
                Toast.makeText(RewardActivity.this, "showFailed:" + msg, Toast.LENGTH_LONG).show();
            }

            @Override
            public void clicked(String msg) {
                Toast.makeText(RewardActivity.this, "clicked:" + msg, Toast.LENGTH_LONG).show();
            }

            @Override
            public void closed() {
                Toast.makeText(RewardActivity.this, "closed:", Toast.LENGTH_LONG).show();
            }


            @Override
            public void rewarded(String name, int amount) {
                Toast.makeText(RewardActivity.this, "rewarded:" + name + "-amount:" + amount, Toast.LENGTH_LONG).show();
            }
        });
        mediationRewardVideoHandler.init(this, paramsMap);
        lifecycleListener = mediationRewardVideoHandler.getLifecycleListener();
    }

    private void initView() {
        showBtn = findViewById(R.id.reward_show);
        loadBtn = findViewById(R.id.reward_load);
        isReadyBtn = findViewById(R.id.reward_is_ready);
        initBtn = findViewById(R.id.reward_init);
    }

    private void initListener() {
        setViewListener(showBtn);
        setViewListener(loadBtn);
        setViewListener(isReadyBtn);
        setViewListener(initBtn);
    }

    private void setViewListener(View view) {
        if (view != null) {
            view.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.reward_init:
                initManager();
                break;
            case R.id.reward_load:
                if (mediationRewardVideoHandler != null) {
                    mediationRewardVideoHandler.load("");
                }
                break;
            case R.id.reward_show:
                if (mediationRewardVideoHandler != null) {
                    mediationRewardVideoHandler.show("");
                }
                break;
            case R.id.reward_is_ready:
                if (mediationRewardVideoHandler != null) {
                    Toast.makeText(RewardActivity.this, "ready:" + mediationRewardVideoHandler.isReady(""), Toast.LENGTH_LONG).show();
                }
                break;
            default:
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (lifecycleListener != null) {
            lifecycleListener.onPause(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (lifecycleListener != null) {
            lifecycleListener.onResume(this);
        }
    }
}
