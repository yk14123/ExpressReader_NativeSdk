package com.cnpeak.expressreader.model.remote;

import android.util.Log;

import com.cnpeak.expressreader.global.ErConstant;
import com.cnpeak.expressreader.net.ApiManager;
import com.cnpeak.expressreader.net.MicrosoftService;
import com.cnpeak.expressreader.tts.AudioOutputFormat;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class TTSModuleFactory {

    public static final String TAG = "TTS";

    public static void tts(String authorization, String content) {
        MediaType mediaType = MediaType.parse("application/ssml+xml");
//        //请求正文格式[{text:""}]
        final JsonArray array = new JsonArray();
        JsonObject object = new JsonObject();
        object.addProperty("text", content);
        array.add(object);

        String text = "<speak version='1.0' xmlns='https://www.w3.org/2001/10/synthesis' xml:lang='zh-CN'> " +
                "<voice name='Microsoft Server Speech Text to Speech Voice (zh-CN，HuihuiRUS)'>" +
                content + "</voice></speak>";

        Log.d(TAG, "tts: request >>> " + text);


        RequestBody body = RequestBody.create(mediaType, text);
        int length = content.getBytes(StandardCharsets.UTF_8).length;
        final Request request = new Request.Builder()
                .url("https://eastasia.tts.speech.microsoft.com/cognitiveservices/v1")
                .post(body)
                .addHeader("Ocp-Apim-Subscription-Key", ErConstant.TRANSLATOR_SUBSCRIPTION_KEY)
                .addHeader("Content-Type", "application/ssml+xml")
                .addHeader("Content-Length", String.valueOf(length))
                .addHeader("User-Agent", "ExpressReader")
                .addHeader("Accept", "*/*")
                .addHeader("X-Microsoft-OutputFormat", AudioOutputFormat.Raw16Khz16BitMonoPcm)
                .addHeader("Authorization", "Bearer " + authorization)
                .build();
        // Instantiates the OkHttpClient.
        ApiManager.builder().getOkHttpClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "onFailure: e >>> " + e.getLocalizedMessage());
            }

            @Override
            public void onResponse(Call call, Response response) {
                Log.d(TAG, "onResponse: >>> " + response);
//                InputStream inputStream = response.body().byteStream();
////                Synthesizer synthesizer = new Synthesizer();
////                ByteArrayOutputStream bout = new ByteArrayOutputStream();
////                byte[] bytes = new byte[1024];
////                int ret = 0;
////                try {
////                    ret = inputStream.read(bytes);
////                    while (ret > 0) {
////                        bout.write(bytes, 0, ret);
////                        ret = inputStream.read(bytes);
////                    }
////                    synthesizer.playSound(bout.toByteArray());
////                } catch (IOException e) {
////                    e.printStackTrace();
////                }
            }
        });
    }

    public static Observable<String> getAuthentication() {
        Log.d(TAG, "getAuthentication start >>> ");
        return ApiManager.builder().createServiceFrom(MicrosoftService.class)
                .getTTSAuthorization().subscribeOn(Schedulers.io());
    }

}
