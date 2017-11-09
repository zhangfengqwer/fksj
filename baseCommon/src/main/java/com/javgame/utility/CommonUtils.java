package com.javgame.utility;

import android.content.Context;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

/**
 * @author czq
 *         其他工具类
 */
public class CommonUtils {


    private static final String TAG = CommonUtils.class.getSimpleName();


    /**
     * map转json字符串
     *
     * @param map
     * @return
     */
    public static String getJsonFromMap(Map<String, String> map) {
        JSONObject obj = new JSONObject(map);
        return obj.toString();
    }

    /**
     * 流转字符串方法
     *
     * @param is
     * @return
     */
    public static String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    public static void showToast(Context context, CharSequence text) {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show();
    }
}
