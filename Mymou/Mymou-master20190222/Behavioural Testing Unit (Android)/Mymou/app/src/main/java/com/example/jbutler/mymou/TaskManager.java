package com.example.jbutler.mymou;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.content.*;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
//1.针对如下类型的异常，调用某些方法时，api也提示了可能会抛出某些异常，但该类异常不是一定要捕获的（不捕获编译也能通过），如下
//int i = Integer.parseInt("ff");//该方法的完整声明public static int parseInt(String s) throws NumberFormatException
//不进行try catch也一样能运行，但运行时如果出错，只会在控制台打印下，后面再发现程序有问题很难查找。
//其实Java已经提供了对上述第二类异常问题的处理方法，那就是为Thread设置UncaughtExceptionHandler,
//2.多线程环境下，无法使用try_catch捕获异常，需要设置UncaughtExceptionHandler
//3.当某一线程因未捕获的异常而即将终止时，Java 虚拟机将使用 Thread.getUncaughtExceptionHandler()
//查询该线程以获得其 UncaughtExceptionHandler 的线程，
//并调用处理程序的 uncaughtException 方法，将线程和异常作为参数传递。

public class TaskManager extends Activity implements Thread.UncaughtExceptionHandler {

    //Task you want to run goes here你想要运行的任务就在这里
    private static TaskExample task = new TaskExample();
    //private static TaskFromPaper task = new TaskFromPaper();
    private static String taskId = "001";  // Unique string prefixed to all log entries

    //Bluetooth variables
    public static int monkeyId = -1;
    public static int numPhotos = 0;
    private static int trialCounter = 0;
    public static RewardSystem rewardSystem;

    private static FaceRecog faceRecog;
    private static ArrayList<String> trialData;
    public static String photoTimestamp;
    public static String message;
    private static Handler logHandler;
    private static HandlerThread logThread;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private static Context mContext;
    private static Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = getApplicationContext();
        activity = (Activity) this;
        //初始化屏幕设置
        initialiseScreenSetttings();

        if (MainMenu.useFaceRecog) {
            //从主线程加载facerecog需要一段时间
            // Load facerecog off the main thread as takes a while
            Thread t = new Thread(new Runnable() {
                public void run() {
                    faceRecog = new FaceRecog();
                }
            });
            t.start();
        }

        registerPowerReceivers();

        initialiseLogHandler();

        this.startLockTask();

//            // Enable this if you want task to automatically restart on crash
//            Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
//                @Override
//                public void uncaughtException(Thread thread, Throwable throwable) {
//                    logHandler.post(new CrashReport(throwable));
//                    quitBt();
//                    restartApp();
//                }
//            });

