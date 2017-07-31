package ru.SnowVolf.translate.api.yandex;

import android.support.annotation.NonNull;

import org.json.simple.JsonArray;
import org.json.simple.JsonObject;
import org.json.simple.Jsoner;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Snow Volf on 25.05.2017, 9:35
 */

public class YandexAPI {

    protected static final String ENCODING = "UTF-8";
    public static final String WEB_URL = "https://translate.yandex.ru/m/translate?=";
    private static int responseCode;
    private static String combinedTranslations;

    protected static String apiKey;
    //private static String referrer;

    protected static final String PARAM_API_KEY = "key=", PARAM_LANG_PAIR = "&lang=", PARAM_TEXT = "&text=";

    public static void setKey(final String girlKey){
        apiKey = girlKey;
    }

//    public static void setReferrer(final String girlReferrer){
//        referrer = girlReferrer;
//    }

    private static String retrieveResponse(final URL url) throws Exception{
        final HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
//        if (referrer != null){
//            urlConnection.setRequestProperty("referrer", referrer);
//            Log.i("VfTr", "referrer set to : " + referrer);
//        }
        urlConnection.setRequestProperty("Content-Type", "text/plain; charset=" + ENCODING);
        urlConnection.setRequestProperty("Accept-Charset", ENCODING);
        urlConnection.setRequestMethod("GET");

        try {
            responseCode = urlConnection.getResponseCode();
            final String result = inputStreamToString(urlConnection.getInputStream());
            if (responseCode != 200) {
                throw new RuntimeException("Error from YandexAPI : " + result);
            }
            return result;
        } finally {
            urlConnection.disconnect();
        }
    }

    public static int getResponseCode() {
        return responseCode;
    }

    @NonNull
    protected static String retriviePropArrString(final URL url, final String jsonValProperty) throws Exception{
        try {
            final String response = retrieveResponse(url);
            String[] translationArr = jsonObjValToStringArr(response, jsonValProperty);
            combinedTranslations = "";

            for (String s : translationArr) {
                combinedTranslations += s;
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return combinedTranslations.trim();
    }

    private static String[] jsonObjValToStringArr(final String inputString, final String subObjPropertyName) throws Exception {
        JsonObject jsonObject = (JsonObject) Jsoner.deserialize(inputString);
        JsonArray jsonArray = (JsonArray) jsonObject.get(subObjPropertyName);
        return jsonArrToStringArr(jsonArray.toJson(), null);
    }

    private static String[] jsonArrToStringArr(final String inputString, final String propertyName) throws Exception{
        final JsonArray jsonArray = (JsonArray) Jsoner.deserialize(inputString);
        String[] values = new String[jsonArray.size()];

        int i = 0;
        for (Object o : jsonArray){
            if (propertyName != null && propertyName.length() != 0){
                final JsonObject object = (JsonObject) o;
                if (object.containsKey(propertyName)){
                    values[i] = object.get(propertyName).toString();
                }
            } else {
                values[i] = o.toString();
            }
            i++;
        }
        return values;
    }

    private static String inputStreamToString(final InputStream inputStream) throws Exception{
        final StringBuilder outBuilder = new StringBuilder();

        try {
            String string;
            if (inputStream != null){
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, ENCODING));
                while ((string = bufferedReader.readLine()) != null){
                    outBuilder.append(string.replaceAll("\uFEFF", ""));
                }
            }
        } catch (Exception ex){
            throw new Exception("error reading translation stream", ex);
        }
        return outBuilder.toString();
    }

    protected static void validateServiceState() throws Exception {
        if (apiKey == null || apiKey.length() < 27){
            throw new RuntimeException("Invalid or incorrect API key!");
        }
    }
}
