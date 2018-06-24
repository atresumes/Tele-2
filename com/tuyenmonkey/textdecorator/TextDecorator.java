package com.tuyenmonkey.textdecorator;

import android.content.res.ColorStateList;
import android.graphics.BlurMaskFilter;
import android.graphics.BlurMaskFilter.Blur;
import android.graphics.EmbossMaskFilter;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.text.Layout.Alignment;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.AlignmentSpan.Standard;
import android.text.style.BackgroundColorSpan;
import android.text.style.BulletSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.MaskFilterSpan;
import android.text.style.QuoteSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.ScaleXSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.SubscriptSpan;
import android.text.style.SuperscriptSpan;
import android.text.style.TextAppearanceSpan;
import android.text.style.TypefaceSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.TextView;
import com.tuyenmonkey.textdecorator.callback.OnTextClickListener;

public class TextDecorator {
    private String content;
    private SpannableString decoratedContent;
    private int flags = 33;
    private TextView textView;

    private TextDecorator(TextView textView, String content) {
        this.textView = textView;
        this.content = content;
        this.decoratedContent = new SpannableString(content);
    }

    public static TextDecorator decorate(TextView textView, String content) {
        return new TextDecorator(textView, content);
    }

    public TextDecorator setFlags(int flags) {
        this.flags = flags;
        return this;
    }

    public TextDecorator underline(int start, int end) {
        checkIndexOutOfBoundsException(start, end);
        this.decoratedContent.setSpan(new UnderlineSpan(), start, end, this.flags);
        return this;
    }

    public TextDecorator underline(String... texts) {
        for (String text : texts) {
            if (this.content.contains(text)) {
                int index = this.content.indexOf(text);
                this.decoratedContent.setSpan(new UnderlineSpan(), index, text.length() + index, this.flags);
            }
        }
        return this;
    }

