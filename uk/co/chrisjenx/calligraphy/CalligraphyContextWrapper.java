package uk.co.chrisjenx.calligraphy;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

public class CalligraphyContextWrapper extends ContextWrapper {
    private final int mAttributeId;
    private CalligraphyLayoutInflater mInflater;

    public static ContextWrapper wrap(Context base) {
        return new CalligraphyContextWrapper(base);
    }

    public static View onActivityCreateView(Activity activity, View parent, View view, String name, Context context, AttributeSet attr) {
        return get(activity).onActivityCreateView(parent, view, name, context, attr);
    }

    static CalligraphyActivityFactory get(Activity activity) {
        if (activity.getLayoutInflater() instanceof CalligraphyLayoutInflater) {
            return (CalligraphyActivityFactory) activity.getLayoutInflater();
        }
        throw new RuntimeException("This activity does not wrap the Base Context! See CalligraphyContextWrapper.wrap(Context)");
    }

    CalligraphyContextWrapper(Context base) {
        super(base);
        this.mAttributeId = CalligraphyConfig.get().getAttrId();
    }

    @Deprecated
    public CalligraphyContextWrapper(Context base, int attributeId) {
        super(base);
        this.mAttributeId = attributeId;
    }

    public Object getSystemService(String name) {
        if (!"layout_inflater".equals(name)) {
            return super.getSystemService(name);
        }
        if (this.mInflater == null) {
            this.mInflater = new CalligraphyLayoutInflater(LayoutInflater.from(getBaseContext()), this, this.mAttributeId, false);
        }
        return this.mInflater;
    }
}
