package com.example.jbutler.mymou;
import android.app.Fragment;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
//因为RewardSystem.java中的initialiseRewardChannelString()方法，点击Reward\nChoice #0，输出的是“1”
//因为Arduino中定义的，对应“1”为channelTwo，HIGH
//**********现参考Android Book看懂P147案例；同时弄懂Think In Java中this的含义；

//现在的问题是：可以识别信号，如何直接到做题那一步，不再选择那一只动物，同时把python界面表示在平板上；

import java.util.Random;
//一个基本的对象识别任务，展示了Mymou系统的主要特征
// A basic object discrimination task showcasing the main features of the Mymou system:
//用面部识别传递不同的任务，对于不同的被实验者；
// Uses facial recognition to deliver seperate tasks to two different subjects
//为成功完成试验提供奖励选择
// Offers choice of rewards for successful trial completion
//1. Fragment中onCreate类似于Activity.onCreate，在其中可初始化除了view之外的一切；
public class TaskExample extends Fragment
        implements View.OnClickListener {//当前应用需要监听用户按钮“点击”事件，因此监听器需要实现View.OnClickListener接口；

    // Debug//调试
    private static TextView textView;
    //用于在需要时覆盖/禁用任务（例如，没有蓝牙连接）
    // Used to cover/disable task when required (e.g. no bluetooth connection)
    public View hideApplication;
    //背景颜色
    // Background colours
    private static View backgroundRed, backgroundPink;
    //主题错误选择的超时
    // Timeouts for wrong choices by subject
    private static int timeoutWrongGoCuePressed = 300;  // Timeout for now pressing their own Go cue
    private int timeoutWrongCueChosen = 1500;  // Timeout for getting the task wrong
    //如果主体在试验中途停止，则计时器重置任务
    // Timer to reset task if subject stops halfway through a trial
    //changetask the old values is 10000,now it change to 100000
    private static int maxTrialDuration = 10000;  // Milliseconds until task timeouts and resets毫秒，直到任务超时和重置
    //上次按下的时间 - 如果达到maxTrialDuration，则用于空闲超时
    private static int time = 0;  // Time from last press - used for idle timeout if it reaches maxTrialDuration
    private static boolean timerRunning;  // Signals if timer currently active如果计时器当前有效则发出信号，静态变量默认值为false;
    // 分配给每个主题的唯一编号，用于面部识别
    // Unique numbers assigned to each subject, used for facial recognition
    private static int monkO = 0, monkV = 1;

    // Task objects
    // 任务主题
    private static Button cueGo_O, cueGo_V; //对应Monkey O/V Start，Go cues to start a trial去提示开始实验
    private static Button[] cues_Reward = new Button[4];  // Reward cues for the different reward options
    // 为Subject O， V列出所有的实验对象；
    private static Button[] cues_O = new Button[2];  // List of all trial objects for Subject O
    private static Button[] cues_V = new Button[2];  // List of all trial objects for Subject V

    // Reward奖励；
    //改变rewardAmount=1000,这个量原本为此，但为了改变奖励时间，改为100,这个改为100,按说100ms后，就会停止给水，但未停止，所以加一个timerEnd();//onePicture
    static int rewardAmount = 800;  // Duration (ms) that rewardSystem activated for奖励系统激活的持续时间（ms）//changeReward
    // 可以在屏幕上显示提示的预定位置，
    // Predetermined locations where cues can appear on screen, calculated by calculateCueLocations()
    private static int maxCueLocations = 8;  // Number of possible locations that cues can appear in
    private static int[] xLocs = new int[maxCueLocations];
    private static int[] yLocs = new int[maxCueLocations];

    // Random number generator随机数发生器
    private static Random r = new Random();

    // Boolean to signal if task should be active or not (e.g. overnight it is set to true)
    //布尔值，表示任务是否应该处于活动状态（例如，夜间它被设置为真）
    public static boolean shutdown = false;

    // Aync handlers used to posting delayed task events
    // 异步处理程序用于发布延迟的任务事件
    /*Handler: 默认构造函数为当前线程把此Handler与{@link Looper}相关联。如果此线程没有尺蠖，则此处理程序将无法接收消息，
    因此会引发异常。*/
    private static Handler h0 = new Handler();  // Task timer任务计时器
    private static Handler h1 = new Handler();  // Prepare for new trial准备新的试验
    private static Handler h2 = new Handler();  // Timeout go cues超时去提示
    private static Handler h3 = new Handler();  // changeTask,add h3
    //2. onCreateView是创建该fragment对应的视图，其中需要创建自己的视图并返回给调用者；

    /*onCreateView实例化fragment视图的布局，然后将实例化的View返回给托管activity，LayoutInflater及ViewGroup是实例化布局的必要参数
    * Bundle用来存储恢复数据，可供该方法从保存状态下重建视图；*/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        /*fragment的视图是直接通过调用LayoutInflater.inflate(...)方法传入布局的资源ID生成的(R.layout.activity_task_example)；
        * container是视图的父视图，通常需要父视图来正确配置组件，第三个参数，false表示将以代码的方式添加视图*/
        return inflater.inflate(R.layout.activity_task_example, container, false);
    }
    // activity_task_example.java文件中有Monkey O Start, Monkey V Start, Monkey O Cue1, Monkey O Cue2等按钮；

    /* onViewCreated
    在创建片段的活动并且实例化此片段的视图层次结构时调用。一旦这些部分就位，它可用于进行最终初始化，例如检索观点或恢复状态。
    它对使用的片段也很有用{@link #setRetainInstance（boolean）}保留他们的实例，
    因为这个回调告诉片段何时完全关联 新活动实例。 这是在{@link #onCreateView}之后调用的 在{@link #onViewStateRestored（Bundle）}之前
    @param savedInstanceState如果正在重新创建片段先前保存的状态，这是状态。*/
    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        //引用组件；
        assignObjects();
        //设置监听器，点击行为
        setOnClickListeners();
        // 在屏幕上制作可以放置提示的位置的预定列表
        calculateCueLocations();
        //设置屏幕亮度；
        TaskManager.setBrightness(150);//255是最大亮度； //change RewardBigCircle
        //禁用所有的提示or线索
        disableAllCues();
        //准备新的实验；
        PrepareForNewTrial(0);

        timer();//change aa move

    }

    // 引用组件： findViewById以组件的资源ID为参数，返回一个视图对象，赋值给对应的成员变量；
   /*getView为fragment布局获得 根View*/
    private void assignObjects() {
        backgroundRed = getView().findViewById(R.id.backgroundred);
        backgroundPink = getView().findViewById(R.id.backgroundpink);
        hideApplication = getView().findViewById(R.id.foregroundblack);
        cueGo_O = getView().findViewById(R.id.buttonGoMonkO);//对应Monkey O Start
        cueGo_V = getView().findViewById(R.id.buttonGoMonkV);//对应Monkey V Start
        cues_O[0] = getView().findViewById(R.id.buttonCue1MonkO);//对应Monkey O Cue 1
        cues_O[1] = getView().findViewById(R.id.buttonCue2MonkO);//对应Monkey O Cue 2
        cues_V[0] = getView().findViewById(R.id.buttonCue1MonkV);//对应Monkey V Cue 1
        cues_V[1] = getView().findViewById(R.id.buttonCue2MonkV);//对应Monkey V Cue 2
        //以下为奖励的四个通道，与外部接连无关，只要它返回的信号是 1，2，3， 4， 5， 6?
        cues_Reward[0]  = getView().findViewById(R.id.buttonRewardZero);
        cues_Reward[1]  = getView().findViewById(R.id.buttonRewardOne);
        cues_Reward[2]  = getView().findViewById(R.id.buttonRewardTwo);
        cues_Reward[3]  = getView().findViewById(R.id.buttonRewardThree);
        textView = getView().findViewById(R.id.tvLog);
    }

    // Make a predetermined list of the locations on the screen where cues can be placed
    // 在屏幕上制作可以放置提示的位置的预定列表
    private void calculateCueLocations() {
        int imageWidths = 175 + 175/2;
        int distanceFromCenter = imageWidths + 30; // Buffer between different task objects不同任务对象之间的缓冲区

        // Find centre of screen in pixels以像素为单位查找屏幕中心
        // getActivity()返回此片段当前与之关联的Activity。
        // getWindowManager()检索窗口管理器以显示自定义窗口。
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int screenWidth = size.x;
        int xCenter = screenWidth / 2;
        xCenter -= imageWidths / 2;
        int screenHeight = size.y;
        int yCenter = screenHeight / 2;

        // Y locations
        yLocs[0] = yCenter - distanceFromCenter;
        yLocs[1] = yCenter;
        yLocs[2] = yCenter + distanceFromCenter;
        yLocs[3] = yCenter;
        yLocs[4] = yCenter + distanceFromCenter;
        yLocs[5] = yCenter - distanceFromCenter;
        yLocs[6] = yCenter + distanceFromCenter;
        yLocs[7] = yCenter - distanceFromCenter;

        // X locations  change xLocs[0]和xLocs[2]
//        xLocs[0] = xCenter;  change aa move
        xLocs[0] = xCenter - distanceFromCenter; //change aa move
        xLocs[1] = xCenter - distanceFromCenter;
        xLocs[2] = xCenter - distanceFromCenter;// change aa move
//        xLocs[2] = xCenter; //change aa move
        xLocs[3] = xCenter + distanceFromCenter;
        xLocs[4] = xCenter - distanceFromCenter;
        xLocs[5] = xCenter - distanceFromCenter;
        xLocs[6] = xCenter + distanceFromCenter;
        xLocs[7] = xCenter + distanceFromCenter;

        int distanceC = 50;//change aa move 把重心整体往上移
        int ydistanceC = 200;//change aa move 把重心整体往上移

        int ydistance = 100;//change aa move 两个图标间的距离；
        int yydistance = 350;//change aa move 两个图标间的距离；
        // Y locations//change aa move
        yLocs[0] -= distanceC+yydistance;//change aa move
        yLocs[1] -= ydistanceC;//change aa move
        yLocs[2] += distanceC+ydistance;//change aa move
        yLocs[3] -= ydistanceC;//change aa move
        yLocs[4] += distanceC+ydistance;//change aa move
        yLocs[5] -= distanceC+yydistance;//change aa move
        yLocs[6] += distanceC+ydistance;//change aa move
        yLocs[7] -= distanceC+yydistance;//change aa move

        int xxdistance = 50;//change aa move 两个图标间的距离；
        int xdistance = 0;//change aa move 两个图标间的距离；
        // X locations//change aa move
//        xLocs[0] -= distanceC;//change aa move
        xLocs[0] -= distanceC+xdistance;//change aa move
        xLocs[1] -= distanceC+xdistance;//change aa move
        xLocs[2] -= distanceC+xdistance;//change aa move
//        xLocs[2] -= distanceC;//change aa move
        xLocs[3] -= distanceC+xxdistance;//change aa move
        xLocs[4] -= distanceC+xdistance;//change aa move
        xLocs[5] -= distanceC+xdistance;//change aa move
        xLocs[6] -= distanceC+xxdistance;//change aa move
        xLocs[7] -= distanceC+xxdistance;//change aa move


        // Go cues are static location so place them now去提示是静态位置，所以现在放置它们
        cueGo_O.setX(xLocs[1]);
        cueGo_O.setY(yLocs[1]);
        cueGo_V.setX(xLocs[3]);
        cueGo_V.setY(yLocs[3]);
    }

    private void setOnClickListenerLoop(Button[] buttons) {
        for (int i = 0; i < buttons.length; i++) {
            buttons[i].setOnClickListener(this);
        }
    }
    // 传入setOnClickListener(OnClickListener)方法的参数是一个监听器，它是实现了OnClickListener接口的对象；
    // OnClickListener接口中唯一的方法：onClick(View)，可否以下的方法，单独写出来，有何关联；
    // 这个this指的是？prefer android book page147, may it is right; this = onClick(View view){} below

    //下一个方法就是onClick(View view)

     private void setOnClickListeners() {
         setOnClickListenerLoop(cues_Reward);
         setOnClickListenerLoop(cues_O);// monkey V中有两个，对应Monkey V Cue 1， Monkey V Cue 2
         setOnClickListenerLoop(cues_V);// monkey V中有两个，对应Monkey V Cue 1， Monkey V Cue 2
         cueGo_O.setOnClickListener(this);//对应Monkey O Start
         cueGo_V.setOnClickListener(this);//对应Monkey V Start
    }


    @Override
    public void onClick(View view) {

        // Always disable all cues after a press as monkeys love to bash repeatedly
        //因为猴子喜欢反复点击，所以在按下后总是禁用所有提示
        disableAllCues();
        //重置任务计时器（用于空闲超时并根据需要计算反应时间）
        // Reset task timer (used for idle timeout and calculating reaction times if desired)
        time = 0;
        // 点亮屏幕；
        // Make screen bright
        TaskManager.setBrightness(150);//change RewardBigCircle
        // 现在根据按下的按钮决定做什么
        // Now decide what to do based on what button pressed
        switch (view.getId()) {
            case R.id.buttonGoMonkO:
                checkMonkeyPressedTheirCue(monkO);
                break;
            case R.id.buttonGoMonkV:
                checkMonkeyPressedTheirCue(monkV);
                break;
            case R.id.buttonCue2MonkO:  //change aa move Banana
//                correctOptionChosen();  change aa
                h0.removeCallbacksAndMessages(null);//change TwoCircle change aa move
                h3.removeCallbacksAndMessages(null);//change TwoCircle change aa move
                toggleButton(cues_O[0],false); //onePicture  change aa move
                toggleButton(cues_O[1],false); //onePicture  change aa move
                timerAppear();   //onePicture change aa move
                break;
            case R.id.buttonCue1MonkV:
                incorrectOptionChosen();
                break;
            case R.id.buttonCue1MonkO:  ////change aa move Banana
                deliverReward(0);//onePicture
//                TaskManager.takePhoto();//bigPicture change aa
                h0.removeCallbacksAndMessages(null);//change TwoCircle change aa move
                h3.removeCallbacksAndMessages(null);//change TwoCircle change aa move
                toggleButton(cues_O[0],false); //onePicture  change aa move
                toggleButton(cues_O[1],false); //onePicture  change aa move
                timerAppear();   //onePicture  change aa move
//                timerEnd();//onePicture
                //incorrectOptionChosen();//changetask
                break;
            case R.id.buttonCue2MonkV:
                correctOptionChosen();
                break;
            case R.id.buttonRewardZero:
                deliverReward(0);
                break;
            case R.id.buttonRewardOne:
                deliverReward(1);
                break;
            case R.id.buttonRewardTwo:
                deliverReward(2);
                break;
            case R.id.buttonRewardThree:
                deliverReward(3);
                break;
        }
    }

    //准备新的实验；以下查看Java命名规则；
    private static void PrepareForNewTrial(int delay) {
        //重置实验数据；
        TaskManager.resetTrialData();
        //实际上也就实现了一个(delay)s的一个定时器,按照定时时间调用Runnable对象；

        /* postDelayed: 导致Runnable r被添加到消息队列中，在指定的时间量过去之后运行。runnable将在连接此处理程序的线程上运行。
         * public final boolean postDelayed(Runnable r, long delayMillis){}：r 将执行的Runnable。
         * delayMillis 执行Runnable之前的延迟（以毫秒为单位）时间。
         *
         * 如果Runnable已成功放入消息队列，则返回true。 失败时返回false，通常是因为处理消息队列的looper正在退出。
         * 请注意，结果为true并不意味着将处理Runnable  - 如果在消息发送时间之前退出looper，则消息将被丢弃。*/
        h1.postDelayed(new Runnable() {
            @Override
            public void run() {
//                randomiseCueLocations(); //changeDon'tMove note
//                fixedCueLocations();//changeDon'tMove  change aa move
                toggleBackground(backgroundRed, false);
                toggleBackground(backgroundPink, false);
                //// Lots of toggles for task objects大量的任务对象切换
//                toggleGoCues(true);  //onePicture
//                textView.setText("Initiation Stage");  //onePicture
//                toggleButton(cues_O[0],true); //onePicture  change aa move
//                toggleButton(cues_O[1],true); //onePicture  change aa move
            }
        }, delay);
    }

    // Each monkey has it's own start cue. At start of each trial make sure the monkey pressed it's own cue using
    // the facial recognition
    //每只猴子都有它自己的开始提示。 在每次试验开始时，确保猴子使用面部识别按下它自己的提示
    private static void checkMonkeyPressedTheirCue(int monkId) {
        boolean correctCuePressed = TaskManager.checkMonkey(monkId);
        if (correctCuePressed) {  // If they clicked their specific cue如果他们点击了他们的特定提示
            startTrial(monkId);
        } else {
            TimeoutGoCues();
        }
    }

    // Wrong Go cue selected so give short timeout选择了错误的提示，以便短暂超时
    private static void TimeoutGoCues() {
        toggleBackground(backgroundRed, true);
        /* postDelayed: 导致Runnable r被添加到消息队列中，在指定的时间量过去之后运行。runnable将在连接此处理程序的线程上运行。
        * public final boolean postDelayed(Runnable r, long delayMillis){}：r 将执行的Runnable。
        * delayMillis 执行Runnable之前的延迟（以毫秒为单位）时间。
        *
        * 如果Runnable已成功放入消息队列，则返回true。 失败时返回false，通常是因为处理消息队列的looper正在退出。
        * 请注意，结果为true并不意味着将处理Runnable  - 如果在消息发送时间之前退出looper，则消息将被丢弃。*/
        h2.postDelayed(new Runnable() {
            @Override
            public void run() {
                toggleGoCues(true);
                toggleBackground(backgroundRed, false);
            }
        }, timeoutWrongGoCuePressed);//300毫秒；
    }

    private static void startTrial(int monkId) {
        logEvent("Trial started for monkey "+monkId);

        if(!timerRunning) {
            timer();//timer函数递归到最后为true；结束if语句；
        }

        toggleTaskCues(monkId, true);
    }

    private void incorrectOptionChosen() {
        logEvent("Error stage: Incorrect cue chosen");
        toggleBackground(backgroundRed, true);
        endOfTrial(0, timeoutWrongCueChosen);
    }
    //正确的选项选择；
    private void correctOptionChosen() {
        logEvent("Reward stage: Correct cue chosen");
        toggleBackground(backgroundPink, true);
        toggleButtonList(cues_Reward, true);
    }
    //传递奖励；
    private void deliverReward(int juiceChoice) {
        logEvent("Delivering "+rewardAmount+"ms reward on channel "+juiceChoice);
        TaskManager.deliverReward(juiceChoice, rewardAmount);
        endOfTrial(1, rewardAmount + 500);//暂时取消，不知道会有何影响  change aa BananaReward
    }

    private void deliverRewardEnd(int juiceChoice){
        logEvent("this cannel, no juice");//bigpicture
        TaskManager.deliverReward(juiceChoice,rewardAmount);
        endOfTrial(1,rewardAmount + 500);//暂时取消，不知道会有何影响  change aa
    }

    private static void endOfTrial(int outcome, int newTrialDelay) {
        TaskManager.commitTrialData(outcome);
//        PrepareForNewTrial(newTrialDelay);//onePicture
    }

    // This is just needed to show user on screen what is happening during the task
    // Normally just use TaskManager.logEvent()
    // 只需要在屏幕上向用户显示任务期间发生的事情
    // 通常只使用TaskManager.logEvent()
    private static void logEvent(String log) {
        TaskManager.logEvent(log);
        textView.setText(log);
    }
    //禁用所有的提示，or 线索
    private static void disableAllCues() {
        toggleGoCues(false);
        toggleTaskCues(-1, false);  // monkId not needed when switching off关闭时不需要monkId
        toggleButtonList(cues_Reward, false);
    }

    // Lots of toggles for task objects大量的任务对象切换
    private static void toggleGoCues(boolean status) {
        toggleButton(cueGo_O, status);
        toggleButton(cueGo_V, status);
    }

    private static void toggleButtonList(Button[] buttons, boolean status) {
        for (int i = 0; i < buttons.length; i++) {
            toggleButton(buttons[i], status);
        }
    }

    private static void toggleTaskCues(int monkId, boolean status) {
        if (status) {
            if (monkId == monkO) {
                toggleButtonList(cues_O, status);
            } else {
                toggleButtonList(cues_V, status);
            }
        } else {
            // If switching off, just always switch off all cues如果关闭，只需关闭所有提示
            toggleButtonList(cues_O, status);
            toggleButtonList(cues_V, status);
        }
    }
    //toggle Button切换按钮，setVisibility()设置此视图的可见性状态；
    private static void toggleButton(Button button, boolean status) {
        if (status) {
            //setVisibility()设置此视图的可见性状态
            button.setVisibility(View.VISIBLE);
        } else {
            button.setVisibility(View.INVISIBLE);
        }
        button.setEnabled(status);
        /*setClickable: 启用或禁用此视图的单击事件。 当一个视图是可点击的，它会在每次点击时将其状态更改为“按下”。
        子类应设置可点击的视图以直观地响应用户的点击。@param clickable为true以使视图可点击，否则为false
        @see #isClickable（）  @attr ref android.R.styleable #View_clickable*/
        button.setClickable(status);
    }
    //toggle Background切换背景；
    private static void toggleBackground(View view, boolean status) {
        if (status) {
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.INVISIBLE);
        }
        view.setEnabled(status);
    }

    // Utility functions实用功能，randomise No Replacement随机无替换
    //random.nextInt生成随机数；
    private static void randomiseNoReplacement(Button[] buttons) {

        int[] chosen = new int[maxCueLocations];
        for (int i = 0; i < maxCueLocations; i++) {
            chosen[i] = 0;
        }
        int choice = r.nextInt(maxCueLocations);
        for (int i = 0; i < buttons.length; i++) {
            while (chosen[choice] == 1) {//保证两个按钮的位置不重叠；
                choice = r.nextInt(maxCueLocations);
            }
            buttons[i].setX(xLocs[choice]);
            buttons[i].setY(yLocs[choice]);
            chosen[choice] = 1;
        }
    }

    //Fixed position
    private static void fixedCueLocations(){

        cues_O[0].setX(xLocs[0]-180);//befere 350   change TwoCircle  change aa
        cues_O[0].setY(yLocs[1]-800);//before 350 600 change TwoCircle  change aa
        cues_O[1].setX(xLocs[0]);//befere 350  when circle is 700,the two value is 400,550  change aa
        cues_O[1].setY(yLocs[1]-80);//before 350 600    this is apple change aa

    }

    //随机提示位置
    private static void randomiseCueLocations() {
        // Place all trial objects in random locations将所有试用对象放在随机位置
        //randomise No Replacement随机无替换
        //randomiseNoReplacement(cues_Reward);
        randomiseNoReplacement(cues_O);
        //randomiseNoReplacement(cues_V);
    }
    // 用于跟踪任务时间的递归函数
    // Recursive function to track task time
    //当点入Monkey V Cue1或进入奖励界面，如果10s不点击屏幕就返回initiation stage Monkey O Start/Monkey V Start界面；
