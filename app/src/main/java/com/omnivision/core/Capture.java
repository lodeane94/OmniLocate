package com.omnivision.core;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.util.Log;

/**
 * Created by lkelly on 3/7/2017.
 */

public class Capture {
    Context captureContext = null;
    public static Camera camera;
    private int cameraId = 0;
    final static String TAG = Capture.class.getSimpleName();

    public  Capture(Context context){
        captureContext = context;
        // do we have a camera?
        if (!captureContext.getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA)) {

            System.out.println("no camera found on this device");
        } else {
            cameraId = findFrontFacingCamera();
            if (cameraId < 0) {

                System.out.println("no front camera found");
            } else {
                camera = Camera.open(cameraId);
            }
        }
    }

    public void captureNow() {
        System.out.println("capture now entered");
        camera.startPreview();
        camera.takePicture(null, null, new Camera.PictureCallback(){
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                Log.d(TAG,"pic taken");
            }
        }); //new PhotoHandler(captureContext));
    }

    public int findFrontFacingCamera() {
        int cameraId = -1;
        // Search for the front facing camera
        int numberOfCameras = Camera.getNumberOfCameras();
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                Log.d(TAG, "Camera found");
                cameraId = i;
                break;
            }
        }
        return cameraId;
    }
}
