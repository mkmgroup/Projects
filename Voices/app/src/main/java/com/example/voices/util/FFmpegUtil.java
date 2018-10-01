package com.example.voices.util;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import com.example.voices.R;
import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.FFmpegExecuteResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.LoadBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegNotSupportedException;
import com.google.android.gms.common.util.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Juan on 22/05/2018.
 */

public class FFmpegUtil {
    FFmpeg ffmpeg;
    Context context;
    Boolean binaryLoaded = false;

    private void loadBinary(){
        try {
            ffmpeg.loadBinary(new LoadBinaryResponseHandler() {
                @Override
                public void onStart() {}

                @Override
                public void onFailure() {}

                @Override
                public void onSuccess() {
                    binaryLoaded = true;
                }

                @Override
                public void onFinish() {}
            });
        } catch (FFmpegNotSupportedException e) {
            // Handle if FFmpeg is not supported by device
        }
    }

    public FFmpegUtil(Context context) {
        this.context = context;
        ffmpeg = FFmpeg.getInstance(context);
        loadBinary();
    }

    public void createBlankForSecs(float sec, ResultListener<String> listener){
        if(binaryLoaded){
            File outputFile = null;
            outputFile = new File(context.getFilesDir(), "blank" + System.nanoTime() + ".m4a");
            String pathToBlank = outputFile.getAbsolutePath();
            String commnd = "-f lavfi -i anullsrc=r=11025:cl=mono -t "+Float.toString(sec)+" -acodec aac "+ pathToBlank;
            String[] cmd = commnd.split(" ");
            try {
                ffmpeg.execute(cmd, new FFmpegExecuteResponseHandler() {
                    @Override
                    public void onSuccess(String message) {
                        Log.d("ffmpeg msg", message);
                        listener.finish(pathToBlank);
                    }

                    @Override
                    public void onProgress(String message) {

                    }

                    @Override
                    public void onFailure(String message) {
                        Log.d("ffmpeg error", message);
                    }

                    @Override
                    public void onStart() {
                        Log.d("ffmpeg start", commnd);
                    }

                    @Override
                    public void onFinish() {

                    }
                });
            } catch (FFmpegCommandAlreadyRunningException e) {
                e.printStackTrace();
            }
        }




    }
    public void concatBlankWith(int id, String pathToBlank, ResultListener<String> listener){
        File outputFile = null;
        outputFile = new File(context.getFilesDir(), "conc" + System.nanoTime() + ".m4a");
        String pathToConcated = outputFile.getAbsolutePath();
        String comnd ="";
        switch (id){
            case R.id.aplause:

                File outputAplausos = new File(Environment.getExternalStorageDirectory().getAbsolutePath() +
                        File.separator + "Voices" + "/aplausos.m4a");

                if (!outputAplausos.exists())
                {
                    try {
                        InputStream raw = context.getAssets().open("aplausos.m4a");
                        OutputStream outputStream = new FileOutputStream(outputAplausos);
                        IOUtils.copyStream(raw, outputStream);
                        outputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                comnd = "-i "+pathToBlank+" -i "+outputAplausos.getAbsolutePath()+" -filter_complex [0:0][1:0]concat=n=2:v=0:a=1[out] -map [out] "+pathToConcated;

                break;
            case R.id.cuak:
                File outputCuak = new File(Environment.getExternalStorageDirectory().getAbsolutePath() +
                        File.separator + "Voices" + "/cuack.m4a");


                if (!outputCuak.exists())
                {
                    try {
                        InputStream raw = context.getAssets().open("cuack.m4a");
                        OutputStream outputStream = new FileOutputStream(outputCuak);
                        IOUtils.copyStream(raw, outputStream);
                        outputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                comnd = "-i "+pathToBlank+" -i "+outputCuak.getAbsolutePath()+" -filter_complex [0:0][1:0]concat=n=2:v=0:a=1[out] -map [out] "+pathToConcated;

                break;
            case R.id.shot:
                File outputShot = new File(Environment.getExternalStorageDirectory().getAbsolutePath() +
                        File.separator + "Voices" + "/disparo.m4a");


                if (!outputShot.exists())
                {
                    try {
                        InputStream raw = context.getAssets().open("disparo.m4a");
                        OutputStream outputStream = new FileOutputStream(outputShot);
                        IOUtils.copyStream(raw, outputStream);
                        outputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                comnd = "-i "+pathToBlank+" -i "+outputShot.getAbsolutePath()+" -filter_complex [0:0][1:0]concat=n=2:v=0:a=1[out] -map [out] "+pathToConcated;


                break;
            case R.id.breaking:

                File outputBreaking = new File(Environment.getExternalStorageDirectory().getAbsolutePath() +
                        File.separator + "Voices" + "/frenando.m4a");


                if (!outputBreaking.exists())
                {
                    try {
                        InputStream raw = context.getAssets().open("frenando.m4a");
                        OutputStream outputStream = new FileOutputStream(outputBreaking);
                        IOUtils.copyStream(raw, outputStream);
                        outputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                comnd = "-i "+pathToBlank+" -i "+outputBreaking.getAbsolutePath()+" -filter_complex [0:0][1:0]concat=n=2:v=0:a=1[out] -map [out] "+pathToConcated;

                break;
            case R.id.punch:

                File outputpunch = new File(Environment.getExternalStorageDirectory().getAbsolutePath() +
                        File.separator + "Voices" + "/golpe.m4a");


                if (!outputpunch.exists())
                {
                    try {
                        InputStream raw = context.getAssets().open("golpe.m4a");
                        OutputStream outputStream = new FileOutputStream(outputpunch);
                        IOUtils.copyStream(raw, outputStream);
                        outputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                comnd = "-i "+pathToBlank+" -i "+outputpunch.getAbsolutePath()+" -filter_complex [0:0][1:0]concat=n=2:v=0:a=1[out] -map [out] "+pathToConcated;

                break;
            case R.id.manShouting:

                File outputManShouting = new File(Environment.getExternalStorageDirectory().getAbsolutePath() +
                        File.separator + "Voices" + "/hombre_gritando.m4a");


                if (!outputManShouting.exists())
                {
                    try {
                        InputStream raw = context.getAssets().open("hombre_gritando.m4a");
                        OutputStream outputStream = new FileOutputStream(outputManShouting);
                        IOUtils.copyStream(raw, outputStream);
                        outputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                comnd = "-i "+pathToBlank+" -i "+outputManShouting.getAbsolutePath()+" -filter_complex [0:0][1:0]concat=n=2:v=0:a=1[out] -map [out] "+pathToConcated;
                break;
            case R.id.kids:
                File outpuKids = new File(Environment.getExternalStorageDirectory().getAbsolutePath() +
                        File.separator + "Voices" + "/ninos_celebrando.m4a");


                if (!outpuKids.exists())
                {
                    try {
                        InputStream raw = context.getAssets().open("ninos_celebrando.m4a");
                        OutputStream outputStream = new FileOutputStream(outpuKids);
                        IOUtils.copyStream(raw, outputStream);
                        outputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                comnd = "-i "+pathToBlank+" -i "+outpuKids.getAbsolutePath()+" -filter_complex [0:0][1:0]concat=n=2:v=0:a=1[out] -map [out] "+pathToConcated;
                break;
            case 9:
                File outputSpring = new File(Environment.getExternalStorageDirectory().getAbsolutePath() +
                        File.separator + "Voices" + "/resorte.m4a");


                if (!outputSpring.exists())
                {
                    try {
                        InputStream raw = context.getAssets().open("resorte.m4a");
                        OutputStream outputStream = new FileOutputStream(outputSpring);
                        IOUtils.copyStream(raw, outputStream);
                        outputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                comnd = "-i "+pathToBlank+" -i "+outputSpring.getAbsolutePath()+" -filter_complex [0:0][1:0]concat=n=2:v=0:a=1[out] -map [out] "+pathToConcated;
                break;
            case R.id.drums:
                File outputDrums = new File(Environment.getExternalStorageDirectory().getAbsolutePath() +
                        File.separator + "Voices" + "/drums.m4a");


                if (!outputDrums.exists())
                {
                    try {
                        InputStream raw = context.getAssets().open("drums.m4a");
                        OutputStream outputStream = new FileOutputStream(outputDrums);
                        IOUtils.copyStream(raw, outputStream);
                        outputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                comnd = "-i "+pathToBlank+" -i "+outputDrums.getAbsolutePath()+" -filter_complex [0:0][1:0]concat=n=2:v=0:a=1[out] -map [out] "+pathToConcated;
                break;
            case R.id.sparta:
                File outputSparta = new File(Environment.getExternalStorageDirectory().getAbsolutePath() +
                        File.separator + "Voices" + "/sparta.m4a");


                if (!outputSparta.exists())
                {
                    try {
                        InputStream raw = context.getAssets().open("sparta.m4a");
                        OutputStream outputStream = new FileOutputStream(outputSparta);
                        IOUtils.copyStream(raw, outputStream);
                        outputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                comnd = "-i "+pathToBlank+" -i "+outputSparta.getAbsolutePath()+" -filter_complex [0:0][1:0]concat=n=2:v=0:a=1[out] -map [out] "+pathToConcated;
                break;
        }

        String[] cmd = comnd.split(" ");
        try {
            ffmpeg.execute(cmd, new FFmpegExecuteResponseHandler() {
                @Override
                public void onSuccess(String message) {
                    Log.d("ffmpeg conc success", message);
                    listener.finish(pathToConcated);
                }

                @Override
                public void onProgress(String message) {

                }

                @Override
                public void onFailure(String message) {
                    Log.d("ffmpeg conc filure", message);
                }

                @Override
                public void onStart() {

                }

                @Override
                public void onFinish() {

                }
            });
        } catch (FFmpegCommandAlreadyRunningException e) {
            e.printStackTrace();
        }
    }
    public void mixWithUserSound(String pathToUser, String pathToEffecWihWhi,ResultListener<String> listener){
        File outputFile = null;
        outputFile = new File(context.getFilesDir(), "mix" + System.nanoTime() + ".m4a");
        String pathToMix = outputFile.getAbsolutePath();
        String comnd = "-i "+pathToUser+" -i "+pathToEffecWihWhi+" -filter_complex amix=inputs=2:duration=longest:dropout_transition=2 " + pathToMix;
        String[] cmd = comnd.split(" ");
        try {
            ffmpeg.execute(cmd, new FFmpegExecuteResponseHandler() {
                @Override
                public void onSuccess(String message) {
                    Log.d("ffmpeg mix success", message);
                    listener.finish(pathToMix);
                }

                @Override
                public void onProgress(String message) {

                }

                @Override
                public void onFailure(String message) {
                    Log.d("ffmpeg mix filure", message);
                }

                @Override
                public void onStart() {

                }

                @Override
                public void onFinish() {

                }
            });
        } catch (FFmpegCommandAlreadyRunningException e) {
            e.printStackTrace();
        }
    }
}
