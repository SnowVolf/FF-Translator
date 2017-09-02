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

import android.content.Context;
import android.text.format.DateUtils;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import ru.SnowVolf.translate.App;
import ru.SnowVolf.translate.R;

/**
 * Created by Snow Volf on 06.08.2017, 9:52
 */

public class CalendarUtils {
    /**
     * Get a time string relative to now
     * @param date A {@link DateTime}
     * @return CharSequence time
     */
    public static CharSequence getRelativeDisplayTime(DateTime date) {
        final Context context = App.getContext();
        final CharSequence value;
        long now = System.currentTimeMillis();
        long time = date.getMillis();
        long delta = now - time;

        if (delta <= DateUtils.SECOND_IN_MILLIS) {
            DateTimeFormatter fmt =
                    DateTimeFormat.forPattern(context.getString(R.string.joda_time_fmt_pattern));
            value = context.getString(R.string.now_fmt, date.toString(fmt));
        } else {
            value =
                    DateUtils.getRelativeDateTimeString(context, time,
                            DateUtils.SECOND_IN_MILLIS, DateUtils.DAY_IN_MILLIS,
                            DateUtils.FORMAT_ABBREV_ALL);
        }
        return value;
    }
}
