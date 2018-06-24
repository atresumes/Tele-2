package com.opentok.jni;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build.VERSION;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.URI;
import java.util.List;

public class ProxyDetector extends BroadcastReceiver {
    private static final String LOGTAG = "opentok-proxy";
    private static Object lockObject = new Object();
    private static ProxyDetector proxyDetector;
    private static int registeredCount = 0;

    private static native void updateProxy(String str, int i);

    static {
        System.loadLibrary("opentok");
    }

    public ProxyDetector() {
        List<Proxy> proxyList = ProxySelector.getDefault().select(URI.create("https://anvil.opentok.com"));
        if (proxyList.size() > 0) {
            Proxy proxy = (Proxy) proxyList.get(0);
            if (proxy.address() instanceof InetSocketAddress) {
                InetSocketAddress ad = (InetSocketAddress) proxy.address();
                if (ad.getHostName() != null && ad.getPort() != 0) {
                    updateProxy(ad.getHostName(), ad.getPort());
                }
            }
        }
    }

    public static void registerProxyDetector(Context context) {
        synchronized (lockObject) {
            if (registeredCount == 0) {
                IntentFilter filter = new IntentFilter();
                filter.addAction("android.intent.action.PROXY_CHANGE");
                if (proxyDetector == null) {
                    proxyDetector = new ProxyDetector();
                }
                context.getApplicationContext().registerReceiver(proxyDetector, filter);
            }
            registeredCount++;
        }
    }

    public static void unregisterProxyDetector(Context context) {
        synchronized (lockObject) {
            registeredCount--;
            if (registeredCount == 0) {
                context.getApplicationContext().unregisterReceiver(proxyDetector);
            }
        }
    }

    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.PROXY_CHANGE")) {
            extractNewProxy(intent);
        }
    }

    private void extractNewProxy(Intent intent) {
        try {
            String className;
            String proxyInfo;
            String getHostName = "getHost";
            String getPortName = "getPort";
            if (VERSION.SDK_INT <= 19) {
                className = "android.net.ProxyProperties";
                proxyInfo = "proxy";
            } else {
                className = "android.net.ProxyInfo";
                proxyInfo = "android.intent.extra.PROXY_INFO";
            }
            Object props = intent.getExtras().get(proxyInfo);
            if (props != null) {
                Class<?> cls = Class.forName(className);
                updateProxy((String) cls.getDeclaredMethod("getHost", new Class[0]).invoke(props, new Object[0]), ((Integer) cls.getDeclaredMethod("getPort", new Class[0]).invoke(props, new Object[0])).intValue());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
