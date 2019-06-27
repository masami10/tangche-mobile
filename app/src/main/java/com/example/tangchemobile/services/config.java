package com.example.tangchemobile.services;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class config {
    public static Map<String, String> Load(Context ctx) {
        SharedPreferences sp = ctx.getSharedPreferences("SP", MODE_PRIVATE);

        Map<String, String> cfg = new HashMap<>();

        String continueMode = sp.getString(SCANNER_CONTINUE_MODE, "");
        if (continueMode.equals("")) {
            continueMode = "false";
        }
        cfg.put(SCANNER_CONTINUE_MODE, continueMode);

        String rfidPort = sp.getString(RFID_PORT, "");
        if (rfidPort.equals("")) {
            rfidPort = "/dev/ttyS2";
        }
        cfg.put(RFID_PORT, rfidPort);

        String powerIndex = sp.getString(RFID_POWER_INDEX, "");
        if (powerIndex.equals("")){
            powerIndex = "0";
        }
        cfg.put(RFID_POWER_INDEX, powerIndex);

        String sensitiveIndex = sp.getString(RFID_SENSITIVE_INDEX, "");
        if (sensitiveIndex.equals("")) {
            sensitiveIndex = "0";
        }
        cfg.put(RFID_SENSITIVE_INDEX, sensitiveIndex);

        String rushUrl = sp.getString(RUSH_URL, "");
        if (rushUrl.equals("")) {
            rushUrl = "http://127.0.0.1:8082";
        }
        cfg.put(RUSH_URL, rushUrl);

        return cfg;
    }

    public static boolean Save(Context ctx, Map<String, String> cfg) {
        SharedPreferences sp = ctx.getSharedPreferences("SP", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        for (Map.Entry<String, String> entry : cfg.entrySet()) {
            editor.putString(entry.getKey(), entry.getValue());
        }

        return editor.commit();
    }

    public static final String SCANNER_CONTINUE_MODE = "SCANNER_CONTINUE_MODE";
    public static final String RFID_PORT = "RFID_PORT";
    public static final String RFID_POWER_INDEX = "RFID_POWER_INDEX";
    public static final String RFID_SENSITIVE_INDEX = "RFID_SENSITIVE_INDEX";
    public static final String RUSH_URL = "RUSH_URL";
}
