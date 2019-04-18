package com.cnpeak.expressreader.view.billing;

import com.cnpeak.expressreader.base.IView;

public interface BillingView extends IView {

    //21000 App Store 不能读取你提供的JSON对象
    //21002 receipt-data 域的数据有问题
    //21003 receipt 无法通过验证
    //21004 提供的 shared secret 不匹配你账号中的 shared secret
    //21005 receipt 服务器当前不可用
    //21006 receipt 合法, 但是订阅已过期. 服务器接收到这个状态码时, receipt 数据仍然会解码并一起发送
    //21007 receipt 是 Sandbox receipt, 但却发送至生产系统的验证服务
    //21008 receipt 是生产 receipt, 但却发送至 Sandbox 环境的验证服务

    //支付成功
    String PAY_RESULT_SUCCESS = "0";

    String PAY_RESULT_ILLEGAL_RECEIPT = "21000";

    String PAY_RESULT_ILLEGAL_SECRET = "21002";
    //    String PAY_RESULT_SUCCESS = "21003";
//    String PAY_RESULT_ILLEGAL_SECRET = "21004";
    String PAY_RESULT_SERVER_ERROR = "21005";
    String PAY_RESULT_SUBSCRIPTION_EXPIRE = "21006";
//    String PAY_RESULT_SUCCESS = "21007";
//    String PAY_RESULT_SUCCESS = "21008";

    void onBillingSuccess();

    void onBillingFail(String statusCode);

    void onBillingError(String errorMsg);


}
