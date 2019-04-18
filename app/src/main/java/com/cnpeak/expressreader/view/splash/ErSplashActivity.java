package com.cnpeak.expressreader.view.splash;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.cnpeak.expressreader.view.home.ErHomeActivity;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * 闪屏页面
 */
public class ErSplashActivity extends AppCompatActivity {

    private Disposable mPendingDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        startPendingIntent();

    }

    private void startPendingIntent() {
        mPendingDisposable = Observable.timer(3, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) {
                        Intent intent = new Intent(ErSplashActivity.this, ErHomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPendingDisposable != null) {
            mPendingDisposable.dispose();
        }
    }
}