    public TextDecorator setTextColor(@ColorRes int resColorId, int start, int end) {
        checkIndexOutOfBoundsException(start, end);
        this.decoratedContent.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this.textView.getContext(), resColorId)), start, end, this.flags);
        return this;
    }

    public TextDecorator setTextColor(@ColorRes int resColorId, String... texts) {
        for (String text : texts) {
            if (this.content.contains(text)) {
                int index = this.content.indexOf(text);
                this.decoratedContent.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this.textView.getContext(), resColorId)), index, text.length() + index, this.flags);
            }
        }
        return this;
    }

    public TextDecorator setBackgroundColor(@ColorRes int colorResId, int start, int end) {
        checkIndexOutOfBoundsException(start, end);
        this.decoratedContent.setSpan(new BackgroundColorSpan(ContextCompat.getColor(this.textView.getContext(), colorResId)), start, end, 0);
        return this;
    }

    public TextDecorator setBackgroundColor(@ColorRes int colorResId, String... texts) {
        for (String text : texts) {
            if (this.content.contains(text)) {
                int index = this.content.indexOf(text);
                this.decoratedContent.setSpan(new BackgroundColorSpan(ContextCompat.getColor(this.textView.getContext(), colorResId)), index, text.length() + index, this.flags);
            }
        }
        return this;
    }

    public TextDecorator insertBullet(int start, int end) {
        checkIndexOutOfBoundsException(start, end);
        this.decoratedContent.setSpan(new BulletSpan(), start, end, this.flags);
        return this;
    }

    public TextDecorator insertBullet(int gapWidth, int start, int end) {
        checkIndexOutOfBoundsException(start, end);
        this.decoratedContent.setSpan(new BulletSpan(gapWidth), start, end, this.flags);
        return this;
    }

    public TextDecorator insertBullet(int gapWidth, @ColorRes int colorResId, int start, int end) {
        checkIndexOutOfBoundsException(start, end);
        this.decoratedContent.setSpan(new BulletSpan(gapWidth, ContextCompat.getColor(this.textView.getContext(), colorResId)), start, end, this.flags);
        return this;
    }

    public TextDecorator makeTextClickable(OnTextClickListener listener, int start, int end, boolean underlineText) {
        checkIndexOutOfBoundsException(start, end);
        final OnTextClickListener onTextClickListener = listener;
        final int i = start;
        final int i2 = end;
        final boolean z = underlineText;
        this.decoratedContent.setSpan(new ClickableSpan() {
            public void onClick(View view) {
                onTextClickListener.onClick(view, TextDecorator.this.content.substring(i, i2));
            }

            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(z);
            }
        }, start, end, this.flags);
        this.textView.setMovementMethod(LinkMovementMethod.getInstance());
        return this;
    }

    public TextDecorator makeTextClickable(final OnTextClickListener listener, final boolean underlineText, String... texts) {
        for (final String text : texts) {
            if (this.content.contains(text)) {
                int index = this.content.indexOf(text);
                this.decoratedContent.setSpan(new ClickableSpan() {
                    public void onClick(View view) {
                        listener.onClick(view, text);
                    }

                    public void updateDrawState(TextPaint ds) {
                        super.updateDrawState(ds);
                        ds.setUnderlineText(underlineText);
                    }
                }, index, text.length() + index, this.flags);
            }
        }
        this.textView.setMovementMethod(LinkMovementMethod.getInstance());
        return this;
    }

    public TextDecorator makeTextClickable(ClickableSpan clickableSpan, int start, int end) {
        checkIndexOutOfBoundsException(start, end);
        this.decoratedContent.setSpan(clickableSpan, start, end, this.flags);
        this.textView.setMovementMethod(LinkMovementMethod.getInstance());
        return this;
    }

    public TextDecorator makeTextClickable(ClickableSpan clickableSpan, String... texts) {
        for (String text : texts) {
            if (this.content.contains(text)) {
                int index = this.content.indexOf(text);
                this.decoratedContent.setSpan(clickableSpan, index, text.length() + index, this.flags);
            }
        }
        this.textView.setMovementMethod(LinkMovementMethod.getInstance());
        return this;
    }

    public TextDecorator insertImage(@DrawableRes int resId, int start, int end) {
        checkIndexOutOfBoundsException(start, end);
        this.decoratedContent.setSpan(new ImageSpan(this.textView.getContext(), resId), start, end, this.flags);
        return this;
    }

    public TextDecorator quote(int start, int end) {
        checkIndexOutOfBoundsException(start, end);
        this.decoratedContent.setSpan(new QuoteSpan(), start, end, this.flags);
        return this;
    }

    public TextDecorator quote(String... texts) {
        for (String text : texts) {
            if (this.content.contains(text)) {
                int index = this.content.indexOf(text);
                this.decoratedContent.setSpan(new QuoteSpan(), index, text.length() + index, this.flags);
            }
        }
        return this;
    }

    public TextDecorator quote(@ColorRes int colorResId, int start, int end) {
        checkIndexOutOfBoundsException(start, end);
        this.decoratedContent.setSpan(new QuoteSpan(ContextCompat.getColor(this.textView.getContext(), colorResId)), start, end, this.flags);
        return this;
    }

    public TextDecorator quote(@ColorRes int colorResId, String... texts) {
        for (String text : texts) {
            if (this.content.contains(text)) {
                int index = this.content.indexOf(text);
                this.decoratedContent.setSpan(new QuoteSpan(ContextCompat.getColor(this.textView.getContext(), colorResId)), index, text.length() + index, this.flags);
            }
        }
        return this;
    }

    public TextDecorator strikethrough(int start, int end) {
        checkIndexOutOfBoundsException(start, end);
        this.decoratedContent.setSpan(new StrikethroughSpan(), start, end, this.flags);
        return this;
    }

    public TextDecorator strikethrough(String... texts) {
        for (String text : texts) {
            if (this.content.contains(text)) {
                int index = this.content.indexOf(text);
                this.decoratedContent.setSpan(new StrikethroughSpan(), index, text.length() + index, this.flags);
            }
        }
        return this;
    }

    public TextDecorator setTextStyle(int style, int start, int end) {
        checkIndexOutOfBoundsException(start, end);
        this.decoratedContent.setSpan(new StyleSpan(style), start, end, this.flags);
        return this;
    }

    public TextDecorator setTextStyle(int style, String... texts) {
        for (String text : texts) {
            if (this.content.contains(text)) {
                int index = this.content.indexOf(text);
                this.decoratedContent.setSpan(new StyleSpan(style), index, text.length() + index, this.flags);
            }
        }
        return this;
    }

    public TextDecorator alignText(Alignment alignment, int start, int end) {
        checkIndexOutOfBoundsException(start, end);
        this.decoratedContent.setSpan(new Standard(alignment), start, end, this.flags);
        return this;
    }

    public TextDecorator alignText(Alignment alignment, String... texts) {
        for (String text : texts) {
            if (this.content.contains(text)) {
                int index = this.content.indexOf(text);
                this.decoratedContent.setSpan(new Standard(alignment), index, text.length() + index, this.flags);
            }
        }
        return this;
    }

    public TextDecorator setSubscript(int start, int end) {
        checkIndexOutOfBoundsException(start, end);
        this.decoratedContent.setSpan(new SubscriptSpan(), start, end, this.flags);
        return this;
    }

    public TextDecorator setSubscript(String... texts) {
        for (String text : texts) {
            if (this.content.contains(text)) {
                int index = this.content.indexOf(text);
                this.decoratedContent.setSpan(new SubscriptSpan(), index, text.length() + index, this.flags);
            }
        }
        return this;
    }

    public TextDecorator setSuperscript(int start, int end) {
        checkIndexOutOfBoundsException(start, end);
        this.decoratedContent.setSpan(new SuperscriptSpan(), start, end, this.flags);
        return this;
    }

    public TextDecorator setSuperscript(String... texts) {
        for (String text : texts) {
            if (this.content.contains(text)) {
                int index = this.content.indexOf(text);
                this.decoratedContent.setSpan(new SuperscriptSpan(), index, text.length() + index, this.flags);
            }
        }
        return this;
    }

    public TextDecorator setTypeface(String family, int start, int end) {
        checkIndexOutOfBoundsException(start, end);
        this.decoratedContent.setSpan(new TypefaceSpan(family), start, end, this.flags);
        return this;
    }

    public TextDecorator setTypeface(String family, String... texts) {
        for (String text : texts) {
            if (this.content.contains(text)) {
                int index = this.content.indexOf(text);
                this.decoratedContent.setSpan(new TypefaceSpan(family), index, text.length() + index, this.flags);
            }
        }
        return this;
    }

    public TextDecorator setTextAppearance(int appearance, int start, int end) {
        checkIndexOutOfBoundsException(start, end);
        this.decoratedContent.setSpan(new TextAppearanceSpan(this.textView.getContext(), appearance), start, end, this.flags);
        return this;
    }

    public TextDecorator setTextAppearance(int appearance, String... texts) {
        for (String text : texts) {
            if (this.content.contains(text)) {
                int index = this.content.indexOf(text);
                this.decoratedContent.setSpan(new TextAppearanceSpan(this.textView.getContext(), appearance), index, text.length() + index, this.flags);
            }
        }
        return this;
    }

    public TextDecorator setTextAppearance(int appearance, int colorList, int start, int end) {
        checkIndexOutOfBoundsException(start, end);
        this.decoratedContent.setSpan(new TextAppearanceSpan(this.textView.getContext(), appearance, colorList), start, end, this.flags);
        return this;
    }

    public TextDecorator setTextAppearance(int appearance, int colorList, String... texts) {
        for (String text : texts) {
            if (this.content.contains(text)) {
                int index = this.content.indexOf(text);
                this.decoratedContent.setSpan(new TextAppearanceSpan(this.textView.getContext(), appearance, colorList), index, text.length() + index, this.flags);
            }
        }
        return this;
    }

    public TextDecorator setTextAppearance(String family, int style, int size, ColorStateList color, ColorStateList linkColor, int start, int end) {
        checkIndexOutOfBoundsException(start, end);
        this.decoratedContent.setSpan(new TextAppearanceSpan(family, style, size, color, linkColor), start, end, this.flags);
        return this;
    }

    public TextDecorator setTextAppearance(String family, int style, int size, ColorStateList color, ColorStateList linkColor, String... texts) {
        for (String text : texts) {
            if (this.content.contains(text)) {
                int index = this.content.indexOf(text);
                this.decoratedContent.setSpan(new TextAppearanceSpan(family, style, size, color, linkColor), index, text.length() + index, this.flags);
            }
        }
        return this;
    }

    public TextDecorator setAbsoluteSize(int size, int start, int end) {
        checkIndexOutOfBoundsException(start, end);
        this.decoratedContent.setSpan(new AbsoluteSizeSpan(size), start, end, this.flags);
        return this;
    }

    public TextDecorator setAbsoluteSize(int size, String... texts) {
        for (String text : texts) {
            if (this.content.contains(text)) {
                int index = this.content.indexOf(text);
                this.decoratedContent.setSpan(new AbsoluteSizeSpan(size), index, text.length() + index, this.flags);
            }
        }
        return this;
    }

    public TextDecorator setAbsoluteSize(int size, boolean dip, int start, int end) {
        checkIndexOutOfBoundsException(start, end);
        this.decoratedContent.setSpan(new AbsoluteSizeSpan(size, dip), start, end, this.flags);
        return this;
    }

    public TextDecorator setAbsoluteSize(int size, boolean dip, String... texts) {
        for (String text : texts) {
            if (this.content.contains(text)) {
                int index = this.content.indexOf(text);
                this.decoratedContent.setSpan(new AbsoluteSizeSpan(size, dip), index, text.length() + index, this.flags);
            }
        }
        return this;
    }

    public TextDecorator setRelativeSize(float proportion, int start, int end) {
        checkIndexOutOfBoundsException(start, end);
        this.decoratedContent.setSpan(new RelativeSizeSpan(proportion), start, end, this.flags);
        return this;
    }

    public TextDecorator setRelativeSize(float proportion, String... texts) {
        for (String text : texts) {
            if (this.content.contains(text)) {
                int index = this.content.indexOf(text);
                this.decoratedContent.setSpan(new RelativeSizeSpan(proportion), index, text.length() + index, this.flags);
            }
        }
        return this;
    }

    public TextDecorator scaleX(float proportion, int start, int end) {
        checkIndexOutOfBoundsException(start, end);
        this.decoratedContent.setSpan(new ScaleXSpan(proportion), start, end, this.flags);
        return this;
    }

    public TextDecorator scaleX(float proportion, String... texts) {
        for (String text : texts) {
            if (this.content.contains(text)) {
                int index = this.content.indexOf(text);
                this.decoratedContent.setSpan(new ScaleXSpan(proportion), index, text.length() + index, this.flags);
            }
        }
        return this;
    }

    public TextDecorator blur(float radius, Blur style, int start, int end) {
        checkIndexOutOfBoundsException(start, end);
        this.decoratedContent.setSpan(new MaskFilterSpan(new BlurMaskFilter(radius, style)), start, end, this.flags);
        return this;
    }

    public TextDecorator blur(float radius, Blur style, String... texts) {
        for (String text : texts) {
            if (this.content.contains(text)) {
                int index = this.content.indexOf(text);
                this.decoratedContent.setSpan(new MaskFilterSpan(new BlurMaskFilter(radius, style)), index, text.length() + index, this.flags);
            }
        }
        return this;
    }

    public TextDecorator emboss(float[] direction, float ambient, float specular, float blurRadius, int start, int end) {
        checkIndexOutOfBoundsException(start, end);
        this.decoratedContent.setSpan(new MaskFilterSpan(new EmbossMaskFilter(direction, ambient, specular, blurRadius)), start, end, this.flags);
        return this;
    }

    public TextDecorator emboss(float[] direction, float ambient, float specular, float blurRadius, String... texts) {
        for (String text : texts) {
            if (this.content.contains(text)) {
                int index = this.content.indexOf(text);
                this.decoratedContent.setSpan(new MaskFilterSpan(new EmbossMaskFilter(direction, ambient, specular, blurRadius)), index, text.length() + index, this.flags);
            }
        }
        return this;
    }

    public void build() {
        this.textView.setText(this.decoratedContent);
    }

    private void checkIndexOutOfBoundsException(int start, int end) {
        if (start < 0) {
            throw new IndexOutOfBoundsException("start is less than 0");
        } else if (end > this.content.length()) {
            throw new IndexOutOfBoundsException("end is greater than content length " + this.content.length());
        } else if (start > end) {
            throw new IndexOutOfBoundsException("start is greater than end");
        }
    }
}
