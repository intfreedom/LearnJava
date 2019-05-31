package com.example.jbutler.mymou;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import androidx.annotation.NonNull;
import androidx.legacy.app.FragmentCompat;
import androidx.core.content.ContextCompat;
import android.util.Log;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

//fragment是一种控制器对象，管理用户界面，又称UI fragment,它自己也有产生于布局文件的视图；
//
public class CameraMain extends Fragment
        implements FragmentCompat.OnRequestPermissionsResultCallback {

    //Generic variables
    //一般变量
    private static String mCameraId;
    private static TextureView mTextureView;
    private static CameraCaptureSession mCaptureSession;
    private static CameraDevice mCameraDevice;
    private static Size mPreviewSize;
    private static final int REQUEST_CAMERA_PERMISSION = 1;
    private static ImageReader mImageReader;
    private static CaptureRequest.Builder mPreviewRequestBuilder;
    private static CaptureRequest mPreviewRequest;
    /*Semaphore计数信号量。 从概念上讲，信号量维护着一组许可证。 如果需要，每个{@link #acquire}都会阻止许可可用，然后接受它。
    每个{@link #release}都会添加许可证，可能释放阻止收购者。但是，没有使用实际的许可证对象; {@code Semaphore}
    只是保留可用数量的计数并相应地采取行动。*/
    private static Semaphore mCameraOpenCloseLock = new Semaphore(1);
    //Background threads and variables for saving images用于保存图像的后台线程和变量
    private HandlerThread mBackgroundThread, mBackgroundThread2;
    private Handler mBackgroundHandler, mBackgroundHandler2;
    public static String timestamp;

    public static CameraMain newInstance() {
        return new CameraMain();
    }
    //1. Fragment中onCreate类似于Activity.onCreate，在其中可初始化除了view之外的一切；
    //2. onCreateView是创建该fragment对应的视图，其中需要创建自己的视图并返回给调用者；

    //onCreateView方法实例化fragment视图的布局，然后将实例化的View返回给托管activity;
    //LayoutInflater及ViewGroup是实例化的必要参数，Bundle用来存储恢复数据，可供该方法从保存状态下重建视图；
    //从activity_camera_main布局中实例化并返回视图；
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //fragment的视图是直接通过调用LayoutInflater.inflate(...)方法并传入布局的资源ID生成的，
        //container是视图的父视图，我们通常需要父视图来正确配置组件；
        //第三个参数：是否将生成的视图添加给父视图；传入false参数表示，将以代码的方式添加视图；
        return inflater.inflate(R.layout.activity_camera_main, container, false);
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        mTextureView = (TextureView) view.findViewById(R.id.texture);

        startBackgroundThread();
        //isAvailable:如果与此TextureView关联的{@link SurfaceTexture}可用于呈现，则返回true。
        // 当此方法返回true时，{@ link #getSurfaceTexture（）}将返回有效的表面纹理。
        if (mTextureView.isAvailable()) {
            openCamera();
        } else {
            //setSurfaceTextureListener()设置用于侦听Surface Texture事件的{@link SurfaceTextureListener}。
            mTextureView.setSurfaceTextureListener(mSurfaceTextureListener);
        }

    }
    //Fragment.onCreate(Bundle)是公共方法，而Activity.onCreate(Bundle)是受保护方法；
    //Fragment的生命周期方法，必须是公共方法，因为托管fragment的activity要调用它们；
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * {@link TextureView.SurfaceTextureListener} handles several lifecycle events on a
     * {@link TextureView}.
     */
    private final TextureView.SurfaceTextureListener mSurfaceTextureListener
            = new TextureView.SurfaceTextureListener() {

        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture texture, int width, int height) {
            //Wait for surface texture ready before opening camera
            //在打开相机之前等待surface texture准备就绪
            openCamera();
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture texture, int width, int height) {}

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture texture) {
            return true;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture texture) {}

    };

    /**
     * {@link CameraDevice.StateCallback} is called when {@link CameraDevice} changes its state.
     */
    private final CameraDevice.StateCallback mStateCallback = new CameraDevice.StateCallback() {

        @Override
        public void onOpened(@NonNull CameraDevice cameraDevice) {
            //This method is called when the camera is opened.  We start camera preview here.
            //打开相机时会调用此方法。 我们在这里开始相机预览。

            mCameraDevice = cameraDevice;
            //创建相机预览会话
            createCameraPreviewSession();
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice cameraDevice) {
            mCameraOpenCloseLock.release();
            cameraDevice.close();
            mCameraDevice = null;
        }

        @Override
        public void onError(@NonNull CameraDevice cameraDevice, int error) {
            mCameraOpenCloseLock.release();
            cameraDevice.close();
            mCameraDevice = null;
            Activity activity = getActivity();
            if (null != activity) {
                activity.finish();
            }
        }

    };

    // "onImageAvailable" will be called when a still image is ready to be saved.
    //当准备好保存静止图像时，将调用“onImageAvailable”。
    private final ImageReader.OnImageAvailableListener mOnImageAvailableListener
            = new ImageReader.OnImageAvailableListener() {
        @Override
        public void onImageAvailable(ImageReader reader) {
                Image image = reader.acquireNextImage();
                mBackgroundHandler.post(new CameraSavePhoto(image, timestamp));
        }

    };

    // Handles events related to JPEG capture.
    //处理与JPEG捕获相关的事件。
    private CameraCaptureSession.CaptureCallback mCaptureCallback
            = new CameraCaptureSession.CaptureCallback() {};

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    //No onResume as app is oneShot，没有onResume作为app是oneShot
    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        closeCamera();
        stopBackgroundThread();
        getActivity().finish();
    }
    //设置相机输出
    private void setUpCameraOutputs() {
        Activity activity = getActivity();//返回此片段当前与之关联的Activity。
        /*CAMERA_SERVICE与{@link #getSystemService（String）}一起使用以
        检索{@link android.hardware.camera2.CameraManager}以与相机设备进行交互。
        @see #getSystemService(String)   @see android.hardware.camera2.CameraManager

        Context: 有关应用程序环境的全局信息的接口。 这是一个抽象类，其实现由Android系统提供。 它允许访问特定于应用程序的资源和类，
        以及对应用程序级操作的上调，例如启动活动，广播和接收意图等。

        CameraManager: 用于检测，表征和连接{@link CameraDevice CameraDevices}的系统服务管理器。
        */
        CameraManager manager = (CameraManager) activity.getSystemService(Context.CAMERA_SERVICE);
        try {
            /*按标识符返回当前连接的摄像机设备列表，包括其他摄像机API客户端可能正在使用的摄像机。
            不可移动摄像机使用从0开始的整数作为其标识符，而可移动摄像机具有每个单独设备的唯一标识符，
            即使它们是相同的型号。@return当前连接的摄像机设备列表。*/
            for (String cameraId : manager.getCameraIdList()) {
                /*CameraCharacteristics: 描述{@link CameraDevice CameraDevice}的属性。这些属性对于给定的CameraDevice是固定的，
                可以通过{@link CameraManager CameraManager}界面使用
                {@link CameraManager＃getCameraCharacteristics}进行查询。{@link CameraCharacteristics}对象是不可变的。*/
                CameraCharacteristics characteristics
                        = manager.getCameraCharacteristics(cameraId);

                // Find selfie camera找自拍相机;.LENS_FACING相机相对于屏幕设备的方向；
                //.LENS_FACING_BACK相机设备面向与设备屏幕相反的方向；public static final int LENS_FACING_BACK = 1
                Integer facing = characteristics.get(CameraCharacteristics.LENS_FACING);
                if (facing != null && facing == CameraCharacteristics.LENS_FACING_BACK) {
                    continue;
                }

                StreamConfigurationMap map = characteristics.get(
                        CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
                if (map == null) {
                    continue;
                }

                // Use the smallest available size.使用最小有效尺寸；
                Size smallest = Collections.min(
                        Arrays.asList(map.getOutputSizes(ImageFormat.JPEG)),
                        new cameraCompareAreas());
                Log.d("cameraResolution", "arr: " + Arrays.deepToString(map.getOutputSizes(ImageFormat.JPEG)));
                Log.d("cameraResolution","width: "+smallest.getWidth()+", height: "+smallest.getHeight());
                mImageReader = ImageReader.newInstance(smallest.getWidth(), smallest.getHeight(),
                        ImageFormat.JPEG, /*maxImages*/2);
                mImageReader.setOnImageAvailableListener(
                        mOnImageAvailableListener, mBackgroundHandler);
                Size[] choices = map.getOutputSizes(SurfaceTexture.class);
                mPreviewSize = choices[0];
                mCameraId = cameraId;
                return;
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }
    //打开相机
    private void openCamera() {
        //设置相机输出
        setUpCameraOutputs();
        //getActivity: 返回此片段当前与之关联的Activity。
        Activity activity = getActivity();
        /*CameraManager用于检测，表征和连接{@link CameraDevice CameraDevices}的系统服务管理器。有关与相机设备通信的更多详细信息，
        请阅读相机开发人员指南或{@link android.hardware.camera2 camera2}包文档。

        CAMERA_SERVICE与{@link #getSystemService（String）}一起使用以
        检索{@link android.hardware.camera2.CameraManager}以与相机设备进行交互。
        @see #getSystemService(String)   @see android.hardware.camera2.CameraManager*/
        CameraManager manager = (CameraManager) activity.getSystemService(Context.CAMERA_SERVICE);
        try {
            /*tryAcquire如果在给定的等待时间内可用，并且当前线程未被{@linkplain Thread＃interrupt interrupted}，则从此信号量获取许可。*/
            if (!mCameraOpenCloseLock.tryAcquire(2500, TimeUnit.MILLISECONDS)) {//时间单位代表千分之一秒。
                throw new RuntimeException("Time out waiting to lock camera opening.");
            }
            try {
                //openCamera打开与具有给定ID的摄像机的连接。
                manager.openCamera(mCameraId, mStateCallback, mBackgroundHandler);
            } catch (SecurityException e) {
                throw new SecurityException("Camera permissions not available.", e);
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException("Interrupted while trying to lock camera opening.", e);
        }
    }
    //关闭相机；
    private void closeCamera() {
        try {
            mCameraOpenCloseLock.acquire();
            if (null != mCaptureSession) {
                mCaptureSession.close();
                mCaptureSession = null;
            }
            if (null != mCameraDevice) {
                mCameraDevice.close();
                mCameraDevice = null;
            }
            if (null != mImageReader) {
                mImageReader.close();
                mImageReader = null;
            }
        } catch (InterruptedException e) {
            Toast.makeText(getActivity().getApplicationContext(),"Error 3",Toast.LENGTH_LONG).show();
            throw new RuntimeException("Interrupted while trying to lock camera closing.", e);
        } finally {
            mCameraOpenCloseLock.release();
        }
    }
    //HandlerThread(继承Thread类 & 封装Handler类)本质上是一个线程类，继承了Thread
    //通过继承Thread类，快速地创建1个带有Looper对象的新工作线程
    //通过封装Handler类，快速创建Handler & 与其他线程进行通信
    private void startBackgroundThread() {
        // 步骤1：创建HandlerThread实例对象
        // 传入参数 = 线程名字，作用 = 标记该线程
        mBackgroundThread = new HandlerThread("CameraBackground");
        // 步骤2：启动线程
        mBackgroundThread.start();
        // 步骤3：创建工作线程Handler
        // 作用：关联HandlerThread的Looper对象、实现消息处理操作 & 与其他线程进行通信
        mBackgroundHandler = new Handler(mBackgroundThread.getLooper());

        mBackgroundThread2 = new HandlerThread("faceRecogBackground");
        mBackgroundThread2.start();
        mBackgroundHandler2 = new Handler(mBackgroundThread2.getLooper());
    }

    private void stopBackgroundThread() {
        mBackgroundThread.quitSafely();
        mBackgroundThread2.quitSafely();
        try {
            mBackgroundThread.join();
            mBackgroundThread = null;
            mBackgroundHandler = null;
            mBackgroundThread2.join();
            mBackgroundThread2 = null;
            mBackgroundHandler2 = null;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    //创建相机预览会话
    private void createCameraPreviewSession() {
        try {
            SurfaceTexture texture = mTextureView.getSurfaceTexture();
            assert texture != null;
            //我们将默认缓冲区的大小配置为我们想要的相机预览的大小。
            // We configure the size of default buffer to be the size of camera preview we want.
            texture.setDefaultBufferSize(mPreviewSize.getWidth(), mPreviewSize.getHeight());
            //这是我们需要开始预览的输出Surface。
            // This is the output Surface we need to start preview.
            Surface surface = new Surface(texture);
            //我们使用输出Surface设置了CaptureRequest.Builder。
            // We set up a CaptureRequest.Builder with the output Surface.
            mPreviewRequestBuilder
                    = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            mPreviewRequestBuilder.addTarget(surface);
            //在这里，我们为相机预览创建一个CameraCaptureSession。
            // Here, we create a CameraCaptureSession for camera preview.
            mCameraDevice.createCaptureSession(Arrays.asList(surface, mImageReader.getSurface()),
                    new CameraCaptureSession.StateCallback() {

                        @Override
                        public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
                            // The camera is already closed
                            if (null == mCameraDevice) {
                                return;
                            }
                            //会话准备就绪后，我们开始显示预览。
                            // When the session is ready, we start displaying the preview.
                            mCaptureSession = cameraCaptureSession;
                            try {
                                // Auto focus should be continuous for camera preview.
                                //对于相机预览，自动对焦应该是连续的。
                                mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE,
                                        CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);

                                // Finally, we start displaying the camera preview.最后，我们开始显示相机预览。
                                mPreviewRequest = mPreviewRequestBuilder.build();
                                mCaptureSession.setRepeatingRequest(mPreviewRequest,
                                        mCaptureCallback, mBackgroundHandler);
                            } catch (CameraAccessException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onConfigureFailed(
                                @NonNull CameraCaptureSession cameraCaptureSession) {
                        }
                    }, null
            );
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    // Say cheese笑一笑，拍照了；
    public static void captureStillPicture() {
        try {
            //这是我们用来拍照的CaptureRequest.Builder。
            // This is the CaptureRequest.Builder that we use to take a picture.

            /*CaptureRequest: 从相机设备捕获单个图像所需的不可变的设置和输出包。Builder: 捕获请求的构建器。要获取构建器实例，
            请使用{@link CameraDevice＃createCaptureRequest}方法，该方法将请求字段初始化为{@link CameraDevice}中定义的模板之一。*/
            final CaptureRequest.Builder captureBuilder =
                    mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
            captureBuilder.addTarget(mImageReader.getSurface());
            //使用与预览相同的AE和AF模式。
            // Use the same AE and AF modes as the preview.
            captureBuilder.set(CaptureRequest.CONTROL_AF_MODE,
                    CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
            //要在此处旋转照片设定角度
            //To rotate photo set angle here
            captureBuilder.set(CaptureRequest.JPEG_ORIENTATION, 270);

            //Set black and white设黑色和白色；
            captureBuilder.set(CaptureRequest.CONTROL_EFFECT_MODE,
                    CaptureRequest.CONTROL_EFFECT_MODE_MONO);

            CameraCaptureSession.CaptureCallback CaptureCallback
                    = new CameraCaptureSession.CaptureCallback() {

                @Override
                public void onCaptureCompleted(@NonNull CameraCaptureSession session,
                                               @NonNull CaptureRequest request,
                                               @NonNull TotalCaptureResult result) {}
            };

            mCaptureSession.stopRepeating();
            mCaptureSession.capture(captureBuilder.build(), CaptureCallback, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }
    //比较两个区域，如果A更大则返回1，如果相同大小则返回0，如果B更大则返回-1
    // Compares two areas and returns 1 if A is bigger, 0 if same size, or -1 if B is bigger
    private class cameraCompareAreas implements Comparator<Size> {
        @Override
        public int compare(Size lhs, Size rhs) {
            // We cast here to ensure the multiplications won't overflow
            //我们在这里投射以确保乘法不会溢出
            return Long.signum((long) lhs.getWidth() * lhs.getHeight() -
                    (long) rhs.getWidth() * rhs.getHeight());
        }

    }

}
