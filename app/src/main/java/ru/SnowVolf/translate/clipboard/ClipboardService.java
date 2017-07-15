package ru.SnowVolf.translate.clipboard;

import android.app.Service;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import ru.SnowVolf.translate.App;
import ru.SnowVolf.translate.R;
import ru.SnowVolf.translate.util.Utils;

/**
 * Created by Snow Volf on 28.06.2017, 17:14
 */

public class ClipboardService extends Service {
    public ClipboardManager mClipboardManager;

    @Override
    public void onCreate() {
        super.onCreate();
        mClipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        mClipboardManager.addPrimaryClipChangedListener(() -> {
            if (mClipboardManager.hasPrimaryClip() && mClipboardManager.getPrimaryClip().getItemAt(0).getText().toString().trim().length() > 0){
                try {
                    new ClipboardTask().execute(Utils.getTextFromClipboard().toString());
                } catch (Exception ex){
                    ex.printStackTrace();
                    Toast.makeText(App.ctx(), R.string.err_clipboard_translate, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
