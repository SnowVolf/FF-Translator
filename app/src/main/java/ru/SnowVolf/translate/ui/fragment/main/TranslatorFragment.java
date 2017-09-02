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

package ru.SnowVolf.translate.ui.fragment.main;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.kcode.lib.UpdateWrapper;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.SnowVolf.translate.App;
import ru.SnowVolf.translate.BuildConfig;
import ru.SnowVolf.translate.R;
import ru.SnowVolf.translate.api.yandex.YandexAPI;
import ru.SnowVolf.translate.api.yandex.language.Language;
import ru.SnowVolf.translate.api.yandex.translate.Translate;
import ru.SnowVolf.translate.history.HistoryItem;
import ru.SnowVolf.translate.model.HistoryDbModel;
import ru.SnowVolf.translate.net.NetworkStateHelper;
import ru.SnowVolf.translate.preferences.Constants;
import ru.SnowVolf.translate.preferences.Preferences;
import ru.SnowVolf.translate.ui.activity.BrowserActivity;
import ru.SnowVolf.translate.ui.activity.FullscreenActivity;
import ru.SnowVolf.translate.ui.activity.HistoryFavActivity;
import ru.SnowVolf.translate.ui.activity.SettingsActivity;
import ru.SnowVolf.translate.ui.fragment.NativeContainerFragment;
import ru.SnowVolf.translate.ui.widget.ExtendedEditText;
import ru.SnowVolf.translate.util.FragmentUtil;
import ru.SnowVolf.translate.util.KeyboardUtil;
import ru.SnowVolf.translate.util.UiErrorHandler;
import ru.SnowVolf.translate.util.Utils;
import ru.SnowVolf.translate.util.runtime.Logger;
import ru.SnowVolf.translate.util.runtime.RuntimeUtil;
import ru.SnowVolf.translate.util.speech.SpeechGirl;

/**
 * Created by Snow Volf on 14.08.2017, 0:42
 */

public class TranslatorFragment extends NativeContainerFragment {
    public static String FRAGMENT_TAG = "translator_fragment";
    private View rootView;
    @BindView(R.id.circular_layout) RelativeLayout mAsyncProgress;
    @BindView(R.id.spinner_from) Spinner mSpinnerFrom;
    @BindView(R.id.spinner_to) Spinner mSpinnerTo;
    @BindView(R.id.inputText) ExtendedEditText mFromLanguage;
    @BindView(R.id.resultText) ExtendedEditText mToLanguage;
    @BindView(R.id.switch_container) Toolbar mBottomPanel;
    @BindView(R.id.outputContainer) TextInputLayout mToContainer;
    @BindView(R.id.button_container) RelativeLayout mButtonCnt;
    @BindView(R.id.button_tr_site) Button mButtonGoToSite;
    @BindView(R.id.switch_button) ImageButton mButtonSwitch;
    @BindView(R.id.button_cancel_task) ImageButton mButtonCancelTask;

    Class aClass = this.getClass();
    private String mOriginal, mTranslated, mTempData, mTempData2;
    HistoryDbModel mDbModel;
    public Language valFrom = null;
    public Language valTo = null;
    private int spinnerPosition1;
    private int spinnerPosition2;
    private int tempInt1, tempInt2;
    AsyncTranslation translation;

