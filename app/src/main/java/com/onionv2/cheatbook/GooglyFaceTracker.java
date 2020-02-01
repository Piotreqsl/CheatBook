/*
 * Copyright (C) The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.onionv2.cheatbook;

import android.content.Context;
import android.graphics.PointF;
import android.os.Debug;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;


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




    //==============================================================================================
    // Methods
    //==============================================================================================

    GooglyFaceTracker(){}



    @Override
    public void onUpdate(FaceDetector.Detections<Face> detectionResults, Face face) {


        updatePreviousProportions(face);

        PointF leftPosition = getLandmarkPosition(face, Landmark.LEFT_EYE);
        PointF rightPosition = getLandmarkPosition(face, Landmark.RIGHT_EYE);

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


        if(isLeftOpen && isRightOpen){setStatus("Both open");}
        if(!isLeftOpen && isRightOpen){setStatus("Left open");}
        if(isLeftOpen && !isRightOpen){setStatus("Right open");}



    }


    @Override
    public void onNewItem(int faceId, Face face) {
    Log.d("hujjjjjj", "hujjj");
    }

    @Override
    public void onMissing(FaceDetector.Detections<Face> detectionResults) {
        setStatus("Nothing detected");
    }




    //==============================================================================================
    // Private
    //==============================================================================================



    private void setStatus(final String text){
        new Handler(Looper.getMainLooper()).post(new Runnable(){
            @Override
            public void run() {
                CameraActivity.status.setText(text);
            }
        });


    }

    private void updatePreviousProportions(Face face) {
        for (Landmark landmark : face.getLandmarks()) {
            PointF position = landmark.getPosition();
            float xProp = (position.x - face.getPosition().x) / face.getWidth();
            float yProp = (position.y - face.getPosition().y) / face.getHeight();
            mPreviousProportions.put(landmark.getType(), new PointF(xProp, yProp));
        }
    }

    /**
     * Finds a specific landmark position, or approximates the position based on past observations
     * if it is not present.
     */
    private PointF getLandmarkPosition(Face face, int landmarkId) {
        for (Landmark landmark : face.getLandmarks()) {
            if (landmark.getType() == landmarkId) {
                return landmark.getPosition();
            }
        }

        PointF prop = mPreviousProportions.get(landmarkId);
        if (prop == null) {
            return null;
        }

        float x = face.getPosition().x + (prop.x * face.getWidth());
        float y = face.getPosition().y + (prop.y * face.getHeight());
        return new PointF(x, y);
    }
}