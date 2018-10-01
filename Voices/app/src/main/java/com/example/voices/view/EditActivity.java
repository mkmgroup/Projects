package com.example.voices.view;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.voices.R;
import com.example.voices.util.FFmpegUtil;
import com.example.voices.util.ResultListener;
import com.example.voices.view.upload.UploadActivity;
import com.example.voices.waveform.PlaybackThread;
import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.piasy.rxandroidaudio.PlayConfig;
import com.github.piasy.rxandroidaudio.RxAudioPlayer;
import com.google.android.gms.common.util.IOUtils;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import rm.com.audiowave.AudioWaveView;
import rm.com.audiowave.OnProgressListener;
import rm.com.audiowave.OnSamplingListener;

public class EditActivity extends AppCompatActivity {

    public static final String FILEPATH = "filePath";
    String filePath;
    PlaybackThread mPlaybackThread;
    TextView duration;
    ImageButton applauseCardView;
    ImageButton cuackCardView;
    ImageButton shotCardView;
    ImageButton breakingCardView;
    ImageButton punchCardView;
    ImageButton shoutCardView;
    ImageButton kidsCardView;
    ImageButton springCardView;
    ImageButton drumsCardView;
    ImageButton spartaCardView;

    Boolean isPlaying = false;

    String durFormated;
    Long dur;

    ConstraintLayout parentView;
    RxAudioPlayer rxAudioPlayer;
    FloatingActionButton floatingActionButtonPlay;

    File fileToPlay;


    FFmpeg ffmpeg;
    FFmpegUtil fFmpegUtil;

    float position = 0;

    long start;
    long stop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        duration = findViewById(R.id.durationText);
        applauseCardView = findViewById(R.id.aplause);
        cuackCardView = findViewById(R.id.cuak);
        shotCardView = findViewById(R.id.shot);
        breakingCardView = findViewById(R.id.breaking);
        parentView = findViewById(R.id.parentEdit);
        punchCardView = findViewById(R.id.punch);
        shoutCardView = findViewById(R.id.manShouting);
        kidsCardView = findViewById(R.id.kids);
        springCardView = findViewById(R.id.spring);
        drumsCardView = findViewById(R.id.drums);
        spartaCardView = findViewById(R.id.sparta);
        floatingActionButtonPlay = findViewById(R.id.floatingActionButtonPlay);

        ffmpeg = FFmpeg.getInstance(this);
        fFmpegUtil = new FFmpegUtil(this);
        //Obtener el Intent
        Intent unIntent = getIntent();

        //Obtener el Bundle
        Bundle unBundle = unIntent.getExtras();

        //Obtener el mensaje
        filePath = unBundle.getString(FILEPATH);
        File sourceFile = new File(filePath);
        fileToPlay = sourceFile;
        rxAudioPlayer = RxAudioPlayer.getInstance();

        AudioWaveView wave = findViewById(R.id.wave);

