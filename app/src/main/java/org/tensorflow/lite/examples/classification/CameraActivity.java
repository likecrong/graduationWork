/*
 * Copyright 2019 The TensorFlow Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.tensorflow.lite.examples.classification;

import android.Manifest;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.Image.Plane;
import android.media.ImageReader;
import android.media.ImageReader.OnImageAvailableListener;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Trace;

import androidx.annotation.UiThread;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.nio.ByteBuffer;
import java.util.Date;
import java.util.List;
import org.tensorflow.lite.examples.classification.env.ImageUtils;
import org.tensorflow.lite.examples.classification.env.Logger;
import org.tensorflow.lite.examples.classification.tflite.Classifier.Device;
import org.tensorflow.lite.examples.classification.tflite.Classifier.Model;
import org.tensorflow.lite.examples.classification.tflite.Classifier.Recognition;

public abstract class CameraActivity extends AppCompatActivity
        implements OnImageAvailableListener,
        Camera.PreviewCallback,
        View.OnClickListener,
        AdapterView.OnItemSelectedListener {

  int [] state;
  int [] game;

  setDB s;
  DataSet d;

  private SfxService sfxService;
  ImageView settingBtn; // ?????????
  int sigOfSound; // ???????????? ?????? ?????? ???????????? ??????

  private static final Logger LOGGER = new Logger();

  private static final int PERMISSIONS_REQUEST = 1;

  private static final String PERMISSION_CAMERA = Manifest.permission.CAMERA;
  protected int previewWidth = 0;
  protected int previewHeight = 0;
  private Handler handler;
  private HandlerThread handlerThread;
  private boolean useCamera2API;
  private boolean isProcessingFrame = false;
  private byte[][] yuvBytes = new byte[3][];
  private int[] rgbBytes = null;
  private int yRowStride;
  private Runnable postInferenceCallback;
  private Runnable imageConverter;

  private Model model = Model.QUANTIZED;
  private Device device = Device.CPU;
  private int numThreads = 2;

  int currentpoistion = 0; //?????? ????????? ??????
  String level; //Stage level
  int count = 0;
  private long time= 0;

  long starttime, endtime;


  //200806 ?????? - ????????? ????????? ?????? ????????? ???????????? ?????? ???????????????
  String[] nameList1_1 = new String[]{"mouse", "wallet","clock",
          "pen", "tissue", "vase", "shoes",
          "pillow", "chair", "eraser"};

  String[] nameList2_1 = new String[]{"bowl", "coffeepot", "cup",
          "frying pan", "ladle", "plate",
          "fridge", "spatula", "toaster", "wok"};

  String[] nameList3_1 = new String[]{"banana", "broccoli", "crab",
          "cucumber", "lemon", "orange",
          "pineapple", "pizza", "cart", "strawberry"};

  String[] nameList4_1 = new String[]{"balloon", "bench", "bus",
          "butterfly", "cat", "dog",
          "dragonfly", "street sign", "swing", "traffic light"};

  String[] nameList5_1 = new String[]{"soccer", "basketball", "golf",
          "rugby", "volleyball", "tennis",
          "racket", "swim cap", "whistle", "scoreboard"};

  String[] nameList6_1 = new String[]{"piano", "guitar", "violin"
          , "drum", "flute", "ocarina"
          , "harmonica", "harp", "accordion", "trombone"};

  String[] nameList_t_1 = new String[] {"sock","sock"};


  String[] levelList_1 = null; //?????? ?????? ????????? ???????????? ?????? ??????


  //20.03.26 ?????? ??????
  //?????? : ~~~??? ????????????!, ?????????????, ?????? ????????? ??????, ?????? ??? ?????????????, ?????? ~~~??????

  LinearLayout startLayout, quitLayout;
  ImageView character, newimage, quitbtn;
  Button startBtn, quitNo, quitBack;
  TextView saying;
  String ask = "";
  String answer1 = "";
  String answer2 = "";
  String string = "";
  Boolean isQuit = false;
  Boolean isStart = false;
  Boolean isAnswer = false;
  Boolean isCorrect = false;
  Boolean isCorrectAnswer = false;
  Handler mHandler;
  Saying1 mSaying1;//?????? ??? ??? ???
  Saying2 mSaying2;//???????????? ??? ???

  int i, j, k, correct, correctionConfirmor = 0;

  //20.03.26 ?????? ??????
  private View decorView;
  private int   uiOption;

  int sigOfSaying;


  @Override
  public void onWindowFocusChanged(boolean hasFocus) {
    // TODO Auto-generated method stub
    // super.onWindowFocusChanged(hasFocus);

    if( hasFocus ) {
      decorView.setSystemUiVisibility( uiOption );
    }
  }
  public void onBackPressed(){
    if(System.currentTimeMillis()-time>=2000){
      time=System.currentTimeMillis();
      Toast.makeText(getApplicationContext(),"?????? ????????? ?????? ??? ????????? ???????????????.",Toast.LENGTH_SHORT).show();
    }else if(System.currentTimeMillis()-time<2000){
      d.callExit();
    }
  }

  @Override
  protected void onCreate(final Bundle savedInstanceState) {
    super.onCreate(null);


    decorView = getWindow().getDecorView();
    uiOption = getWindow().getDecorView().getSystemUiVisibility();
    if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH )
      uiOption |= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
    if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN )
      uiOption |= View.SYSTEM_UI_FLAG_FULLSCREEN;
    if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT )
      uiOption |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

    getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

    setContentView(R.layout.activity_camera);

    //????????????????????? ????????? ??????????????? ??????
    LayoutInflater inflater = (LayoutInflater)getSystemService(

            Context.LAYOUT_INFLATER_SERVICE);

    LinearLayout camera_picure = (LinearLayout)inflater.inflate(R.layout.camera_picture, null);//????????? ???????????? ????????? ????????? ???????????? ??????
    LinearLayout.LayoutParams paramlinear1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
    addContentView(camera_picure, paramlinear1);

    LinearLayout camera_quit = (LinearLayout)inflater.inflate(R.layout.camera_quit, null);
    LinearLayout.LayoutParams paramlinear3 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
    addContentView(camera_quit, paramlinear3);//??? ????????? ??????????????? ????????? ??????


//    if (hasPermission()) {
//      setFragment();
//    }

    model = Model.valueOf("Quantized".toUpperCase());
    device = Device.valueOf("CPU");
    numThreads = 2;

    character = findViewById(R.id.character);
    saying = findViewById(R.id.saying);
    startBtn = findViewById(R.id.startBtn);
    quitbtn = findViewById(R.id.quitbtn);
    startLayout = findViewById(R.id.startLayout);
    quitLayout = findViewById(R.id.quitLayout);
    newimage = findViewById(R.id.newimage);

    quitNo = findViewById(R.id.quitNo);
    quitBack = findViewById(R.id.quitback);
    quitLayout.setVisibility(View.GONE);
    quitbtn.setClickable(false);

    isAnswer = false;

    s = new setDB(getApplicationContext());
    d = new DataSet(CameraActivity.this);

    settingBtn = findViewById(R.id.setsound); //?????????
    settingBtn.setClickable(true);


    Intent intent = getIntent();

    currentpoistion = intent.getIntExtra("cToW1",0);
    level = intent.getStringExtra("cToW2");

    d.initializeData(level);

    newimage.setImageResource(d.imageList.get(currentpoistion));

    if (level.equals("t") == true) {
      levelList_1 = nameList_t_1;
    }else if(level.equals("st1") == true){
      levelList_1 = nameList1_1;
    } else if (level.equals("st2") == true) {
      levelList_1 = nameList2_1;
    } else if (level.equals("st3") == true) {
      levelList_1 = nameList3_1;
    } else if (level.equals("st4") == true) {
      levelList_1 = nameList4_1;
    } else if (level.equals("st5") == true) {
      levelList_1 = nameList5_1;
    } else if (level.equals("b") == true) {
      levelList_1 = nameList6_1;
    }

    mSaying1 = new Saying1();
    mSaying1.setDaemon(true);
    mSaying1.start();

    startBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {

        sfxService.SfxPlay(4);

        starttime = new Date().getTime();

        startLayout.setVisibility(View.GONE);
        mSaying2 = new Saying2();
        mSaying2.setDaemon(true);
        mSaying2.start();
        quitbtn.setClickable(true);//???????????? ?????? ?????????
      }
    });

    quitbtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {

        sfxService.SfxPlay(0);

        quitLayout.setVisibility(View.VISIBLE);
        quitbtn.setClickable(false);
        startBtn.setClickable(false);
        settingBtn.setClickable(false);
      }
    });

    quitNo.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {

        sfxService.SfxPlay(0);

        quitLayout.setVisibility(View.GONE);
        quitbtn.setClickable(true);
        startBtn.setClickable(true);
        settingBtn.setClickable(true);
      }
    });

    quitBack.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {

        sfxService.SfxPlay(0);

        quitLayout.setVisibility(View.GONE);
        isQuit = true;
        Intent i = new Intent(CameraActivity.this, h3_ImageViewMain.class);
        i.putExtra("cToW3",currentpoistion);
        i.putExtra("cToW4",level);
        startActivity(i);
        //20.08.24_yeaji
        overridePendingTransition(R.anim.fadein_for_main, R.anim.fadeout_for_main);
        finish();
      }
    });
  }

  protected int[] getRgbBytes() {
    imageConverter.run();
    return rgbBytes;
  }

  protected int getLuminanceStride() {
    return yRowStride;
  }

  protected byte[] getLuminance() {
    return yuvBytes[0];
  }

  /** Callback for android.hardware.Camera API */
  @Override
  public void onPreviewFrame(final byte[] bytes, final Camera camera) {
    if (isProcessingFrame) {

      return;
    }

    try {
      // Initialize the storage bitmaps once when the resolution is known.
      if (rgbBytes == null) {
        Camera.Size previewSize = camera.getParameters().getPreviewSize();
        previewHeight = previewSize.height;
        previewWidth = previewSize.width;
        rgbBytes = new int[previewWidth * previewHeight];
        onPreviewSizeChosen(new Size(previewSize.width, previewSize.height), 0);
      }
    } catch (final Exception e) {

      return;
    }

    isProcessingFrame = true;
    yuvBytes[0] = bytes;
    yRowStride = previewWidth;

    imageConverter =
            new Runnable() {
              @Override
              public void run() {
                ImageUtils.convertYUV420SPToARGB8888(bytes, previewWidth, previewHeight, rgbBytes);
              }
            };

    postInferenceCallback =
            new Runnable() {
              @Override
              public void run() {
                camera.addCallbackBuffer(bytes);
                isProcessingFrame = false;
              }
            };
    processImage();
  }

  /** Callback for Camera2 API */
  @Override
  public void onImageAvailable(final ImageReader reader) {
    // We need wait until we have some size from onPreviewSizeChosen
    if (previewWidth == 0 || previewHeight == 0) {
      return;
    }
    if (rgbBytes == null) {
      rgbBytes = new int[previewWidth * previewHeight];
    }
    try {
      final Image image = reader.acquireLatestImage();

      if (image == null) {
        return;
      }

      if (isProcessingFrame) {
        image.close();
        return;
      }
      isProcessingFrame = true;
      Trace.beginSection("imageAvailable");
      final Plane[] planes = image.getPlanes();
      fillBytes(planes, yuvBytes);
      yRowStride = planes[0].getRowStride();
      final int uvRowStride = planes[1].getRowStride();
      final int uvPixelStride = planes[1].getPixelStride();

      imageConverter =
              new Runnable() {
                @Override
                public void run() {
                  ImageUtils.convertYUV420ToARGB8888(
                          yuvBytes[0],
                          yuvBytes[1],
                          yuvBytes[2],
                          previewWidth,
                          previewHeight,
                          yRowStride,
                          uvRowStride,
                          uvPixelStride,
                          rgbBytes);
                }
              };

      postInferenceCallback =
              new Runnable() {
                @Override
                public void run() {
                  image.close();
                  isProcessingFrame = false;
                }
              };

      processImage();
    } catch (final Exception e) {

      Trace.endSection();
      return;
    }
    Trace.endSection();
  }

  //onResume??? ???????????? ???, ?????? ??? ??? ????????? ??????
  public void setFirstSoundOnOff(){

    if(sigOfSound == 1){ //db????????? ????????? ???????????????
      settingBtn.setImageResource(R.drawable.sound_on);
      Intent intent=  new Intent(getApplicationContext(), MusicService.class);
      intent.putExtra("index",6);//????????? ????????? ????????? ????????? MusicService??? ??????
      startService(intent);

    }
    else{  //db????????? ????????? ???????????????
      settingBtn.setImageResource(R.drawable.sound_off);
      stopService(new Intent(CameraActivity.this, MusicService.class));

    }
    settingBtn.setVisibility(View.VISIBLE);
  }
  //onResume??? ???????????? ???, ????????? ?????? ???????????? ????????? ??????
  public void setChangeSoundOnOff(){
    if(sigOfSound == 1){
      //?????? ????????? ???????????????
      //????????? ??????

      settingBtn.setImageResource(R.drawable.sound_off);
      stopService(new Intent(CameraActivity.this, MusicService.class));

      s.update(1,0, 2);
      s.update(2,0, 2);
      sigOfSound = 0;

    }
    else{
      //?????? ????????? ???????????????
      //????????? ??????
      settingBtn.setImageResource(R.drawable.sound_on);
      Intent intent=  new Intent(getApplicationContext(), MusicService.class);
      intent.putExtra("index",6);//????????? ????????? ????????? ????????? MusicService??? ??????
      startService(intent);
      s.update(1,1, 2);
      s.update(2,1, 2);
      sigOfSound = 0;

    }
  }

  @Override
  public synchronized void onResume() {

    super.onResume();

    state = new int[s.dbHelper2.getDatabaseSize()];
    game = new int[s.dbHelper1.getDatabaseSize()];

    state = s.getDB2();
    game = s.getDB1();

    sigOfSound = state[1];
    d.setTuto(state[3]);//????????????

    setFirstSoundOnOff();

    if (hasPermission()) {
      setFragment();
    }

    settingBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        //??????????????? ?????? ??????
        state = s.getDB2();
        sigOfSound = state[1];
        s.printDB(state,s.dbHelper2.getDatabaseSize(),2);

        sfxService.SfxPlay(0);
        setChangeSoundOnOff();
      }
    });

    if(d.getTuto()==0){
      quitbtn.setVisibility(View.INVISIBLE);
    }


    //h3_ImageViewMain?????? ?????? ??? ?????????
    Intent intent = getIntent();
    currentpoistion = intent.getIntExtra("cToW1",0);
    level = intent.getStringExtra("cToW2");

    handlerThread = new HandlerThread("inference");
    handlerThread.start();
    handler = new Handler(handlerThread.getLooper());
  }

  @Override
  public synchronized void onPause() {


    handlerThread.quitSafely();
    try {
      handlerThread.join();
      handlerThread = null;
      handler = null;
    } catch (final InterruptedException e) {
//      LOGGER.e(e, "Exception!");
    }
    super.onPause();
    stopService(new Intent(CameraActivity.this, MusicService.class));
    sigOfSound = 0;

  }

  @Override
  public synchronized void onStop() {


    isQuit = true;
    super.onStop();
  }

  @Override
  protected void onStart() {
    super.onStart();
    sfxService = new SfxService(getApplicationContext());
    sfxService.SfxLoad();
    //if DB??? ???????????? off??? sfxService.volume = 0.0f;
  }

  @Override
  protected void onRestart() { //???????????????
    super.onRestart();  // Always call the superclass method first
    isQuit = false;
    if(sigOfSaying == 1) {
      mSaying1 = new Saying1();
      mSaying1.setDaemon(true);
      mSaying1.start();
    }
    else if (sigOfSaying == 2) {
      mSaying2 = new Saying2();
      mSaying2.setDaemon(true);
      mSaying2.start();
    }
  }

  @Override
  public synchronized void onDestroy() {
    super.onDestroy();
  }

  protected synchronized void runInBackground(final Runnable r) {
    if (handler != null) {
      handler.post(r);
    }
  }

  @Override
  public void onRequestPermissionsResult(
          final int requestCode, final String[] permissions, final int[] grantResults) {
    if (requestCode == PERMISSIONS_REQUEST) {
      if (allPermissionsGranted(grantResults)) {
        setFragment();
      } else {
        requestPermission();
      }
    }
  }

  private static boolean allPermissionsGranted(final int[] grantResults) {
    for (int result : grantResults) {
      if (result != PackageManager.PERMISSION_GRANTED) {
        return false;
      }
    }
    return true;
  }

  private boolean hasPermission() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      return checkSelfPermission(PERMISSION_CAMERA) == PackageManager.PERMISSION_GRANTED;
    } else {
      return true;
    }
  }

  private void requestPermission() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      if (shouldShowRequestPermissionRationale(PERMISSION_CAMERA)) {
        Toast.makeText(
                CameraActivity.this,
                "Camera permission is required for this demo",
                Toast.LENGTH_LONG)
                .show();
      }
      requestPermissions(new String[] {PERMISSION_CAMERA}, PERMISSIONS_REQUEST);
    }
  }

  // Returns true if the device supports the required hardware level, or better.
  private boolean isHardwareLevelSupported(
          CameraCharacteristics characteristics, int requiredLevel) {
    int deviceLevel = characteristics.get(CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL);
    if (deviceLevel == CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL_LEGACY) {
      return requiredLevel == deviceLevel;
    }
    // deviceLevel is not LEGACY, can use numerical sort
    return requiredLevel <= deviceLevel;
  }

  private String chooseCamera() {
    final CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
    try {
      for (final String cameraId : manager.getCameraIdList()) {
        final CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraId);

        // We don't use a front facing camera in this sample.
        final Integer facing = characteristics.get(CameraCharacteristics.LENS_FACING);
        if (facing != null && facing == CameraCharacteristics.LENS_FACING_FRONT) {
          continue;
        }

        final StreamConfigurationMap map =
                characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);

        if (map == null) {
          continue;
        }

        // Fallback to camera1 API for internal cameras that don't have full support.
        // This should help with legacy situations where using the camera2 API causes
        // distorted or otherwise broken previews.
        useCamera2API =
                (facing == CameraCharacteristics.LENS_FACING_EXTERNAL)
                        || isHardwareLevelSupported(
                        characteristics, CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL_FULL);
        return cameraId;
      }
    } catch (CameraAccessException e) {
//      LOGGER.e(e, "Not allowed to access camera");
    }

    return null;
  }

  protected void setFragment() {
    String cameraId = chooseCamera();

    Fragment fragment;

    if (useCamera2API) {
      CameraConnectionFragment camera2Fragment =
              CameraConnectionFragment.newInstance(
                      new CameraConnectionFragment.ConnectionCallback() {
                        @Override
                        public void onPreviewSizeChosen(final Size size, final int rotation) {
                          previewHeight = size.getHeight();
                          previewWidth = size.getWidth();
                          CameraActivity.this.onPreviewSizeChosen(size, rotation);
                        }
                      },
                      this,
                      getLayoutId(),
                      getDesiredPreviewFrameSize());

      camera2Fragment.setCamera(cameraId);
      fragment = camera2Fragment;
    } else {
      fragment =
              new LegacyCameraConnectionFragment(this, getLayoutId(), getDesiredPreviewFrameSize());
    }

    getFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
  }

  protected void fillBytes(final Plane[] planes, final byte[][] yuvBytes) {
    // Because of the variable row stride it's not possible to know in
    // advance the actual necessary dimensions of the yuv planes.
    for (int i = 0; i < planes.length; ++i) {
      final ByteBuffer buffer = planes[i].getBuffer();
      if (yuvBytes[i] == null) {
        yuvBytes[i] = new byte[buffer.capacity()];
      }
      buffer.get(yuvBytes[i]);
    }
  }

  protected void readyForNextImage() {
    if (postInferenceCallback != null) {
      postInferenceCallback.run();
    }
  }

  protected int getScreenOrientation() {
    switch (getWindowManager().getDefaultDisplay().getRotation()) {
      case Surface.ROTATION_270:
        return 270;
      case Surface.ROTATION_180:
        return 180;
      case Surface.ROTATION_90:
        return 90;
      default:
        return 0;
    }
  }

  @UiThread
  protected void showResultsInBottomSheet(List<Recognition> results) {
    if (results != null && results.size() >= 3) {
      Recognition recognition = results.get(0);
      if (recognition != null) {
//        if (recognition.getTitle() != null) Log.d("TrueResult","1??? ?????? : " + recognition.getTitle());
      }

      Recognition recognition1 = results.get(1);
      if (recognition1 != null) {
//        if (recognition1.getTitle() != null) Log.d("TrueResult","2??? ?????? : " + recognition1.getTitle());
      }

      Recognition recognition2 = results.get(2);
      if (recognition2 != null) {
//        if (recognition2.getTitle() != null) Log.d("TrueResult","3??? ?????? : " + recognition2.getTitle());
      }
      //??????-3????????? ???????????? ??????
      //200806 ?????? - ??????????????? ????????? ?????? ????????? ??? ??? ?????? ????????? answer1, answer2??? ??????
      //?????? ????????? ?????? ???????????? ????????? ??????????????? ?????????
      //?????? saying2??? ??? ?????? ??? ??????
      answer1 = recognition.getTitle();
      answer2 = recognition1.getTitle();

    }
  }

  public class Saying1 extends Thread{//?????? ???
    @Override
    public void run(){
      sigOfSaying = 1;
      while (i<5 && !isQuit){
        (CameraActivity.this).runOnUiThread(new Runnable() {
          @Override
          public void run() {
            if(i == 0) {
              character.setImageResource(R.drawable.kevin_basic);
            }else if(i ==1){
              saying.setVisibility(View.VISIBLE);
              saying.setText("??????!");
              character.setImageResource(R.drawable.kevin_wink_one_arm);
            }else if(i ==2){
              saying.setText("?????? ????????????!");
              character.setImageResource(R.drawable.kevin_basic);
            }else if(i ==3){
              saying.setText("????????? ??????????");
              character.setImageResource(R.drawable.kevin_wink_camera);
            }else if(i ==4){
              saying.setVisibility(View.INVISIBLE);
              character.setImageResource(R.drawable.kevin_basic);
              startLayout.setVisibility(View.VISIBLE);
              isStart = true;
            }
            i++;
          }
        });
        try {
          Thread.sleep(2000);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }

    }

  }
  public class Saying2 extends Thread{//?????? ???

    @Override
    public void run(){
      sigOfSaying = 2;
      while (j < 5 && !isAnswer && !isQuit){
        (CameraActivity.this).runOnUiThread(new Runnable() {
          @Override
          public void run() {

            if(answer1.equals(levelList_1[currentpoistion])){//????????? ????????? ????????? ???
              string = answer1;
              isCorrectAnswer = true;
            }
            else if(answer2.equals(levelList_1[currentpoistion])){//????????? ????????? ????????? ???
              string = answer2;
              isCorrectAnswer = true;
            }
            else{
              string = answer1;//??? ??? ?????????(??????)
              isCorrectAnswer = false;
            }

            if(!isStart) {

              if (isCorrectAnswer){

                if(correct==0){
                  character.setImageResource(R.drawable.kevin_question);
                  saying.setVisibility(View.VISIBLE);
                  ask = string + "?";
                  saying.setText(ask);
                }else if(correct==1){

                  sfxService.SfxPlay(3);

                  character.setImageResource(R.drawable.kevin_wink_one_arm);
                  ask = string + " ??????!";
                  if(j % 2 == 0){
                    saying.setText(ask);
                  }
                  else{
                    saying.setText("????????????!");
                  }

                  isAnswer = true;
                  //????????????
                  mHandler = new Handler();

                  Runnable mTask = new Runnable() {
                    @Override
                    public void run() {
                      correct = 0;
                      endtime = new Date().getTime();
                      Intent go = new Intent(CameraActivity.this, h3_WriteAction.class);

                      go.putExtra("cToW1",currentpoistion);
                      go.putExtra("cToW2",level);
                      go.putExtra("time",endtime-starttime);
                      startActivity(go);
                      //20.08.24_yeaji
                      overridePendingTransition(R.anim.fadein_for_main, R.anim.fadeout_for_main);
                      mSaying2.interrupt();
                      finish();
                    }
                  };
                  mHandler.postDelayed(mTask,1300);
                }
                correct++;
                isCorrect = true;

              }

            }
            if(correct!=0 && correct==correctionConfirmor){
              correct = 0;
              correctionConfirmor = 0;
              isCorrect = false;
            }
            if(isCorrect){
              correctionConfirmor = correct;
            }
            if(correct == 0){
              if(j == 0) {
                if(isStart) {
                  character.setImageResource(R.drawable.kevin_wink_one_arm);
                  saying.setVisibility(View.VISIBLE);
                  saying.setText("???????????? ??????!");
                  isStart = false;
                }else{
                  character.setImageResource(R.drawable.kevin_question);
                  ask = answer1 + "?";
                  saying.setText(ask);
                }
              }else if(j ==1){
                character.setImageResource(R.drawable.kevin_basic);
                saying.setText("???...");
              }else if(j ==2){
                character.setImageResource(R.drawable.kevin_question);
                ask = answer1 + "?";
                saying.setText(ask);
              }else if(j ==3){
                if(k==3){
                  character.setImageResource(R.drawable.kevin_surprised);
                  saying.setText("?????? ?????????????");
                  k=0;
                }else if(k%2==0) {
                  character.setImageResource(R.drawable.kevin_basic);
                  saying.setText("??????...");
                } else{
                  character.setImageResource(R.drawable.kevin_basic);
                  saying.setText("???...");
                }
              }
            }
            j++;
            if(j == 4) {
              j = 0;
              k++;
            }
          }
        });

        try {
          Thread.sleep(1300);//?????????, ?????? ??????
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    }
  }

  protected void showFrameInfo(String frameInfo) {
  }

  protected void showCropInfo(String cropInfo) {
  }

  protected void showCameraResolution(String cameraInfo) {
  }

  protected void showRotationInfo(String rotation) {

  }

  protected void showInference(String inferenceTime) {
  }

  protected Model getModel() {
    return model;
  }

  private void setModel(Model model) {
    if (this.model != model) {
      this.model = model;
      onInferenceConfigurationChanged();
    }
  }

  protected Device getDevice() {
    return device;
  }

  private void setDevice(Device device) {
    if (this.device != device) {
      this.device = device;
      final boolean threadsEnabled = device == Device.CPU;
      onInferenceConfigurationChanged();
    }
  }

  protected int getNumThreads() {
    return numThreads;
  }

  protected abstract void processImage();

  protected abstract void onPreviewSizeChosen(final Size size, final int rotation);

  protected abstract int getLayoutId();

  protected abstract Size getDesiredPreviewFrameSize();

  protected abstract void onInferenceConfigurationChanged();

  @Override
  public void onClick(View v) {
  }

  @Override
  public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
  }

  @Override
  public void onNothingSelected(AdapterView<?> parent) {
    // Do nothing.
  }
}