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

    public static StringBuilder parseText(String fileUrl){
        final StringBuilder sb = new StringBuilder();
        try {
            BufferedReader br1 = new BufferedReader(new InputStreamReader(App.ctx().getAssets().open(fileUrl), "UTF-8"));
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
