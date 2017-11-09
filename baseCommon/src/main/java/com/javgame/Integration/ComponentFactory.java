package com.javgame.Integration;

import android.app.Activity;

import com.javgame.utility.AndroidUtil;

/**
 * @author zhangf
 * @date 2017/10/25
 */

public class ComponentFactory {

    private static ComponentFactory instance;
    private Activity activity;
    public static ComponentFactory getInstance() {
        if (instance == null) {
            instance = new ComponentFactory();
        }
        return instance;
    }

    public void init(Activity activity) {
        this.activity = activity;
    }

    public Object getLoginUser(Activity activity) {
        String name = AndroidUtil.getStringResource(activity, "packageLogin");
        Class localClass = null;
        if (name != null) {
            try {
                localClass = Class.forName(name);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        try {
            return localClass.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return null;
    }


    public Object getPay() {
        String name = AndroidUtil.getStringResource(activity, "packagePay");
        Class localClass = null;
        if (name != null) {
            try {
                localClass = Class.forName(name);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        try {
            return localClass.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return null;
    }
}
