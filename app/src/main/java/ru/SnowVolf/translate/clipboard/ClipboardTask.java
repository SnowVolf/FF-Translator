package ru.SnowVolf.translate.clipboard;

import android.app.NotificationManager;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import org.acra.ACRA;

import ru.SnowVolf.translate.App;
import ru.SnowVolf.translate.R;
import ru.SnowVolf.translate.api.yandex.language.Language;
import ru.SnowVolf.translate.api.yandex.translate.Translate;
import ru.SnowVolf.translate.util.Constants;
import ru.SnowVolf.translate.util.Preferences;
import ru.SnowVolf.translate.util.Utils;
import ru.SnowVolf.translate.util.runtime.Logger;

/**
 * Created by Snow Volf on 28.06.2017, 18:08
 */

class ClipboardTask extends AsyncTask<String, Integer, String>{
    private Class aClass = this.getClass();
    private String translated;
    @Override
    protected void onPreExecute(){
        Logger.i(aClass, "onPreExecute()");
    }

    @Override
    protected String doInBackground(String... strings) {
        Logger.i(aClass, "doInBackground(String... strings)");
        String exec = strings[0];
        try {
            if (!isCancelled()){//Пока не отменено
                if (exec == null){
                    exec = Utils.getTextFromClipboard().toString();
                }
                Translate.setKey(App.ctx().getPreferences().getString(Constants.Prefs.API_KEY, ""));
                Logger.i(aClass, "!isCancelled()");
                if (!Preferences.isDetectAllowed()) {
                    translated = Translate.execute(exec, Language.fromString(Preferences.getFromLang()), Language.fromString(Preferences.getToLang()));
                } else {
                    translated = Translate.executeAuto(exec, Language.fromString(Preferences.getToLang()));
                }
            } else return "VolfGirl";//Конец пока
        } catch (Exception e) {
            ACRA.getErrorReporter().handleException(e);
        }
        Log.i("VfTr", "Translated in background :\n" + exec);
        return translated;
    }

    @Override
    protected void onPostExecute(String result){
        Logger.i(aClass, "onPostExecute(String result)");
        super.onPostExecute(result);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(App.ctx())
                .setSmallIcon(R.drawable.ic_notification_translate)
                .setContentTitle(App.ctx().getString(R.string.translate_success))
                .setContentText(result);
        NotificationManager manager = (NotificationManager) App.ctx().getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(996, mBuilder.build());
    }

    //Если пользователь отменил задачу
    @Override
    protected void onCancelled(){
        Logger.i(aClass, "onCancelled()");
    }
}