package com.mintegral.mediation.adapter.mtg;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;

import com.mintegral.mediation.common.CommonConst;
import com.mintegral.mediation.common.LifecycleListener;
import com.mintegral.mediation.common.adapter.BaseInterstitialAdapter;
import com.mintegral.mediation.common.listener.MediationAdapterInitListener;
import com.mintegral.mediation.common.listener.MediationAdapterInterstitialListener;
import com.mintegral.msdk.MIntegralConstans;
import com.mintegral.msdk.MIntegralSDK;
import com.mintegral.msdk.interstitialvideo.out.InterstitialVideoListener;
import com.mintegral.msdk.interstitialvideo.out.MTGInterstitialVideoHandler;
import com.mintegral.msdk.out.MIntegralSDKFactory;

import java.util.Map;

/**
 * MTGInterstitialAdapter
 *
 * Adapter for Mintegral Interstitial Video ad.
 *
 * @author hanliontien
 */
public class MTGInterstitialAdapter extends BaseInterstitialAdapter {
    private static final String TAG = MTGInterstitialAdapter.class.getSimpleName();

    private boolean isMute = false;
    private boolean isReady = false;
    private long loadSucessTime;
    private String appId;
    private String appKey;
    private String mInterstitialUnitId;
    private String mPlacementId;
    private MTGInterstitialVideoHandler mMTGInterstitalVideoHandler;
    private MediationAdapterInitListener mMediationAdapterInitListener;
    private MediationAdapterInterstitialListener mMediationAdapterInterstitialListener;

    @Override
    public void init(Activity activity, String mediationUnitId, Map<String, Object> localExtras, Map<String, String> serverExtras) {
        if (activity == null || activity.isFinishing()) {
            Log.e(TAG, "Activity is null or finishing.");
            if (mMediationAdapterInitListener != null) {
                mMediationAdapterInitListener.onInitFailed();
            }
            return;
        }

        if (localExtras == null || localExtras.isEmpty()) {
            Log.e(TAG, "Local parameters cannot be null.");
            if (mMediationAdapterInitListener != null) {
                mMediationAdapterInitListener.onInitFailed();
            }
            return;
        }

        isMute = (boolean) localExtras.get(CommonConst.KEY_MUTE);
        appId = (String) localExtras.get(CommonConst.KEY_APP_ID);
        appKey = (String) localExtras.get(CommonConst.KEY_APP_KEY);
        mInterstitialUnitId = (String)localExtras.get(CommonConst.KEY_INTERSTITIAL_UNIT_ID);
        mPlacementId = (String)localExtras.get(CommonConst.KEY_PLACEMENT_ID);

        if (TextUtils.isEmpty(appId) || TextUtils.isEmpty(appKey) || TextUtils.isEmpty(mInterstitialUnitId)) {
            Log.e(TAG, "appId/appKey/interstitialUnitId cannot be null.");
            if (mMediationAdapterInitListener != null) {
                mMediationAdapterInitListener.onInitFailed();
            }
            return;
        }

        MIntegralSDK sdk = MIntegralSDKFactory.getMIntegralSDK();
        Map<String, String> map = sdk.getMTGConfigurationMap(appId, appKey);
        sdk.init(map, activity.getApplication());

        mMTGInterstitalVideoHandler = new MTGInterstitialVideoHandler(activity,mPlacementId, mInterstitialUnitId);
        if (mMediationAdapterInitListener != null) {
            mMediationAdapterInitListener.onInitSucceed();
        }
    }

    @Override
    public void load(String param) {
        mMTGInterstitalVideoHandler.setInterstitialVideoListener(mInterstitialVideoListener);
        mMTGInterstitalVideoHandler.load();
        mMTGInterstitalVideoHandler.playVideoMute(isMute ? MIntegralConstans.REWARD_VIDEO_PLAY_MUTE : MIntegralConstans.REWARD_VIDEO_PLAY_NOT_MUTE);
    }

    @Override
    public void show(String param) {
        if (isReady) {
            //判断isReady状态，如果为true且ready的时间超过1小时，则置为false
            long time = System.currentTimeMillis() - loadSucessTime;
            isReady = time < 3600000 && isReady;
        }

        if (mMTGInterstitalVideoHandler != null && isReady) {
            mMTGInterstitalVideoHandler.show();
        } else {
            Log.e(TAG, "MTG InterstitalVideo not ready.");
            if (mMediationAdapterInterstitialListener != null) {
                mMediationAdapterInterstitialListener.loadFailed("MTG InterstitalVideo not ready.");
            }
        }
    }

    @Override
    public boolean isReady(String param) {
        if (isReady) {
            long time = System.currentTimeMillis() - loadSucessTime;
            isReady = time < 3600000 && isReady;
        }
        return isReady;
    }

    @Override
    public void setSDKInitListener(MediationAdapterInitListener mediationAdapterInitListener) {
        if (mediationAdapterInitListener == null) {
            Log.e(TAG, "MediationAdapterInitListener cannot be null.");
            return;
        }

        mMediationAdapterInitListener = mediationAdapterInitListener;
    }

    @Override
    public LifecycleListener getLifecycleListener() {
        return null;
    }

    @Override
    public void setSDKInterstitial(MediationAdapterInterstitialListener mediationAdapterInterstitialListener) {
//        if (mediationAdapterInterstitialListener == null) {
//            Log.e(TAG, "MediationAdapterInterstitialListener cannot be null.");
//            return;
//        }
        mMediationAdapterInterstitialListener = mediationAdapterInterstitialListener;
    }

    private InterstitialVideoListener mInterstitialVideoListener = new InterstitialVideoListener() {

        @Override
        public void onLoadSuccess(String s, String s1) {

        }

        @Override
        public void onVideoLoadSuccess(String s, String s1) {
            loadSucessTime = System.currentTimeMillis();
            isReady = true;
            if (mMediationAdapterInterstitialListener != null) {
                mMediationAdapterInterstitialListener.loadSucceed();
            }
        }

        @Override
        public void onVideoLoadFail(String s) {
            isReady = false;
            if (mMediationAdapterInterstitialListener != null) {
                mMediationAdapterInterstitialListener.loadFailed(s);
            }
        }

        @Override
        public void onAdShow() {
            isReady = false;
            if (mMediationAdapterInterstitialListener != null) {
                mMediationAdapterInterstitialListener.showSucceed();
            }
        }

        @Override
        public void onAdClose(boolean b) {
            if (mMediationAdapterInterstitialListener != null) {
                mMediationAdapterInterstitialListener.closed();
            }
        }

        @Override
        public void onShowFail(String s) {
            if (mMediationAdapterInterstitialListener != null) {
                mMediationAdapterInterstitialListener.showFailed(s);
            }
        }

        @Override
        public void onVideoAdClicked(String s, String s1) {
            if (mMediationAdapterInterstitialListener != null) {
                mMediationAdapterInterstitialListener.clicked(s);
            }
        }

        @Override
        public void onVideoComplete(String s, String s1) {

        }

        @Override
        public void onAdCloseWithIVReward(boolean b, int i) {

        }

        @Override
        public void onEndcardShow(String s, String s1) {

        }

    };
}
