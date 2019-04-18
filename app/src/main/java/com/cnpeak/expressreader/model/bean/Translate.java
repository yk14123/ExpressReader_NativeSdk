package com.cnpeak.expressreader.model.bean;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Translate implements Serializable {

    private List<TranslationsBean> translations;

    public static List<Translate> arrayTranslateResultFromData(String str) {

        Type listType = new TypeToken<ArrayList<Translate>>() {
        }.getType();

        return new Gson().fromJson(str, listType);
    }

    public List<TranslationsBean> getTranslations() {
        return translations;
    }

    public void setTranslations(List<TranslationsBean> translations) {
        this.translations = translations;
    }

    public static class TranslationsBean {
        /**
         * text : Since the implementation of the active fiscal policy in 1998, China has not only successfully responded to the impact of the Asian financial crisis and the subprime mortgage crisis in the United States, but also effectively promoted structural adjustment and upgrading. The Central Economic Work Conference stressed that the positive fiscal policy in 2019 should be effective. But so far, people's understanding of active fiscal policy is not consistent: Some people equate active fiscal policy with expansionary fiscal policy, others equate China's structural tax reduction with the supply school tax reduction. What is the goal orientation of China to implement a positive fiscal policy? We will have a discussion on this issue in three ways.
         * to : en
         */

        private String text;
        private String to;

        public static List<TranslationsBean> arrayTranslationsBeanFromData(String str) {

            Type listType = new TypeToken<ArrayList<TranslationsBean>>() {
            }.getType();

            return new Gson().fromJson(str, listType);
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getTo() {
            return to;
        }

        public void setTo(String to) {
            this.to = to;
        }
    }
}
