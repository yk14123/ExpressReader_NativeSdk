package com.cnpeak.expressreader.model.remote;

import android.util.Log;

import com.cnpeak.expressreader.global.ErConstant;
import com.cnpeak.expressreader.interf.TranslateEventCallback;
import com.cnpeak.expressreader.model.bean.Translate;
import com.cnpeak.expressreader.net.ApiManager;
import com.cnpeak.expressreader.net.MicrosoftService;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class TranslatorModuleFactory {

    public static final String TAG = "Translator";

    //    Observable<String>
    public static void translate(String content, String from, String to, final TranslateEventCallback callback) {

        MediaType mediaType = MediaType.parse("application/json");
//        //请求正文格式[{text:""}]
        final JsonArray array = new JsonArray();
        JsonObject object = new JsonObject();
        object.addProperty("text", content);
        array.add(object);
        Log.d(TAG, "tts: request >>> " + array.toString());

        RequestBody body = RequestBody.create(mediaType, array.toString());

        int length = content.getBytes(StandardCharsets.UTF_8).length;
        final Request request = new Request.Builder()
                //"https://api.cognitive.microsofttranslator.com/translate?api-version=3.0&from=cn&to=en"
                .url(ErConstant.TRANSLATOR_API)
                .post(body)
                .addHeader("Ocp-Apim-Subscription-Key", ErConstant.TRANSLATOR_SUBSCRIPTION_KEY)
                .addHeader("Content-Type", "application/json")
                .addHeader("Content-Length", String.valueOf(length))
                .build();
        // Instantiates the OkHttpClient.
        ApiManager.builder().getOkHttpClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "onFailure: e >>> " + e.getLocalizedMessage());
            }

            @Override
            public void onResponse(Call call, Response response) {
                if (response.code() == 200) {
                    Gson gson = new Gson();
                    try {
                        Translate[] results = gson.fromJson(
                                response.body().string(), Translate[].class);
                        if (results != null && results.length != 0) {
                            List<Translate> translateList = Arrays.asList(results);
                            Log.d(TAG, "onResponse: size >>> " + translateList.get(0).getTranslations().size());
                            StringBuilder builder = new StringBuilder();
                            for (Translate translate : translateList) {
                                if (translate != null) {
                                    List<Translate.TranslationsBean> translations = translate.getTranslations();
                                    if (translations != null && translations.size() != 0) {
                                        for (int i = 0; i < translations.size(); i++) {
                                            Translate.TranslationsBean translationsBean = translations.get(i);
                                            if (translationsBean != null) {
                                                builder.append(translationsBean.getText())
                                                        .append("\n");
                                            }
                                        }
                                    }
                                }
                            }
                            Log.d(TAG, "onResponse: result >>> " + builder.toString());
                            if (callback != null) {
                                callback.onTranslateResult(builder.toString(), "");
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        if (callback != null) {
                            callback.onTranslateError(-1, e.getLocalizedMessage(), "");
                        }
                    }
                } else {
                    if (callback != null) {
                        callback.onTranslateError(-1, "failed to connect..", "");
                    }
                }
            }
        });
    }

    public static Observable<String> getAuthentication() {
        Log.d(TAG, "getAuthentication start >>> ");
        return ApiManager.builder().createServiceFrom(MicrosoftService.class)
                .getTranslationAuthorization().subscribeOn(Schedulers.io());
    }

}
