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
    private static String taskId = "001";  // Unique string prefixed to all log entries所有日志条目前缀的唯一字符串

    //Bluetooth variables蓝牙变量；
    public static int monkeyId = -1;
    public static int numPhotos = 0;
    private static int trialCounter = 0;
    public static RewardSystem rewardSystem;

    private static FaceRecog faceRecog;
    private static ArrayList<String> trialData;
    public static String photoTimestamp;
    public static String message;
    private static Handler logHandler;
    //HandlerThread用于启动具有looper的新线程的方便类。然后可以使用looper来创建处理程序类。 请注意，仍然必须调用start()。
    private static HandlerThread logThread;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private static Context mContext;
    private static Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //
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
        //注册电源接收器；
        registerPowerReceivers();
        //初始化Log Handler
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
        //初始化奖励系统；
        initaliseRewardSystem();

    }
    //初始化Log Handler
    private void initialiseLogHandler() {
        logThread = new HandlerThread("LogBackground");
        logThread.start();
        /*Handler(Looper looper)使用提供的{@link Looper}而不是默认值。The looper, must not be null.
        getLooper此方法返回与此线程关联的Looper。 如果此线程未启动或由于任何原因isAlive（）返回false，则此方法将返回null。
        如果此线程已启动，则此方法将阻塞，直到已经初始化了循环器。
        @return looper。*/
        logHandler = new Handler(logThread.getLooper());
    }
    //初始化奖励系统；
    private void initaliseRewardSystem() {
        boolean successfullyEstablished = false;
        //退出蓝牙；
        rewardSystem.quitBt();
        rewardSystem = new RewardSystem(this);
        if (rewardSystem.bluetoothConnection | !MainMenu.useBluetooth) {//java位运算符；|对应位都是0结果为0；
            // true | false结果为true;
            successfullyEstablished = enableApp(true);
        }

        // Repeat if either couldn't connect or couldn't enable app
        //如果无法连接或无法启用应用程序，请重复此操作
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
    //启动任务
    private void startTask() {
        //返回FragmentManager以与与此活动关联的片段进行交互。
        fragmentManager = getFragmentManager();
        /*.beginTransaction()在与此FragmentManager关联的Fragments上启动一系列编辑操作。注意：只能在活动保存其状态之前创建/提交片段事务。
        如果您尝试提交事务在{@link Activity＃onSaveInstanceState Activity.onSaveInstanceState（）}之后（以及之后的
        {@link Activity＃onStart Activity.onStart}或{@link Activity＃onResume Activity.onResume（）}之后，您将收到错误。
        是因为框架负责在状态中保存当前片段，如果在状态保存后进行了更改，那么它们将丢失。*/
        fragmentTransaction = fragmentManager.beginTransaction();

        /*setContentView从布局资源设置活动内容。 资源将膨胀，将所有顶级视图添加到活动中。*/
        setContentView(R.layout.activity_all_tasks);
        CameraMain cM = new CameraMain();
        fragmentTransaction.add(R.id.container, cM);
        fragmentTransaction.add(R.id.container, task);
        fragmentTransaction.commit();
    }
    //
    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        logHandler.post(new CrashReport(throwable));
        rewardSystem.quitBt();
        restartApp();
    }
    //重新启动app
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
    //注册电源接收器；
    private void registerPowerReceivers() {
        //IntentFilter新的IntentFilter匹配单个操作而没有数据。 如果随后未指定任何数据特征，则过滤器将仅匹配不包含数据的意图。
        IntentFilter unplugIntent = new IntentFilter(Intent.ACTION_POWER_DISCONNECTED);
        IntentFilter plugIntent = new IntentFilter(Intent.ACTION_POWER_CONNECTED);
        registerReceiver(powerPlugReceiver, plugIntent);
        registerReceiver(powerUnplugReceiver, unplugIntent);
    }
    /*BroadcastReceiver接收和处理{@link android.content.Context #sendBroadcast（Intent）}发送的广播意图的代码的基类。
    您可以使用{@link Context #registerReceiver Context.registerReceiver（）}动态注册此类的实例。
    或者在<code> AndroidManifest.xml </ code>中使用{@link android.R.styleable #AndroidManifestReceiver＆lt; receiver＆gt;}
    标记静态声明实现。*/
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
                // Do something when power disconnected电源断开时做一些事情???
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
    //初始化屏幕设置，
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
    //启用app
    public static boolean enableApp(boolean bool) {
        if (task.hideApplication != null) {
            if (bool) {
                //setEnabled(boolean enabled)设置此视图的启用状态。启用状态的解释因子类而异。
                task.hideApplication.setEnabled(false);
                //setVisibility设置此视图的可见性状态。
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
    //设置屏幕亮度；
    public static void setBrightness(int brightness) {
        if (Settings.System.canWrite(mContext)) {
            if (brightness > 255) {
                brightness = 150;  //change RewardBigCircle
            } else if (brightness < 0) {
                brightness = 0;
            }
            // ContentResolver 此类为应用程序提供对内容模型的访问权限
            // 返回应用程序包的ContentResolver实例
            ContentResolver cResolver = mContext.getContentResolver();//返回应用程序包的ContentResolver实例
            // SCREEN_BRIGHTNESS屏幕背光亮度介于0和255之间屏幕最大亮度为255，屏幕最低亮度为0。
            Settings.System.putInt(cResolver, Settings.System.SCREEN_BRIGHTNESS, brightness);
        }
    }

    public static void logEvent(String data) {
        String timestamp = new SimpleDateFormat("HHmmss_SSS").format(Calendar.getInstance().getTime());
        String msg = TaskManager.photoTimestamp + "," + timestamp + "," + data;
        trialData.add(msg);
    }
    //重置实验数据；
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
        /*使用给定模式构造<code> SimpleDateFormat </ code>，并使用默认的{@link java.util.Locale.Category #FORMAT FORMAT}
        语言环境的默认日期格式符号。 <b>注意：</ b>此构造函数可能不支持所有语言环境。
        要获得完整的覆盖率，请使用{@link DateFormat}类中的工厂方法。*/
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
    //退出线程
    private void quitThreads() {
        try {
            /*安全地退出处理程序线程的looper,一旦处理了已经到期的消息队列中的所有剩余消息，
            就会导致处理程序线程的循环器终止。将来不会发送到期时间到期的延迟消息。在要求退出looper之后，任何尝试将消息发布到队列都将失败。
            例如，{@link Handler #sendMessage（Message）}方法将返回false。如果线程尚未启动或已完成（即{@link #getLooper}返
            回null），则返回false。 否则，要求looper退出并返回true。如果已经要求looper looper退出，则为True，如果线程尚未开始运行则为false。*/
            logThread.quitSafely();
            //这个线程死了。调用此方法的行为与调用完全相同
            logThread.join();
            logThread = null;
            logHandler = null;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {

        }
    }
    //取消注册接收者
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
