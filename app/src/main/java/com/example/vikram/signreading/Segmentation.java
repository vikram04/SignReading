package com.example.vikram.signreading;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.Toast;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Size;

import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

import static org.opencv.imgproc.Imgproc.MORPH_CROSS;

/**
 * Created by Vikram on 24-05-2017.
 */

public class Segmentation  {
    Context context;


    Segmentation(Context context)
    {
        this.context=context;
//        tts = new TextToSpeech(context, this);
    }



//    @Override
//    public void onInit(int status) {
//
//        Log.d("Speech", "OnInit - Status ["+status+"]");
//
//        if (status == TextToSpeech.SUCCESS) {
//            Log.d("Speech", "Success!");
//            tts.setLanguage(Locale.UK);
//        }
//        else if (status == TextToSpeech.ERROR) {
//            Toast.makeText(context,"Error occurred while initializing Text-To-Speech engine", Toast.LENGTH_LONG).show();
//        }
//    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void Segm(Bitmap bitmap) {

        Rect rect = null;
        //System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        //  Mat image = Highgui.imread("src/man4.png", Highgui.CV_LOAD_IMAGE_UNCHANGED);
        Mat image= new Mat();
        Utils.bitmapToMat(bitmap,image);
        Size size = new Size(256, 256);

        Imgproc.resize(image, image, size);
        Imgproc.cvtColor(image, image, Imgproc.COLOR_BGR2GRAY);                                    //convert RGB 2 Gray
        Mat image2=image;
        Imgproc.threshold(image, image, 127, 255, Imgproc.THRESH_OTSU + Imgproc.THRESH_BINARY); //Thresold Image
        Mat dilated = new Mat();                                                                      //dilate Image Start
        Mat kernel = Imgproc.getStructuringElement(MORPH_CROSS, new Size(3, 3));
        Imgproc.dilate(image, dilated, kernel, new Point(-1, -1), 0);                               //dilate image end +dilate 37
        //Highgui.imwrite("src/output/dilate1.jpg", dilated);
        List<MatOfPoint> contours = new ArrayList<MatOfPoint>();                                            //segmentation start
        Imgproc.findContours(dilated, contours, new Mat(), Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);  //CHAIN_APPROX_SIMPLE
        MatOfPoint2f approxCurve = new MatOfPoint2f();

        if(contours.size()==1){
            Imgproc.threshold(image2, image, 127, 255, Imgproc.THRESH_OTSU + Imgproc.THRESH_BINARY_INV); //Thresold Image
            //Mat dilated = new Mat();                                                                      //dilate Image Start
            kernel = Imgproc.getStructuringElement(MORPH_CROSS, new Size(3, 3));
            Imgproc.dilate(image, dilated, kernel, new Point(-1, -1), 0);                               //dilate image end +dilate 37
            //Highgui.imwrite("src/output/dilate1.jpg", dilated);
            //List<MatOfPoint> contours = new ArrayList<MatOfPoint>();                                            //segmentation start
            Imgproc.findContours(dilated, contours, new Mat(), Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);  //CHAIN_APPROX_SIMPLE
            //MatOfPoint2f approxCurve = new MatOfPoint2f();
        }

        Mat line_countour[]=new Mat[contours.size()];

        for (int i = 0; i < contours.size(); i++) {
            MatOfPoint2f contour2f = new MatOfPoint2f(contours.get(i).toArray());  //Processing on mMOP2f1 which is in type MatOfPoint2f
            double approxDistance = Imgproc.arcLength(contour2f, true) * 0.02;
            Imgproc.approxPolyDP(contour2f, approxCurve, approxDistance, true);     //Convert back to MatOfPoint
            MatOfPoint points = new MatOfPoint(approxCurve.toArray());            // Get bounding rect of contour
            rect = Imgproc.boundingRect(points);                                    // draw enclosing rectangle (all same color, but you could use variable i to make them unique)

            //Imgproc.rectangle(image,new Point(rect.x,rect.y),new Point(rect.x+rect.width,rect.y+rect.height),new Scalar(255,255,255),1);
            // Highgui.imwrite("src/output/rectangle"+i+".jpg", image);
            // Mat line_countour[i] = new Mat();
            line_countour[i] = image.submat(rect);
            //Highgui.imwrite("src/output/part" + i + ".jpg", line_countour);
        }
        int m = contours.size() - 2;
        // System.out.println(m);
        // Mat part2 = Highgui.imread("src/output/part" + m + ".jpg", Highgui.CV_LOAD_IMAGE_UNCHANGED);
        Mat part2=line_countour[m];
        Comparision c = new Comparision(context);
        char answer = c.compare(part2);
        System.err.println("answer: " + answer);
       /* TextToSpeak speak = new TextToSpeak();
        if (answer == 'm') {
            speak.Speak("this is man washroom.");
        } else {
            speak.Speak("this is woman washroom.");
        }*/

        if (answer == 'm') {
            Log.e("Male","m");
            Toast.makeText(context,"Male",Toast.LENGTH_LONG).show();
            CameraActivity.t1.speak("this is men washroom.",TextToSpeech.QUEUE_FLUSH,null);
        } else {
            Log.e("Female","fm");
            Toast.makeText(context,"FeMale",Toast.LENGTH_LONG).show();
            CameraActivity.t1.speak("this is women washroom.",TextToSpeech.QUEUE_FLUSH,null);
        }



    }

}