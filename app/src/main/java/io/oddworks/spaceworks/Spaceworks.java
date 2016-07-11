package io.oddworks.spaceworks;

import android.app.Application;

import io.oddworks.device.request.ApiCaller;
import io.oddworks.device.request.RestServiceProvider;

public class Spaceworks extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        RestServiceProvider.init(this, "http://odd-gdgnyc.herokuapp.com/");
    }
}
