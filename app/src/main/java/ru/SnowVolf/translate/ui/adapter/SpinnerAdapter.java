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

package ru.SnowVolf.translate.ui.adapter;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

import ru.SnowVolf.translate.preferences.Preferences;

/**
 * Created by Snow Volf on 13.08.2017, 12:18
 */

public class SpinnerAdapter extends ArrayAdapter<String> {
    public SpinnerAdapter(@NonNull Context context, @LayoutRes int resource) {
        super(context, resource);
    }

    public SpinnerAdapter(@NonNull Context context, @LayoutRes int resource, @IdRes int textViewResourceId) {
        super(context, resource, textViewResourceId);
    }

    public SpinnerAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull String[] objects) {
        super(context, resource, objects);
    }

    public SpinnerAdapter(@NonNull Context context, @LayoutRes int resource, @IdRes int textViewResourceId, @NonNull String[] objects) {
        super(context, resource, textViewResourceId, objects);
    }

    public SpinnerAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<String> objects) {
        super(context, resource, objects);
    }

    public SpinnerAdapter(@NonNull Context context, @LayoutRes int resource, @IdRes int textViewResourceId, @NonNull List<String> objects) {
        super(context, resource, textViewResourceId, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return convertView;
    }
}
