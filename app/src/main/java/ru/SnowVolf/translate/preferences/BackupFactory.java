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

package ru.SnowVolf.translate.preferences;

import android.Manifest;
import android.app.Activity;
import android.content.ContextWrapper;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import ru.SnowVolf.translate.App;


/**
 * Created by Snow Volf on 05.08.2017, 12:12
 *
 * Класс экспорта/импорта БД и настроек
 *
 * Оригинал: https://github.com/Deletescape/Lawnchair/app/src/main/java/ch/deletescape/lawnchair/DumbImportExportTask.java
 */

public class BackupFactory {
    // Экспорт БД
    public static void exportDb(Activity activity) {
        ContextWrapper contextWrapper = new ContextWrapper(activity);
        File favDb = contextWrapper.getDatabasePath(Constants.favDb.DB_NAME);
        File historyDb = contextWrapper.getDatabasePath(Constants.historyDb.DB_NAME);
        exportFile(favDb, activity);
        exportFile(historyDb, activity);
    }

    //Импорт БД
    public static void importDb(Activity activity) {
        ContextWrapper contextWrapper = new ContextWrapper(activity);
        File favDb = contextWrapper.getDatabasePath(Constants.favDb.DB_NAME);
        File historyDb = contextWrapper.getDatabasePath(Constants.historyDb.DB_NAME);
        importFile(favDb, activity);
        importFile(historyDb, activity);
    }

    // Экспорт файла настроек
    public static void exportPrefs(Activity activity) {
        ApplicationInfo info = activity.getApplicationInfo();
        String dir = new ContextWrapper(activity).getCacheDir().getParent();
        File prefs = new File(dir, "shared_prefs/" + info.packageName + "_preferences.xml");
        exportFile(prefs, activity);
    }

    // Импорт файла настроек
    public static void importPrefs(Activity activity) {
        ApplicationInfo info = activity.getApplicationInfo();
        String dir = new ContextWrapper(activity).getCacheDir().getParent();
        File prefs = new File(dir, "shared_prefs/" + info.packageName + "_preferences.xml");
        importFile(prefs, activity);
    }

    // Экспорт файла
    private static void exportFile(File file, Activity activity) {
        // Если нет разрешения на запись
        if (!isExternalStorageWritable() || !canWriteStorage(activity)) {
            Toast.makeText(activity, "Нет разрешения на чтение/запись!", Toast.LENGTH_SHORT).show();
            return;
        }
        File backup = new File(getFolder(), file.getName());
        if (backup.exists()) {
            backup.delete();
        }
        if (copy(file, backup)) {
            Toast.makeText(activity, "Успешно!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(activity, "Ошибка!", Toast.LENGTH_SHORT).show();
        }
    }

    // Импорт файла
    private static void importFile(File file, Activity activity) {
        // Если нет разрешения на запись
        if (!isExternalStorageReadable() || !canWriteStorage(activity)) {
            Toast.makeText(activity, "Нет разрешения на чтение/запись!", Toast.LENGTH_SHORT).show();
            return;
        }
        File backup = new File(getFolder(), file.getName());
        if (!backup.exists()) {
            Toast.makeText(activity, "Файлы бэкапа отсутствуют!", Toast.LENGTH_LONG).show();
            return;
        }
        if (file.exists()) {
            file.delete();
        }
        if (copy(backup, file)) {
            Toast.makeText(activity, "Успешно!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(activity, "Ошибка!", Toast.LENGTH_SHORT).show();
        }
    }

    // Получение пути к папке бэкапов
    // [SDCARD]/Documents/ff-girl/backup/
    @NonNull
    private static File getFolder() {
        File folder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), Constants.common.TAG + "/backup");
        if (!folder.exists()) {
            folder.mkdirs();
        }
        return folder;
    }

    // Проверка на возможность записи
    private static boolean canWriteStorage(Activity activity) {
        return ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    // Копирование файлов тудым-сюдым
    private static boolean copy(File inFile, File outFile) {
        FileInputStream in;
        FileOutputStream out;
        try {
            in = new FileInputStream(inFile);
            out = new FileOutputStream(outFile);
            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();

            // write the output file
            out.flush();
            out.close();
            return true;
        } catch (Exception e) {
            Toast.makeText(App.ctx(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return false;
    }

    /* Checks if external storage is available for read and write */
    private static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    /* Checks if external storage is available to at least read */
    private static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);
    }
}
