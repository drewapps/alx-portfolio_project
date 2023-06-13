package com.drewapps.ai;

public class Config {


    /**
     * AppVersion
     * Current App Version
     */
    public static String APP_VERSION = "2.0";

    /**
     * API Related
     * Change your API URL Here
     * Your API KEY no need to add because we pass api key through firebase
     */
    public static final String APP_API_URL = "https://aiassistant.drewapps.com/";
    public static String API_KEY = "";


    public static final int LIMIT = 12; // Keep More Then 10

    public static final boolean IS_DEVELOPING = true;

    public static final int IMAGE_CACHE_LIMIT = 200;

    public static boolean IS_CONNECTED = false;
    public static final boolean PRE_LOAD_IMAGE = false;

    public static final String EMPTY_STRING = "";
    public static final String ZERO = "0";
    public static final String ONE = "1";
    public static final String TWO = "2";
    public static final String THREE = "3";

    /**
     * Error Codes
     */
    public static int ERROR_CODE_10001 = 10001; // Totally No Record
    public static int ERROR_CODE_10002 = 10002; // No More Record at pagination

}
