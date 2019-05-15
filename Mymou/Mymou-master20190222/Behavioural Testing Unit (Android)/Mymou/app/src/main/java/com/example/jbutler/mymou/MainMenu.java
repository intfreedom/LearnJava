package com.example.jbutler.mymou;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MainMenu extends Activity  {

    // If true this automatically starts the task upon application startup
    //如果为true，则会在应用程序启动时自动启动任务
    // Speeds up debugging/testing
    public static final boolean testingMode = true;
    //可以在这里禁用蓝牙和RewardSystem连接
    // Can disable bluetooth and RewardSystem connectivity here
    //public static final boolean useBluetooth = false;//原本值；
    //修改之后界面上显示可以连接蓝牙，但是还是无法控制给水；
    public static final boolean useBluetooth = true;//为了连接，修改为true；

    //Can disable facial recognition here
    //要使用faceRecog必须具有Mymou文件夹中存在的ANN（wo.txt，wi.txt，meanAndVar.txt）的权重
    //这个图像识别程序是怎样的，是mymou先收集照片，然后再Artificial Neural Network (Python)中进行训练，
    //然后得到的数据，分别保存为meanAndVar.txt，wi.txt - 输入隐藏层的权重，wo.txt - 隐藏输出层的权重，以供mymou使用；
    //To use faceRecog must have the weights for the ANN (wo.txt, wi.txt, meanAndVar.txt) present in the Mymou folder
    public static final boolean useFaceRecog = false;

    public static RewardSystem rewardSystem;

    //Permission variables权限变量
    private boolean permissions = false;
    String[] permissionCodes = {
        Manifest.permission.CAMERA,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.BLUETOOTH,
        Manifest.permission.BLUETOOTH_ADMIN,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.WRITE_SETTINGS,
    };
    private Button[] permissionButtons = new Button[6];
    //activity子类的实例创建后，onCreate(Bundle)方法会被调用
    //setContentView(int layoutResID)获取activity的用户界面；
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        //初始化布局参数
        initialiseLayoutParameters();
        //确保所有权限被授予
        checkPermissions();
        //如果崩溃然后重启任务
        checkIfCrashed();
        //初始化奖励系统
        initaliseRewardSystem();

        if(testingMode && permissions) {
            startTask();
        }
    }
