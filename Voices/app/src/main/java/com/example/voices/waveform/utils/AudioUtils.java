package com.example.voices.waveform.utils;

/**
 * Created by Juan on 17/05/2018.
 */

public final class AudioUtils {
    public static int calculateAudioLength(int samplesCount, int sampleRate, int channelCount) {
        return ((samplesCount / channelCount) * 1000) / sampleRate;
    }
}