        Uri uri = Uri.parse(filePath);
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(this,uri);
        String durationStr = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);

        Long aLong = Long.parseLong(durationStr);
        dur = aLong;

        durFormated = String.format("%02d: %02d",
                TimeUnit.MILLISECONDS.toMinutes(aLong),
                TimeUnit.MILLISECONDS.toSeconds(aLong) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(aLong))
        );

        duration.setText(durFormated);

        applauseCardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                ClipData data = ClipData.newPlainText("", "");
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(
                        view);
                view.startDrag(data, shadowBuilder, view, 0);
                return true;
            }
        });
        applauseCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rxAudioPlayer.play(PlayConfig.res(getBaseContext(), R.raw.aplausos) .looping(false).leftVolume(1.0F).rightVolume(1.0F).build()).subscribeOn(Schedulers.io())
                        .subscribe(new Observer<Boolean>() {
                            @Override
                            public void onSubscribe(final Disposable disposable) {

                            }

                            @Override
                            public void onNext(final Boolean aBoolean) {
                                // prepared
                            }

                            @Override
                            public void onError(final Throwable throwable) {

                            }

                            @Override
                            public void onComplete() {
                                // play finished
                                // NOTE: if looping, the Observable will never finish, you need stop playing
                                // onDestroy, otherwise, memory leak will happen!
                            }
                        });;
            }
        });

        cuackCardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                ClipData data = ClipData.newPlainText("", "");
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(
                        view);
                view.startDrag(data, shadowBuilder, view, 0);
                return true;
            }
        });
        cuackCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rxAudioPlayer.play(PlayConfig.res(getBaseContext(), R.raw.cuack) .looping(false).leftVolume(1.0F).rightVolume(1.0F).build()).subscribeOn(Schedulers.io())
                        .subscribe(new Observer<Boolean>() {
                            @Override
                            public void onSubscribe(final Disposable disposable) {

                            }

                            @Override
                            public void onNext(final Boolean aBoolean) {
                                // prepared
                            }

                            @Override
                            public void onError(final Throwable throwable) {

                            }

                            @Override
                            public void onComplete() {
                                // play finished
                                // NOTE: if looping, the Observable will never finish, you need stop playing
                                // onDestroy, otherwise, memory leak will happen!
                            }
                        });

            }
        });


        shotCardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                ClipData data = ClipData.newPlainText("", "");
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(
                        view);
                view.startDrag(data, shadowBuilder, view, 0);
                return true;
            }
        });
        shotCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rxAudioPlayer.play(PlayConfig.res(getBaseContext(), R.raw.disparo) .looping(false).leftVolume(1.0F).rightVolume(1.0F).build()).subscribeOn(Schedulers.io())
                        .subscribe(new Observer<Boolean>() {
                            @Override
                            public void onSubscribe(final Disposable disposable) {

                            }

                            @Override
                            public void onNext(final Boolean aBoolean) {
                                // prepared
                            }

                            @Override
                            public void onError(final Throwable throwable) {

                            }

                            @Override
                            public void onComplete() {
                                // play finished
                                // NOTE: if looping, the Observable will never finish, you need stop playing
                                // onDestroy, otherwise, memory leak will happen!
                            }
                        });;
            }
        });

        breakingCardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                ClipData data = ClipData.newPlainText("", "");
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(
                        view);
                view.startDrag(data, shadowBuilder, view, 0);
                return true;
            }
        });
        breakingCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rxAudioPlayer.play(PlayConfig.res(getBaseContext(), R.raw.frenando).looping(false).leftVolume(1.0F).rightVolume(1.0F).build()).subscribeOn(Schedulers.io())
                        .subscribe(new Observer<Boolean>() {
                            @Override
                            public void onSubscribe(final Disposable disposable) {

                            }

                            @Override
                            public void onNext(final Boolean aBoolean) {
                                // prepared
                            }

                            @Override
                            public void onError(final Throwable throwable) {

                            }

                            @Override
                            public void onComplete() {
                                // play finished
                                // NOTE: if looping, the Observable will never finish, you need stop playing
                                // onDestroy, otherwise, memory leak will happen!
                            }
                        });;
            }
        });

        punchCardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                ClipData data = ClipData.newPlainText("", "");
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(
                        view);
                view.startDrag(data, shadowBuilder, view, 0);
                return true;
            }
        });
        punchCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rxAudioPlayer.play(PlayConfig.res(getBaseContext(), R.raw.golpe) .looping(false).leftVolume(1.0F).rightVolume(1.0F).build()).subscribeOn(Schedulers.io())
                        .subscribe(new Observer<Boolean>() {
                            @Override
                            public void onSubscribe(final Disposable disposable) {

                            }

                            @Override
                            public void onNext(final Boolean aBoolean) {
                                // prepared
                            }

                            @Override
                            public void onError(final Throwable throwable) {

                            }

                            @Override
                            public void onComplete() {
                                // play finished
                                // NOTE: if looping, the Observable will never finish, you need stop playing
                                // onDestroy, otherwise, memory leak will happen!
                            }
                        });;
            }
        });

        shoutCardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                ClipData data = ClipData.newPlainText("", "");
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(
                        view);
                view.startDrag(data, shadowBuilder, view, 0);
                return true;
            }
        });
        shoutCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rxAudioPlayer.play(PlayConfig.res(getBaseContext(), R.raw.hombre_gritando) .looping(false).leftVolume(1.0F).rightVolume(1.0F).build()).subscribeOn(Schedulers.io())
                        .subscribe(new Observer<Boolean>() {
                            @Override
                            public void onSubscribe(final Disposable disposable) {

                            }

                            @Override
                            public void onNext(final Boolean aBoolean) {
                                // prepared
                            }

                            @Override
                            public void onError(final Throwable throwable) {

                            }

                            @Override
                            public void onComplete() {
                                // play finished
                                // NOTE: if looping, the Observable will never finish, you need stop playing
                                // onDestroy, otherwise, memory leak will happen!
                            }
                        });;
            }
        });
        kidsCardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                ClipData data = ClipData.newPlainText("", "");
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(
                        view);
                view.startDrag(data, shadowBuilder, view, 0);
                return true;
            }
        });
        kidsCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rxAudioPlayer.play(PlayConfig.res(getBaseContext(), R.raw.ninos_celebrando) .looping(false).leftVolume(1.0F).rightVolume(1.0F).build()).subscribeOn(Schedulers.io())
                        .subscribe(new Observer<Boolean>() {
                            @Override
                            public void onSubscribe(final Disposable disposable) {

                            }

                            @Override
                            public void onNext(final Boolean aBoolean) {
                                // prepared
                            }

                            @Override
                            public void onError(final Throwable throwable) {

                            }

                            @Override
                            public void onComplete() {
                                // play finished
                                // NOTE: if looping, the Observable will never finish, you need stop playing
                                // onDestroy, otherwise, memory leak will happen!
                            }
                        });;
            }
        });

        springCardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                ClipData data = ClipData.newPlainText("", "");
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(
                        view);
                view.startDrag(data, shadowBuilder, view, 0);
                return true;
            }
        });
        springCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rxAudioPlayer.play(PlayConfig.res(getBaseContext(), R.raw.resorte) .looping(false).leftVolume(1.0F).rightVolume(1.0F).build()).subscribeOn(Schedulers.io())
                        .subscribe(new Observer<Boolean>() {
                            @Override
                            public void onSubscribe(final Disposable disposable) {

                            }

                            @Override
                            public void onNext(final Boolean aBoolean) {
                                // prepared
                            }

                            @Override
                            public void onError(final Throwable throwable) {

                            }

                            @Override
                            public void onComplete() {
                                // play finished
                                // NOTE: if looping, the Observable will never finish, you need stop playing
                                // onDestroy, otherwise, memory leak will happen!
                            }
                        });;
            }
        });

        drumsCardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                ClipData data = ClipData.newPlainText("", "");
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(
                        view);
                view.startDrag(data, shadowBuilder, view, 0);
                return true;
            }
        });
        drumsCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rxAudioPlayer.play(PlayConfig.res(getBaseContext(), R.raw.drums) .looping(false).leftVolume(1.0F).rightVolume(1.0F).build()).subscribeOn(Schedulers.io())
                        .subscribe(new Observer<Boolean>() {
                            @Override
                            public void onSubscribe(final Disposable disposable) {

                            }

                            @Override
                            public void onNext(final Boolean aBoolean) {
                                // prepared
                            }

                            @Override
                            public void onError(final Throwable throwable) {

                            }

                            @Override
                            public void onComplete() {
                                // play finished
                                // NOTE: if looping, the Observable will never finish, you need stop playing
                                // onDestroy, otherwise, memory leak will happen!
                            }
                        });
            }
        });

        spartaCardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                ClipData data = ClipData.newPlainText("", "");
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(
                        view);
                view.startDrag(data, shadowBuilder, view, 0);
                return true;
            }
        });
        spartaCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rxAudioPlayer.play(PlayConfig.res(getBaseContext(), R.raw.sparta) .looping(false).leftVolume(1.0F).rightVolume(1.0F).build()).subscribeOn(Schedulers.io())
                        .subscribe(new Observer<Boolean>() {
                            @Override
                            public void onSubscribe(final Disposable disposable) {

                            }

                            @Override
                            public void onNext(final Boolean aBoolean) {
                                // prepared
                            }

                            @Override
                            public void onError(final Throwable throwable) {

                            }

                            @Override
                            public void onComplete() {
                                // play finished
                                // NOTE: if looping, the Observable will never finish, you need stop playing
                                // onDestroy, otherwise, memory leak will happen!
                            }
                        });
            }
        });



        Context me = this;
        wave.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View view, DragEvent event) {
                int action = event.getAction();
                switch (event.getAction()) {
                    case DragEvent.ACTION_DRAG_STARTED:
                        // do nothing
                        break;
                    case DragEvent.ACTION_DRAG_ENTERED:
                        Toast.makeText(getApplicationContext(), "entro", Toast.LENGTH_LONG).show();
                        break;
                    case DragEvent.ACTION_DRAG_EXITED:

                        break;
                    case DragEvent.ACTION_DROP:
                        // Dropped, reassign View to ViewGroup

                        final ProgressDialog progress = new ProgressDialog(me);
                        progress.setMessage("Procesando efecto");
                        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
                        progress.show();

                        View v = (View) event.getLocalState();
                        float secDroped = ((event.getX() * aLong) / view.getWidth()) / 1000;
                        /*Integer sec = Math.round(secDroped);*/
                        Toast.makeText(getApplicationContext(), Float.toString(secDroped), Toast.LENGTH_LONG).show();
                        ImageView imageView = new ImageView(me);
                        imageView.setLayoutParams(new ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT));
                        switch (v.getId()){
                            case R.id.aplause:
                                imageView.setImageResource(R.mipmap.applause);
                                parentView.addView(imageView);
                                imageView.setY((wave.getHeight() / 2) - 100 );
                                imageView.setX(event.getX());
                                addEffect(R.id.aplause, secDroped, new ResultListener<Boolean>() {
                                    @Override
                                    public void finish(Boolean resultado) {
                                        progress.dismiss();
                                    }
                                });
                                break;
                            case R.id.cuak:
                                imageView.setImageResource(R.mipmap.duck);
                                parentView.addView(imageView);
                                imageView.setY((wave.getHeight() / 2) - 100 );
                                imageView.setX(event.getX());
                                addEffect(R.id.cuak, secDroped, new ResultListener<Boolean>() {
                                    @Override
                                    public void finish(Boolean resultado) {
                                        progress.dismiss();
                                    }
                                });
                                break;
                            case R.id.shot:
                                imageView.setImageResource(R.mipmap.pistol);
                                parentView.addView(imageView);
                                imageView.setY((wave.getHeight() / 2) - 100 );
                                imageView.setX(event.getX());
                                addEffect(R.id.shot, secDroped, new ResultListener<Boolean>() {
                                    @Override
                                    public void finish(Boolean resultado) {
                                        progress.dismiss();
                                    }
                                });
                                break;
                            case R.id.breaking:
                                imageView.setImageResource(R.mipmap.breaking);
                                parentView.addView(imageView);
                                imageView.setY((wave.getHeight() / 2) - 100 );
                                imageView.setX(event.getX());
                                addEffect(R.id.breaking, secDroped, new ResultListener<Boolean>() {
                                    @Override
                                    public void finish(Boolean resultado) {
                                        progress.dismiss();
                                    }
                                });
                                break;
                            case R.id.punch:
                                imageView.setImageResource(R.mipmap.punch);
                                parentView.addView(imageView);
                                imageView.setY((wave.getHeight() / 2) - 100 );
                                imageView.setX(event.getX());
                                addEffect(R.id.punch, secDroped, new ResultListener<Boolean>() {
                                    @Override
                                    public void finish(Boolean resultado) {
                                        progress.dismiss();
                                    }
                                });
                                break;
                            case R.id.manShouting:
                                imageView.setImageResource(R.mipmap.shout);
                                parentView.addView(imageView);
                                imageView.setY((wave.getHeight() / 2) - 100 );
                                imageView.setX(event.getX());
                                addEffect(R.id.manShouting, secDroped, new ResultListener<Boolean>() {
                                    @Override
                                    public void finish(Boolean resultado) {
                                        progress.dismiss();
                                    }
                                });
                                break;
                            case R.id.kids:
                                imageView.setImageResource(R.mipmap.boy);
                                parentView.addView(imageView);
                                imageView.setY((wave.getHeight() / 2) - 100 );
                                imageView.setX(event.getX());
                                addEffect(R.id.kids, secDroped, new ResultListener<Boolean>() {
                                    @Override
                                    public void finish(Boolean resultado) {
                                        progress.dismiss();
                                    }
                                });
                                break;
                            case R.id.spring:
                                imageView.setImageResource(R.mipmap.bounce);
                                parentView.addView(imageView);
                                imageView.setY((wave.getHeight() / 2) - 100 );
                                imageView.setX(event.getX());
                                addEffect(9, secDroped, new ResultListener<Boolean>() {
                                    @Override
                                    public void finish(Boolean resultado) {
                                        progress.dismiss();
                                    }
                                });
                                break;
                            case R.id.drums:
                                imageView.setImageResource(R.mipmap.drums);
                                parentView.addView(imageView);
                                imageView.setY((wave.getHeight() / 2) - 100 );
                                imageView.setX(event.getX());
                                addEffect(R.id.drums, secDroped, new ResultListener<Boolean>() {
                                    @Override
                                    public void finish(Boolean resultado) {
                                        progress.dismiss();
                                    }
                                });
                                break;
                            case R.id.sparta:
                                imageView.setImageResource(R.mipmap.sparta);
                                parentView.addView(imageView);
                                imageView.setY((wave.getHeight() / 2) - 100 );
                                imageView.setX(event.getX());
                                addEffect(R.id.sparta, secDroped, new ResultListener<Boolean>() {
                                    @Override
                                    public void finish(Boolean resultado) {
                                        progress.dismiss();
                                    }
                                });
                                break;
                        }

                        break;
                    case DragEvent.ACTION_DRAG_ENDED:

                    default:
                        break;
                }
                return true;
            }
        });
        try {
            FileInputStream inputStream = new FileInputStream(sourceFile);
            byte[] data = new byte[0];
            try {
                data = IOUtils.toByteArray(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }
            }



            wave.setRawData(data);



            /*ShortBuffer sb = ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer();
            short[] samples = new short[sb.limit()];
            sb.get(samples);*/




        } catch (IOException e) {
            e.printStackTrace();
        }

        wave.setOnProgressListener(new OnProgressListener() {
            @Override
            public void onStartTracking(float v) {
                position = v;
                start = System.currentTimeMillis();

            }

            @Override
            public void onStopTracking(float v) {
                stop = System.currentTimeMillis();

                float timeSel = (aLong * (v/100))/1000;

                /*if (stop - start > 2000 ){
                    Toast.makeText(getApplicationContext(), Float.toString(timeSel), Toast.LENGTH_SHORT).show();
                }*/


                Toast.makeText(getApplicationContext(), Float.toString(v), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onProgressChanged(float v, boolean b) {
            }
        });





        floatingActionButtonPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Timer timer = new Timer();
                rxAudioPlayer.play(PlayConfig.file(fileToPlay).looping(false).leftVolume(1.0F).rightVolume(1.0F).build()).subscribeOn(Schedulers.io())
                        .subscribe(new Observer<Boolean>() {
                            @Override
                            public void onSubscribe(final Disposable disposable) {
                                Long dur = Long.parseLong(durationStr);
                                isPlaying = true;

                                timer.scheduleAtFixedRate(new TimerTask() {
                                    @Override
                                    public void run() {
                                        if (rxAudioPlayer.getMediaPlayer() != null){

                                            if((rxAudioPlayer.getMediaPlayer().getCurrentPosition() * 100) / dur < 100){
                                                wave.setProgress((rxAudioPlayer.getMediaPlayer().getCurrentPosition() * 100) / dur);
                                            }
                                        }

                                    }
                                },0,50);


                            }

                            @Override
                            public void onNext(final Boolean aBoolean) {
                                // prepared
                            }

                            @Override
                            public void onError(final Throwable throwable) {

                            }

                            @Override
                            public void onComplete() {
                                // play finished
                                // NOTE: if looping, the Observable will never finish, you need stop playing
                                // onDestroy, otherwise, memory leak will happen!
                                wave.setProgress(0);
                                timer.cancel();
                                isPlaying = false;
                            }
                        });
            }
        });
    }
    public void addEffect(int id, float sec,ResultListener<Boolean> listener){
        fFmpegUtil.createBlankForSecs(sec, new ResultListener<String>() {
            @Override
            public void finish(String resultado) {
                fFmpegUtil.concatBlankWith(id, resultado, new ResultListener<String>() {
                    @Override
                    public void finish(String resultado) {
                        fFmpegUtil.mixWithUserSound(fileToPlay.getAbsolutePath(), resultado, new ResultListener<String>() {
                            @Override
                            public void finish(String resultado) {
                                fileToPlay = new File(resultado);
                                Toast.makeText(getApplicationContext(), resultado, Toast.LENGTH_LONG).show();
                                listener.finish(true);

                            }
                        });
                    }
                });
            }
        });
    }
    public void onNextClick(View view){
        Intent intent = new Intent(getApplicationContext(), UploadActivity.class);
        Bundle unBundle = new Bundle();
        unBundle.putString(UploadActivity.FILEPATH, fileToPlay.getAbsolutePath());
        unBundle.putString(UploadActivity.DURATION, durFormated);
        Gson gson = new Gson();
        String durLong = gson.toJson(dur);
        unBundle.putString(UploadActivity.DURLONG, durLong);
        intent.putExtras(unBundle);
        startActivityForResult(intent, 55);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == 55){
            if (resultCode == 123) {
                setResult(resultCode);
                finish();
            }
        }

    }

}
