package com.cnpeak.expressreader.view.billing;

import com.cnpeak.expressreader.base.BasePresenter;
import com.cnpeak.expressreader.global.ErConstant;
import com.cnpeak.expressreader.model.bean.PayStatus;
import com.cnpeak.expressreader.model.bean.UserBean;
import com.cnpeak.expressreader.model.local.UserInfoLocalFactory;
import com.cnpeak.expressreader.model.remote.PayStatusModuleFactory;
import com.google.gson.JsonObject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;

/**
 * 支付业务操作类
 */
public class BillingPresenter extends BasePresenter<BillingView> {

    private BillingView mBillingView;

    /**
     * Instantiates a new Billing presenter.
     *
     * @param billingView 结果回调
     */
    public BillingPresenter(BillingView billingView) {
        this.mBillingView = billingView;
    }

    /**
     * 发送订单验证接口
     *
     * @param productId        产品ID
     * @param subscriptionType 定于类型
     * @param transitionId     transitionId
     * @param receipt          receipt
     * @param signature        signature
     */
    public void sendPayReceipt(String productId, String subscriptionType,
                               String transitionId, String receipt, String signature) {

        UserBean.UserInfoBean userInfo = UserInfoLocalFactory.builder().getUserInfo();
        if (userInfo != null) {
            String memberName = userInfo.getMemberName();
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty(ErConstant.MEMBER_NAME, memberName);
            jsonObject.addProperty(ErConstant.PRODUCT_ID, productId);
            jsonObject.addProperty(ErConstant.SUBSCRIPTION_TYPE, subscriptionType);
            jsonObject.addProperty(ErConstant.TRANSITION_ID, transitionId);
            jsonObject.addProperty(ErConstant.RECEIPT, receipt);
            jsonObject.addProperty(ErConstant.SIGNATURE, signature);
            PayStatusModuleFactory.sendPayReceipt(jsonObject)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new DisposableObserver<PayStatus>() {
                        @Override
                        public void onNext(PayStatus payStatus) {
                            String ststus = payStatus.getStstus();
                            if (ststus.equals(BillingView.PAY_RESULT_SUCCESS)) {
                                mBillingView.onBillingSuccess();
                            } else {
                                mBillingView.onBillingFail(ststus);
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            mBillingView.onBillingError(e.getLocalizedMessage());
                        }

                        @Override
                        public void onComplete() {

                        }
                    });


        }

    }

}
