package com.example.voices.waveform;

/**
 * Created by Juan on 17/05/2018.
 */

public interface PlaybackListener {
    void onProgress(int progress);
    void onCompletion();
}