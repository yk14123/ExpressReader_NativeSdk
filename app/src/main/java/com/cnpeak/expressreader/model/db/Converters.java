package com.cnpeak.expressreader.model.db;

import androidx.room.TypeConverter;
import android.text.TextUtils;

import com.cnpeak.expressreader.model.bean.IssueList;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Room自定义复杂数据转换成基本数据转化器
 * 主要职责：
 * Android Room转化器将复杂的数据转化成String存储到本地数据库中
 */
public class Converters {


    /**
     * Convert url list to string.
     *
     * @param source the source
     * @return the string
     */
    @TypeConverter
    public static String convertUrlListToStr(List<String> source) {
        if (source != null && source.size() != 0) {
            StringBuilder buffer = new StringBuilder(source.size());
            for (String url : source) {
                buffer.append(url);
                buffer.append(",");
            }
            return buffer.toString();
        } else return "";
    }


    /**
     * Convert str to url list.
     *
     * @param urls the urls
     * @return the list
     */
    @TypeConverter
    public static List<String> convertStrToUrlList(String urls) {
        List<String> urlList = new ArrayList<>();
        if (!TextUtils.isEmpty(urls) && urls.contains(",")) {
            String[] split = urls.split(",");
            urlList.addAll(Arrays.asList(split));
        }
        return urlList;
    }


    /**
     * Convert issue list to string.
     *
     * @param source the source
     * @return the string
     */
    @TypeConverter
    public static String convertIssueListToStr(List<IssueList> source) {
        if (source != null && source.size() != 0) {
            StringBuilder buffer = new StringBuilder(source.size());
            buffer.append("[");
            Gson gson = new Gson();
            for (int i = 0; i < source.size(); i++) {
                String issueBeanStr = gson.toJson(source.get(i));
                buffer.append(issueBeanStr);
                if (i == source.size() - 1) {
                    buffer.append("]");
                } else {
                    buffer.append(",");
                }
            }
            return buffer.toString();
        } else return "";
    }

    /**
     * Convert str to issue list.
     *
     * @param issueStr the issue str
     * @return the list
     */
    @TypeConverter
    public static List<IssueList> convertStrToIssueList(String issueStr) {
        Gson gson = new Gson();
        return gson.fromJson(issueStr,
                new TypeToken<List<IssueList>>() {
                }.getType());
    }

}
