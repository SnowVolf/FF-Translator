package ru.SnowVolf.translate.browser;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.webkit.WebView;


/**
 * Created by Snow Volf on 25.06.2017, 16:18
 */

public class ExtendedWebView extends WebView {
    private ExtendedWebView.OnStartActionModeListener actionModeListener;

    @TargetApi(19)
    public final void evalJs(@NonNull String js) {
        if(Build.VERSION.SDK_INT >= 19) {
            evaluateJavascript(js, null);
        } else {
            loadUrl("javascript:" + js);
        }
    }

    public final void setActionModeListener(@NonNull ExtendedWebView.OnStartActionModeListener actionModeListener) {
        this.actionModeListener = actionModeListener;
    }

    @NonNull
    public ActionMode startActionMode(@NonNull ActionMode.Callback callback) {
        return this.myActionMode(callback, 0);
    }

    @NonNull
    public ActionMode startActionMode(@NonNull ActionMode.Callback callback, int type) {
        return this.myActionMode(callback, type);
    }

    private final ActionMode myActionMode(ActionMode.Callback callback, int type) {
        ActionMode actionMode;
        if(Build.VERSION.SDK_INT >= 23) {
            actionMode = super.startActionMode(callback, type);
        } else {
            actionMode = this.startActionMode(callback);
        }

        if(this.actionModeListener != null) {
            ExtendedWebView.OnStartActionModeListener var4 = this.actionModeListener;
            var4.OnStart(actionMode, callback, type);
        }

        return actionMode;
    }

    protected void onCreateContextMenu(@NonNull ContextMenu contextMenu) {
        super.onCreateContextMenu(contextMenu);
        this.requestFocusNodeHref((new Handler(msg -> {
            HitTestResult result = ExtendedWebView.this.getHitTestResult();
            return true;
        })).obtainMessage());
    }

    public ExtendedWebView(@NonNull Context ctx) {
        super(ctx);
    }

    public ExtendedWebView(@NonNull Context ctx, @NonNull AttributeSet attributeSet) {
        super(ctx, attributeSet);
    }

    public ExtendedWebView(@NonNull Context ctx, @NonNull AttributeSet attributeSet, int defStyleAttr) {
        super(ctx, attributeSet, defStyleAttr);
    }


    public interface OnStartActionModeListener {
        void OnStart(@NonNull ActionMode var1, @NonNull ActionMode.Callback var2, int var3);
    }
}

