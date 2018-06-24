package io.fabric.sdk.android;

public class SilentLogger implements Logger {
    private int logLevel = 7;

    public boolean isLoggable(String tag, int level) {
        return false;
    }

    public void mo3811d(String tag, String text, Throwable throwable) {
    }

    public void mo3822v(String tag, String text, Throwable throwable) {
    }

    public void mo3816i(String tag, String text, Throwable throwable) {
    }

    public void mo3824w(String tag, String text, Throwable throwable) {
    }

    public void mo3813e(String tag, String text, Throwable throwable) {
    }

    public void mo3810d(String tag, String text) {
    }

    public void mo3821v(String tag, String text) {
    }

    public void mo3815i(String tag, String text) {
    }

    public void mo3823w(String tag, String text) {
    }

    public void mo3812e(String tag, String text) {
    }

    public void log(int priority, String tag, String msg) {
    }

    public void log(int priority, String tag, String msg, boolean forceLog) {
    }

    public int getLogLevel() {
        return this.logLevel;
    }

    public void setLogLevel(int logLevel) {
    }
}
