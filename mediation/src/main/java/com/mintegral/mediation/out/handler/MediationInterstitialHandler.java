package com.mintegral.mediation.out.handler;

import android.app.Activity;

import com.mintegral.mediation.common.LifecycleListener;
import com.mintegral.mediation.common.MediationMTGErrorCode;
import com.mintegral.mediation.common.interceptor.BaseInterceptor;
import com.mintegral.mediation.common.listener.MediationAdapterInitListener;
import com.mintegral.mediation.common.listener.MediationAdapterInterstitialListener;
import com.mintegral.mediation.common.manager.MediationInterstitialManager;

import java.util.Map;

/**
 *
 */
public class MediationInterstitialHandler extends BaseHandler{

    /**
     *
     */
    private MediationInterstitialManager mediationInterstitialManager;

    /**
     *
     */
    private MediationAdapterInterstitialListener mMediationAdapterInterstitialListener;
    /**
     *
     */
    private MediationAdapterInitListener mMediationAdapterInitListener;
    /**
     * 后续版本使用，标记在当前聚合平台的广告位
     */
    private String mediationUnitId = "";

    public MediationInterstitialHandler(){
        mediationInterstitialManager = new MediationInterstitialManager();
    }
    /**
     *
     * @param activity context ,but ironsource need activity
     * @param localParams set params in code
     */
    @Override
    public void init(final Activity activity,  final Map<String, Object> localParams) {
        try {
            mediationInterstitialManager.init(activity, mediationUnitId, localParams);
        } catch (Exception e) {
            if (mMediationAdapterInitListener != null){
                mMediationAdapterInitListener.onInitFailed();
            }
        }
    }

    /**
     *
     */
    @Override
    public void load(String param) {
        try {
            if (mediationInterstitialManager != null){
                mediationInterstitialManager.load(param);
            } else{
                if (mMediationAdapterInterstitialListener != null){
                    mMediationAdapterInterstitialListener.loadFailed(MediationMTGErrorCode.UNSPECIFIED);
                }
            }
        } catch (Exception e) {
            if (mMediationAdapterInterstitialListener != null){
                mMediationAdapterInterstitialListener.loadFailed(MediationMTGErrorCode.UNSPECIFIED);
            }
        }
    }

    /**
     *
     * @return
     */
    @Override
    public boolean isReady(String param) {
        try {
            if (mediationInterstitialManager != null){
                return mediationInterstitialManager.isReady(param);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     *
     */
    @Override
    public void show(String param) {
        try {
            if (mediationInterstitialManager != null){
                mediationInterstitialManager.show(param);
            } else{
                if (mMediationAdapterInterstitialListener != null){
                    mMediationAdapterInterstitialListener.showFailed(MediationMTGErrorCode.UNSPECIFIED);
                }
            }
        } catch (Exception e) {
            if (mMediationAdapterInterstitialListener != null){
                mMediationAdapterInterstitialListener.showFailed(MediationMTGErrorCode.UNSPECIFIED);
            }
        }
    }

    /**
     *
     * @param mediationAdapterInitListener initListener
     */
    @Override
    public void setMediationAdapterInitListener(final MediationAdapterInitListener mediationAdapterInitListener) {
        try {
            mMediationAdapterInitListener = mediationAdapterInitListener;
            if (mediationInterstitialManager != null){
                mediationInterstitialManager.setMediationAdapterInitListener(mediationAdapterInitListener);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @return
     */
    @Override
    public LifecycleListener getLifecycleListener() {
        try {
            if (mediationInterstitialManager != null){
                return mediationInterstitialManager.getLifecycleListener();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * set Interstitial listener
     * @param mediationAdapterInterstitialListener listener interstitial result
     */
    public void setMediationAdapterInterstitialListener(final MediationAdapterInterstitialListener mediationAdapterInterstitialListener){
        try {
            mMediationAdapterInterstitialListener = mediationAdapterInterstitialListener;
            if (mediationInterstitialManager != null){
                mediationInterstitialManager.setMediationAdapterInterstitialListener(mediationAdapterInterstitialListener);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setInterceptor(BaseInterceptor interceptor) {
        try {
            if (mediationInterstitialManager != null){
                mediationInterstitialManager.setInterceptor(interceptor);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
