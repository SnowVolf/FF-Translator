package ru.SnowVolf.translate.ui.activity;


import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Process;
import android.provider.Settings;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.SnowVolf.translate.App;
import ru.SnowVolf.translate.R;
import ru.SnowVolf.translate.api.yandex.YandexAPI;
import ru.SnowVolf.translate.api.yandex.language.Language;
import ru.SnowVolf.translate.api.yandex.translate.Translate;
import ru.SnowVolf.translate.history.HistoryItem;
import ru.SnowVolf.translate.model.HistoryDbModel;
import ru.SnowVolf.translate.net.NetworkStateHelper;
import ru.SnowVolf.translate.ui.widget.ExtendedEditText;
import ru.SnowVolf.translate.util.Constants;
import ru.SnowVolf.translate.util.KeyboardUtil;
import ru.SnowVolf.translate.util.Preferences;
import ru.SnowVolf.translate.util.UiErrorHandler;
import ru.SnowVolf.translate.util.Utils;
import ru.SnowVolf.translate.util.runtime.Logger;
import ru.SnowVolf.translate.util.runtime.RuntimeUtil;
import ru.SnowVolf.translate.util.speech.SpeechGirl;

public class TranslatorActivity extends BaseActivity {
    @BindView(R.id.circular_layout) RelativeLayout mAsyncProgress;
    @BindView(R.id.spinner_from) Spinner mSpinnerFrom;
    @BindView(R.id.spinner_to) Spinner mSpinnerTo;
    @BindView(R.id.inputText) ExtendedEditText mFromLanguage;
    @BindView(R.id.resultText) ExtendedEditText mToLanguage;
    @BindView(R.id.switch_container) Toolbar mBottomPanel;
    @BindView(R.id.outputContainer) TextInputLayout mToContainer;
    @BindView(R.id.button_container) RelativeLayout mButtonCnt;
    @BindView(R.id.button_tr_site) Button mButtonGoToSite;

    Class aClass = this.getClass();
    private String mOriginal, mTranslated, mTempData, mTempData2;
    HistoryDbModel mDataHandler;
    public Language valFrom = null;
    public Language valTo = null;
    private int spinnerPosition1, spinnerPosition2;
    AsyncTranslation translation;
    //Активити создана
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Logger.i(aClass, "onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translator);
        ButterKnife.bind(this);
        Translate.setKey(App.ctx().getPreferences().getString(Constants.Prefs.API_KEY, ""));
        mBottomPanel.setOnMenuItemClickListener(m -> {
            int id = m.getItemId();
            switch (id){
                case R.id.action_settings: {
                    startActivity(new Intent(this, SettingsActivity.class));
                }
                return true;
                case R.id.action_history_fav:{
                        startActivity(new Intent(this, HistoryFavActivity.class));
                }
                return true;
                case R.id.action_translate:
                    if (valFrom != valTo)
                        translateText();
                    return true;
                case R.id.action_mic:
                    verifyMic();
                    return true;

            } return super.onOptionsItemSelected(m);
        });

        mBottomPanel.inflateMenu(R.menu.menu_translator);
        mFromLanguage.setImeActionLabel(getString(R.string.action_translate), KeyEvent.KEYCODE_ENTER);
        mDataHandler = new HistoryDbModel(this);
        mButtonGoToSite.setOnClickListener(v -> {
            Intent mIntent = new Intent(TranslatorActivity.this, BrowserActivity.class);
            mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            mIntent.putExtra("intent.url", mFromLanguage.getText().toString());
            startActivity(mIntent);
        });

        ArrayAdapter<?> mAdapter = ArrayAdapter.createFromResource(this, R.array.supported_lang, android.R.layout.simple_spinner_item);
        mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerTo.setAdapter(mAdapter);
        mSpinnerFrom.setAdapter(mAdapter);
        mSpinnerTo.setSelection(Preferences.getSpinnerPosition(Constants.Prefs.SPINNER_2));
        mSpinnerFrom.setSelection(Preferences.getSpinnerPosition(Constants.Prefs.SPINNER_1));
        spinnerHelper();
        try {
            mTempData = getIntent().getStringExtra(Constants.Intents.INTENT_SOURCE);
            mTempData2 = getIntent().getStringExtra(Constants.Intents.INTENT_TRANSLATED);
        } catch (NullPointerException e){
            UiErrorHandler.get().handle(e, mToLanguage);
        }
        if (Utils.isNotNull(mTempData)){
            mToContainer.setVisibility(View.VISIBLE);
            mButtonCnt.setVisibility(View.GONE);
            mToLanguage.setText(mTempData);
        }
        if (Utils.isNotNull(mTempData2))
        mFromLanguage.setText(mTempData2);
        if (Preferences.isClipboardTranslatable() && Utils.hasClipData() && mTempData == null) {
            final Handler copyHandler = new Handler();
            copyHandler.postDelayed(() -> Snackbar.make(findViewById(R.id.switch_container), R.string.text_in_clip_detected, Snackbar.LENGTH_LONG).setDuration(3000).setAction(R.string.action_translate, view -> mFromLanguage.setText(Utils.getTextFromClipboard())).show(), 700);
        }
        if (Preferences.isShowKeyboardAllowed()){
            mFromLanguage.requestFocus();
            KeyboardUtil.showKeyboard();
        }
        setupTextWatcher();
        setupControls();
    }

