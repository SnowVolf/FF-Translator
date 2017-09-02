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

package ru.SnowVolf.translate.api.yandex.detect;

import android.util.Log;

import java.net.URL;
import java.net.URLEncoder;
import java.util.Locale;

import ru.SnowVolf.translate.api.yandex.APIKeys;
import ru.SnowVolf.translate.api.yandex.YandexAPI;
import ru.SnowVolf.translate.api.yandex.language.Language;
import ru.SnowVolf.translate.api.yandex.translate.Translate;

/**
 * Created by Snow Volf on 21.05.2017, 11:16
 *
 * Расширение {@link YandexAPI} которое предоставляет возможность
 * определить язык написанного текста
 * путем отправки строки исходного текста на сервис перевода.
 * В ответ получаем только объект {@link Language}
 */

public class Detect extends YandexAPI {
    // URL сервиса
    private static final String SERVICE_URL = "https://translate.yandex.net/api/v1.5/tr.json/detect?";
    // Label параметра
    private static final String DETECTION_LABEL = "lang";
    // Рекомендуемый конструктор
    // Предоставляет возможность расширения через другие классы
    private Detect(){}

    /**
     * Тут короче кодируем всё, и отправляем на сервер (ждём ответа)
     * Перед этим конечно же не забываем проверить API ключ на null и соответствие длине
     * Ну и еще проверяем на количество символов. Не более 10000 за 1 перевод.
     */
    public static Language execute(final String text) throws Exception{
        validateServiceState(text);
        final String params =
                PARAM_API_KEY + URLEncoder.encode(apiKey, ENCODING) +
                        PARAM_TEXT + URLEncoder.encode(text, ENCODING);
        final URL url = new URL(SERVICE_URL + params);
        return Language.fromString(retrievePropArrString(url, DETECTION_LABEL));
    }

    /**
     * Если в тексте больше 10000 символов - выбрасываем RuntimeException
     * Потом проверяем API ключ на null и соответствие длине
     */
    private static void validateServiceState(final String text){
        final int byteLimit = text.getBytes().length;
        if (byteLimit > 10000){
            throw new RuntimeException(String.format(Locale.ENGLISH, "Maximum byte limit = 10000 digits, your byte limit = %d", byteLimit));
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
            Language translation = Detect.execute("Hello world!");
            Log.i("VfTr", "Detected : " + translation.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
