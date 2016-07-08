package io.oddworks.spaceworks;

import android.app.Application;

import io.oddworks.device.request.ApiCaller;
import io.oddworks.device.request.RestServiceProvider;

public class Spaceworks extends Application {

    private static Spaceworks SINGLETON;

    public static Spaceworks getInstance() {
        return SINGLETON;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        SINGLETON = this;

        setupOddworks();
    }

    private void setupOddworks() {
        RestServiceProvider.init(this);

        ApiCaller apiCaller = RestServiceProvider.getInstance().getApiCaller();
        apiCaller.setBaseUrl("http://192.168.12.111:3000/");
    }
}
