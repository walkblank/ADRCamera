package com.example.a310255511.adrcamera;

import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


public class MainActivity extends AppCompatActivity implements SurfaceHolder.Callback{

    private static final String TAG =  "ADRCam";

    private SurfaceView mSurfaceView = null;
    private SurfaceHolder mSurfaceHolder = null;
    private Camera mCamera = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initSurfaceView();
    }

     private void initSurfaceView () {
        mSurfaceView = (SurfaceView) this.findViewById(R.id.surfaceView);
        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.addCallback(this);
    }

    private void initCamera() {
        Log.i(TAG, "going into camera init");
       if(mCamera != null) {
           //camera service settings
          Camera.Parameters parameters = mCamera.getParameters();
          parameters.setPreviewFormat(PixelFormat.);
       }
    }

    //surfaceHolder.callback function
    @Override
    public void surfaceCreated(SurfaceHolder holder){
        // called when SurfaceView initialization
        mCamera = Camera.open();
        try {
            Log.i(TAG, "SurfaceHolder.Callback : surface create\n");
            mCamera.setPreviewDisplay(mSurfaceHolder);
        } catch (Exception ex){
            if (null != mCamera)
            {
                mCamera.release();
                mCamera = null;
            }
            Log.i(TAG+ "init camera", ex.getMessage());
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.i(TAG, "Surface holder callback: surface changed\n");
        initCamera();

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
    }



}