//Intent是一种运行时绑定（runtime binding)机制，它能在程序运行的过程中连接两个不同的组件。
//通过Intent，你的程序可以向Android表达某种请求或者意愿，Android会根据意愿的内容选择适当的组件来响应。
//activity、service和broadcast receiver之间是通过Intent进行通信的，而另外一个组件Content Provider本身就是一种通信机制，不需要通过Intent。
    private void startTask() {
        //为何没有将findViewById(...)方法返回的View类型转换为Button;
        Button startButton = findViewById(R.id.buttonStart);
        startButton.setText("Loading..");
        rewardSystem.quitBt();  // Reconnect from next activity从下一个活动重新连接
        //public Intent(Context packageContext, Class<?> cls),第一个参数告诉ActivityManager该启动哪个activity,第一个参数告诉在哪里可以找到它；
        Intent intent = new Intent(this, TaskManager.class);
        //一个activity启动另一个activity最简单的方式就是使用startActivity方法；
        //public void startActivity(Intent intent),activity调用该方法，调用请求实际发送给了操作系统的ActivityManager；
        startActivity(intent);
    }
    //初始化奖励系统
    private void initaliseRewardSystem() {
        rewardSystem = new RewardSystem(this);
        TextView tv1 = findViewById(R.id.tvBluetooth);//textColor="#FFFFFF"白色；十六进制
        if (rewardSystem.bluetoothConnection) {
            tv1.setText("Bluetooth status: Connected");
        } else if (!useBluetooth) {
            tv1.setText("Bluetooth status: Disabled");
        }
    }

    private void checkIfCrashed() {
        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            if (extras.getBoolean("restart")) {
                //If crashed then restart task如果崩溃然后重启任务
                startTask();
            }
        }
    }

    private void checkPermissions() {
        //Check if all permissions granted
        //确保所有权限被授予
        boolean permissionFlag = true;
        for (int i = 0; i < permissionCodes.length; i++){
            if(!checkPermissionNested(i)) {
                permissionFlag = false;
                break;
            }
        }
        if(permissionFlag) {
            View layout = findViewById(R.id.layoutCoveringUi);
            layout.setVisibility(View.INVISIBLE);
            permissions = true;
        }
    }
    //检查权限嵌套；
    private boolean checkPermissionNested(int i_perm) {
        final String permissionItem = permissionCodes[i_perm];
        int hasPermission=-1;
        if (i_perm<5) {
            //ContextWrapper代理Context的实现，简单地将其所有调用委托给另一个Context。 可以子类化以修改行为而无需更改原始上下文。
            //设置此ContextWrapper的基本上下文。 然后将所有调用委托给基本上下文。 如果已设置基本上下文，则抛出IllegalStateException。
            //mBase = base此包装器的新基本上下文。
            //public int checkSelfPermission(String permission) {
            //       return mBase.checkSelfPermission(permission);
            //    }
            hasPermission = checkSelfPermission(permissionItem);
        } else {
            //Settings“设置”提供程序包含全局系统级设备首选项。
            //System系统设置，包含其他系统首选项。 该表包含简单的名称/值对。 存在用于访问各个设置条目的便利功能。
            //canWrite(...)
            if (Settings.System.canWrite(this)) {
                //PackageManager用于检索与当前安装在设备上的应用程序包相关的各种信息的类。
                //您可以通过{@link Context＃getPackageManager}找到此类。
                //PERMISSION_GRANTED权限检查结果：如果已授予给定包的权限，则由{@link #checkPermission}返回。
                hasPermission = PackageManager.PERMISSION_GRANTED;
            }
        }
        if (hasPermission != PackageManager.PERMISSION_GRANTED) {
            if (!shouldShowRequestPermissionRationale(permissionItem)) {
                Toast.makeText(this, "All permissions must be enabled before app can run", Toast.LENGTH_LONG).show();
                requestPermissionLocal(i_perm);
                return false;
            }
            requestPermissionLocal(i_perm);
            return false;
        } else {
            permissionButtons[i_perm].setText("Granted");
            return true;
        }
    }

    private void requestPermissionLocal(int i_perm){
        if (i_perm==5) {  // This one is handled differently
            Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS);
            intent.setData(Uri.parse("package:" + this.getPackageName()));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            this.startActivity(intent);
        } else {
            requestPermissions(new String[] {permissionCodes[i_perm]},123);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if(grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission enabled", Toast.LENGTH_SHORT).show();
                checkPermissions();
            } else {
                // Permission Denied
                Toast.makeText(this, "Permission denied, all permissions must be enabled before app can run", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void initialiseLayoutParameters() {
        //Permission buttons权限按钮
        //public View findViewById(int id)以组件的资源ID作为参数，返回一个视图对象；
        //使用按钮的资源ID获取视图对象，赋值给对应的成员变量，赋值前，应将返回的View类型转换为Button
        //这里为何不加，(Button) findViewById(R.id.permbuttonCamera)强制转换；
        permissionButtons[0] = findViewById(R.id.permbuttonCamera);
        permissionButtons[1] = findViewById(R.id.permbuttonWrite);
        permissionButtons[2] = findViewById(R.id.permbuttonBt0);
        permissionButtons[3] = findViewById(R.id.permbuttonBt1);
        permissionButtons[4] = findViewById(R.id.permbuttonBt2);
        permissionButtons[5] = findViewById(R.id.permbuttonSettings);
        //这里利用监听器实现buttonClickListener = new View.OnClickListener() 接口
        //按钮Button[] permissionButtons被点击后，监听器会立刻通知我们。传入setOnClickListener(OnClickListener)方法的参数
        // 是一个监听器；是一个实现了OnClickListener接口的对象；



        findViewById(R.id.mainPermButton).setOnClickListener(buttonClickListener);
        for (int i = 0; i < permissionButtons.length; i++) {
            permissionButtons[i].setOnClickListener(buttonClickListener);
        }
        findViewById(R.id.buttonStart).setOnClickListener(buttonClickListener);

        initialiseToggleButtons();
    }

    private void initialiseToggleButtons() {
        //CompoundButton具有两种状态的按钮，已选中和未选中。按下或单击按钮时，状态会自动更改。
        //OnCheckedChangeListener已检查状态的复合按钮，变更时要调用的回调的接口定义。
        //CompoundButton.OnCheckedChangeListener 当复合按钮的检查状态发生变化时调用。实现方法：onCheckedChanged
        CompoundButton.OnCheckedChangeListener multiListener = new CompoundButton.OnCheckedChangeListener() {
            //CompoundButton: 状态已更改的复合按钮视图，isChecked: buttonView的新检查状态。
            public void onCheckedChanged(CompoundButton v, boolean isChecked) {
                if (!rewardSystem.bluetoothConnection) {
                    Log.d("MainMenu", "Error: Bluetooth not connected");
                    return;//这个return 返回到哪一步？
                }
                int chan = -1;
                switch (v.getId()){
                    case R.id.chanZeroButt:
                        chan = 0;
                        break;
                    case R.id.chanOneButt:
                        chan = 1;
                        break;
                    case R.id.chanTwoButt:
                        chan = 2;
                        break;
                    case R.id.chanThreeButt:
                        chan = 3;
                        break;
                }
                if (isChecked) {
                    rewardSystem.startChannel(chan);
                } else {
                    rewardSystem.stopChannel(chan);
                }
            }
        };
        //ToggleButton, 将已选中/未选中状态显示为带有“亮”指示的按钮，默认情况下伴随文本“开”或“关”。
        //setOnCheckedChangeListener注册当此按钮的选中状态更改时要调用的回调。
        //findViewById方法：引用已生成的组件，以组件的资源ID作为参数，返回一个视图对象；
        ((ToggleButton)  findViewById(R.id.chanZeroButt)).setOnCheckedChangeListener(multiListener);
        ((ToggleButton)  findViewById(R.id.chanOneButt)).setOnCheckedChangeListener(multiListener);
        ((ToggleButton)  findViewById(R.id.chanTwoButt)).setOnCheckedChangeListener(multiListener);
        ((ToggleButton)  findViewById(R.id.chanThreeButt)).setOnCheckedChangeListener(multiListener);
    }

    //Android应用属于典型的事件驱动类型；为响应某个事件而创建的对象叫做事件监听器；（listener）
    //监听器会实现特定事件的监听器接口（listener interface）
    //监听用户的按钮点击事件-----实现View.OnClickListener接口；
    //传入setOnClickListener(OnClickListener)方法的参数是一个监听器；
    //需要实现该接口的唯一方法onClick(View);

    private View.OnClickListener buttonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.buttonStart:
                    startTask();
                    break;
                case R.id.mainPermButton:
                    checkPermissions();
                    break;
                case R.id.permbuttonCamera:
                    checkPermissionNested(0);
                    break;
                case R.id.permbuttonWrite:
                    checkPermissionNested(1);
                    break;
                case R.id.permbuttonBt0:
                    checkPermissionNested(2);
                    break;
                case R.id.permbuttonBt1:
                    checkPermissionNested(3);
                    break;
                case R.id.permbuttonBt2:
                    checkPermissionNested(4);
                    break;
                case R.id.permbuttonSettings:
                    checkPermissionNested(5);
                    break;
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("stop","stopped");
        if(permissions) {
            rewardSystem.quitBt();
        }
    }

}
