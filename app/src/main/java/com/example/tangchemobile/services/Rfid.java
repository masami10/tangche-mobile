package com.example.tangchemobile.services;

import android.content.Context;

import com.example.tangchemobile.MainActivity;
import com.magicrf.uhfreaderlib.reader.NewSendCommendManager;
import com.magicrf.uhfreaderlib.reader.Tools;
import com.magicrf.uhfreaderlib.reader.UhfReader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Rfid {

    private Context parent;

    public Rfid(Context parent) {
        this.parent = parent;
        this.power = powers[0];
        this.sen = sensitives[0];
    }

    public boolean fileExists(String strFile) {
        try {
            File f=new File(strFile);
            if(!f.exists()) {
                return false;
            }
        }
        catch (Exception e) {
            return false;
        }

        return true;
    }

    public Boolean Open(String port) {

        if (!fileExists(port)) {
            return false;
        }

        UhfReader.setPortPath(port);

        try {
            this.reader = UhfReader.getInstance();
        } catch (Exception e) {
            return false;
        }

        if (!this.reader.setOutputPower(this.power)) {
            return false;
        }

        this.reader.setSensitivity(this.sen);

        if (this.thInventory != null) {
            this.thInventory.interrupt();
        }

        this.thInventory = new InventoryThread();
        this.thInventory.start();

        return true;
    }

    public void SetPower(int power) {
        this.power = powers[power];
    }

    public void SetSensitive(int sen) {
        this.sen = sensitives[sen];
    }

    private Integer power;
    private Integer sen;

    private UhfReader reader;
    private Thread thInventory;

    private static final Integer[] powers = {2600,2400,2000,1850,1700,1550,1400,1250};
    private static final Integer[] sensitives = {NewSendCommendManager.SENSITIVE_HIHG,NewSendCommendManager.SENSITIVE_MIDDLE,NewSendCommendManager.SENSITIVE_LOW,NewSendCommendManager.SENSITIVE_VERY_LOW};

    class InventoryThread extends Thread {
        private List<byte[]> epcList;

        @Override
        public void run() {
            super.run();
            while (true) {
                epcList = reader.inventoryRealTime();
                if (epcList != null && !epcList.isEmpty()) {
                    for (byte[] epc : epcList) {
                        if (epc != null) {
                            String epcStr = Tools.Bytes2HexString(epc, epc.length);

                            // 处理内容
                            final MainActivity activity = (MainActivity)parent;

                            // 界面显示
                            activity.operationFragment.SetInfo("RFID", epcStr);

                            // 推送rush
                            activity.Rush().Put("rfid", epcStr);
                        }
                    }
                }
                epcList = null;

                try {
                    Thread.sleep(40);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
