package com.mintegral.mediation.adapter.iron;

import android.app.Activity;
import android.text.TextUtils;

import com.ironsource.mediationsdk.IronSource;
import com.ironsource.mediationsdk.logger.IronSourceError;
import com.ironsource.mediationsdk.sdk.InterstitialListener;
import com.mintegral.mediation.common.BaseLifecycleListener;
import com.mintegral.mediation.common.LifecycleListener;
import com.mintegral.mediation.common.MediationMTGErrorCode;
import com.mintegral.mediation.common.adapter.BaseInterstitialAdapter;
import com.mintegral.mediation.common.listener.MediationAdapterInitListener;
import com.mintegral.mediation.common.listener.MediationAdapterInterstitialListener;

import java.util.Map;

public class IronInterstitialAdapter extends BaseInterstitialAdapter implements InterstitialListener {

    private MediationAdapterInterstitialListener mMediationAdapterInterstitialListener;
    private MediationAdapterInitListener mMediationAdapterInitListener;
    private String appKey = "";
    // This is the instance id used inside ironSource SDK
    private String mInstanceId = "0";
    // This is the placement name used inside ironSource SDK
    private String mPlacementName = null;

    @Override
    public void setSDKInitListener(MediationAdapterInitListener mediationAdapterInitListener) {
        mMediationAdapterInitListener = mediationAdapterInitListener;
    }

    @Override
    public void setSDKInterstitial(MediationAdapterInterstitialListener mediationAdapterInterstitialListener) {
        mMediationAdapterInterstitialListener = mediationAdapterInterstitialListener;
        IronSource.setInterstitialListener(this);
    }

    @Override
    public void init(Activity activity, String mediationUnitId, Map<String, Object> localExtras, Map<String, String> serverExtras) {
        if(localExtras != null){
            Object ob = localExtras.get("local");
            if(ob instanceof String){
                //初始化reward video
                appKey = ob.toString();
                if (!TextUtils.isEmpty(appKey)) {
                    IronSource.init(activity, appKey, IronSource.AD_UNIT.INTERSTITIAL);
                    if (mMediationAdapterInitListener != null) {
                        mMediationAdapterInitListener.onInitSucceed();
                    }
                    return;
                }
            }

        }
        if (mMediationAdapterInitListener != null) {
            mMediationAdapterInitListener.onInitFailed();
        }

    }

    @Override
    public void load(String param) {
        if (IronSource.isInterstitialReady()) {
            onInterstitialAdReady();
        } else {
            IronSource.loadInterstitial();
        }
    }


    @Override
    public void show(String param) {
        try {
            if (IronSource.isInterstitialReady()) {
                IronSource.showInterstitial();
            } else {
                sendToMediationShowFailed(MediationMTGErrorCode.NETWORK_NO_FILL);

            }
        } catch (Exception e) {
            sendToMediationShowFailed(MediationMTGErrorCode.NETWORK_NO_FILL);

        }
    }


    @Override
    public boolean isReady(String param) {
        return IronSource.isInterstitialReady();
    }



    private void sendToMediationShowFailed(String msg){
        if(mMediationAdapterInterstitialListener != null){
            mMediationAdapterInterstitialListener.showFailed(msg);
        }
    }


    private String getMIntergralErrorMessage(IronSourceError ironSourceError) {
        if (ironSourceError == null) {
            return MediationMTGErrorCode.INTERNAL_ERROR;
        }
        switch (ironSourceError.getErrorCode()) {
            case IronSourceError.ERROR_CODE_NO_CONFIGURATION_AVAILABLE:
            case IronSourceError.ERROR_CODE_KEY_NOT_SET:
            case IronSourceError.ERROR_CODE_INVALID_KEY_VALUE:
            case IronSourceError.ERROR_CODE_INIT_FAILED:
                return MediationMTGErrorCode.ADAPTER_CONFIGURATION_ERROR;
            case IronSourceError.ERROR_CODE_USING_CACHED_CONFIGURATION:
                return MediationMTGErrorCode.VIDEO_CACHE_ERROR;
            case IronSourceError.ERROR_CODE_NO_ADS_TO_SHOW:
                return MediationMTGErrorCode.NETWORK_NO_FILL;
            case IronSourceError.ERROR_CODE_GENERIC:
                return MediationMTGErrorCode.INTERNAL_ERROR;
            case IronSourceError.ERROR_NO_INTERNET_CONNECTION:
                return MediationMTGErrorCode.NO_CONNECTION;
            default:
                return MediationMTGErrorCode.UNSPECIFIED;
        }
    }


    @Override
    public LifecycleListener getLifecycleListener() {
        return mLifecycleListener;
    }

    private LifecycleListener mLifecycleListener = new BaseLifecycleListener() {
        @Override
        public void onPause( Activity activity) {
            super.onPause(activity);
            IronSource.onPause(activity);
        }

        @Override
        public void onResume( Activity activity) {
            super.onResume(activity);
            IronSource.onResume(activity);
        }
    };

    @Override
    public void onInterstitialAdReady() {
        if(mMediationAdapterInterstitialListener != null){
            mMediationAdapterInterstitialListener.loadSucceed();
        }
    }

    @Override
    public void onInterstitialAdLoadFailed(IronSourceError ironSourceError) {
        if(mMediationAdapterInterstitialListener != null){
            mMediationAdapterInterstitialListener.loadFailed(getMIntergralErrorMessage(ironSourceError));
        }
    }

    @Override
    public void onInterstitialAdOpened() {

    }

    @Override
    public void onInterstitialAdClosed() {
        if(mMediationAdapterInterstitialListener != null){
            mMediationAdapterInterstitialListener.closed();
        }
    }

    @Override
    public void onInterstitialAdShowSucceeded() {
        if(mMediationAdapterInterstitialListener != null){
            mMediationAdapterInterstitialListener.showSucceed();
        }
    }

    @Override
    public void onInterstitialAdShowFailed(IronSourceError ironSourceError) {
        if(mMediationAdapterInterstitialListener != null){
            mMediationAdapterInterstitialListener.showFailed(getMIntergralErrorMessage(ironSourceError));
        }
    }

    @Override
    public void onInterstitialAdClicked() {
        if(mMediationAdapterInterstitialListener != null){
            mMediationAdapterInterstitialListener.clicked(mInstanceId);
        }
    }
}
