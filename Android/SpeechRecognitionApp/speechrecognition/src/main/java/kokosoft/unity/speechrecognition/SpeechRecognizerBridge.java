package kokosoft.unity.speechrecognition;

import com.unity3d.player.UnityPlayer;

/**
 * Created by piotr on 04/10/16.
 */

public class SpeechRecognizerBridge {

    static private final String GAME_OBJECT_NAME = "KKSpeechRecognizerListener";

    static private KKSpeechRecognizer speechRecognizer;

    static private SendToUnitySpeechRecognizerListener listener = new SendToUnitySpeechRecognizerListener(GAME_OBJECT_NAME);

    public static boolean EngineExists() {
        return KKSpeechRecognizer.isRecognitionAvailable(UnityPlayer.currentActivity);
    }

    public static boolean IsRecording() {
        if (speechRecognizer == null) {
            return false;
        } else {
            return speechRecognizer.isRecording();
        }
    }

    public static boolean IsAvailable() {
        return true;
    }

    public static int AuthorizationStatus() {
        return 0;
    }

    public static void StopIfRecording() {
        UnityPlayer.currentActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (speechRecognizer != null ) {
                    speechRecognizer.stopIfRecording();
                }
            }
        });
    }

    public static void StartRecording(final boolean shouldCollectPartialResult) {
        UnityPlayer.currentActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (speechRecognizer == null) {
                    speechRecognizer = new KKSpeechRecognizer(UnityPlayer.currentActivity);
                    speechRecognizer.setListener(listener);
                }

                speechRecognizer.startRecording(shouldCollectPartialResult);
            }
        });
    }

    public static void RequestAccess() {
        UnityPlayer.UnitySendMessage(GAME_OBJECT_NAME, "AuthorizationStatusFetched", "authorized");
    }
}