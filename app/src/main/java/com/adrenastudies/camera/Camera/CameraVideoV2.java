package com.adrenastudies.camera.Camera;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import com.adrenastudies.camera.R;

public class CameraVideoV2 extends CameraVideoFragment {

    AutoFitTextureView mTextureView;
    ImageView mRecordVideo;
    VideoView mVideoView;
    ImageView mPlayVideo;
    private String mOutputFilePath;

    // Required empty public constructor
    public CameraVideoV2() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_video_v2, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        // TODO Puede elegir entre cualquiera de las 3 resoluciones declaradas
        initCamera(qualityHIGH);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mPlayVideo   = view.findViewById(R.id.mPlayVideo);
        mRecordVideo = view.findViewById(R.id.mRecordVideo);
        mVideoView   = view.findViewById(R.id.mVideoView);
        mTextureView = view.findViewById(R.id.mTextureView);

        mRecordVideo.setOnClickListener(v -> {

            if (mIsRecordingVideo) {
                try {
                    stopRecordingVideo();
                    prepareViews();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                startRecordingVideo();
                mRecordVideo.setImageResource(R.drawable.ic_btn_photo_red);
                //Receive out put file here
                mOutputFilePath = getCurrentFile().getAbsolutePath();
                Log.e("outp", mOutputFilePath);
            }
        });

        mPlayVideo.setOnClickListener(v -> {
            // File videoFile =  FileUtils.Companion.getInstance().buildVideoFile("1233", "456");
            mVideoView.start();
            mPlayVideo.setVisibility(View.GONE);
        });
    }

    @Override
    public int getTextureResource() {
        return R.id.mTextureView;
    }

    private void prepareViews() {
        if (mVideoView.getVisibility() == View.GONE) {
            mVideoView.setVisibility(View.VISIBLE);
            mPlayVideo.setVisibility(View.VISIBLE);
            mTextureView.setVisibility(View.GONE);
            setMediaForRecordVideo();
        }
    }
    private void setMediaForRecordVideo() {
        // Set media controller
        mVideoView.setMediaController(new MediaController(getActivity()));
        mVideoView.requestFocus();
        mVideoView.setVideoPath(mOutputFilePath);
        mVideoView.seekTo(100);
        mVideoView.setOnCompletionListener(mp -> {
            // Reset player
            mVideoView.setVisibility(View.GONE);
            mTextureView.setVisibility(View.VISIBLE);
            mPlayVideo.setVisibility(View.GONE);
            mRecordVideo.setImageResource(R.drawable.ic_play);
        });
    }

}