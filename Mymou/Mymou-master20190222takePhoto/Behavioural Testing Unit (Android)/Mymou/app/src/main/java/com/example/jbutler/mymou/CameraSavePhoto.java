package com.example.jbutler.mymou;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.Image;
import android.os.Environment;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Saves linked file into linked filename
 */
//1.定义Runnable接口的实现类，并重写其中的run方法。run()方法的方法体是线程执行体。
//2.创建Runnable接口实现类的实例。sonThread s1 = new SonThread();
//3.用该实例作为Thread的target来创建Thread对象。Thread t1 =new Thread(s1);
//4.调用该对象的start()方法启动线程。t1.start();
class CameraSavePhoto implements Runnable {

    private final Image mImage;
    private String timestamp;
    private final String day;

    public CameraSavePhoto(Image image, String timestampU) {
        Log.d("CameraSavePhotoThree", "CameraSavePhoto instantiated");
        mImage = image;
        timestamp = timestampU;
        day = new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime());

    }

    @Override
    public void run() {
        // Convert photo (176 x 144) to byte array (1x25344) to bitmap (176 x 144)
        //将照片（176 x 144）转换为字节数组（1x25344）到位图（176 x 144）
        ByteBuffer buffer = mImage.getPlanes()[0].getBuffer();
        byte[] bytes = new byte[buffer.remaining()];
        buffer.get(bytes);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

        // Crop bitmap as you want:根据需要裁剪位图
        boolean cropBitmap = false;
        Bitmap bitmapCropped;
        if (cropBitmap) {
            int cropLeft = 28;
            int cropRight = 68;
            int cropTop = 31;
            int cropBottom = 31;
            int startX = cropLeft;
            int startY = cropTop;
            int endX = bitmap.getWidth() - cropLeft - cropRight;
            int endY = bitmap.getHeight() - cropTop - cropBottom;

            bitmapCropped = Bitmap.createBitmap(bitmap, startX, startY, endX, endY);
        } else {
            bitmapCropped = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight());
        }

        // Create integer array for facerecog        为facerecog创建整数数组
        int x = bitmapCropped.getWidth();
        int y = bitmapCropped.getHeight();
        int[] intArray = new int[x * y];
        bitmapCropped.getPixels(intArray, 0, x, 0, 0, x, y);
        for (int i = 0; i < intArray.length; i++) {
            intArray[i] = Color.red(intArray[i]); //Any colour will do as greyscale   任何颜色都可以做灰度
        }

        //Log text data with photoId使用photoId记录文本数据
        TaskManager.setMonkeyId(intArray);

        //Save pixel values保存像素值
        saveIntArray(intArray);

        //Save photo as jpeg将照片另存为jpeg
        savePhoto(bitmapCropped);
    }

    private File getfolder(String suffix) {
        String path= Environment.getExternalStorageDirectory().getAbsolutePath() + "/Mymou/" + day;

        FolderManager folderManager = new FolderManager();
        File appFolder = folderManager.getFolderName();

        path = path + "/" + suffix + "/";
        File file = new File(path);
        Log.d("getfolder","path output: "+path);

        return file;
    }

    private void savePhoto(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] bytes = stream.toByteArray();
        File folder = getfolder("i");
        String fileName = day + "_" + timestamp + ".jpg";
        File filetowrite = new File(folder, fileName);
        FileOutputStream output = null;
        try {
            output = new FileOutputStream(filetowrite);
            output.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            mImage.close();
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void saveIntArray(int[] intArray) {
        File folder = getfolder("f");
        String fileName = "f"+ day + "_" + timestamp + ".txt";
        File savefile = new File(folder, fileName);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(savefile, true);
            PrintWriter printWriter = new PrintWriter(fileOutputStream);
            int n = intArray.length;
            for(int i = 0; i < n; i++) {
                printWriter.println(Double.toString(intArray[i]));
                printWriter.flush();
            }
            printWriter.close();
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

