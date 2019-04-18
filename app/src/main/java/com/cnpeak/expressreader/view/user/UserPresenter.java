package com.cnpeak.expressreader.view.user;

import android.text.TextUtils;
import android.util.Log;

import com.cnpeak.expressreader.base.BasePresenter;
import com.cnpeak.expressreader.global.ErConstant;
import com.cnpeak.expressreader.model.bean.ResultBean;
import com.cnpeak.expressreader.model.bean.UserBean;
import com.cnpeak.expressreader.model.remote.UserBeanModuleFactory;
import com.cnpeak.expressreader.model.remote.ResultBeanModuleFactory;
import com.cnpeak.expressreader.utils.RegexUtils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import okhttp3.ResponseBody;
import retrofit2.HttpException;
import retrofit2.Response;

/**
 * 用户操作API
 * The type User presenter.
 */
public class UserPresenter extends BasePresenter<UserView> {

    private static final String TAG = "UserPresenter";

    private UserView mView;

    /**
     * Instantiates a new User presenter.
     *
     * @param mView the m view
     */
    public UserPresenter(UserView mView) {
        this.mView = mView;
    }

    /**
     * 邮箱注册
     *
     * @param memberName 用户名
     * @param memberPsd  密码
     * @param LCID       语系
     */
    public void register(final String memberName, final String memberPsd, String LCID) {

        if (!RegexUtils.isEmail(memberName)) {
            mView.onResult(UserView.RESULT_CODE_ILLEGAL_ACCOUNT);
            return;
        } else if (!RegexUtils.isPassword(memberPsd)) {
            mView.onResult(UserView.RESULT_CODE_ILLEGAL_PSD);
            return;
        }

        JsonObject jsonObject;
        try {
            jsonObject = new JsonObject();
            jsonObject.addProperty(ErConstant.MEMBER_NAME, memberName);
            jsonObject.addProperty(ErConstant.MEMBER_PASSWORD, memberPsd);
            jsonObject.addProperty("Lcid", LCID);

            ResultBeanModuleFactory.registerByEmail(jsonObject)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new DisposableObserver<ResultBean>() {
                        @Override
                        public void onNext(ResultBean resultBean) {
                            String result = resultBean.getResult();
                            if (TextUtils.equals("0x201", result)) {
                                mView.onResult(UserView.RESULT_CODE_REGISTER_SUCCESS);
                                signIn(memberName, memberPsd);
                            }

                        }

                        @Override
                        public void onError(Throwable e) {
                            parseErrorResult(e);
                        }

                        @Override
                        public void onComplete() {

                        }
                    });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 邮箱登陆
     *
     * @param memberName 邮箱账号
     * @param memberPsd  密码
     */
    public void signIn(String memberName, String memberPsd) {
        if (!RegexUtils.isEmail(memberName)) {
            mView.onResult(UserView.RESULT_CODE_ILLEGAL_ACCOUNT);
            return;
        } else if (!RegexUtils.isPassword(memberPsd)) {
            mView.onResult(UserView.RESULT_CODE_ILLEGAL_PSD);
            return;
        }

        JsonObject jsonObject;
        try {
            jsonObject = new JsonObject();
            jsonObject.addProperty(ErConstant.EMAIL, memberName);
            jsonObject.addProperty(ErConstant.PASSWORD, memberPsd);

            UserBeanModuleFactory.signInByEmail(jsonObject)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new DisposableObserver<UserBean.UserInfoBean>() {
                        @Override
                        public void onNext(UserBean.UserInfoBean userInfo) {
                            if (userInfo != null) {
                                mView.onResult(UserView.RESULT_CODE_LOGIN_SUCCESS);
                            } else {
                                mView.onResult(UserView.RESULT_CODE_DATA_PARSE_ERROR);
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            parseErrorResult(e);
                        }

                        @Override
                        public void onComplete() {

                        }
                    });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void parseErrorResult(Throwable throwable) {
        if (throwable instanceof HttpException) {
            HttpException httpException = (HttpException) throwable;
            Response<?> response = httpException.response();
            ResponseBody errorBody = response.errorBody();
            if (errorBody != null) {
                String content;
                try {
                    content = errorBody.string();
                    if (!TextUtils.isEmpty(content)) {
                        Gson gson = new Gson();
                        ResultBean resultBean = gson.fromJson(content, ResultBean.class);
                        if (resultBean != null) {
                            String result = resultBean.getResult();
                            callbackErrorResult(result);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        } else mView.onResult(UserView.RESULT_CODE_DATA_PARSE_ERROR);

    }

    private void callbackErrorResult(String result) {
        Log.d(TAG, "callbackErrorResult errorBody content >>> " + result);
        switch (result) {
            case "0x401":
                mView.onResult(UserView.RESULT_CODE_EMAIL_OCCUPIED);
                break;
            case "0x402":
                mView.onResult(UserView.RESULT_CODE_ACCOUNT_PSD_ERROR);
                break;
            default:
                mView.onResult(UserView.RESULT_CODE_SERVER_ERROR);
                break;
        }

    }

}
