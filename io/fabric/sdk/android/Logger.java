package io.fabric.sdk.android;

public interface Logger {
    void mo3810d(String str, String str2);

    void mo3811d(String str, String str2, Throwable th);

    void mo3812e(String str, String str2);

    void mo3813e(String str, String str2, Throwable th);

    int getLogLevel();

    void mo3815i(String str, String str2);

    void mo3816i(String str, String str2, Throwable th);

    boolean isLoggable(String str, int i);

    void log(int i, String str, String str2);

    void log(int i, String str, String str2, boolean z);

    void setLogLevel(int i);

    void mo3821v(String str, String str2);

    void mo3822v(String str, String str2, Throwable th);

    void mo3823w(String str, String str2);

    void mo3824w(String str, String str2, Throwable th);
}
