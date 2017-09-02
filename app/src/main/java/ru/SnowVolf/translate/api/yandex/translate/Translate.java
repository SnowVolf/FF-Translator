/*
 * Copyright (c) 2017 Snow Volf (Artem Zhiganov).
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ru.SnowVolf.translate.api.yandex.translate;

import android.support.annotation.NonNull;
import android.util.Log;

import java.net.URL;
import java.net.URLEncoder;

import ru.SnowVolf.translate.api.yandex.APIKeys;
import ru.SnowVolf.translate.api.yandex.YandexAPI;
import ru.SnowVolf.translate.api.yandex.language.Language;

/**
 * Created by Snow Volf on 21.05.2017, 11:26
 *
 * Расширение {@link YandexAPI} позволяющее осуществлять запросы на сервер
 * Подключение происходит через {@link javax.net.ssl.HttpsURLConnection}
 * Ответы обрабатываются в UI-потоке приложения
 */

public class Translate extends YandexAPI{
    // URL сервиса
    private static final String SERVICE_URL = "https://translate.yandex.net/api/v1.5/tr.json/translate?";
    // Label параметра
    private static final String TRANSLATION_LABEL = "text";

    private Translate(){}
    /**
     * Запрос на сервер перевода
     * Экранируем все символы, и потом отправляем запрос
     *
     * @param text текст для перевода
     * @param from язык с которого переводим
     * @param to язык на который нужно перевести
     * @return JSON, который потом обрабатываем
     */
    @NonNull
    public static String execute(final String text, final Language from, final Language to) throws Exception{
        validateServiceState(text);
        final String params =
                PARAM_API_KEY + URLEncoder.encode(apiKey, ENCODING) +
                PARAM_LANG_PAIR + URLEncoder.encode(from.toString(), ENCODING) + URLEncoder.encode("-", ENCODING) + URLEncoder.encode(to.toString(), ENCODING) +
                PARAM_TEXT + URLEncoder.encode(text, ENCODING);
        final URL url = new URL(SERVICE_URL + params);
        return retrievePropArrString(url, TRANSLATION_LABEL).trim();
    }

    /**
     * Запрос на сервер перевода
     * @see #execute(String, Language, Language)
     * Всё отличие здесь в отсутствии исходного языка. Сервис сам пытается определить на каком языке
     * написан исходный текст.
     *
     * @param text текст для перевода
     * @param to язык на который нужно перевести
     * @return JSON, который потом обрабатываем
     */
    @NonNull
    public static String executeAuto(final String text, final Language to) throws Exception{
        validateServiceState(text);
        final String params =
                PARAM_API_KEY + URLEncoder.encode(apiKey, ENCODING) +
                        PARAM_LANG_PAIR + URLEncoder.encode(to.toString(), ENCODING) +
                        PARAM_TEXT + URLEncoder.encode(text, ENCODING);
        final URL url = new URL(SERVICE_URL + params);
        return retrievePropArrString(url, TRANSLATION_LABEL).trim();
    }

    /**
     * Если в тексте больше 10000 символов - выбрасываем RuntimeException
     * Потом проверяем API ключ на null и соответствие длине
     */
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

    // Для теста
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
