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

package ru.SnowVolf.translate.clipboard;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.io.Serializable;

import ru.SnowVolf.translate.App;
import ru.SnowVolf.translate.util.runtime.Logger;

/**
 * This class represents the data for a single clipboard entry
 */
class ClipItem implements Serializable {
    private String mText;

    private ClipItem(String text) {
        init();
        mText = text;
    }

    /**
     * Get the text on the Clipboard as a ClipItem
     * @param clipboard The {@link ClipboardManager}
     * @return Clipboard content as ClipItem
     */
    @Nullable
    static ClipItem getFromClipboard(ClipboardManager clipboard) {
        final ClipData clipData = clipboard.getPrimaryClip();
        if (clipData == null) {
            return null;
        }

        final ClipData.Item item = clipData.getItemAt(0);

        CharSequence clipText = item.getText();
        if (clipText == null) {
            // If the Uri contains something, just coerce it to text
            if (item.getUri() != null) {
                try {
                    clipText = item.coerceToText(App.getContext());
                } catch (Exception ex) {
                    Logger.log(ex);
                    return null;
                }
            }
        }
        ClipItem clipItem = null;
        if ((clipText != null) && (TextUtils.getTrimmedLength(clipText) > 0)) {
            clipItem = new ClipItem(String.valueOf(clipText));
        }

        return clipItem;
    }


    public String getText() {
        return mText;
    }

    public void setText(String text) {
        mText = text;
    }


    /**
     * Initialize the members
     */
    private void init() {
        mText = "";
    }
}
