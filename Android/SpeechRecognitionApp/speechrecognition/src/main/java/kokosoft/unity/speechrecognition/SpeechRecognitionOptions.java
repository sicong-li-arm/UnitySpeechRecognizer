package kokosoft.unity.speechrecognition;

/**
 * Created by piotr on 05/10/16.
 */

public class SpeechRecognitionOptions {

    public String prompt;
    public boolean shouldCollectPartialResults;
    public boolean enableFormatting;
    public String languageID;
    public Integer completeSilenceLengthMillis;
    public Integer possiblyCompleteSilenceLengthMillis;

    public SpeechRecognitionOptions() {

    }
}
