package il.ac.tau.adviplab.androidOpenCVllab;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfInt;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by iplab_4_2 on 3/9/2016.
 */
public class MyImageProc extends CameraListener {

    public static void calcHist(Mat image, Mat[] histList, int histSizeNum,
                                int normalizationConst, int normalizationNorm) {
        Mat mat0 = new Mat();
        MatOfInt histSize = new MatOfInt(histSizeNum);
        MatOfFloat ranges = new MatOfFloat(0f, 256f);
        int numberOfChannel = Math.min(image.channels(), 3);
        MatOfInt[] channels = new MatOfInt[numberOfChannel];
        for (int i = 0; i < numberOfChannel; i++) {
            channels[i] = new MatOfInt(i);
        }

        int chIdx = 0;
        for (MatOfInt channel : channels) {
            Imgproc.calcHist(Arrays.asList(image), channel, mat0,
                    histList[chIdx], histSize, ranges);

            Core.normalize(histList[chIdx], histList[chIdx],
                    normalizationConst, 0, normalizationNorm);
            chIdx++;
        }

        mat0.release();
        histSize.release();
        ranges.release();
        for (MatOfInt channel : channels) {
            channel.release();
        }

    }

    public static void calcHist(Mat image, Mat[] histList, int histSizeNum) {
        int normalizationConst = image.height() / 2;
        int normalizationNorm = Core.NORM_INF;

        calcHist(image, histList, histSizeNum,
                normalizationConst, normalizationNorm);
    }

    public static void showHist(Mat image, Mat[] histList, int histSizeNum,
                                int offset, int thickness) {
        float[] buff = new float[histSizeNum];
        int numberOfChannels = Math.min(image.channels(), 3);
        // if image is RGBA, ignore the last channel
        Point mP1 = new Point();
        Point mP2 = new Point();
        Scalar mColorsRGB[];
        mColorsRGB = new Scalar[]{new Scalar(200, 0, 0, 255), new Scalar(0,
                200, 0, 255), new Scalar(0, 0, 200, 255)};

        for (int chIdx = 0; chIdx < numberOfChannels; chIdx++) {
            histList[chIdx].get(0, 0, buff);
            for (int h = 0; h < histSizeNum; h++) {
                mP1.x = mP2.x = offset + (chIdx * (histSizeNum + 10) + h) *
                        thickness;
                mP1.y = image.height() - 1;
                mP2.y = mP1.y - 2 - (int) buff[h];
                Core.line(image, mP1, mP2, mColorsRGB[chIdx], thickness);
            }
        }
    }

    public static void showHist(Mat image, Mat[] histList, int histSizeNum) {
        int thickness = Math.min(image.width() / (histSizeNum + 10) / 5, 5);
        int offset = (image.width() - (5 * histSizeNum + 4 * 10) * thickness) / 2;

        showHist(image, histList, histSizeNum, offset, thickness);
    }

    public static void equalizeHist(Mat image) {
        List<Mat> RGBAChannels = new ArrayList<Mat>(4);
        Core.split(image, RGBAChannels);
        for (Mat colorChannel : RGBAChannels) {
            Imgproc.equalizeHist(colorChannel, colorChannel);
        }
        Core.merge(RGBAChannels, image);
        for (Mat mRGBAChannel : RGBAChannels) {
            mRGBAChannel.release();
        }
    }

    public static void calcCumulativeHist(Mat hist, Mat cumuHist) {

        int histSizeNum = (int)hist.total();
        float[] buff = new float[histSizeNum];
        float[] CumulativeSum = new float[histSizeNum];
        hist.get(0, 0, buff);
        float sum =0;

        for (int h = 1; h < histSizeNum; h++) {
            sum += buff[h];
            CumulativeSum[h] = sum;
        }
        cumuHist.put(0,0,CumulativeSum);
    }

    public static void applyIntensityMapping(Mat srcImage, Mat lookUpTable)
    {
        Mat tempMat = new Mat();
        Core.LUT(srcImage, lookUpTable, tempMat);
        tempMat.convertTo(srcImage, CvType.CV_8UC1);
        tempMat.release();
    }

    public static void matchHistogram(Mat histSrc, Mat histDst, Mat
            lookUpTable) {

//        Mat histSrc - source histogram
//        Mat histDst - destination histogram
//        Mat lookUpTable - lookUp table
//        Add your implementation here

    }

}