    public static TranslatorFragment newInstance(int position1, int position2, String param1, boolean forceTranslate) {
        TranslatorFragment fragment = new TranslatorFragment();
        Bundle args = new Bundle();
        args.putInt(Constants.intents.INTENT_FROM, position1);
        args.putInt(Constants.intents.INTENT_TO, position2);
        args.putString(Constants.intents.INTENT_SOURCE, param1);
        args.putBoolean(Constants.intents.INTENT_FORCE_TRANSLATE, forceTranslate);
        fragment.setArguments(args);
        return fragment;
    }
    
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setRetainInstance(true);
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);

        if (getArguments() != null){
            tempInt1 = getArguments().getInt(Constants.intents.INTENT_FROM);
            tempInt2 = getArguments().getInt(Constants.intents.INTENT_TO);
            mTempData = getArguments().getString(Constants.intents.INTENT_SOURCE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.content_translator, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Проверка обновлений
        initUpdater();
        // Восстановление API ключа из настроек
        Translate.setKey(App.ctx().getPreferences().getString(Constants.prefs.API_KEY, ""));

        mButtonSwitch.setOnClickListener(this::switchLanguage);
        mButtonCancelTask.setOnClickListener(this::cancelAsyncTask);
        // Задание overflow иконки
        Drawable overflow = AppCompatResources.getDrawable(getActivity(), R.drawable.ic_menu);
        mBottomPanel.setOverflowIcon(overflow);
        // Инициализация меню.
        // Меню доступное через аппаратную кнопку НЕ БУДЕТ РАБОТАТЬ
        // Причина: Google забила болт на ее нормальную поддержку, а я не стал заморачиваться
        mBottomPanel.setOnMenuItemClickListener(m -> {
            int id = m.getItemId();
            switch (id){
                case R.id.action_settings: {
                    startActivity(new Intent(getActivity(), SettingsActivity.class));
                }
                return true;
                case R.id.action_history_fav:{
                    startActivity(new Intent(getActivity(), HistoryFavActivity.class));
                }
                return true;
                case R.id.action_translate:
                    if (valFrom != valTo)
                        translateText();
                    return true;
            } return super.onOptionsItemSelected(m);
        });

        mBottomPanel.inflateMenu(R.menu.menu_translator);
        // Инцициализация вспомогательного класса для работы с БД истории
        mDbModel = new HistoryDbModel(getActivity());
        // Кнопка перевода сайтов
        // Intent на другую Activity
        mButtonGoToSite.setOnClickListener(v -> {
            Intent mIntent = new Intent(getActivity(), BrowserActivity.class);
            mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            mIntent.putExtra("intent.url", mFromLanguage.getText().toString());
            startActivity(mIntent);
        });

        // Инициализация Spinner
        // Создание адаптера
        ArrayAdapter<?> mAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.supported_lang,  android.R.layout.simple_spinner_item);
        // Задание ресурса для открытого списка
        mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Установка адаптера
        mSpinnerTo.setAdapter(mAdapter);
        mSpinnerFrom.setAdapter(mAdapter);
        // Эти 2 int нужны для задания нужного языка при переходе из истории или избранного
        if (getArguments() == null) {
            tempInt1 = getActivity().getIntent().getIntExtra(Constants.intents.INTENT_FROM, Preferences.getSpinnerPosition(Constants.prefs.SPINNER_1));
            tempInt2 = getActivity().getIntent().getIntExtra(Constants.intents.INTENT_TO, Preferences.getSpinnerPosition(Constants.prefs.SPINNER_2));
        }
        Logger.log("Get int extra :: " + tempInt1 + " : " + tempInt2);
        mSpinnerFrom.setSelection(tempInt1);
        mSpinnerTo.setSelection(tempInt2);
        spinnerHelper();
        // Эти 2 String нужны для установки значений ТЕКСТ-ПЕРЕВОД в соответствующие поля
        if (getArguments() == null) {
            try {
                mTempData = getActivity().getIntent().getStringExtra(Constants.intents.INTENT_SOURCE);
                mTempData2 = getActivity().getIntent().getStringExtra(Constants.intents.INTENT_TRANSLATED);
            } catch (NullPointerException e) {
                UiErrorHandler.get().handle(e, mToLanguage);
            }
        }
        if (Utils.isNotNull(mTempData)){
            mToContainer.setVisibility(View.VISIBLE);
            mButtonCnt.setVisibility(View.GONE);
            mFromLanguage.setText(mTempData);
            mFromLanguage.setSelection(mTempData.length());
        }
        if (Utils.isNotNull(mTempData2)) {
            mToLanguage.setText(mTempData2);
            mToLanguage.setSelection(mTempData2.length());
        }
        // Предложение для перевода текста из буфера при старте
        // Выводится только если в буфере есть что переводить, и поставлена соотв. галка в настройках
        if (Preferences.isClipboardTranslatable() && Utils.hasClipData() && mTempData == null) {
            final Handler copyHandler = new Handler();
            copyHandler.postDelayed(() -> Snackbar.make(mBottomPanel, R.string.text_in_clip_detected, Snackbar.LENGTH_LONG).setDuration(3000).setAction(R.string.action_translate, view ->{
                mFromLanguage.setText(Utils.getTextFromClipboard());
                translateText();
            }).show(), 700);
        }
        if (Preferences.isShowKeyboardAllowed()){
            mFromLanguage.requestFocus();
            KeyboardUtil.showKeyboard();
        }

        // Инициализация TextWatcher для отслеживания пустоты поля с переводом
        setupTextWatcher();
        // Инициализация кнопок КОПИРОВАТЬ, ВСТАВИТЬ и т.д.
        setupControls();
        if (getArguments() != null) {
            if (getArguments().getBoolean(Constants.intents.INTENT_FORCE_TRANSLATE)) {
                translateText();
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();

        // Eще раз восстанавливаем ключ, если он вдруг изменился
        // TODO: Сделать сравнение предыдущего и нового ключа, чтоб один и тот же не устанавливать [04.08.2017]
        Preferences.setKey();
        if (mFromLanguage != null) {
            // Применение размера шрифта из настроек
            mFromLanguage.setTextSize(App.ctx().getPreferences().getInt(Constants.prefs.UI_FONTSIZE, 16));
            // Перевод по ENTER
            if (Preferences.isReturnAllowed()) {
                mFromLanguage.setOnEditorActionListener((textView, i, keyEvent) -> {
                    if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                        translateText();
                        KeyboardUtil.hideKeyboard(getActivity());
                    }
                    return true;
                });
            }
        }
        else Logger.i(aClass, "mFromLanguage field NULL");
        if (mToLanguage != null)
            mToLanguage.setTextSize(App.ctx().getPreferences().getInt(Constants.prefs.UI_FONTSIZE, 16));
        else  Logger.i(aClass, "mToLanguage field NULL");
        if (mSpinnerFrom != null) {
            // Если включена опция определения языка
            if (Preferences.isDetectAllowed()) {
                mSpinnerFrom.setEnabled(false);
            } else {
                mSpinnerFrom.setEnabled(true);
            }
        }
    }

    private void setupTextWatcher(){
        mFromLanguage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Если текст в поле ввода соответствует URL адресу
                if (!charSequence.toString().isEmpty() && !charSequence.toString().matches(Patterns.WEB_URL.toString())) {
                    mToContainer.setVisibility(View.VISIBLE);
                    mButtonCnt.setVisibility(View.GONE);
                }
            }

            // Проверка по мере набора текста
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                charSequence = mFromLanguage.getText();
                // Если не пусто и соответствует URL
                if (!charSequence.toString().isEmpty() && charSequence.toString().matches(Patterns.WEB_URL.toString())){
                    mToContainer.setVisibility(View.GONE);
                    mButtonCnt.setVisibility(View.VISIBLE);
                } else if (!charSequence.toString().isEmpty() && !charSequence.toString().matches(Patterns.WEB_URL.toString())){
                    // Если не пусто и не соответствует URL
                    mToContainer.setVisibility(View.VISIBLE);
                    mButtonCnt.setVisibility(View.GONE);
                } else {
                    // Иначе - Гитлер капут
                    mToContainer.setVisibility(View.GONE);
                    mButtonCnt.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }

    /**
     * Инициализация кнопок ВСТАВИТЬ, ОЧИСТИТЬ, КОПИРОВАТЬ, ПОДЕЛИТЬСЯ, НА ПОЛНЫЙ ЭКРАН
     */
    private void setupControls(){
        ImageButton mBtnPaste = rootView.findViewById(R.id.bar_paste);
        ImageButton mBtnMic = rootView.findViewById(R.id.bar_mic);
        ImageButton mBtnClear = rootView.findViewById(R.id.bar_clear);
        ImageButton mBtnCopy = rootView.findViewById(R.id.bar_copy);
        ImageButton mBtnShare = rootView.findViewById(R.id.bar_share);
        ImageButton mBtnFullscreen = rootView.findViewById(R.id.bar_fullscreen);

        // Установка слушателей нажатий для всех кнопок
        mBtnPaste.setOnClickListener(v -> mFromLanguage.setText(Utils.getTextFromClipboard()));
        mBtnClear.setOnClickListener(v -> {
            mFromLanguage.setText("");
            mToLanguage.setText("");
            nullable();
        });
        mBtnMic.setOnClickListener(v -> verifyMic());
        mBtnCopy.setOnClickListener(v -> {
            if (!mToLanguage.getText().toString().isEmpty()) {
                Utils.copyToClipboard(mToLanguage.getText().toString());
                Snackbar.make(mBottomPanel, R.string.copied_to_clipboard, Snackbar.LENGTH_SHORT).show();
            } else {
                Snackbar.make(mBottomPanel, R.string.err_nothing_copy, Snackbar.LENGTH_SHORT).show();
            }
        });
        mBtnShare.setOnClickListener(v -> startActivity(Intent.createChooser(
                new Intent(Intent.ACTION_SEND).setType("text/plain")
                        .putExtra(Intent.EXTRA_TEXT, mToLanguage.getText().toString()),
                getString(R.string.send)
        )));
        mBtnFullscreen.setOnClickListener(v -> {
            if (!mToLanguage.getText().toString().isEmpty()) {
                final Intent fullScreen = new Intent(getActivity(), FullscreenActivity.class);
                fullScreen.putExtra(Constants.intents.INTENT_TRANSLATED, mToLanguage.getText().toString());
                startActivity(fullScreen);
            } else {
                Snackbar.make(mBottomPanel, R.string.err_fullscreen, Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Инициализация проверки обновлений
     */
    private void initUpdater(){
        Logger.i(aClass, "initUpdater()");
        if (Preferences.isUpdateAllowed()) {
            UpdateWrapper updateWrapper = new UpdateWrapper.Builder(getActivity())
                    .setTime(Constants.time.SIX_HOUR)
                    .setNotificationIcon(R.drawable.ic_notification_update)
                    .setUrl(Constants.common.UPDATE_URL)
                    .setIsShowToast(false)
                    .setCallback(model -> Log.d(Constants.common.TAG, "new version :: " + model.getVersionName()))
                    .build();
            updateWrapper.start();
        }
    }

    // Управление и переключение языков во всплывающем списке
    private void spinnerHelper(){
        Logger.i(aClass, "spinnerHelper()");
        mSpinnerFrom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            public void onItemSelected(AdapterView<?> parent, View is, int pos, long id) {
                spinnerPosition1 = mSpinnerFrom.getSelectedItemPosition();
                // Запоминаем текущую позицию Spinner
                Preferences.setSpinnerPosition(Constants.prefs.SPINNER_1, spinnerPosition1);
                for (int i = 0; i < mSpinnerFrom.getCount(); i++) {
                    if (pos == i){
                        valFrom = App.langs[i];
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
                // Запоминаем текущую позицию Spinner
                Preferences.setSpinnerPosition(Constants.prefs.SPINNER_2, spinnerPosition2);
                for (int i = 0; i < mSpinnerTo.getCount(); i++) {
                    if (pos == i){
                        valTo = App.langs[i];
                        Preferences.setToLang(valTo);
                    }
                }
            }
            public void onNothingSelected(AdapterView<?> parent){
                valTo = Language.RUSSIAN;
            }
        });
    }

    // Перевод текста (Нажатие кнопки Меню > Перевести)
    private void translateText() {
        Logger.i(aClass, "translateText()");
        mOriginal = mFromLanguage.getText().toString();
        // Если есть что переводить
        if (!mOriginal.isEmpty()) {
            // Проверка на доступность сети
            if (NetworkStateHelper.isAnyNetworkAvailable()) {
                try {
                    translation = new AsyncTranslation();
                    translation.execute(mOriginal);
                } catch (Exception ex) {
                    UiErrorHandler.get().handle(ex, mToLanguage);
                }
            } else {
                FragmentUtil.ctx().iterate(getActivity(), R.id.main_container,
                        NoNetworkConnectionFragment.newInstance(0, spinnerPosition1, spinnerPosition2, mOriginal));
            }
        }
    }

    // Смена языков местами
    public void switchLanguage(View v){
        Logger.i(aClass, "switchLanguage(View v)");
        int pos1 = mSpinnerTo.getSelectedItemPosition();
        int pos2 = mSpinnerFrom.getSelectedItemPosition();
        if (pos1 != pos2){
            // Анимация переворота стрелки
            Animation revertAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.revert_anim);
            v.setAnimation(revertAnim);
            mSpinnerTo.setSelection(pos2);
            mSpinnerFrom.setSelection(pos1);
        } else {
            // Если языки одинаковы
            Snackbar.make(mBottomPanel, R.string.err_lang_identical, Snackbar.LENGTH_SHORT).show();
        }
    }

    // Проверка есть ли разрешение на доступ к микрофону
    private void verifyMic(){
        // Если меньше Marshmallow - пропускаем без очереди
        if (Build.VERSION.SDK_INT <= 22){
            SpeechGirl.getCtx().openInput(getActivity());
        } else {
            // Если Marshmallow, и разрешения не даны
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                RuntimeUtil.audio(getActivity());
            } else {
                // Всё OK
                SpeechGirl.getCtx().openInput(getActivity());
            }
        }
    }

    // Вызывается по завершении перевода речи в текст
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case SpeechGirl.REQUEST_GIRL_READY:{
                // Если перевод закончен, и есть что положить в TextView
                if (resultCode == Activity.RESULT_OK && data != null) {
                    ArrayList<String> speechText = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    mFromLanguage.setText(speechText.get(0));
                    mFromLanguage.setSelection(mFromLanguage.getText().length());
                }
            } break;
        }
    }

    // Зануляем временные переменные
    private void nullable(){
        Logger.i(aClass, "nullable()");
        mOriginal = null;
        mTranslated = null;
        mTempData = null;
        mTempData2 = null;
    }

    // Останавливаем перевод (если нажата соотв. кнопка)
    public void cancelAsyncTask(View v){
        if (!translation.isCancelled()){
            translation.cancel(false);
            Logger.i(aClass, "cancelAsyncTask(View v) != true");
        }
    }

    /**
     * Класс асинхронного перевода
     * Подключение к серверу идет в отдельном потоке.
     * Там же идет и парсинг JSON
     * На выходе получаем только сам текст
     */
    private class AsyncTranslation extends AsyncTask<String, Integer, String> {
        @Override
        protected void onPreExecute(){
            Logger.i(aClass, "onPreExecute()");
            // Показываем прогресс
            mAsyncProgress.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... strings) {
            Logger.i(aClass, "doInBackground(String... strings)");
            String exec = strings[0];
            try {
                // Пока не отменено
                if (!isCancelled()){
                    Logger.i(aClass, "!isCancelled()");
                    if (!Preferences.isDetectAllowed()) {
                        mTranslated = Translate.execute(exec, valFrom, valTo);
                    } else {
                        mTranslated = Translate.executeAuto(exec, valTo);
                    }
                    try {
                        HistoryItem item = new HistoryItem(System.currentTimeMillis());
                        item.setToPosition(spinnerPosition1);
                        item.setFromPosition(spinnerPosition2);
                        item.setTitle(exec);
                        item.setSource(exec);
                        item.setTranslation(mTranslated);
                        item.setFromCode(Preferences.getFromLang());
                        item.setToCode(Preferences.getToLang());
                        mDbModel.add(item);
                    } catch (NullPointerException ignored) {}
                    // Конец пока
                } else return "VolfGirl";
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
            // В зависимости от того какой ответ пришел от сервера, делаем следующее:
            // Если что-то пошло не так, и код не равен 200 (успешно)
            if (YandexAPI.getResponseCode() != YandexAPI.RESPONSE_SUCCESS) {
                FragmentUtil.ctx().iterate(getActivity(), R.id.main_container, NoNetworkConnectionFragment.newInstance(YandexAPI.getResponseCode(), spinnerPosition1, spinnerPosition2, mOriginal));
            } else {
                // Всё OK. Можно вставлять текст в поле
                mToLanguage.setText(mTranslated);
            }
            // Убираем прогресс
            mAsyncProgress.setVisibility(View.GONE);
        }

        // Если пользователь отменил перевод
        @Override
        protected void onCancelled(){
            Logger.i(aClass, "onCancelled()");
            Snackbar.make(mBottomPanel, R.string.async_task_canceled, Snackbar.LENGTH_LONG).show();
            mAsyncProgress.setVisibility(View.GONE);
        }
    }

    // Вызывается сразу после получения разрешений
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case RuntimeUtil.REQUEST_AUDIO:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    SpeechGirl.getCtx().openInput(getActivity());
                } else {
                    Snackbar.make(mSpinnerFrom, R.string.no_net_connection, Snackbar.LENGTH_INDEFINITE).setAction(R.string.action_settings, view -> {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", BuildConfig.APPLICATION_ID, null);
                        intent.setData(uri);
                        startActivityForResult(intent, 5);
                        Toast.makeText(getActivity(), R.string.goto_settings_toast, Toast.LENGTH_SHORT).show();
                    }).show();
                }
                break;
        }
    }
}
