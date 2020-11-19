package com.namespace.objectdetection;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.util.Arrays;
import java.util.List;

import ai.fritz.core.Fritz;
import ai.fritz.vision.FritzVision;
import ai.fritz.vision.FritzVisionImage;
import ai.fritz.vision.FritzVisionModels;
import ai.fritz.vision.FritzVisionObject;
import ai.fritz.vision.objectdetection.FritzVisionObjectPredictor;
import ai.fritz.vision.objectdetection.FritzVisionObjectPredictorOptions;
import ai.fritz.vision.objectdetection.FritzVisionObjectResult;
import ai.fritz.vision.objectdetection.ObjectDetectionOnDeviceModel;

public class MainActivity extends AppCompatActivity {
    Button buttonClick;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Fritz.configure(this, "YOUR_API_KEY");
        buttonClick = findViewById(R.id.buttonClick);
        imageView = findViewById(R.id.imageView);


    }
    public void detectObjects(View view){
        List<String> labels = Arrays.asList("Background", "cat", "dog");
        ObjectDetectionOnDeviceModel onDeviceModel = new ObjectDetectionOnDeviceModel(
                "file:///android_asset/CatsAndDogsFast.tflite",
                "ee788094573d4c609463b3a73465d328",
                2,
                labels
        );
        Bitmap image = BitmapFactory.decodeResource(getResources(), R.drawable.cat);

        FritzVisionImage visionImage = FritzVisionImage.fromBitmap(image);

        FritzVisionObjectPredictorOptions options = new FritzVisionObjectPredictorOptions();
        options.confidenceThreshold = 0.1f;
        FritzVisionObjectPredictor predictor = FritzVision.ObjectDetection.getPredictor(onDeviceModel,options);

        FritzVisionObjectResult objectResult = predictor.predict(visionImage);

        List<FritzVisionObject> visionObjects =  objectResult.getObjects();

        if (objectResult != null) {
            for (FritzVisionObject object : visionObjects) {
                Bitmap boundingBoxOnImage = visionImage.overlayBoundingBox(object);
                imageView.setImageBitmap(boundingBoxOnImage);
            }
            Log.i("Info", "The Label is" + objectResult.getObjects());
        }

    }
}