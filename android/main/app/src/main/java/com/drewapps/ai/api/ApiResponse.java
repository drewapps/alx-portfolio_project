package com.drewapps.ai.api;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.collection.ArrayMap;

import com.drewapps.ai.utils.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * Common class used by API responses.
 * @param <T>
 */
public class ApiResponse<T> {

    private static final Pattern LINK_PATTERN = Pattern
            .compile("<([^>]*)>[\\s]*;[\\s]*rel=\"([a-zA-Z0-9]+)\"");
    private static final Pattern PAGE_PATTERN = Pattern.compile("\\bpage=(\\d+)");
    private static final String NEXT_LINK = "next";

    public int code;
    @Nullable
    public T body;
    @Nullable
    public String errorMessage;
    @NonNull
    private Map<String, String> links;

    public ApiResponse(Throwable error) {
        code = 500;
        body = null;
        errorMessage = error.getMessage();
        links = Collections.emptyMap();

        Util.showLog("API Response Error : " + errorMessage);
    }

    public ApiResponse(Response<T> response) {

        Util.showLog("URL : " + response.raw().request().url());
        if(response.isSuccessful()) {
            Util.showLog("ApiResponse Successful. " + response.raw().toString());
            code = response.code();
            body = response.body();
            errorMessage = null;
        } else {
            Util.showLog("ApiResponse Something wrong.");
            String message = null;

            try {
                ResponseBody responseBody = response.errorBody();
                if(responseBody != null) {
                    message = responseBody.string();
                    try {
                        JSONObject jObjError = new JSONObject(message);
                        Util.showLog("API Error Response : " + jObjError.getString("message"));
                        message = jObjError.getString("message");

                        if (message.contains("##")) {
                            String[] messageDataList = message.split("##");
                            if (messageDataList.length > 1) {
                                code = Integer.parseInt(messageDataList[0]);
                                message = messageDataList[1];
                            }
                        }
                    }
                    catch (JSONException e) {
                        Util.showErrorLog("JSON Parsing error.", e);
                    }
                }

            } catch (NullPointerException ne) {
                Util.showErrorLog("Null Pointer Exception.", ne);
            }catch (IOException ignored) {
                Util.showErrorLog("error while parsing response", ignored);
            }

            if (message == null || message.trim().length() == 0) {
                message = response.message();
            }

            if (code == 0) {
                code = response.code();
            }
            errorMessage = message;
            body = null;
        }
        String linkHeader = response.headers().get("link");
        if (linkHeader == null) {
            links = Collections.emptyMap();
        } else {
            links = new ArrayMap<>();
            Matcher matcher = LINK_PATTERN.matcher(linkHeader);

            while (matcher.find()) {
                int count = matcher.groupCount();
                if (count == 2) {
                    links.put(matcher.group(2), matcher.group(1));
                }
            }
        }
    }

    public boolean isSuccessful() {
        return code >= 200 && code < 300;
    }

    public Integer getNextPage() {
        String next = links.get(NEXT_LINK);
        if (next == null) {
            return null;
        }
        Matcher matcher = PAGE_PATTERN.matcher(next);
        if (!matcher.find() || matcher.groupCount() != 1) {
            return null;
        }
        try {
            return Integer.parseInt(matcher.group(1));
        } catch (NumberFormatException ex) {
            Util.showLog("cannot parse next page from %s");
            return null;
        }
    }
}

