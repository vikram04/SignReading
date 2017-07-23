package com.example.vikram.signreading;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.speech.tts.TextToSpeech;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.content.ContentValues.TAG;

public class CameraActivity extends AppCompatActivity {
    static {
        System.loadLibrary("opencv_java");
        System.loadLibrary("nonfree");

    }




    private Camera mCamera;
    private CameraPreview mPreview;
    static Uri u;
    public static TextToSpeech t1;

    byte bit[]=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        mCamera = getCameraInstance();

        mPreview = new CameraPreview(this, mCamera);
        FrameLayout preview = (FrameLayout) findViewById(R.id.preview);
        preview.addView(mPreview);
        Button captureButton = (Button) findViewById(R.id.capture);

        t1=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.UK);
                }
            }
        });

        final Camera.PictureCallback mPicture = new Camera.PictureCallback() {

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {

                Bitmap image=byteToBitmap(data);
                Matrix m=new Matrix();
                m.postRotate(90);
                image = Bitmap.createBitmap(image, 0, 0, image.getWidth(), image.getHeight(), m, true);

                data=bitmapToByte(image);
                bit=data;
                System.out.println("byte is : "+bit);
                File pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);
              //  u=Uri.fromFile(pictureFile);
                if (pictureFile == null){
                   /* Log.d(TAG, "Error creating media file, check storage permissions: "
                            e.getMessage());*/
                    return;
                }

                try {
                    FileOutputStream fos = new FileOutputStream(pictureFile);
                    fos.write(data);
                    Segmentation s =new Segmentation(getApplicationContext());
                    s.Segm(image);

                    fos.close();
                } catch (FileNotFoundException e) {
                    Log.d(TAG, "File not found: " + e.getMessage());
                } catch (IOException e) {
                    Log.d(TAG, "Error accessing file: " + e.getMessage());
                }
            }
        };

        captureButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // get an image from the camera
                        mCamera.takePicture(null, null, mPicture);



                        //Uri picUri=getOutputMediaFileUri(1);
                        /*Intent cropIntent=new Intent("com.android.camera.action.CROP");
                        cropIntent.setDataAndType(u, "image/*");
                        cropIntent.putExtra("aspectX",1);
                        cropIntent.putExtra("aspectY",0);
                        // indicate output X and Y
                        cropIntent.putExtra("outputX", 128);
                        cropIntent.putExtra("outputY", 128);
                        // retrieve data on return
                        cropIntent.putExtra("return-data", true);
                        // start the activity - we handle returning in onActivityResult
                        startActivityForResult(cropIntent,1);*/

                    }
                }
        );
       /* crop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("hello");
                Bundle b = new Bundle();                                                               //code is added
                b.putByteArray("byte", bit);
                Intent i = new Intent(com.example.akshit.project_washroom_signs.CameraActivity.this, CropActivity.class);
                i.putExtra("bundle", b);
                startActivity(i);
                System.out.println("world");
            }
        });*/

    }
   /* @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if (data != null) {
                // get the returned data
                Bundle extras = data.getExtras();
               // byte[] b=extras.getByteArray("data");
                //System.out.println(b);
                // get the cropped bitmap
                Bitmap selectedBitmap = extras.getParcelable("data");
                byte img[]=bitmapToByte(selectedBitmap);
                Bundle bit=new Bundle();
                bit.putByteArray("img",img);

                Intent i=new Intent(com.example.akshit.project_washroom_signs.CameraActivity.this,Process.class);
                i.putExtra("bundle",bit);
                startActivity(i);
              //  imgView.setImageBitmap(selectedBitmap);
            }
        }
    }*/
    public static Bitmap byteToBitmap(byte b[])
    {
        return  BitmapFactory.decodeByteArray(b, 0, b.length);

        //mCrop.setImageBitmap(selectedBitmap);
    }
    public static byte[] bitmapToByte(Bitmap b)
    {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        b.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }


    public static Camera getCameraInstance(){
        Camera c = null;
        try {
            c = Camera.open();                                                                         // attempt to get a Camera instance
        }
        catch (Exception e){
                                                                                                       // Camera is not available (in use or does not exist)
        }
        return c;                                                                                      //returns null if camera is unavailable
    }
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;

    /** Create a file Uri for saving an image or video */
    private static Uri getOutputMediaFileUri(int type){
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /** Create a File for saving an image or video */
    private static File getOutputMediaFile(int type){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "SmartSearch");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE){
           // mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_"+ timeStamp + ".jpg");
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +"A.jpg");
            u=Uri.fromFile(mediaFile);
        } else if(type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "VID_"+ timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }
}

class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
    private SurfaceHolder mHolder;
    private Camera mCamera;


    public CameraPreview(Context context, Camera camera) {
        super(context);
        mCamera = camera;

        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        mHolder = getHolder();
        mHolder.addCallback(this);
        // deprecated setting, but required on Android versions prior to 3.0
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    public void surfaceCreated(SurfaceHolder holder) {
        // The Surface has been created, now tell the camera where to draw the preview.
        try {

            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
        } catch (IOException e) {
            Log.d(TAG, "Error setting camera preview: " + e.getMessage());
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        // empty. Take care of releasing the Camera preview in your activity.
        mCamera.stopPreview();
      //  mCamera.release();
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        // get Camera parameters
        Camera.Parameters params = mCamera.getParameters();
// set the focus mode
        params.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        params.setJpegQuality(100);
        List<Camera.Size> supportedSizes = params.getSupportedPictureSizes();
        for(int i=0;i<supportedSizes.size();i++)
        {
            System.out.println("supported size is : " +supportedSizes.get(i).height+ "width is : "+supportedSizes.get(i).width);
        }
        Camera.Size sizePicture = (supportedSizes.get(12));
        params.setPictureSize(sizePicture.width,sizePicture.height);
// set Camera parameters
        mCamera.setParameters(params);
        mCamera.setDisplayOrientation(90);

        // If your preview can change or rotate, take care of those events here.
        // Make sure to stop the preview before resizing or reformatting it.

        if (mHolder.getSurface() == null){
            // preview surface does not exist
            return;
        }

        // stop preview before making changes
        try {
            mCamera.stopPreview();
        } catch (Exception e){
            // ignore: tried to stop a non-existent preview
        }

        // set preview size and make any resize, rotate or
        // reformatting changes here

        // start preview with new settings
        try {
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();

        } catch (Exception e){
            Log.d(TAG, "Error starting camera preview: " + e.getMessage());
        }
    }
}
