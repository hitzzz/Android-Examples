package com.example.hitesh.cameraapplication;

/**
 * Created by hitesh on 11/21/15.
 */

import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;



import android.app.Activity;
import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

/**
 * Created by ajoy3 on 11/19/2015.
 */
public class Preview extends SurfaceView implements SurfaceHolder.Callback {
    private SurfaceHolder mHolder;
    private static Camera mCamera;

    public Preview(Context context, Camera camera) {
        super(context);
        mCamera = camera;

        /*Camera.CameraInfo camInfo = new Camera.CameraInfo();
        int cameraRotationOffset = camInfo.orientation;
        Log.i("RotOffset",Integer.toString(cameraRotationOffset));
        int rotation = ((Activity)context).getWindowManager().getDefaultDisplay().getRotation();
        Log.i("Rot",Integer.toString(rotation));
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break; // Natural orientation
            case Surface.ROTATION_90:
                degrees = 90;
                break; // Landscape left
            case Surface.ROTATION_180:
                degrees = 180;
                break;// Upside down
            case Surface.ROTATION_270:
                degrees = 270;
                break;// Landscape right
        }*/
        int displayRotation;
        displayRotation = 90;
        Log.i("displayRot", Integer.toString(displayRotation));
        mCamera.setDisplayOrientation(displayRotation);
        /*Camera.Parameters parameters = camera.getParameters();
        int rotate;
        rotate = (360 + cameraRotationOffset - degrees) % 360;
        parameters.setRotation(rotate);
        camera.setParameters(parameters);*/
        mHolder = getHolder();
        mHolder.addCallback(this);
    }

    public void surfaceCreated(SurfaceHolder holder) {
        try {
            // create the surface and start camera preview
            if (mCamera == null) {
                mCamera.setPreviewDisplay(holder);
                mCamera.startPreview();
            }
        } catch (IOException e) {
            Log.d("Exception1", "Error setting camera preview: " + e.getMessage());
        }
    }

    public void refreshCamera(Camera camera) {
        if (mHolder.getSurface() == null) {
            // preview surface does not exist
            return;
        }
        // stop preview before making changes
        try {
            mCamera.stopPreview();
        } catch (Exception e) {
            // ignore: tried to stop a non-existent preview
        }
        // set preview size and make any resize, rotate or
        // reformatting changes here
        // start preview with new settings
        setCamera(camera);
        try {
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();
        } catch (Exception e) {
            Log.d("Exception1", "Error starting camera preview: " + e.getMessage());
        }
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        // If your preview can change or rotate, take care of those events here.
        // Make sure to stop the preview before resizing or reformatting it.
        refreshCamera(mCamera);
    }

    public void setCamera(Camera camera) {
        //method to set a camera instance
        mCamera = camera;
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // TODO Auto-generated method stub
        // mCamera.release();

    }
}

