package com.example.voices.view;

import android.content.Context;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.Toast;

import com.cleveroad.audiovisualization.AudioVisualization;
import com.example.voices.R;
import com.github.piasy.rxandroidaudio.AudioRecorder;
import com.google.android.gms.common.util.IOUtils;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;


public class RecordFragment extends Fragment {

    private AudioVisualization audioVisualization;
    private Chronometer chronometer;
    AudioRecorder mAudioRecorder;
    AVLoadingIndicatorView avi;
    String filePath;
    /*WaveView waveView;*/
    long count = 0;

    private RecordOnFragmentInteractionListener mListener;

    public RecordFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_record, container, false);

        avi = (AVLoadingIndicatorView) view.findViewById(R.id.aviRecord);


        chronometer = (Chronometer) view.findViewById(R.id.chronometerFragRecord);
        /*waveView = (WaveView) view.findViewById(R.id.waveviewFragRecord);*/

        chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {

                count ++;

                if (count >= 60){
                    onStoptRecording();
                }
            }
        });
        mAudioRecorder = AudioRecorder.getInstance();

        

        filePath = Environment.getExternalStorageDirectory().getAbsolutePath() +
                File.separator + System.nanoTime() + ".m4a";
        File mOutputFile = new File(filePath);
       /* File mOutputFile = new File(Environment.getExternalStorageDirectory() + "/2.file.m4a");*/

        mAudioRecorder.prepareRecord(MediaRecorder.AudioSource.MIC,
                MediaRecorder.OutputFormat.MPEG_4, MediaRecorder.AudioEncoder.AAC,
                mOutputFile);
        try {
            FileInputStream inputStream = new FileInputStream(mOutputFile);
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

            ShortBuffer sb = ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer();
            short[] samples = new short[sb.limit()];
            sb.get(samples);
        } catch (IOException e) {
            e.printStackTrace();
        }


        onStartRecording();


        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.recordOnFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof RecordOnFragmentInteractionListener) {
            mListener = (RecordOnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement RecordOnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface RecordOnFragmentInteractionListener {
        // TODO: Update argument type and name
        void recordOnFragmentInteraction(Uri uri);
    }


    public void onStartRecording(){
        mAudioRecorder.startRecord();
        chronometer.start();
    }
    public String onStoptRecording(){
        mAudioRecorder.stopRecord();
        chronometer.stop();
        avi.getIndicator().stop();
        return filePath;
    }
}
