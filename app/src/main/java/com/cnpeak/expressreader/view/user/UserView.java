package com.cnpeak.expressreader.view.user;

import com.cnpeak.expressreader.base.IView;

/**
 * 用户操作回调接口类
 */
public interface UserView extends IView {

    /**
     * 邮箱参数异常
     */
    int RESULT_CODE_ILLEGAL_ACCOUNT = 0x100;
    /**
     * 密码参数异常
     */
    int RESULT_CODE_ILLEGAL_PSD = 0x101;
    /**
     * 注册成功
     */
    int RESULT_CODE_REGISTER_SUCCESS = 0x201;
    /**
     * 登陆成功
     */
    int RESULT_CODE_LOGIN_SUCCESS = 0x200;
    /**
     * 重复注册/注册失败
     */
    int RESULT_CODE_EMAIL_OCCUPIED = 0x401;
    /**
     * 账号密码错误
     */
    int RESULT_CODE_ACCOUNT_PSD_ERROR = 0x402;
    /**
     * 內部或網路錯誤
     */
    int RESULT_CODE_SERVER_ERROR = 0x501;
    /**
     * 数据解析出错，请重试
     */
    int RESULT_CODE_DATA_PARSE_ERROR = 0x502;

    /**
     * 用户相关响应码
     *
     * @param stateCode 响应码
     */
    void onResult(int stateCode);

}
