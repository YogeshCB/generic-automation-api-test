package org.kubric.commonUtils;

/**
 * // TODO Comment
 */

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Utils {

    private static Utils instance = new Utils();
    static String regex = "[0-9]";
    static Date datetoday = new Date();

    private Utils() {
    }

    public static Utils getInstance() {
        return instance;
    }

    private String sheetName;
    private String columnNames = "*";
    private boolean debug = false;
    private boolean loadEmptyColumns = true;

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

    public void setColumnNames(String columnNames) {
        this.columnNames = columnNames;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public void setLoadEmptyColumns(boolean value) {
        this.loadEmptyColumns = value;
    }

    public static String resolveEnvVars(String input) {
        if (null == input) {
            return null;
        }
        Pattern pattern = Pattern.compile("\\$(?:\\{(\\w+)\\}|(\\w+))");
        Matcher matcher = pattern.matcher(input);
        StringBuffer stringBuilder = new StringBuffer();
        while (matcher.find()) {
            String envVarName = null == matcher.group(1) ? matcher.group(2)
                    : matcher.group(1);
            String envVarValue = getPropertyEnv(envVarName, null);
            matcher.appendReplacement(stringBuilder,
                    null == envVarValue ? "" : envVarValue.replace("\\", "\\\\"));
        }
        matcher.appendTail(stringBuilder);
        return stringBuilder.toString();
    }


    public static String getPropertyEnv(String name, String defaultValue) {
        String value = System.getProperty(name);
        if (value == null) {
            value = System.getenv(name);
            if (value == null) {
                value = defaultValue;
            }
        }
        return value;
    }


}