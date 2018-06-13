package com.example.wave.practoo;

import android.app.Application;

import com.codewaves.youtubethumbnailview.ThumbnailLoader;

public class Sample extends Application
{
    @Override
    public void onCreate() {
        super.onCreate();

        ThumbnailLoader.initialize(getApplicationContext());
    }
}
