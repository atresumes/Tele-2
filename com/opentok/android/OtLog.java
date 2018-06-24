package com.opentok.android;

import android.util.Log;
import java.io.PrintWriter;
import java.io.StringWriter;

public class OtLog {

    public static class LogToken {
        private final boolean enabled;
        private final String tag;

        public LogToken() {
            this(getCallerClassName(), true);
        }

        public LogToken(boolean enabled) {
            this(getCallerClassName(), enabled);
        }

        public LogToken(String name, boolean enabled) {
            this.tag = name;
            this.enabled = enabled & 0;
        }

        public void m15d(String message, Object... args) {
            if (this.enabled) {
                Log.d(this.tag, String.format(message, args));
            }
        }

        public void m16d(Throwable throwable, String message, Object... args) {
            m15d(appendStackTraceString(message, throwable), args);
        }

        public void m19i(String message, Object... args) {
            if (this.enabled) {
                Log.i(this.tag, String.format(message, args));
            }
        }

        public void m20i(Throwable throwable, String message, Object... args) {
            m19i(appendStackTraceString(message, throwable), args);
        }

        public void m21v(String message, Object... args) {
            if (this.enabled) {
                Log.v(this.tag, String.format(message, args));
            }
        }

        public void m22v(Throwable throwable, String message, Object... args) {
            m21v(appendStackTraceString(message, throwable), args);
        }

        public void m23w(String message, Object... args) {
            if (this.enabled) {
                Log.w(this.tag, String.format(message, args));
            }
        }

        public void m24w(Throwable throwable, String message, Object... args) {
            m23w(appendStackTraceString(message, throwable), args);
        }

        public void m17e(String message, Object... args) {
            if (this.enabled) {
                Log.e(this.tag, String.format(message, args));
            }
        }

        public void m18e(Throwable throwable, String message, Object... args) {
            m17e(appendStackTraceString(message, throwable), args);
        }

        private static String getCallerClassName() {
            StackTraceElement[] stElements = Thread.currentThread().getStackTrace();
            String otLogClsName = OtLog.class.getName();
            String otLogTokenClsName = LogToken.class.getName();
            boolean belowCurrentFrame = false;
            for (StackTraceElement ste : stElements) {
                if (!belowCurrentFrame && ste.getClassName().equals(otLogTokenClsName)) {
                    belowCurrentFrame = true;
                } else if (!(!belowCurrentFrame || ste.getClassName().equals(otLogTokenClsName) || ste.getClassName().equals(otLogClsName))) {
                    try {
                        return Class.forName(ste.getClassName()).getSimpleName();
                    } catch (ClassNotFoundException e) {
                        return ste.getClassName();
                    }
                }
            }
            return otLogClsName;
        }

        private static String appendStackTraceString(String message, Throwable throwable) {
            StringWriter sw = new StringWriter(256);
            PrintWriter pw = new PrintWriter(sw, false);
            throwable.printStackTrace(pw);
            pw.flush();
            if (message != null) {
                return message + "\n" + sw.toString();
            }
            return sw.toString();
        }
    }

    public static void m25d(String message, Object... args) {
        new LogToken().m15d(message, args);
    }

    public static void m26d(Throwable throwable, String message, Object... args) {
        new LogToken().m16d(throwable, message, args);
    }

    public static void m29i(String message, Object... args) {
        new LogToken().m19i(message, args);
    }

    public static void m30i(Throwable throwable, String message, Object... args) {
        new LogToken().m20i(throwable, message, args);
    }

    public static void m31v(String message, Object... args) {
        new LogToken().m21v(message, args);
    }

    public static void m32v(Throwable throwable, String message, Object... args) {
        new LogToken().m22v(throwable, message, args);
    }

    public static void m33w(String message, Object... args) {
        new LogToken().m23w(message, args);
    }

    public static void m34w(Throwable throwable, String message, Object... args) {
        new LogToken().m24w(throwable, message, args);
    }

    public static void m27e(String message, Object... args) {
        new LogToken().m17e(message, args);
    }

    public static void m28e(Throwable throwable, String message, Object... args) {
        new LogToken().m18e(throwable, message, args);
    }
}
