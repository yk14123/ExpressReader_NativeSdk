package com.cnpeak.expressreader.net;


import com.cnpeak.expressreader.global.ErConstant;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Microsoft service.
 */
public interface MicrosoftService {

    /**
     * 获取翻译授权验证.
     *
     * @return the token
     */
    @Headers({"Ocp-Apim-Subscription-Key:3f9c2858ef78400bbef2674209090b11"})
    @POST("https://api.cognitive.microsoft.com/sts/v1.0/issueToken")
    Observable<String> getTranslationAuthorization();

    /**
     * 获取TTS授权验证.
     *
     * @return the token
     */
    @Headers({"Ocp-Apim-Subscription-Key:e41d44a3f5c84d818d23ce46a2834feb",
            "Content-type: application/x-www-form-urlencoded"})
    @POST("https://eastasia.api.cognitive.microsoft.com/sts/v1.0/issueToken")
    Observable<String> getTTSAuthorization();


    /**
     * 获取翻译授权验证.
     *
     * @return the token
     */
    @Headers({"Ocp-Apim-Subscription-Key:3f9c2858ef78400bbef2   674209090b11",
            "Content-type: application/x-www-form-urlencoded"})
    @POST("https://eastasia.api.cognitive.microsoft.com/sts/v1.0/issueToken")
    Observable<String> getVoice();

    /**
     * Microsoft Translation API
     *
     * @param from        源语言
     * @param to          目标语言
     * @param requestBody 请求体
     * @return the observable
     */
    @Headers({"Ocp-Apim-Subscription-Key:" + ErConstant.TRANSLATOR_SUBSCRIPTION_KEY,
            "Content-Type:application/json", "Content-Length"})
    @POST("https://api.cognitive.microsofttranslator.com/tts?api-version=3.0")
    Observable<String> translate(@Query("api-version") String apiVersion, @Query("from") String from,
                                 @Query("to") String to, @Body RequestBody requestBody);

}
