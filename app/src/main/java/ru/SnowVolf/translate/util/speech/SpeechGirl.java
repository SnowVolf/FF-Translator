package ru.SnowVolf.translate.util.speech;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.widget.Toast;

import java.util.Locale;

import ru.SnowVolf.translate.App;
import ru.SnowVolf.translate.R;
import ru.SnowVolf.translate.util.runtime.Logger;

/**
 * Created by Snow Volf on 22.06.2017, 8:10
 */

public class SpeechGirl {
    private static SpeechGirl girl = null;
    public static final int REQUEST_GIRL_READY = 3;

    public SpeechGirl(){
        girl = this;
    }

    public static SpeechGirl getCtx(){
        if (girl == null){
            new SpeechGirl();
        }
        return girl;
    }

    public void openInput(Activity act){
        Intent speechIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        speechIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, act.getString(R.string.speak_now));
        try {
            act.startActivityForResult(speechIntent, REQUEST_GIRL_READY);
        } catch (ActivityNotFoundException anfe){
            Toast.makeText(act, R.string.speech_girl_is_not_supported, Toast.LENGTH_LONG).show();
        }
    }

    public void speak(String text){
        TextToSpeech toSpeech = new TextToSpeech(App.ctx(), status -> {
            if (status == TextToSpeech.SUCCESS){
                Logger.log("TEXT TO SPEECH SUCCESS");
            }
        });
        toSpeech.setLanguage(Locale.US);
        toSpeech.speak(text, TextToSpeech.QUEUE_ADD, null);

    }
}