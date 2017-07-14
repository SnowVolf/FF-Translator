package ru.SnowVolf.translate.api.yandex.translate;

import android.support.annotation.NonNull;
import android.util.Log;

import java.net.URL;
import java.net.URLEncoder;

import ru.SnowVolf.translate.api.yandex.APIKeys;
import ru.SnowVolf.translate.api.yandex.YandexAPI;
import ru.SnowVolf.translate.api.yandex.language.Language;

/**
 * Created by Snow Volf on 25.05.2017, 11:26
 */

public class Translate extends YandexAPI{
    private static final String SERVICE_URL = "https://translate.yandex.net/api/v1.5/tr.json/translate?";
    private static final String TRANSLATION_LABEL = "text";

    private Translate(){}

    @NonNull
    public static String execute(final String text, final Language from, final Language to) throws Exception{
        validateServiceState(text);
        final String params =
                PARAM_API_KEY + URLEncoder.encode(apiKey, ENCODING) +
                PARAM_LANG_PAIR + URLEncoder.encode(from.toString(), ENCODING) + URLEncoder.encode("-", ENCODING) + URLEncoder.encode(to.toString(), ENCODING) +
                PARAM_TEXT + URLEncoder.encode(text, ENCODING);
        final URL url = new URL(SERVICE_URL + params);
        return retriviePropArrString(url, TRANSLATION_LABEL).trim();
    }

    @NonNull
    public static String executeAuto(final String text, final Language to) throws Exception{
        validateServiceState(text);
        final String params =
                PARAM_API_KEY + URLEncoder.encode(apiKey, ENCODING) +
                        PARAM_LANG_PAIR + URLEncoder.encode(to.toString(), ENCODING) +
                        PARAM_TEXT + URLEncoder.encode(text, ENCODING);
        final URL url = new URL(SERVICE_URL + params);
        return retriviePropArrString(url, TRANSLATION_LABEL).trim();
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
            String translation = Translate.execute("Hello world!", Language.ENGLISH, Language.RUSSIAN);
            Log.i("VfTr", "Translation : " + translation);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
