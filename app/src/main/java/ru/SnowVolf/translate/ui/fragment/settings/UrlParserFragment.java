package ru.SnowVolf.translate.ui.fragment.settings;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import butterknife.BindView;
import ru.SnowVolf.translate.R;
import ru.SnowVolf.translate.ui.fragment.NativeContainerFragment;
import ru.SnowVolf.translate.util.runtime.Logger;

/**
 * Created by Snow Volf on 24.07.2017, 6:37
 */

public class UrlParserFragment extends NativeContainerFragment {
    @BindView(R.id.scrollContainer) ScrollView mainScroll;
    @BindView(R.id.parseContent) TextView content;
    @BindView(R.id.buttonContainer) RelativeLayout emptyContainer;
    @BindView(R.id.buttonStopConnect) Button buttonStop;
    public String WEB_URL = "";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_web_parser, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    private class ParseNet extends AsyncTask<String, Void, String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mainScroll.setVisibility(View.GONE);
        }

        @Override
        protected String doInBackground(String... strings) {
            try {

            } catch (Exception ex){
                Logger.e(this.getClass(), ex);
            }
            return "";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }
}