//onePicture
    private void timerEnd(){
        h3.postDelayed(new Runnable(){
            @Override
            public void run(){
                deliverRewardEnd(0);//onePicture here
                textView.setText("");
            }
        },3000);
    }

//    private static void deliverRewardChanel(int juiceChoice) {
//        logEvent("Stop* Delivering "+rewardAmount+"ms reward on channel "+juiceChoice);
//        TaskManager.deliverReward(juiceChoice, rewardAmount);
//        endOfTrial(1, rewardAmount + 500);
//    }

    private static void timer() {
        h0.postDelayed(new Runnable() {
            @Override
            public void run() {
//                randomiseCueLocations();//change aa move
//                fixedCueLocations();//changeDon'tMove  change aa move
//                randomiseNoReplacement(cues_O);//changetask-onePicture //changeDon'tMove note
                toggleButton(cues_O[0],false); //onePicture change aa  move
                toggleButton(cues_O[1],false); //onePicture  change aa move
                textView.setText("  ");
                timerAppear();// change aa move
            }
        }, 2000);//changetask  changeReward  change aa
    }

    private static void timerAppear() {
        h3.postDelayed(new Runnable() {
            @Override
            public void run() {
                randomiseCueLocations();//change aa move
//                fixedCueLocations();//changeDon'tMove
//                randomiseNoReplacement(cues_O);//changetask-onePicture //changeDon'tMove note
                toggleButton(cues_O[0],true); //onePicture TwoCircle  change aa move
                toggleButton(cues_O[1],true); //onePicture TwoCircle
                textView.setText("  ");
                timer();//TwoCircle
            }
        }, 2000);//changetask  changeReward
    }

    private static void timer1() {
        /* postDelayed: 导致Runnable r被添加到消息队列中，在指定的时间量过去之后运行。runnable将在连接此处理程序的线程上运行。
         * public final boolean postDelayed(Runnable r, long delayMillis){}：r 将执行的Runnable。
         * delayMillis 执行Runnable之前的延迟（以毫秒为单位）时间。
         *
         * 如果Runnable已成功放入消息队列，则返回true。 失败时返回false，通常是因为处理消息队列的looper正在退出。
         * 请注意，结果为true并不意味着将处理Runnable  - 如果在消息发送时间之前退出looper，则消息将被丢弃。*/
        h0.postDelayed(new Runnable() {
            @Override
            public void run() {
                time += 10000;//单位为mm，//onePicture 1000 to 10000
                if (time < maxTrialDuration) {//onePicture > to <
                    disableAllCues();
                    endOfTrial(7, 0);

                    //Decrease brightness while not in use不使用时降低亮度；
                    TaskManager.setBrightness(50);

                    time = 0;
                    timerRunning = false;
                } else {
//                    randomiseNoReplacement(cues_O);//changetask-onePicture
                    timer1();
                    timerRunning = true;
                }
            }
        }, 800);//changetask
    }

    private void cancelHandlers() {
        //删除任何待处理的回调帖子并发送消息 <var> obj </ var>是<var> token </ var>。 如果<var> token </ var>为null，
        //所有回调和消息都将被删除。
        h0.removeCallbacksAndMessages(null);
        h1.removeCallbacksAndMessages(null);
        h2.removeCallbacksAndMessages(null);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        cancelHandlers();
    }

}
