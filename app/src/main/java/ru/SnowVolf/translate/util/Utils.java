package ru.SnowVolf.translate.util;


import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.res.Resources;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import ru.SnowVolf.translate.App;

/**
 * Created by Snow Volf on 11.06.2017, 10:39
 */

public class Utils {
    //Проверка на null
    public static boolean isNotNull(Object o){
        return (o != null);
    }

    //То же самое что и assert object != null;
    public static boolean assertNull(Object o){
        assert o != null;
        return true;
    }
    //Копирование текста в буфер обмена
    public static void copyToClipboard(String s){
        ClipboardManager clipboard = (ClipboardManager) App.ctx().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("JavaGirl", s);
        clipboard.setPrimaryClip(clip);
    }

    //Получение текста из буфера обмена
    public static CharSequence getTextFromClipboard(){
        ClipboardManager clipboard = (ClipboardManager) App.ctx().getSystemService(Context.CLIPBOARD_SERVICE);
        if (clipboard.hasPrimaryClip()) {
            android.content.ClipDescription description = clipboard.getPrimaryClipDescription();
            android.content.ClipData data = clipboard.getPrimaryClip();
            if (data != null && description != null && description.hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN))
                return String.valueOf(data.getItemAt(0).getText());
        }
        return null;
    }

    //Есть ли в буфере обмена текст
    public static boolean hasClipData(){
        ClipboardManager clipboard = (ClipboardManager) App.ctx().getSystemService(Context.CLIPBOARD_SERVICE);
        return clipboard.hasPrimaryClip();
    }

    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public static File streamToCacheFile(Context context, InputStream inputStream, String name) {
        return streamToCacheFile(context, inputStream, "", name);
    }

    public static File streamToCacheFile(Context context, InputStream inputStream, String path, String name) {
        String FilePath = context.getCacheDir().getAbsolutePath() + "/" + path + "/" + name;

        return streamToFile(inputStream, FilePath);
    }

    public static File streamToFile(InputStream inputStream, String path) {
        File file = new File(path);
        if (!file.getParentFile().exists()) {
            //noinspection ResultOfMethodCallIgnored
            file.getParentFile().mkdirs();
        }
        try {
            //noinspection ResultOfMethodCallIgnored
            file.createNewFile();

            FileOutputStream outputStream = new FileOutputStream(path);

            int bytesRead;
            byte[] buffer = new byte[1024];
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    public static String getNormalDate(long time){
        Date date = new Date(time);
        Format format = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.ENGLISH);
        return format.format(date);
    }
}
