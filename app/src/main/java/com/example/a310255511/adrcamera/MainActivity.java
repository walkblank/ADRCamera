package com.example.a310255511.adrcamera;

import android.content.res.Configuration;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.util.List;


public class MainActivity extends AppCompatActivity implements SurfaceHolder.Callback{

    private static final String TAG =  "ADRCam";

    private SurfaceView mSurfaceView = null;
    private SurfaceHolder mSurfaceHolder = null;
    private VideoView mVideoView = null;
    private Camera mCamera = null;
//    private EditText mEditText = null;
    private TextView mTextView = null;
    private long time_start, time_now;
    private long frame_count = 0;
    private long fps = 0;
//    private JustClass jstClass = null;


    private int mPreviewHeight, mPreviewWidth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
//        initSurfaceView();
        initVideoView();

        JustClass.CallMyName();
    }

    public  void BtnsOnClick(View view) {
        switch (view.getId()) {
            case R.id.connBtn:
                Toast.makeText(MainActivity.this, "Clicked conn", Toast.LENGTH_SHORT).show();
                break;
            case R.id.playBtn:
                Toast.makeText(MainActivity.this, "Click play", Toast.LENGTH_SHORT).show();
                break;
        }
    }
    private void initVideoView() {
//        String rtspUrl = "rtsp://192.168.1.254/xxx.mp4";
        String rtspUrl = "http://techslides.com/demos/sample-videos/small.mp4";
        mVideoView =  this.findViewById(R.id.videoView);
//        mVideoView.setVideoURI(Uri.parse(rtspUrl));
        mVideoView.setVideoPath(rtspUrl);
        mVideoView.requestFocus();
        mVideoView.start();
    }


    private void initSurfaceView () {
        mSurfaceView = (SurfaceView) this.findViewById(R.id.surfaceView);
        mTextView = this.findViewById(R.id.textView);
        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.addCallback(this);
    }

    private void initCamera() {
        Log.i(TAG, "going into camera init");
        if(mCamera != null) {
            //camera service settings
            Camera.Parameters parameters = mCamera.getParameters();
//          parameters.setPreviewFormat(PixelFormat.RGB_888);
            List<Camera.Size> pictureSizes = mCamera.getParameters().getSupportedPictureSizes();
            List<Camera.Size> previewSizes = mCamera.getParameters().getSupportedPreviewSizes();
            List<Integer> previewFormats = mCamera.getParameters().getSupportedPreviewFormats();
            List<Integer> previewFramerates = mCamera.getParameters().getSupportedPreviewFrameRates();

            Log.i(TAG+"initCamera", "cyy support parameters is ");
            Camera.Size psize = null;
            for (int i = 0; i < pictureSizes.size(); i++)
            {
                psize = pictureSizes.get(i);
                Log.i(TAG+"initCamera", "PictrueSize,width: " + psize.width + " height" + psize.height);
            }
            for (int i = 0; i < previewSizes.size(); i++)
            {
                psize = previewSizes.get(i);
                Log.i(TAG+"initCamera", "PreviewSize,width: " + psize.width + " height" + psize.height);
            }
            Integer pf = null;
            for (int i = 0; i < previewFormats.size(); i++)
            {
                pf = previewFormats.get(i);
                Log.i(TAG+"initCamera", "previewformates:" + pf);
            }

            if (this.getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE)
            {
                parameters.set("orientation", "portrait"); //
                parameters.set("rotation", 90); // 镜头角度转90度（默认摄像头是横拍）
                mCamera.setDisplayOrientation(90); // 在2.2以上可以使用
            } else// 如果是横屏
            {
                parameters.set("orientation", "landscape"); //
                mCamera.setDisplayOrientation(0); // 在2.2以上可以使用
            }


            parameters.setPictureSize(1280, 720);
            parameters.setPreviewSize(1280, 720);

            parameters.setPreviewFrameRate(30);
            mCamera.setParameters(parameters);
            time_start = System.currentTimeMillis();
            Log.i(TAG, "start time:"+Long.toString(time_start));
            mCamera.setPreviewCallback(callback);

            mCamera.startPreview();





        }
    }

    private Camera.PreviewCallback callback = new Camera.PreviewCallback() {

        @Override
        public void onPreviewFrame(byte[] bytes, Camera camera) {

            frame_count+=1;
            time_now = System.currentTimeMillis();
            Log.i(TAG, "onPreviewFrame");
            Log.i(TAG, "start time:"+Long.toString(time_now));
            fps = frame_count /(((time_now - time_start)/1000)+1);
            Log.i(TAG, "frame_count : "+ Long.toString(frame_count)+", fps :"+ Long.toString(fps));

            mTextView.setText(Long.toString(fps));

        }
    };

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
        mPreviewHeight = height;
        mPreviewWidth = width;
        initCamera();

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.i(TAG, "SurfaceHolder.Callback：Surface Destroyed");
        if(null != mCamera)
        {
            mCamera.setPreviewCallback(null); //！！这个必须在前，不然退出出错
            mCamera.stopPreview();
//            bIfPreview = false;
            mCamera.release();
            mCamera = null;
        }
    }



}
