package ru.yandex.testopithecus.utils

import android.util.Log
import androidx.test.uiautomator.UiDevice

object Reinstaller {

    fun reinstall(device: UiDevice, pckg: String, apk: String) {
        Log.i(LOG_TAG, "uninstalling $pckg")
        Log.i(LOG_TAG, device.executeShellCommand("pm uninstall $pckg"))
        Log.i(LOG_TAG, "clearing $pckg")
        Log.i(LOG_TAG, device.executeShellCommand("pm clear $pckg"))
        Log.i(LOG_TAG, "installing $pckg")
        Log.i(LOG_TAG, device.executeShellCommand("pm install -t -r /data/local/tmp/apks/$apk"))
    }

    private const val LOG_TAG = "REINSTALLER"

}