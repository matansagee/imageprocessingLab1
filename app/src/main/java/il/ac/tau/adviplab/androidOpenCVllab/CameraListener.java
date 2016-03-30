package il.ac.tau.adviplab.androidOpenCVllab;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.core.Mat;

/**
 * Created by amitboy on 2/28/2015.
 */
//todo: Change the tutorial such that CameraListener is a different class

public class CameraListener implements CameraBridgeViewBase.CvCameraViewListener2 {

    // Constants:
    public static final int VIEW_MODE_DEFAULT = 0;
    public static final int VIEW_MODE_RGBA = 1;
    public static final int VIEW_MODE_GRAYSCALE =2;
    public static final int VIEW_MODE_SHOW_HIST =3;
    public static final int HIST_NORMALIZATION_CONST =10000;
    public static final int VIEW_MODE_HIST_EQUALIZE = 4;
    public static final int VIEW_MODE_HIST_CUMULATIVE = 5;
    public static final int VIEW_MODE_HIST_MATCHING = 6;


    //Mode selectors:
    private int mViewMode = VIEW_MODE_DEFAULT;
    private int mColorMode = VIEW_MODE_RGBA;
    private boolean mShowHistogram = false;



    //members
    Mat mImToProcess;
    Mat[] mHistArray;
    private Mat[] mHistCumulativeArray;

    //Getters and setters
    //todo: add to tutorial
    public int getColorwMode() {
        return mColorMode;
    }

    public void setColorwMode(int mColorMode) {
        this.mColorMode = mColorMode;
    }

    public int getViewMode() {
        return mViewMode;
    }

    public void setViewMode(int mViewMode) {
        this.mViewMode = mViewMode;
    }


    public boolean isShowHistogram() {
        return mShowHistogram;
    }

    public void setShowHistogram(boolean showHistogram) {
        mShowHistogram = showHistogram;
    }

    @Override
        public void onCameraViewStarted(int width, int height) {
            mHistArray = new Mat[]{new Mat(), new Mat(), new Mat()};
            mHistCumulativeArray = new Mat[]{new Mat(), new Mat(), new Mat()};
        }

        @Override
        public void onCameraViewStopped() {
            for (Mat histMat : mHistArray){
                histMat.release();
            }
        }

        @Override
        public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
            switch (this.mColorMode) {
                case CameraListener.VIEW_MODE_RGBA:
                    mImToProcess = inputFrame.rgba();

                    break;
                case CameraListener.VIEW_MODE_GRAYSCALE:
                    mImToProcess = inputFrame.gray();
                    break;
            }
            switch (this.mViewMode) {
                case VIEW_MODE_DEFAULT:
                    break;
                case CameraListener.VIEW_MODE_HIST_EQUALIZE:
                    MyImageProc.equalizeHist(mImToProcess);
                    break;
                case CameraListener.VIEW_MODE_HIST_CUMULATIVE:
                    int histSizeNum = 100;
                    MyImageProc.calcHist(mImToProcess, mHistArray, histSizeNum);
                    int idx = 0;
                    for (Mat histMat : mHistArray){
                        mHistCumulativeArray[idx].create(histMat.size(),histMat.type());
                        MyImageProc.calcCumulativeHist(histMat, mHistCumulativeArray[idx]);
                        idx++;
                    }
                    MyImageProc.showHist(mImToProcess, mHistCumulativeArray, histSizeNum);

                    break;
            }

            if (this.mShowHistogram) {
                int histSizeNum = 100;
                MyImageProc.calcHist(mImToProcess, mHistArray, histSizeNum);
                MyImageProc.showHist(mImToProcess, mHistArray, histSizeNum);
            }

            return mImToProcess;
        }

    };



