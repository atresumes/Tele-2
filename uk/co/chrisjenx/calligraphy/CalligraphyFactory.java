package uk.co.chrisjenx.calligraphy;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build.VERSION;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.TextView;
import java.lang.ref.WeakReference;
import java.lang.reflect.Method;

class CalligraphyFactory {
    private static final String ACTION_BAR_SUBTITLE = "action_bar_subtitle";
    private static final String ACTION_BAR_TITLE = "action_bar_title";
    private final int[] mAttributeId;

    private static class ToolbarLayoutListener implements OnGlobalLayoutListener {
        static String BLANK = " ";
        private final WeakReference<CalligraphyFactory> mCalligraphyFactory;
        private final WeakReference<Context> mContextRef;
        private final WeakReference<Toolbar> mToolbarReference;
        private final CharSequence originalSubTitle;

        private ToolbarLayoutListener(CalligraphyFactory calligraphyFactory, Context context, Toolbar toolbar) {
            this.mCalligraphyFactory = new WeakReference(calligraphyFactory);
            this.mContextRef = new WeakReference(context);
            this.mToolbarReference = new WeakReference(toolbar);
            this.originalSubTitle = toolbar.getSubtitle();
            toolbar.setSubtitle(BLANK);
        }

        @TargetApi(16)
        public void onGlobalLayout() {
            Toolbar toolbar = (Toolbar) this.mToolbarReference.get();
            Context context = (Context) this.mContextRef.get();
            CalligraphyFactory factory = (CalligraphyFactory) this.mCalligraphyFactory.get();
            if (toolbar != null) {
                if (factory == null || context == null) {
                    removeSelf(toolbar);
                    return;
                }
                int childCount = toolbar.getChildCount();
                if (childCount != 0) {
                    for (int i = 0; i < childCount; i++) {
                        factory.onViewCreated(toolbar.getChildAt(i), context, null);
                    }
                }
                removeSelf(toolbar);
                toolbar.setSubtitle(this.originalSubTitle);
            }
        }

        private void removeSelf(Toolbar toolbar) {
            if (VERSION.SDK_INT < 16) {
                toolbar.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            } else {
                toolbar.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        }
    }

    protected static int[] getStyleForTextView(TextView view) {
        int[] styleIds = new int[]{-1, -1};
        if (isActionBarTitle(view)) {
            styleIds[0] = 16843470;
            styleIds[1] = 16843512;
        } else if (isActionBarSubTitle(view)) {
            styleIds[0] = 16843470;
            styleIds[1] = 16843513;
        }
        if (styleIds[0] == -1) {
            styleIds[0] = CalligraphyConfig.get().getClassStyles().containsKey(view.getClass()) ? ((Integer) CalligraphyConfig.get().getClassStyles().get(view.getClass())).intValue() : 16842804;
        }
        return styleIds;
    }

    @SuppressLint({"NewApi"})
    protected static boolean isActionBarTitle(TextView view) {
        if (matchesResourceIdName(view, ACTION_BAR_TITLE)) {
            return true;
        }
        if (parentIsToolbarV7(view)) {
            return TextUtils.equals(((Toolbar) view.getParent()).getTitle(), view.getText());
        }
        return false;
    }

    @SuppressLint({"NewApi"})
    protected static boolean isActionBarSubTitle(TextView view) {
        if (matchesResourceIdName(view, ACTION_BAR_SUBTITLE)) {
            return true;
        }
        if (parentIsToolbarV7(view)) {
            return TextUtils.equals(((Toolbar) view.getParent()).getSubtitle(), view.getText());
        }
        return false;
    }

    protected static boolean parentIsToolbarV7(View view) {
        return CalligraphyUtils.canCheckForV7Toolbar() && view.getParent() != null && (view.getParent() instanceof Toolbar);
    }

    protected static boolean matchesResourceIdName(View view, String matches) {
        if (view.getId() == -1) {
            return false;
        }
        return view.getResources().getResourceEntryName(view.getId()).equalsIgnoreCase(matches);
    }

    public CalligraphyFactory(int attributeId) {
        this.mAttributeId = new int[]{attributeId};
    }

    public View onViewCreated(View view, Context context, AttributeSet attrs) {
        if (!(view == null || view.getTag(C2005R.id.calligraphy_tag_id) == Boolean.TRUE)) {
            onViewCreatedInternal(view, context, attrs);
            view.setTag(C2005R.id.calligraphy_tag_id, Boolean.TRUE);
        }
        return view;
    }

    void onViewCreatedInternal(View view, Context context, AttributeSet attrs) {
        if (view instanceof TextView) {
            if (!TypefaceUtils.isLoaded(((TextView) view).getTypeface())) {
                boolean deferred;
                String textViewFont = resolveFontPath(context, attrs);
                if (TextUtils.isEmpty(textViewFont)) {
                    int[] styleForTextView = getStyleForTextView((TextView) view);
                    if (styleForTextView[1] != -1) {
                        textViewFont = CalligraphyUtils.pullFontPathFromTheme(context, styleForTextView[0], styleForTextView[1], this.mAttributeId);
                    } else {
                        textViewFont = CalligraphyUtils.pullFontPathFromTheme(context, styleForTextView[0], this.mAttributeId);
                    }
                }
                if (matchesResourceIdName(view, ACTION_BAR_TITLE) || matchesResourceIdName(view, ACTION_BAR_SUBTITLE)) {
                    deferred = true;
                } else {
                    deferred = false;
                }
                CalligraphyUtils.applyFontToTextView(context, (TextView) view, CalligraphyConfig.get(), textViewFont, deferred);
            } else {
                return;
            }
        }
        if (CalligraphyUtils.canCheckForV7Toolbar() && (view instanceof Toolbar)) {
            Toolbar toolbar = (Toolbar) view;
            toolbar.getViewTreeObserver().addOnGlobalLayoutListener(new ToolbarLayoutListener(context, toolbar));
        }
        Typeface typeface;
        if (view instanceof HasTypeface) {
            typeface = getDefaultTypeface(context, resolveFontPath(context, attrs));
            if (typeface != null) {
                ((HasTypeface) view).setTypeface(typeface);
            }
        } else if (CalligraphyConfig.get().isCustomViewTypefaceSupport() && CalligraphyConfig.get().isCustomViewHasTypeface(view)) {
            Method setTypeface = ReflectionUtils.getMethod(view.getClass(), "setTypeface");
            typeface = getDefaultTypeface(context, resolveFontPath(context, attrs));
            if (setTypeface != null && typeface != null) {
                ReflectionUtils.invokeMethod(view, setTypeface, typeface);
            }
        }
    }

    private Typeface getDefaultTypeface(Context context, String fontPath) {
        if (TextUtils.isEmpty(fontPath)) {
            fontPath = CalligraphyConfig.get().getFontPath();
        }
        if (TextUtils.isEmpty(fontPath)) {
            return null;
        }
        return TypefaceUtils.load(context.getAssets(), fontPath);
    }

    private String resolveFontPath(Context context, AttributeSet attrs) {
        String textViewFont = CalligraphyUtils.pullFontPathFromView(context, attrs, this.mAttributeId);
        if (TextUtils.isEmpty(textViewFont)) {
            textViewFont = CalligraphyUtils.pullFontPathFromStyle(context, attrs, this.mAttributeId);
        }
        if (TextUtils.isEmpty(textViewFont)) {
            return CalligraphyUtils.pullFontPathFromTextAppearance(context, attrs, this.mAttributeId);
        }
        return textViewFont;
    }
}
