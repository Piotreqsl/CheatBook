
package com.onionv2.cheatbook;


import android.graphics.PointF;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;



import com.google.android.gms.vision.Tracker;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;
import com.google.android.gms.vision.face.Landmark;

import java.util.HashMap;
import java.util.Map;


class GooglyFaceTracker extends Tracker<Face> {
    private static final float EYE_CLOSED_THRESHOLD = 0.4f;
    private Map<Integer, PointF> mPreviousProportions = new HashMap<>();
    private boolean mPreviousIsLeftOpen = true;
    private boolean mPreviousIsRightOpen = true;
    private StatusView statusView;






    //==============================================================================================
    // Methods
    //==============================================================================================

    GooglyFaceTracker(StatusView st){this.statusView = st;}



    @Override
    public void onUpdate(FaceDetector.Detections<Face> detectionResults, Face face) {


        updatePreviousProportions(face);

        boolean previousBoth = mPreviousIsLeftOpen && mPreviousIsRightOpen;


        float leftOpenScore = face.getIsLeftEyeOpenProbability();
        boolean isLeftOpen;
        if (leftOpenScore == Face.UNCOMPUTED_PROBABILITY) {
            isLeftOpen = mPreviousIsLeftOpen;
        } else {
            isLeftOpen = (leftOpenScore > EYE_CLOSED_THRESHOLD);
            mPreviousIsLeftOpen = isLeftOpen;
        }

        float rightOpenScore = face.getIsRightEyeOpenProbability();
        boolean isRightOpen;
        if (rightOpenScore == Face.UNCOMPUTED_PROBABILITY) {
            isRightOpen = mPreviousIsRightOpen;
        } else {
            isRightOpen = (rightOpenScore > EYE_CLOSED_THRESHOLD);
            mPreviousIsRightOpen = isRightOpen;
        }






        if(isLeftOpen && isRightOpen ){ statusView.setStatus("Both open", "#88E188"); }
        if(!isLeftOpen && isRightOpen && previousBoth){ statusView.setStatus("Left open", "#88E188"); }
        if(isLeftOpen && !isRightOpen && previousBoth){ statusView.setStatus("Right open", "#88E188"); }



    }


    @Override
    public void onNewItem(int faceId, Face face) {
    Log.d("hujjjjjj", "hujjj");
    }

    @Override
    public void onMissing(FaceDetector.Detections<Face> detectionResults) {
        statusView.setStatus("Nothing detected", "#B22222");
    }





    private void updatePreviousProportions(Face face) {
        for (Landmark landmark : face.getLandmarks()) {
            PointF position = landmark.getPosition();
            float xProp = (position.x - face.getPosition().x) / face.getWidth();
            float yProp = (position.y - face.getPosition().y) / face.getHeight();
            mPreviousProportions.put(landmark.getType(), new PointF(xProp, yProp));
        }
    }



}