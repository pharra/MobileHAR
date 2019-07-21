package com.example.mobilehar.har;

import android.content.res.AssetManager;

import com.example.mobilehar.log.Logger;

// import org.tensorflow.contrib.android.TensorFlowInferenceInterface;

import java.nio.FloatBuffer;

public class Classifier {
//    private TensorFlowInferenceInterface inferenceInterface;
//
//    // input variable name
//    private static final String x = "inputs";
//
//    // input variable name
//    private static final String y = "labels";
//
//    // predict variable name
//    private static final String pred = "logits";
//
//    private static final String[] labels = {"WALKING",
//            "WALKING_UPSTAIRS",
//            "WALKING_DOWNSTAIRS",
//            "SITTING",
//            "STANDING",
//            "LAYING",
//            "STAND_TO_SIT",
//            "SIT_TO_STAND",
//            "SIT_TO_LIE",
//            "LIE_TO_SIT",
//            "STAND_TO_LIE",
//            "LIE_TO_STAND"};
//
//    private static final int label_len = 12;
//
//    static {
//        // load libtensorflow_inference.so
//        System.loadLibrary("tensorflow_inference");
//        Logger.i("tf", "Load libtensorflow_inference.so successful");
//    }
//
//    public Classifier(AssetManager assetManager, String modePath) {
//        inferenceInterface = new TensorFlowInferenceInterface(assetManager, modePath);
//        Logger.i("tf", "Load TensorFlow mode file successful");
//    }
//
//    public int predict(float[] inputs, long... dim) {
//        inferenceInterface.feed(x, inputs, dim);
//
//        int[] _y = new int[label_len];
//        inferenceInterface.feed(y, _y, 1, 12);
//        //运行模型,run的参数必须是String[]类型
//        String[] outputNames = new String[]{pred};
//        inferenceInterface.run(outputNames);
//        //获取结果
//        FloatBuffer ret = FloatBuffer.allocate(12);
//        inferenceInterface.fetch(pred, ret);
//        return 1;
//    }
}
