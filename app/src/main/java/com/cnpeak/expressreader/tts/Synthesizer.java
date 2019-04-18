
package com.cnpeak.expressreader.tts;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.cnpeak.expressreader.interf.TTSEventCallback;
import com.cnpeak.expressreader.model.remote.TTSModuleFactory;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import io.reactivex.observers.DisposableObserver;

public class Synthesizer {
    /**
     * 日志TAG
     */
    private static final String TAG = "TTS";

    /**
     * 提供当前对象
     */
    private static Synthesizer instance;


    private static final String API = "https://eastasia.tts.speech.microsoft.com/cognitiveservices/v1";

    /**
     * 发言人对象
     */
    private Voice voice;

    /**
     * 回调对象
     */
    private TTSEventCallback callback;

    private String mAudioOutputFormat;

    private TtsServiceClient mTtsServiceClient;
    private AudioTrack audioTrack;

    private Synthesizer(Voice voice) {
        this.voice = voice;
        mTtsServiceClient = new TtsServiceClient();
        this.mAudioOutputFormat = AudioOutputFormat.Raw16Khz16BitMonoPcm;
    }

    public static Synthesizer getDefault(Voice voice) {
        if (instance == null) {
            synchronized (Synthesizer.class) {
                if (instance == null) {
                    instance = new Synthesizer(voice);
                }
            }
        }
        return instance;
    }

    public Synthesizer setCallback(TTSEventCallback callback) {
        this.callback = callback;
        return this;
    }

    public Synthesizer setVoice(Voice serviceVoice) {
        voice = serviceVoice;
        return this;
    }

    public void start(final String content) {
        //获取授权 Auth
        TTSModuleFactory.getAuthentication().subscribe(new DisposableObserver<String>() {
            @Override
            public void onNext(String token) {
                Log.d(TAG, "getAuthentication: accessToken >>> " + token);
                if (TextUtils.isEmpty(token)) {
                    if (callback != null) {
                        callback.onAuthError("get accessToken failed..");
                    }
                } else {
                    //获取音频流直接播放
                    playSound(getAudioStream(token, content));
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });

    }

    private byte[] getAudioStream(String accessToken, String content) {
        int code = -1;
        synchronized (TtsServiceClient.class) {
            try {
                URL url = new URL(API);
                HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);
                urlConnection.setConnectTimeout(5000);
                urlConnection.setReadTimeout(15000);
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type", mAudioOutputFormat);
                urlConnection.setRequestProperty("X-MICROSOFT-OutputFormat", mAudioOutputFormat);
                urlConnection.setRequestProperty("Authorization", "Bearer " + accessToken);
                urlConnection.setRequestProperty("User-Agent", "ExpressReader");
                urlConnection.setRequestProperty("Accept", "*/*");
                byte[] ssmlBytes = content.getBytes();
                urlConnection.setRequestProperty("content-length", String.valueOf(ssmlBytes.length));
                urlConnection.connect();
                urlConnection.getOutputStream().write(ssmlBytes);
                code = urlConnection.getResponseCode();
                Log.d(TAG, "getAudioStream: responseCode >>> " + code);
                if (code == 200) {
                    InputStream in = urlConnection.getInputStream();
                    ByteArrayOutputStream bout = new ByteArrayOutputStream();
                    byte[] bytes = new byte[1024];
                    int ret = in.read(bytes);
                    while (ret > 0) {
                        bout.write(bytes, 0, ret);
                        ret = in.read(bytes);
                    }
                    return bout.toByteArray();
                } else
                    urlConnection.disconnect();
                return null;
            } catch (Exception e) {
                Log.e(TAG, "Exception error", e);
                return null;
            }
        }
    }

    private void playSound(final byte[] sound) {
        if (sound == null || sound.length == 0) {
            if (callback != null) {
                callback.onConnectError("contentBytes is Notnull...", -1);
            }
            return;
        }
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                final int SAMPLE_RATE = 16000;

                audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, SAMPLE_RATE, AudioFormat.CHANNEL_CONFIGURATION_MONO,
                        AudioFormat.ENCODING_PCM_16BIT, AudioTrack.getMinBufferSize(SAMPLE_RATE, AudioFormat.CHANNEL_CONFIGURATION_MONO, AudioFormat.ENCODING_PCM_16BIT), AudioTrack.MODE_STREAM);

                if (audioTrack.getState() == AudioTrack.STATE_INITIALIZED) {
                    audioTrack.play();
                    audioTrack.write(sound, 0, sound.length);
                    audioTrack.stop();
                    audioTrack.release();
                }
            }
        });
    }

    //stop playing audio data
    // if use STREAM mode, will wait for the end of the last write buffer data will stop.
    // if you stop immediately, call the pause() method and then call the flush() method to discard the data that has not yet been played
    public void stopSound() {
        try {
            if (audioTrack != null && audioTrack.getState() == AudioTrack.STATE_INITIALIZED) {
                audioTrack.pause();
                audioTrack.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private byte[] speak(String text) {
//        String builder = "<speak version='1.0' xmlns=\"https://www.w3.org/2001/10/synthesis\" xml:lang='" +
//                voice.lang + "'>" +
//                "<voice name='" + voice.voiceName + "'>" +
//                "<prosody rate='" + voice.rate + "'>" +
//                text +
//                "</prosody></voice>";
//        Log.d(TAG, "speak: data >>> " + builder);

        String ssml = "<speak version='1.0' xml:lang='" +
                voice.lang + "'><voice xml:lang='" + voice.lang +
                "' xml:gender='" + voice.gender + "'";
        if (voice.voiceName.length() > 0) {
            ssml += " name='" + voice.voiceName + "'>";
        } else {
            ssml += ">";
        }
        ssml += text + "</voice></speak>";
        return SpeakSSML(ssml);
    }

    public void speakToAudio(String text) {
        playSound(speak(text));
    }

    public void speakSSMLToAudio(String ssml) {
        playSound(SpeakSSML(ssml));
    }

    public byte[] SpeakSSML(String ssml) {
        byte[] result = null;
        /*
         * check current network environment
         * to do...
         */
        result = mTtsServiceClient.SpeakSSML(ssml);
        if (result == null || result.length == 0) {
            return null;
        }

        return result;
    }


}