        startTask();
        //这是最后一次，因为它与任务中的对象进行交互
        // This is last as it interacts with objects in the task
        initaliseRewardSystem();

    }

    private void initialiseLogHandler() {
        logThread = new HandlerThread("LogBackground");
        logThread.start();
        logHandler = new Handler(logThread.getLooper());
    }

    private void initaliseRewardSystem() {
        boolean successfullyEstablished = false;
        rewardSystem.quitBt();
        rewardSystem = new RewardSystem(this);
        if (rewardSystem.bluetoothConnection | !MainMenu.useBluetooth) {
            successfullyEstablished = enableApp(true);
        }

        // Repeat if either couldn't connect or couldn't enable app
        if (!successfullyEstablished){
            Handler handlerOne = new Handler();
            handlerOne.postDelayed(new Runnable() {
                @Override
                public void run() {
                    initaliseRewardSystem();
                }
            }, 5000);
        }
    }

    private void startTask() {
        fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        setContentView(R.layout.activity_all_tasks);
        CameraMain cM = new CameraMain();
        fragmentTransaction.add(R.id.container, cM);
        fragmentTransaction.add(R.id.container, task);
        fragmentTransaction.commit();
    }

    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        logHandler.post(new CrashReport(throwable));
        rewardSystem.quitBt();
        restartApp();
    }

    private void restartApp() {
        Intent intent=new Intent(getApplicationContext(), TaskManager.class);
        intent.putExtra("restart",true);
        final PendingIntent pendingIntent = PendingIntent.getActivity(
                getApplicationContext(),
                0, intent, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager mgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 1000, pendingIntent);
        System.exit(2);
    }


    public static void deliverReward(int juiceChoice, int rewardAmount) {
        rewardSystem.activateChannel(juiceChoice, rewardAmount);
    }

    private void registerPowerReceivers() {
        IntentFilter unplugIntent = new IntentFilter(Intent.ACTION_POWER_DISCONNECTED);
        IntentFilter plugIntent = new IntentFilter(Intent.ACTION_POWER_CONNECTED);
        registerReceiver(powerPlugReceiver, plugIntent);
        registerReceiver(powerUnplugReceiver, unplugIntent);
    }

    private final BroadcastReceiver powerPlugReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action.equals(Intent.ACTION_POWER_CONNECTED)) {
                // Do something when power connected
                //task.enableApp();
            }
        }
    };

    private final BroadcastReceiver powerUnplugReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action.equals(Intent.ACTION_POWER_DISCONNECTED)) {
                // Do something when power disconnected
                //task.hideApplication();
            }
        }
    };


    public static void setMonkeyId(int[] intArray) {
        if (faceRecog != null) {
            monkeyId = faceRecog.idImage(intArray);
        }
        numPhotos += 1;
    }

    public static void commitTrialData(int overallTrialOutcome) {
        if (dateHasChanged()) {
            trialCounter = 0;
        } else {
            trialCounter++;
        }
        if (trialData != null) {
            int length = trialData.size();
            for (int i = 0; i < length; i++) {
                String s = trialData.get(i);
                // Prefix variables that were constant throughout trial (trial result, which monkey, etc)
                s = taskId +"," + trialCounter +"," + monkeyId + "," + overallTrialOutcome + "," + s;
                logHandler.post(new LogEvent(s));
                Log.d("log", s);
            }
        }
    }

    private void initialiseScreenSetttings() {
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        final View decorView = TaskManager.this.getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    public static void shutdownLoop() {
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR);
        int AMPM = c.get(Calendar.AM_PM);
        if (hour >= 7 && AMPM == Calendar.PM) {
            enableApp(false);
            boolean restartNextDay = true;
            if(restartNextDay) {
                int day = c.get(Calendar.DAY_OF_WEEK);
                if (day == Calendar.THURSDAY | day == Calendar.FRIDAY | day == Calendar.SATURDAY) {
                    startupLoop();
                }
            }
        } else {
            Handler handlerOne = new Handler();
            handlerOne.postDelayed(new Runnable() {
                @Override
                public void run() {
                    shutdownLoop();
                }
            }, 60000);
        }
    }

    private static void startupLoop() {
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR);
        int AMPM = c.get(Calendar.AM_PM);
        if (hour >= 7 && AMPM == Calendar.AM) {
                enableApp(true);
                shutdownLoop();
        } else {
            Handler handlerOne = new Handler();
            handlerOne.postDelayed(new Runnable() {
                @Override
                public void run() {
                    startupLoop();
                }
            }, 60000);
        }
    }

    public static boolean enableApp(boolean bool) {
        if (task.hideApplication != null) {
            if (bool) {
                task.hideApplication.setEnabled(false);
                task.hideApplication.setVisibility(View.INVISIBLE);
            } else {
                task.hideApplication.setEnabled(true);
                task.hideApplication.setVisibility(View.VISIBLE);
                setBrightness(1);
            }
            return true;
        } else {
            Log.d("TaskManager", "hideApplication object not instantiated");
            return false;
        }
    }

    public static void setBrightness(int brightness) {
        if (Settings.System.canWrite(mContext)) {
            if (brightness > 255) {
                brightness = 255;
            } else if (brightness < 0) {
                brightness = 0;
            }
            // ContentResolver 此类为应用程序提供对内容模型的访问权限
            // 返回应用程序包的ContentResolver实例
            ContentResolver cResolver = mContext.getContentResolver();
            // SCREEN_BRIGHTNESS屏幕背光亮度介于0和255之间
            Settings.System.putInt(cResolver, Settings.System.SCREEN_BRIGHTNESS, brightness);
        }
    }

    public static void logEvent(String data) {
        String timestamp = new SimpleDateFormat("HHmmss_SSS").format(Calendar.getInstance().getTime());
        String msg = TaskManager.photoTimestamp + "," + timestamp + "," + data;
        trialData.add(msg);
    }

    public static void resetTrialData() {
        trialData = new ArrayList<String>();
    }

    // Takes selfie and checks to see if it matches with which monkey it should be
    //采取自拍和检查，看它是否与应该是哪只猴子相匹配
    public static boolean checkMonkey(int monkId) {
        if (!MainMenu.useFaceRecog) {
            // If face recog disabled just take a photo and return
            //如果面部识别被禁用，仅拍照并返回；
            takePhoto();
            return true;
        } else {
            // Lock main thread and wait until background thread takes photo and finishes face recog
            // 锁定主线程并等待后台线程拍照并完成面部识别
            int currentnumphotos = numPhotos;
            Log.d("MonkeyId", "Starting face recognition ");
            takePhoto();
            while (currentnumphotos == numPhotos) { }
            Log.d("MonkeyId", "End face recognition (value: " + monkeyId + ")");
            if (monkeyId == monkId) {  // If they clicked correct button
                return true;
            } else {
                return false;
            }
        }
    }


    public static void takePhoto() {
        photoTimestamp = new SimpleDateFormat("HHmmss_SSS").format(Calendar.getInstance().getTime());
        CameraMain.timestamp = photoTimestamp;
        CameraMain.captureStillPicture();
    }

    //Checks if todays date is the same as the last time function was called
    public static boolean dateHasChanged() {
        String todaysDate = new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime());
        SharedPreferences sharedPref = activity.getPreferences(mContext.MODE_PRIVATE);
        String lastRecordedDate = sharedPref.getString("lastdate", "null");
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("lastdate", todaysDate);
        editor.commit();
        if (todaysDate.equals(lastRecordedDate)) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("stop","stopped");
        rewardSystem.quitBt();
        unregisterReceivers();
        quitThreads();
        this.stopLockTask();
    }

    private void quitThreads() {
        try {
            logThread.quitSafely();
            logThread.join();
            logThread = null;
            logHandler = null;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {

        }
    }

    private void unregisterReceivers() {
        try {
            unregisterReceiver(powerPlugReceiver);
            unregisterReceiver(powerUnplugReceiver);
        } catch(IllegalArgumentException e) {
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }

    @Override
    public boolean onPrepareOptionsMenu (Menu menu) {
        return false;
    }

}