    private String lang = null;
    //Активити возвращается в активное состояние
    @Override
    public void onResume() {
        super.onResume();
        Preferences.setKey();
        if (mFromLanguage != null) {
            mFromLanguage.setTextSize(App.ctx().getPreferences().getInt(Constants.Prefs.UI_FONTSIZE, 16));
            if (Preferences.isReturnAllowed()) {
                mFromLanguage.setOnEditorActionListener((textView, i, keyEvent) -> {
                    if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                        translateText();
                        KeyboardUtil.hideKeyboard(this);
                    }
                    return true;
                });
            }
        }
        else Logger.i(aClass, "mFromLanguage field NULL");
        if (mToLanguage != null)
            mToLanguage.setTextSize(App.ctx().getPreferences().getInt(Constants.Prefs.UI_FONTSIZE, 16));
        else  Logger.i(aClass, "mToLanguage field NULL");
        if (mSpinnerFrom != null) {
            if (Preferences.isDetectAllowed()) {
                mSpinnerFrom.setEnabled(false);
            } else {
                mSpinnerFrom.setEnabled(true);
            }
        }

        //Lang
        if (lang == null){
            lang = Preferences.getDefaultLanguage();
        }
        if (!Preferences.getDefaultLanguage().equals(lang)){
            new AlertDialog.Builder(this)
                    .setMessage(R.string.app_lang_changed)
                    .setPositiveButton(R.string.ok, (d, i) -> {
                        Intent mStartActivity = new Intent(TranslatorActivity.this, TranslatorActivity.class);
                        int mIntentPendingId = 6;
                        PendingIntent mPendingIntent = PendingIntent.getActivity(TranslatorActivity.this, mIntentPendingId, mStartActivity,PendingIntent.FLAG_CANCEL_CURRENT);
                        AlarmManager manager = (AlarmManager) TranslatorActivity.this.getSystemService(Context.ALARM_SERVICE);
                        manager.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
                        System.exit(0);
                    })
                    .setNegativeButton(android.R.string.cancel, null)
                    .show();
        }
    }

    private static long press_time = System.currentTimeMillis();
    //Нажатие кнопки назад
    @Override
    public void onBackPressed() {
        Logger.i(aClass, "onBackPressed()");
        if (Preferences.isBackNotif()) {
            if (press_time + 2000 > System.currentTimeMillis()) {
                if (Preferences.isKillAllowed()) {
                    Process.killProcess(Process.myPid());
                } else finish();
            } else {
                Toast.makeText(this, R.string.press_once_more, Toast.LENGTH_SHORT).show();
                press_time = System.currentTimeMillis();
            }
        } else {
            if (!Preferences.isKillAllowed()) {
                finish();
            } else Process.killProcess(Process.myPid());
        }

    }

    private void setupTextWatcher(){
        mFromLanguage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().isEmpty() && !charSequence.toString().matches(Patterns.WEB_URL.toString())) {
                    mToContainer.setVisibility(View.VISIBLE);
                    mButtonCnt.setVisibility(View.GONE);
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                charSequence = mFromLanguage.getText();
                if (!charSequence.toString().isEmpty() && charSequence.toString().matches(Patterns.WEB_URL.toString())){
                    mToContainer.setVisibility(View.GONE);
                    mButtonCnt.setVisibility(View.VISIBLE);
                } else if (!charSequence.toString().isEmpty() && !charSequence.toString().matches(Patterns.WEB_URL.toString())){
                    mToContainer.setVisibility(View.VISIBLE);
                    mButtonCnt.setVisibility(View.GONE);
                } else {
                    mToContainer.setVisibility(View.GONE);
                    mButtonCnt.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    private void setupControls(){
        ImageButton mBtnPaste = (ImageButton) findViewById(R.id.bar_paste);
        ImageButton mBtnClear = (ImageButton) findViewById(R.id.bar_clear);
        ImageButton mBtnCopy = (ImageButton) findViewById(R.id.bar_copy);
        ImageButton mBtnShare = (ImageButton) findViewById(R.id.bar_share);
        ImageButton mBtnFullscreen = (ImageButton) findViewById(R.id.bar_fullscreen);

        mBtnPaste.setOnClickListener(v -> mFromLanguage.setText(Utils.getTextFromClipboard()));
        mBtnClear.setOnClickListener(v -> {
            mFromLanguage.setText("");
            mToLanguage.setText("");
            nullable();
        });
        mBtnCopy.setOnClickListener(v -> {
            if (!mToLanguage.getText().toString().isEmpty()) {
                Utils.copyToClipboard(mToLanguage.getText().toString());
                Snackbar.make(findViewById(R.id.switch_container), R.string.copied_to_clipboard, Snackbar.LENGTH_SHORT).show();
            } else {
                Snackbar.make(findViewById(R.id.switch_container), R.string.err_nothing_copy, Snackbar.LENGTH_SHORT).show();
            }
        });
        mBtnShare.setOnClickListener(v -> startActivity(Intent.createChooser(
                new Intent(Intent.ACTION_SEND).setType("text/plain")
                        .putExtra(Intent.EXTRA_TEXT, mToLanguage.getText().toString()),
                getString(R.string.send)
        )));
        mBtnFullscreen.setOnClickListener(v -> {
            if (!mToLanguage.getText().toString().isEmpty()) {
                final Intent fullScreen = new Intent(this, FullscreenActivity.class);
                fullScreen.putExtra(Constants.Intents.INTENT_TRANSLATED, mToLanguage.getText().toString());
                startActivity(fullScreen);
            } else {
                Snackbar.make(findViewById(R.id.switch_container), R.string.err_fullscreen, Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    //Управление и переключение языков во всплывающем списке
    private void spinnerHelper(){
        final Language[] lng = {Language.AFRICANS, Language.ALBANIAN, Language.ARABIAN, Language.ARMENIAN, Language.AZERBAIJANI, Language.BELORUSSIAN, Language.BULGARIAN, Language.CATALAN,
                Language.CROATIAN, Language.CZECH, Language.DANISH, Language.DUTCH, Language.ENGLISH, Language.ESTONIAN, Language.FINNISH, Language.FRENCH, Language.GERMAN, Language.GEORGIAN,
                Language.GREEK, Language.HUNGARIAN, Language.ITALIAN, Language.LATVIAN, Language.LITHUANIAN, Language.MACEDONIAN, Language.NORWEGIAN, Language.POLISH, Language.PORTUGUESE,
                Language.ROMANIAN, Language.RUSSIAN, Language.SERBIAN, Language.SLOVAK, Language.SLOVENIAN, Language.SPANISH, Language.SWEDISH, Language.TURKISH, Language.UKRAINIAN };
        Logger.i(aClass, "spinnerHelper()");
        mSpinnerFrom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            public void onItemSelected(AdapterView<?> parent, View is, int pos, long id) {
                spinnerPosition1 = mSpinnerFrom.getSelectedItemPosition();
                Preferences.setSpinnerPosition(Constants.Prefs.SPINNER_1, spinnerPosition1);
                for (int i = 0; i < mSpinnerFrom.getCount(); i++) {
                    if (pos == i){
                        valFrom = lng[i];
                        Preferences.setFromLang(valFrom);
                    }
                }
            }
            public void onNothingSelected(AdapterView<?> parent){
                valFrom = Language.RUSSIAN;
            }
        });
        mSpinnerTo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            public void onItemSelected(AdapterView<?> parent, View is, int pos, long id) {
                spinnerPosition2 = mSpinnerTo.getSelectedItemPosition();
                Preferences.setSpinnerPosition(Constants.Prefs.SPINNER_2, spinnerPosition2);
                for (int i = 0; i < mSpinnerTo.getCount(); i++) {
                    if (pos == i){
                        valTo = lng[i];
                        Preferences.setToLang(valTo);
                    }
                }
            }
            public void onNothingSelected(AdapterView<?> parent){
                valTo = Language.RUSSIAN;
            }
        });
    }

    //Перевод текста (Нажатие кнопки Меню > Перевести)
    private void translateText() {
        Logger.i(aClass, "translateText()");
        mOriginal = mFromLanguage.getText().toString();
        if (!mOriginal.isEmpty()) {
            if (NetworkStateHelper.isAnyNetworkAvailable()) {
                try {
                    translation = new AsyncTranslation();
                    translation.execute(mOriginal);
                } catch (Exception ex) {
                    UiErrorHandler.get().handle(ex, mToLanguage);
                }
            } else Snackbar.make(mSpinnerFrom, R.string.no_net_connection, Snackbar.LENGTH_INDEFINITE).setAction(R.string.snackbar_network_settings, view -> {
                Intent netSettings = new Intent(Settings.ACTION_NETWORK_OPERATOR_SETTINGS);
                startActivity(netSettings);
            }).show();
        }
    }

    //Смена языков местами
    public void switchLanguage(View v){
        Logger.i(aClass, "switchLanguage(View v)");
        int pos1 = mSpinnerTo.getSelectedItemPosition();
        int pos2 = mSpinnerFrom.getSelectedItemPosition();
        if (pos1 != pos2){
            Animation revertAnim = AnimationUtils.loadAnimation(this, R.anim.revert_anim);
            v.setAnimation(revertAnim);
            mSpinnerTo.setSelection(pos2);
            mSpinnerFrom.setSelection(pos1);
        } else {
            Snackbar.make(findViewById(R.id.switch_container), R.string.err_lang_identical, Snackbar.LENGTH_SHORT).show();
        }
    }

    //Проверка есть ли разрешение на доступ к микрофону
    private void verifyMic(){
        if (Build.VERSION.SDK_INT <= 22){
            SpeechGirl.getCtx().openInput(this);
        } else {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                RuntimeUtil.audio(this);
            } else {
                SpeechGirl.getCtx().openInput(this);
            }
        }
    }

    //Вызывается после получения разрешений
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case RuntimeUtil.REQUEST_AUDIO:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    SpeechGirl.getCtx().openInput(this);
                } else {
                    Toast.makeText(this, "PERMISSION IS NOT GRANTED!!!", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    //Вызывается по завершении перевода речи в текст
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case SpeechGirl.REQUEST_GIRL_READY:{
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> speechText = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    mFromLanguage.setText(speechText.get(0));
                    mFromLanguage.setSelection(mFromLanguage.getText().length());
                }
            } break;
        }
    }

    //Зануляем переменные
    private void nullable(){
        Logger.i(aClass, "nullable()");
        mOriginal = null;
        mTranslated = null;
        mTempData = null;
        mTempData2 = null;
    }

    //Останавливаем перевод
    public void cancelAsyncTask(View v){
        if (!translation.isCancelled()){
            translation.cancel(false);
            Logger.i(aClass, "cancelAsyncTask(View v) != true");
        }
    }

    //Класс асинхронного перевода
    //Подключение к серверу идет в отдельном потоке.
    //Там же идет и парсинг JSON
    //На выходе получаем только сам текст
    private class AsyncTranslation extends AsyncTask<String, Integer, String>{
        @Override
        protected void onPreExecute(){
            Logger.i(aClass, "onPreExecute()");
            mAsyncProgress.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... strings) {
            Logger.i(aClass, "doInBackground(String... strings)");
            String exec = strings[0];
            try {
                if (!isCancelled()){//Пока не отменено
                    Logger.i(aClass, "!isCancelled()");
                    if (!Preferences.isDetectAllowed()) {
                        mTranslated = Translate.execute(exec, valFrom, valTo);
                    } else {
                        mTranslated = Translate.executeAuto(exec, valTo);
                    }
                try {
                    //if (!Preferences.isSyncTranslateAllowed())
                    mDataHandler.add(new HistoryItem(System.currentTimeMillis(), exec, mTranslated, exec));
                } catch (Exception ignored) {}
            } else return "VolfGirl";//Конец пока
            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.i("VfTr", "Translated in background :\n" + exec);
            return mTranslated;
        }

        @Override
        protected void onPostExecute(String result){
            Logger.i(aClass, "onPostExecute(String result)");
            super.onPostExecute(result);
            switch (YandexAPI.getResponseCode()) {
                case 200: mToLanguage.setText(mTranslated); break;
                case 0:
                    Snackbar.make(mToLanguage, R.string.no_net_connection, Snackbar.LENGTH_INDEFINITE).setAction(R.string.try_again, view -> translateText()).show();
                    Logger.e(aClass, "No network connection");
                    break;
            }
            if (YandexAPI.getResponseCode() != 200) {
                BottomSheetDialog errDialog = new BottomSheetDialog(TranslatorActivity.this);
                errDialog.setContentView(R.layout.dialog_simple_bottom_sheet);
                TextView message = (TextView) errDialog.findViewById(R.id.dlg_message);
                Utils.assertNull(message);
                errDialog.show();

                switch (YandexAPI.getResponseCode()) {
                    case 0: message.setText(R.string.no_net_connection); break;
                    case 402: message.setText(R.string.err_resp_402); break;
                    case 401: message.setText(R.string.err_resp_401); break;
                    case 404: message.setText(R.string.err_resp_404); break;
                    case 413: message.setText(R.string.err_resp_413); break;
                    case 422: message.setText(R.string.err_resp_422); break;
                    case 501: message.setText(R.string.err_resp_501); break;
                    default:
                            Logger.i(aClass, "Unknown response from server :: " + YandexAPI.getResponseCode());
                            message.setText(getString(R.string.err_resp_unknown) + "\n" + getString(R.string.err_resp_code_explanation) + " " + YandexAPI.getResponseCode());
                        break;
                }
            }
            mAsyncProgress.setVisibility(View.GONE);
        }

        //Если пользователь отменил задачу
        @Override
        protected void onCancelled(){
            Logger.i(aClass, "onCancelled()");
            Snackbar.make(findViewById(R.id.switch_container), R.string.async_task_canceled, Snackbar.LENGTH_LONG).show();
            mAsyncProgress.setVisibility(View.GONE);
        }
    }
}
