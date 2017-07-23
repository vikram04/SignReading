package com.example.vikram.signreading;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import org.opencv.imgproc.Imgproc;

import org.opencv.android.Utils;
import org.opencv.core.MatOfDMatch;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.features2d.DMatch;
import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.features2d.FeatureDetector;
import org.opencv.features2d.Features2d;
import org.opencv.features2d.KeyPoint;
import org.opencv.highgui.Highgui;


import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    DescriptorExtractor descriptorExtractor;
    DescriptorMatcher descriptorMatcher;

    static {
        System.loadLibrary("opencv_java");
        System.loadLibrary("nonfree");

    }

    private ImageView imageView;
    private Bitmap inputImage; // make bitmap from image resource
    private FeatureDetector detector = FeatureDetector.create(FeatureDetector.SIFT);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inputImage = BitmapFactory.decodeResource(getResources(), R.drawable.images);
        setContentView(R.layout.activity_main);
        imageView = (ImageView) this.findViewById(R.id.imageView);

        sift();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void sift() {
        Mat rgba = new Mat();
        Mat rgba1 = new Mat();
        Utils.bitmapToMat(inputImage, rgba);
        Utils.bitmapToMat(inputImage, rgba1);
        Imgproc.cvtColor(rgba, rgba, Imgproc.COLOR_RGBA2GRAY);
        Imgproc.cvtColor(rgba1, rgba1, Imgproc.COLOR_RGBA2GRAY);
        //MatOfKeyPoint keyPoints = new MatOfKeyPoint();
        //Imgproc.cvtColor(rgba, rgba, Imgproc.COLOR_RGBA2GRAY);
        //detector.detect(rgba, keyPoints);
        //Features2d.drawKeypoints(rgba, keyPoints, rgba);
        //descriptorExtractor=DescriptorExtractor.create(DescriptorExtractor.SURF);
        //descriptorMatcher= DescriptorMatcher.create(6);

//        Utils.matToBitmap(rgba, inputImage);
        //      imageView.setImageBitmap(inputImage);
        MatOfKeyPoint objectKeyPoints = new MatOfKeyPoint();
        FeatureDetector featureDetector = FeatureDetector.create(FeatureDetector.SIFT);
        System.out.println("Detecting key points...");
        featureDetector.detect(rgba, objectKeyPoints);
        KeyPoint[] keypoints = objectKeyPoints.toArray();
        MatOfKeyPoint objectDescriptors = new MatOfKeyPoint();
        DescriptorExtractor descriptorExtractor = DescriptorExtractor.create(DescriptorExtractor.SIFT);
        System.out.println("Computing descriptors...");
        descriptorExtractor.compute(rgba, objectKeyPoints, objectDescriptors);

//        System.out.println("descriptor 1: " + objectDescriptors.toArray().length);


        System.out.println(keypoints);
        System.out.println("key point 1 length: " + objectKeyPoints.toArray().length);
        //  Mat outputImage = new Mat(rgba.rows(), rgba.cols(), Imgcodecs.CV_LOAD_IMAGE_COLOR);
        Mat outputImage = rgba;
        Scalar newKeypointColor = new Scalar(255, 0, 0);

        System.out.println("Drawing key points on object image...");
        Features2d.drawKeypoints(rgba, objectKeyPoints, outputImage, newKeypointColor, 0);
        // Features2d.drawKeypoints(rgba,objectKeyPoints,outputImage);
        Utils.matToBitmap(outputImage, inputImage);
        imageView.setImageBitmap(inputImage);

        Mat sceneImage = rgba1;
        MatOfKeyPoint sceneKeyPoints = new MatOfKeyPoint();
        MatOfKeyPoint sceneDescriptors = new MatOfKeyPoint();
        System.out.println("Detecting key points in background image...");
        featureDetector.detect(sceneImage, sceneKeyPoints);
        System.out.println("Computing descriptors in background image...");
        descriptorExtractor.compute(sceneImage, sceneKeyPoints, sceneDescriptors);

        System.out.println("key point 2 length: " + sceneKeyPoints.toArray().length);
        System.out.println("descriptor 2  : " + sceneDescriptors.toArray().length);
        Features2d.drawKeypoints(sceneImage, objectKeyPoints, outputImage, newKeypointColor, 0);
        Utils.matToBitmap(outputImage, inputImage);
        imageView.setImageBitmap(inputImage);
        Mat matchoutput = new Mat(sceneImage.rows() * 2, sceneImage.cols() * 2, Highgui.CV_LOAD_IMAGE_COLOR);
        // Mat matchoutput=rgba;
        Scalar matchestColor = new Scalar(0, 255, 0);

        List<MatOfDMatch> matches = new LinkedList<>();
        descriptorExtractor.compute(sceneImage, sceneKeyPoints, sceneDescriptors);
        DescriptorMatcher descriptorMatcher = DescriptorMatcher.create(DescriptorMatcher.BRUTEFORCE);
        descriptorMatcher.knnMatch(objectDescriptors, sceneDescriptors, matches, 2);

        LinkedList<DMatch> goodMatchesList = new LinkedList<>();

        float nndrRatio = 0.75f;
        // System.out.println("Matches"+matches.size());

        for (int k = 0; k < matches.size(); k++) {
            MatOfDMatch DMatch = matches.get(k);
            DMatch[] dmatcharray = DMatch.toArray();                               ;
            DMatch m1 = dmatcharray[0];

            DMatch m2 = dmatcharray[1];
             System.err.println("m1 :"+m1.distance);
             System.err.println("m2 :"+m2.distance);
            if (m1.distance <= m2.distance * nndrRatio) {
                goodMatchesList.addLast(m1);


           }
            System.out.println("match list: " + goodMatchesList.size());

        }
    }

}

