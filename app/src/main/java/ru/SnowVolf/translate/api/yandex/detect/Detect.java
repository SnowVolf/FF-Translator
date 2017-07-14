package ru.SnowVolf.translate.api.yandex.detect;

import android.util.Log;

import java.net.URL;
import java.net.URLEncoder;

import ru.SnowVolf.translate.api.yandex.APIKeys;
import ru.SnowVolf.translate.api.yandex.YandexAPI;
import ru.SnowVolf.translate.api.yandex.language.Language;
import ru.SnowVolf.translate.api.yandex.translate.Translate;

/**
 * Created by Snow Volf on 25.05.2017, 11:16
 */

public class Detect extends YandexAPI {
    private static final String SERVICE_URL = "https://translate.yandex.net/api/v1.5/tr.json/detect?";
    private static final String DETECTION_LABEL = "lang";

    private Detect(){}

    public static Language execute(final String text) throws Exception{
        validateServiceState(text);
        final String params =
                PARAM_API_KEY + URLEncoder.encode(apiKey, ENCODING) +
                        PARAM_TEXT + URLEncoder.encode(text, ENCODING);
        final URL url = new URL(SERVICE_URL + params);
        return Language.fromString(retriviePropArrString(url, DETECTION_LABEL));
    }

    private static void validateServiceState(final String text){
        final int byteLimit = text.getBytes().length;
        if (byteLimit > 10000){
            throw new RuntimeException("Text TOO LARGE");
        }
        try {
            validateServiceState();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main (String[] args){
        try {
            Translate.setKey(APIKeys.YANDEX_API_KEY);
            Language translation = Detect.execute("Hello world!");
            Log.i("VfTr", "Detected : " + translation.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
