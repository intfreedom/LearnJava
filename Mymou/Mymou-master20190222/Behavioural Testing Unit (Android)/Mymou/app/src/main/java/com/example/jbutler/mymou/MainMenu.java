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
    // Speeds up debugging/testing
    public static final boolean testingMode = true;

    // Can disable bluetooth and RewardSystem connectivity here
    public static final boolean useBluetooth = false;

    // Can disable facial recognition here
    //要使用faceRecog必须具有Mymou文件夹中存在的ANN（wo.txt，wi.txt，meanAndVar.txt）的权重
    //这个图像识别程序是怎样的，是mymou先收集照片，然后再Artificial Neural Network (Python)中进行训练，
    //然后得到的数据，分别保存为meanAndVar.txt，wi.txt - 输入隐藏层的权重，wo.txt - 隐藏输出层的权重，以供mymou使用；
    // To use faceRecog must have the weights for the ANN (wo.txt, wi.txt, meanAndVar.txt) present in the Mymou folder
    public static final boolean useFaceRecog = false;

    public static RewardSystem rewardSystem;

    //Permission variables
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

        initialiseLayoutParameters();

        checkPermissions();

        checkIfCrashed();

        initaliseRewardSystem();

        if(testingMode && permissions) {
            startTask();
        }
    }
//Intent是一种运行时绑定（runtime binding)机制，它能在程序运行的过程中连接两个不同的组件。
//通过Intent，你的程序可以向Android表达某种请求或者意愿，Android会根据意愿的内容选择适当的组件来响应。
//activity、service和broadcast receiver之间是通过Intent进行通信的，而另外一个组件Content Provider本身就是一种通信机制，不需要通过Intent。
    private void startTask() {
        Button startButton = findViewById(R.id.buttonStart);
        startButton.setText("Loading..");
        rewardSystem.quitBt();  // Reconnect from next activity
        Intent intent = new Intent(this, TaskManager.class);
        startActivity(intent);
    }

    private void initaliseRewardSystem() {
        rewardSystem = new RewardSystem(this);
        TextView tv1 = findViewById(R.id.tvBluetooth);
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
                //If crashed then restart task
                startTask();
            }
        }
    }

    private void checkPermissions() {
        // Check if all permissions granted
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

    private boolean checkPermissionNested(int i_perm) {
        final String permissionItem = permissionCodes[i_perm];
        int hasPermission=-1;
        if (i_perm<5) {
            hasPermission = checkSelfPermission(permissionItem);
        } else {
            if (Settings.System.canWrite(this)) {
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
        //Permission buttons
        permissionButtons[0] = findViewById(R.id.permbuttonCamera);
        permissionButtons[1] = findViewById(R.id.permbuttonWrite);
        permissionButtons[2] = findViewById(R.id.permbuttonBt0);
        permissionButtons[3] = findViewById(R.id.permbuttonBt1);
        permissionButtons[4] = findViewById(R.id.permbuttonBt2);
        permissionButtons[5] = findViewById(R.id.permbuttonSettings);

        findViewById(R.id.mainPermButton).setOnClickListener(buttonClickListener);
        for (int i = 0; i < permissionButtons.length; i++) {
            permissionButtons[i].setOnClickListener(buttonClickListener);
        }
        findViewById(R.id.buttonStart).setOnClickListener(buttonClickListener);

        initialiseToggleButtons();
    }

    private void initialiseToggleButtons() {
        CompoundButton.OnCheckedChangeListener multiListener = new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton v, boolean isChecked) {
                if (!rewardSystem.bluetoothConnection) {
                    Log.d("MainMenu", "Error: Bluetooth not connected");
                    return;
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
