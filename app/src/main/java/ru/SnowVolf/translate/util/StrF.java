package ru.SnowVolf.translate.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import ru.SnowVolf.translate.App;

/**
 * Created by Snow Volf on 15.06.2017, 2:12
 */
//Работа с большими строками

public class StrF {
    public static StringBuilder parseHtml(String url){
        final StringBuilder sb = new StringBuilder();
        try {
            BufferedReader br1 = new BufferedReader(new InputStreamReader(App.ctx().getAssets().open(url), "UTF-8"));
            String line;
            while ((line = br1.readLine()) != null) {
                sb.append(line).append("<br>");
            }
            br1.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb;
    }

    public static StringBuilder parseText(String url){
        final StringBuilder sb = new StringBuilder();
        try {
            BufferedReader br1 = new BufferedReader(new InputStreamReader(App.ctx().getAssets().open(url), "UTF-8"));
            String line;
            while ((line = br1.readLine()) != null) {
                sb.append(line).append("\n");
            }
            br1.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb;
    }
}
