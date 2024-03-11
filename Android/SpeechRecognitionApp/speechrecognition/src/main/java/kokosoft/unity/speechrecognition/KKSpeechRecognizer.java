package kokosoft.unity.speechrecognition;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognitionPart;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by piotr on 04/10/16.
 */

public class KKSpeechRecognizer implements RecognitionListener {

    private static final String TAG = "KKSpeechRecognizer";

    public interface KKSpeechRecognizerListener {
        void onFailedToStartRecordingWithReason(String reason);
        void onFailedDuringRecordingWithReason(String reason);
        void gotPartialResult(String result);
        void gotFinalResult(String result);
        void onEndOfSpeech();
        void onReadyForSpeech();
    }

    public static boolean isRecognitionAvailable(Context context) {
        return SpeechRecognizer.isRecognitionAvailable(context);
    }

    private SpeechRecognizer mInternalSpeechRecognizer;
    private boolean mIsRecording;
    private KKSpeechRecognizerListener mListener;

    public KKSpeechRecognizer(Context context) {
        super();
        mInternalSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(context);
        mInternalSpeechRecognizer.setRecognitionListener(this);
    }

    public void setListener(KKSpeechRecognizerListener listener) {
        mListener = listener;
    }

    public void startRecording(SpeechRecognitionOptions options) {
        Intent intent = createRecordingIntent(options);
        mIsRecording = true;
        mInternalSpeechRecognizer.startListening(intent);
    }

    public void stopIfRecording() {
        if (mIsRecording) {
            mIsRecording = false;
            mInternalSpeechRecognizer.stopListening();
        }
    }

    public boolean isAvailable() {
        return true;
    }

    public boolean isRecording() {
        return mIsRecording;
    }

    private Intent createRecordingIntent(SpeechRecognitionOptions options) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_PREFER_OFFLINE, true);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,"kokosoft.unity");
        intent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, options.shouldCollectPartialResults);
        intent.putExtra(RecognizerIntent.EXTRA_ENABLE_FORMATTING, RecognizerIntent.FORMATTING_OPTIMIZE_QUALITY);
        intent.putExtra(RecognizerIntent.EXTRA_HIDE_PARTIAL_TRAILING_PUNCTUATION, false);

        Log.i(TAG, "!!!!! Intent " + intent.toString());
        if (!TextUtils.isEmpty(options.prompt)) {
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, options.prompt);
        }

        if (!TextUtils.isEmpty(options.languageID)) {
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, options.languageID);
        }
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);

        if (options.possiblyCompleteSilenceLengthMillis != null) {
            intent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_POSSIBLY_COMPLETE_SILENCE_LENGTH_MILLIS, options.possiblyCompleteSilenceLengthMillis);
        }

        if (options.completeSilenceLengthMillis != null) {
            intent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS, options.completeSilenceLengthMillis);
        }

        return intent;
    }

    @Override
    public void onReadyForSpeech(Bundle params) {
        Log.i(TAG, "onReadyForSpeech");
        if (mListener != null) {
            mListener.onReadyForSpeech();
        }
    }

    @Override
    public void onBeginningOfSpeech() {
        Log.i(TAG, "onBeginningOfSpeech");
    }

    @Override
    public void onRmsChanged(float rmsdB) {

    }

    @Override
    public void onBufferReceived(byte[] buffer) {

    }

    @Override
    public void onEndOfSpeech() {
        Log.i(TAG, "onEndOfSpeech");
        if (mListener != null) {
            mListener.onEndOfSpeech();
        }
    }

    @Override
    public void onError(int error) {
        mIsRecording = false;

        if (error == SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS) {
            if (mListener != null) {
                mListener.onFailedToStartRecordingWithReason(String.format("[%d] %s", error, "Insufficient permissions, check your manifest file!"));
            }
        } else {
            if (mListener != null) {
                mListener.onFailedDuringRecordingWithReason(String.format("SpeechRecognizer error code %d", error));
            }
        }
    }

    @Override
    public void onResults(Bundle results) {
        mIsRecording = false;
        ArrayList<String> strings = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        Log.i(TAG, "OnResults!!!");
        for (String s : strings
        ) {
            Log.i(TAG, s);
        }
        if (mListener != null) {
            mListener.gotFinalResult(strings.get(0));
        }
    }


    @Override
    public void onPartialResults(Bundle partialResults) {
        ArrayList<String> strings = partialResults.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        Log.i(TAG, "onPartialResults!!!");
        for (String s : strings
        ) {
            Log.i(TAG, s);
        }
        if (mListener != null) {
            mListener.gotPartialResult(TextUtils.join(" ", strings));
        }
    }

    @Override
    public void onEvent(int eventType, Bundle params) {

    }
}
