package com.pavlukhin.acropanda.bitmaputils.allochack;

import android.util.Log;

import com.pavlukhin.acropanda.PandaApplication;

import java.lang.reflect.Method;

/**
 * Created by ivan on 23.06.2014.
 * Copy-pasted
 * Thanks Buggins
 */
public class VMRuntimeHack {
    private Object runtime = null;
    private Method trackAllocation = null;
    private Method trackFree = null;

    public VMRuntimeHack() {
        try {
            Class<?> cl = Class.forName("dalvik.system.VMRuntime");
            Method getRt = cl.getMethod("getRuntime");
            runtime = getRt.invoke(null);
            trackAllocation = cl.getMethod("trackExternalAllocation", long.class);
            trackFree = cl.getMethod("trackExternalFree", long.class);
            Log.d(PandaApplication.LOG_TAG, "Hacked runtime load successfully");
        } catch (Exception e) {
            Log.d(PandaApplication.LOG_TAG, "Exception hacking runtime", e);
            runtime = null;
            trackAllocation = null;
            trackFree = null;
        }
    }

    public void trackAlloc(long size) {
        if (runtime == null) return;
        try {
            Object res = trackAllocation.invoke(runtime, size);
        } catch (Exception e) {}
    }

    public void trackFree(long size) {
        if (runtime == null) return;
        try {
            Object res = trackFree.invoke(runtime, size);
        } catch (Exception e) {}
    }
}
