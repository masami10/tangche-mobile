package com.example.tangchemobile.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ContextWrapper;
import android.device.ScanDevice;
import android.widget.Toast;

public class Scanner {
    private Context parent;
    private ScanDevice dev;

    public Scanner(Context parent) {
        this.parent = parent;
    }

    public boolean Open() {
        if (this.dev == null) {
            try {
                this.dev = new ScanDevice();
            } catch (Exception e) {
                Toast.makeText(parent, "打开扫码失败:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        this.Close();

        this.dev.openScan();
        this.dev.setOutScanMode(0);

        return true;
    }

    public boolean Close() {
        if (this.dev == null) {
            return false;
        }

        return this.dev.closeScan();
    }

    public boolean Start() {
        if (this.dev == null) {
            return false;
        }

        return this.dev.startScan();
    }

    public boolean Stop() {
        if (this.dev == null) {
            return false;
        }

        return this.dev.stopScan();
    }

    public boolean SetContinueMode(boolean flag) {
        if (this.dev == null) {
            return false;
        }

        if (flag) {
            this.dev.setScanLaserMode(4);
        } else {
            this.dev.setScanLaserMode(8);
        }

        return true;
    }
}
