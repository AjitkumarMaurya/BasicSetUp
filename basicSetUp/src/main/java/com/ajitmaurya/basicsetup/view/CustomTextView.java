package com.ajitmaurya.basicsetup.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;

import com.ajitmaurya.basicsetup.R;

@SuppressLint("AppCompatCustomView")
public class CustomTextView extends TextView {

    String customFont;

    public CustomTextView(Context context) {
        super(context);
    }

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        style(context, attrs);
    }

    public CustomTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        style(context, attrs);
    }

    public void setTextWithMarquee(String str) {
        this.setText(str);
        this.setSingleLine(true);
        this.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        this.setHorizontallyScrolling(true);
        this.setLines(1);
        this.setMarqueeRepeatLimit(-1);
        this.setSelected(true);
    }


    private void style(Context context, AttributeSet attrs) {

        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.CustomFontTextView);
        int cf = a.getInteger(R.styleable.CustomFontTextView_fontName, 0);
        int fontName = 0;
        switch (cf) {
            case 1:
                fontName = R.string.OpenSans_Bold;
                break;
            case 2:
                fontName = R.string.OpenSans_BoldItalic;
                break;
            case 3:
                fontName = R.string.OpenSans_ExtraBold;
                break;
            case 4:
                fontName = R.string.OpenSans_ExtraBoldItalic;
                break;
            case 5:
                fontName = R.string.OpenSans_Italic;
                break;
            case 6:
                fontName = R.string.OpenSans_Light;
                break;
            case 7:
                fontName = R.string.OpenSans_LightItalic;
                break;
            case 8:
                fontName = R.string.OpenSans_Regular;
                break;
            case 9:
                fontName = R.string.OpenSans_SemiBold;
                break;
            case 10:
                fontName = R.string.OpenSans_SemiBoldItalic;
                break;
            default:
                fontName = R.string.OpenSans_Regular;
                break;
        }

        customFont = getResources().getString(fontName);

        Typeface tf = Typeface.createFromAsset(context.getAssets(),
                "font/" + customFont + ".ttf");
        setTypeface(tf);
        a.recycle();
    }

}