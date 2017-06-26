package com.omnivision.utilities;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * Created by Lodeane on 24/6/2017.
 */

public class PhotoManager {
    private final String TAG = PhotoManager.class.getSimpleName();
    private File newImage;
    private File cachedImage;

    public PhotoManager(File image){
        Log.d(TAG,"initializing photo handler");
        this.cachedImage = image;
        this.newImage = getDir();
    }

    /**
     * @author lkelly
     * @desc Copies cached image from the image taken by the hidden camera service
     *       to the new created file
     * @params
     * @return
     * */
    public void save()  {
        FileInputStream inStream;
        FileOutputStream outStream;
        try {
            inStream = new FileInputStream(cachedImage);
            outStream = new FileOutputStream(newImage);
            FileChannel inChannel = inStream.getChannel();
            FileChannel outChannel = outStream.getChannel();
            inChannel.transferTo(0, inChannel.size(), outChannel);
            inStream.close();
            outStream.close();
        }catch (IOException ex){
            ex.printStackTrace();
        }
    }

    /**
     * @author lkelly
     * @desc Action the command that was received
     * @params
     * @return newFile to which the cached picture will be stored
     * */
    private File getDir() {
        File sdDir = Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        String newName = String.valueOf(System.currentTimeMillis())
                .substring(1,10) + ".jpg";
        return new File(sdDir, newName);
    }
}
