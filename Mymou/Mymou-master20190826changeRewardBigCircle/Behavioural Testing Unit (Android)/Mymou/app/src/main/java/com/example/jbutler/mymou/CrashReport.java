package com.example.jbutler.mymou;

import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Saves linked String into  CURRENT_DATE.txt
 */
//1、通过实现Runnable接口创建线程
//(1).定义一个类实现Runnable接口，重写接口中的run()方法。在run()方法中加入具体的任务代码或处理逻辑。
//(2).创建Runnable接口实现类的对象。
//(3).创建一个Thread类的对象，需要封装前面Runnable接口实现类的对象。（接口可以实现多继承）
//(4).调用Thread对象的start()方法，启动线程
class CrashReport implements Runnable {

    private final Throwable message;

    public CrashReport(Throwable msg) {
        message = msg;
    }

    @Override
    public void run() {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        message.printStackTrace(pw);
        String exceptionAsString = sw.toString(); // stack trace as a string    将跟踪堆栈作为字符串
        File backupFile;
        File appFolder;
        String folderName = new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime());
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Mymou/" + folderName;
        appFolder = new File(path);
        if (!appFolder.exists())
            appFolder.mkdir();
        String fileName = "crashReport.txt";
        String date = new SimpleDateFormat("yyyyMMdd_HHmmss_SSS").format(Calendar.getInstance().getTime());
        String toLog = date + " " + exceptionAsString;
        backupFile = new File(appFolder, fileName);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(backupFile, true);
            PrintWriter printWriter = new PrintWriter(fileOutputStream);
            printWriter.println(toLog);
            printWriter.flush();
            printWriter.close();
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}