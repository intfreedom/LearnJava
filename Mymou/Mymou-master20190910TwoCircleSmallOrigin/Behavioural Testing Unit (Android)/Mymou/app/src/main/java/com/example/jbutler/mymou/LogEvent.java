package com.example.jbutler.mymou;

import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Writes linked String into  CURRENT_DATE.txt
 */
//1、通过实现Runnable接口创建线程
//(1).定义一个类实现Runnable接口，重写接口中的run()方法。在run()方法中加入具体的任务代码或处理逻辑。
//(2).创建Runnable接口实现类的对象。
//(3).创建一个Thread类的对象，需要封装前面Runnable接口实现类的对象。（接口可以实现多继承）
//(4).调用Thread对象的start()方法，启动线程
class LogEvent implements Runnable {

    private final String message;

    public LogEvent(String msg) {
        message = msg;
    }

    @Override
    public void run() {
        FolderManager folderManager = new FolderManager();
        File appFolder = folderManager.getFolderName();
        String fileName = new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime());
        fileName = fileName + ".txt";
        File savefile = new File(appFolder, fileName);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(savefile, true);
            PrintWriter printWriter = new PrintWriter(fileOutputStream);
            printWriter.println(message);
            printWriter.flush();
            printWriter.close();
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d("LogEvent","Data logged");
    }

}