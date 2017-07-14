package ru.SnowVolf.translate.clipboard;

import android.app.NotificationManager;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import ru.SnowVolf.translate.App;
import ru.SnowVolf.translate.R;
import ru.SnowVolf.translate.api.yandex.language.Language;
import ru.SnowVolf.translate.api.yandex.translate.Translate;
import ru.SnowVolf.translate.util.Preferences;
import ru.SnowVolf.translate.util.runtime.Logger;

/**
 * Created by Snow Volf on 28.06.2017, 18:08
 */

class ClipboardTask extends AsyncTask<String, Integer, String>{
    private Class aClass = this.getClass();
    private String translated;
    private String smallText;
    @Override
    protected void onPreExecute(){
        Logger.logi(aClass, "onPreExecute()");
    }

    @Override
    protected String doInBackground(String... strings) {
        Logger.logi(aClass, "doInBackground(String... strings)");
        String exec = strings[0];
        try {
            if (!isCancelled()){//Пока не отменено
                Logger.logi(aClass, "!isCancelled()");
                if (!Preferences.isDetectAllowed()) {
                    translated = Translate.execute(exec, Language.fromString(Preferences.getFromLang()), Language.fromString(Preferences.getToLang()));
                } else {
                    translated = Translate.executeAuto(exec, Language.fromString(Preferences.getToLang()));
                }
            } else return "VolfGirl";//Конец пока
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i("VfTr", "Translated in background :\n" + exec);
        return translated;
    }

    @Override
    protected void onPostExecute(String result){
        Logger.logi(aClass, "onPostExecute(String result)");
        super.onPostExecute(result);
        try {
            if (result.length() > 30) {
                smallText = result.substring(30).concat("...");
            } else {
                smallText = result;
            }
        } catch (Exception e){
            e.printStackTrace();
            Toast.makeText(App.ctx(), R.string.err_clipboard_translate, Toast.LENGTH_SHORT).show();
        }
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(App.ctx())
                .setSmallIcon(R.drawable.ic_notification_translate)
                .setContentTitle("Перевод выполнен")
                .setContentText(smallText);
        NotificationManager manager = (NotificationManager) App.ctx().getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(996, mBuilder.build());
    }

    //Если пользователь отменил задачу
    @Override
    protected void onCancelled(){
        Logger.logi(aClass, "onCancelled()");
    }
}