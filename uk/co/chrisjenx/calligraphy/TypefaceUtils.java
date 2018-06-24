package uk.co.chrisjenx.calligraphy;

import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.util.Log;
import java.util.HashMap;
import java.util.Map;

public final class TypefaceUtils {
    private static final Map<String, Typeface> sCachedFonts = new HashMap();
    private static final Map<Typeface, CalligraphyTypefaceSpan> sCachedSpans = new HashMap();

    public static Typeface load(AssetManager assetManager, String filePath) {
        synchronized (sCachedFonts) {
            try {
                if (sCachedFonts.containsKey(filePath)) {
                    Typeface typeface = (Typeface) sCachedFonts.get(filePath);
                    return typeface;
                }
                Typeface typeface2 = Typeface.createFromAsset(assetManager, filePath);
                sCachedFonts.put(filePath, typeface2);
                return typeface2;
            } catch (Exception e) {
                Log.w("Calligraphy", "Can't create asset from " + filePath + ". Make sure you have passed in the correct path and file name.", e);
                sCachedFonts.put(filePath, null);
                return null;
            }
        }
    }

    public static CalligraphyTypefaceSpan getSpan(Typeface typeface) {
        if (typeface == null) {
            return null;
        }
        synchronized (sCachedSpans) {
            if (sCachedSpans.containsKey(typeface)) {
                CalligraphyTypefaceSpan calligraphyTypefaceSpan = (CalligraphyTypefaceSpan) sCachedSpans.get(typeface);
                return calligraphyTypefaceSpan;
            }
            CalligraphyTypefaceSpan span = new CalligraphyTypefaceSpan(typeface);
            sCachedSpans.put(typeface, span);
            return span;
        }
    }

    public static boolean isLoaded(Typeface typeface) {
        return typeface != null && sCachedFonts.containsValue(typeface);
    }

    private TypefaceUtils() {
    }
}
