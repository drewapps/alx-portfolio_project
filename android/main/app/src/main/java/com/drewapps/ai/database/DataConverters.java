package com.drewapps.ai.database;

import androidx.room.TypeConverter;

import com.drewapps.ai.items.ChatItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.drewapps.ai.items.AdapTemplateData;
import com.drewapps.ai.items.Message;
import com.drewapps.ai.items.Template;

import java.lang.reflect.Type;
import java.util.List;

public class DataConverters {

    @TypeConverter // note this annotation
    public String fromOptionValuesList(List<String> optionValues) {
        if (optionValues == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<String>>() {
        }.getType();
        String json = gson.toJson(optionValues, type);
        return json;
    }

    @TypeConverter // note this annotation
    public List<String> toOptionValuesList(String optionValuesString) {
        if (optionValuesString == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<String>>() {
        }.getType();
        List<String> productCategoriesList = gson.fromJson(optionValuesString, type);
        return productCategoriesList;
    }

    @TypeConverter // note this annotation
    public String fromValuesList(List<Integer> optionValues) {
        if (optionValues == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<Integer>>() {
        }.getType();
        String json = gson.toJson(optionValues, type);
        return json;
    }

    @TypeConverter // note this annotation
    public List<Integer> toValuesList(String optionValuesString) {
        if (optionValuesString == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<Integer>>() {
        }.getType();
        List<Integer> productCategoriesList = gson.fromJson(optionValuesString, type);
        return productCategoriesList;
    }

    @TypeConverter // note this annotation
    public String fromTemplateList(List<Template> templateList) {
        if (templateList == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<Template>>() {
        }.getType();
        String json = gson.toJson(templateList, type);
        return json;
    }

    @TypeConverter // note this annotation
    public List<Template> toTemplateList(String optionValuesString) {
        if (optionValuesString == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<Template>>() {
        }.getType();
        List<Template> templateList = gson.fromJson(optionValuesString, type);
        return templateList;
    }

    @TypeConverter // note this annotation
    public String fromAdapTemplateDataList(List<AdapTemplateData> userMonthlyUsageWordList) {
        if (userMonthlyUsageWordList == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<AdapTemplateData>>() {
        }.getType();
        String json = gson.toJson(userMonthlyUsageWordList, type);
        return json;
    }

    @TypeConverter // note this annotation
    public List<AdapTemplateData> toAdapTemplateDataList(String optionValuesString) {
        if (optionValuesString == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<AdapTemplateData>>() {
        }.getType();
        List<AdapTemplateData> adapTemplateData = gson.fromJson(optionValuesString, type);
        return adapTemplateData;
    }


    @TypeConverter // note this annotation
    public String frommessageList(List<Message> messageList) {
        if (messageList == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<Message>>() {
        }.getType();
        String json = gson.toJson(messageList, type);
        return json;
    }

    @TypeConverter // note this annotation
    public List<Message> tomessageList(String optionValuesString) {
        if (optionValuesString == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<Message>>() {
        }.getType();
        List<Message> messageList = gson.fromJson(optionValuesString, type);
        return messageList;
    }


    @TypeConverter // note this annotation
    public String chatHistList(List<ChatItem> messageList) {
        if (messageList == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<ChatItem>>() {
        }.getType();
        String json = gson.toJson(messageList, type);
        return json;
    }

    @TypeConverter // note this annotation
    public List<ChatItem> toChatHistList(String optionValuesString) {
        if (optionValuesString == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<ChatItem>>() {
        }.getType();
        List<ChatItem> messageList = gson.fromJson(optionValuesString, type);
        return messageList;
    }


}
