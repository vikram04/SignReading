package com.example.vikram.signreading;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDMatch;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.core.Scalar;
import org.opencv.features2d.DMatch;
import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.features2d.FeatureDetector;
import org.opencv.features2d.KeyPoint;
import org.opencv.highgui.Highgui;

import java.util.LinkedList;
import java.util.List;


/**
 * Created by vikram on 24-05-2017.
 */

public class Comparision {
    Context context;
    Comparision(Context ctx)
    {
        this.context=ctx;
    }
    //    TextToSpeak t;
    public char compare(Mat imageCompare){
        int man_count = 0,woman_count = 0;
        Mat image=imageCompare;
        // Highgui.imwrite("src/output/keypoint5.jpg", image);
//        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
//        Mat image = Highgui.imread("src/output/woman/bw0.jpg", Highgui.CV_LOAD_IMAGE_UNCHANGED);     ////image as input
        Mat outputImage = new Mat(image.rows(), image.cols(), Highgui.CV_LOAD_IMAGE_COLOR);
        MatOfKeyPoint objectKeyPoints = new MatOfKeyPoint();                                 //// for storing keypoint
        FeatureDetector featureDetector = FeatureDetector.create(FeatureDetector.SIFT);      ////
        System.out.println("Detecting key points...");
        featureDetector.detect(image, objectKeyPoints);
        KeyPoint[] keypoints = objectKeyPoints.toArray();
        System.err.println(keypoints.length);
        Scalar newKeypointColor = new Scalar(255, 0, 0);
        // Features2d.drawKeypoints(image, objectKeyPoints, outputImage, newKeypointColor, 0);
        // Highgui.imwrite("src/output/keypoint.jpg", outputImage);
//        for(int j=0;j<keypoints.length;j++){
//            System.out.println(keypoints[j]);
//        }
//
        MatOfKeyPoint objectDescriptors = new MatOfKeyPoint();
        int id;
        Mat image1 = new Mat();
        int woman[]={R.drawable.bw0,R.drawable.bw1,R.drawable.bw2,R.drawable.bw3,R.drawable.bw4};
        int man[]={R.drawable.bm0,R.drawable.bm1,R.drawable.bm2,R.drawable.bm3,R.drawable.bm4};
        for(int i=0;i<=4;i++){
            //id=R.drawable.bm0;

            // image1 = Highgui.imread("src/output/woman/bw"+i+".jpg", Highgui.CV_LOAD_IMAGE_UNCHANGED);    ////image to compare
            Bitmap inputimage= BitmapFactory.decodeResource(context.getResources(),woman[i]);
            Utils.bitmapToMat(inputimage,image1);
            MatOfKeyPoint objectKeyPoints1 = new MatOfKeyPoint();                                 //// for storing keypoint
            featureDetector.detect(image1, objectKeyPoints1);
            KeyPoint[] keypoints1 = objectKeyPoints1.toArray();
            System.err.println(keypoints1.length);
            // Features2d.drawKeypoints(image1, objectKeyPoints1, outputImage, newKeypointColor, 0);
            //  Highgui.imwrite("src/output/keypoint1.jpg", outputImage);
//            for(int l=0;l<keypoints1.length;l++){
//                System.out.println(keypoints1[l]);
//            }



            MatOfKeyPoint objectDescriptors1 = new MatOfKeyPoint();
            List<MatOfDMatch> matches = new LinkedList<MatOfDMatch>();
            DescriptorExtractor descriptorExtractor = DescriptorExtractor.create(DescriptorExtractor.SIFT);
            System.out.println("Computing descriptors...");
            descriptorExtractor.compute(image, objectKeyPoints, objectDescriptors);
            System.out.println("descriptor 1: "+objectDescriptors.toArray().length);
//            for(int j=0;j<objectDescriptors.toArray().length;j++){
//                System.out.println("od= "+objectDescriptors.toArray()[j]);
//            }
            descriptorExtractor.compute(image1, objectKeyPoints1, objectDescriptors1);
            DescriptorMatcher descriptorMatcher = DescriptorMatcher.create(DescriptorMatcher.BRUTEFORCE);
            descriptorMatcher.knnMatch(objectDescriptors, objectDescriptors1, matches, 2);

            LinkedList<DMatch> goodMatchesList = new LinkedList<DMatch>();

            float nndrRatio = 0.75f;
            // System.out.println("Matches"+matches.size());

            for (int k = 0; k < matches.size(); k++) {
                MatOfDMatch matofDMatch = matches.get(k);
                DMatch[] dmatcharray = matofDMatch.toArray();
                DMatch m1 = dmatcharray[0];

                DMatch m2 = dmatcharray[1];
                // System.err.println("m1 :"+m1.distance);
                // System.err.println("m2 :"+m2.distance);
                if (m1.distance <= m2.distance * nndrRatio) {
                    goodMatchesList.addLast(m1);



                }
                System.out.println("maatch list: "+ goodMatchesList.size());
                System.err.println("woman pehla :"+woman_count);
                woman_count+=goodMatchesList.size();
                System.err.println("woman pa6i :"+woman_count);
            }
        }

        for(int a=0;a<=4;a++){
            // image1 = Highgui.imread("src/output/man/bm"+a+".jpg", Highgui.CV_LOAD_IMAGE_UNCHANGED);    ////image to compare
            Bitmap inputimage= BitmapFactory.decodeResource(context.getResources(),man[a]);
            image1= Utils.bitmapToMat(inputimage,image1);

            MatOfKeyPoint objectKeyPoints1 = new MatOfKeyPoint();                                 //// for storing keypoint
            featureDetector.detect(image1, objectKeyPoints1);
            KeyPoint[] keypoints1 = objectKeyPoints1.toArray();
            System.err.println(keypoints1.length);
//            Features2d.drawKeypoints(image1, objectKeyPoints1, outputImage, newKeypointColor, 0);
            // Highgui.imwrite("src/output/keypoint1.jpg", outputImage);
//            for(int l=0;l<keypoints1.length;l++){
//                System.out.println(keypoints1[l]);
//            }



            MatOfKeyPoint objectDescriptors1 = new MatOfKeyPoint();
            List<MatOfDMatch> matches = new LinkedList<MatOfDMatch>();
            DescriptorExtractor descriptorExtractor = DescriptorExtractor.create(DescriptorExtractor.SIFT);
            System.out.println("Computing descriptors...");
            descriptorExtractor.compute(image, objectKeyPoints, objectDescriptors);
            System.out.println("descriptor 1: "+objectDescriptors.toArray().length);
//            for(int j=0;j<objectDescriptors.toArray().length;j++){
//                System.out.println("od= "+objectDescriptors.toArray()[j]);
//            }
            descriptorExtractor.compute(image1, objectKeyPoints1, objectDescriptors1);
            DescriptorMatcher descriptorMatcher = DescriptorMatcher.create(DescriptorMatcher.BRUTEFORCE);
            descriptorMatcher.knnMatch(objectDescriptors, objectDescriptors1, matches, 2);

            LinkedList<DMatch> goodMatchesList = new LinkedList<DMatch>();

            float nndrRatio = 0.75f;
            //System.out.println("Matches"+matches.size());

            for (int k = 0; k < matches.size(); k++) {
                MatOfDMatch matofDMatch = matches.get(k);
                DMatch[] dmatcharray = matofDMatch.toArray();
                DMatch m1 = dmatcharray[0];

                DMatch m2 = dmatcharray[1];
                // System.err.println("m1 :"+m1.distance);
                // System.err.println("m2 :"+m2.distance);
                if (m1.distance <= m2.distance * nndrRatio) {
                    goodMatchesList.addLast(m1);


                }
                System.out.println("maatch list: "+ goodMatchesList.size());
                System.err.println("man pehla :"+man_count);
                man_count+=goodMatchesList.size();
                System.err.println("man pa6i :"+man_count);
            }
        }
        System.err.println("man_count "+man_count);
        System.err.println("woman_count "+woman_count);
        if(man_count>woman_count){
            return 'm';
        }
        else{
            return 'w';
        }
    }
}