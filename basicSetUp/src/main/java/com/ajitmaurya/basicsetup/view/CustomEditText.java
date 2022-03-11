package com.ajitmaurya.basicsetup.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.EditText;

import com.ajitmaurya.basicsetup.R;

@SuppressLint("AppCompatCustomView")
public class CustomEditText extends EditText {

    String customFont;

    public CustomEditText(Context context) {
        super(context);
    }

    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        style(context, attrs);
    }

    public CustomEditText(Context context, AttributeSet attrs, int defStyle) {
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
            case 11:
                fontName = R.string.spro_regular;
                break;
            case 12:
                fontName = R.string.spro_blackIt;
                break;
            case 13:
                fontName = R.string.spro_bold;
                break;
            case 14:
                fontName = R.string.spro_black;
                break;
            case 15:
                fontName = R.string.spro_boldIt;
                break;
            case 16:
                fontName = R.string.spro_extralight;
                break;
            case 17:
                fontName = R.string.spro_extralightIt;
                break;
            case 18:
                fontName = R.string.spro_It;
                break;
            case 19:
                fontName = R.string.spro_light;
                break;
            case 20:
                fontName = R.string.spro_lightIt;
                break;
            case 21:
                fontName = R.string.spro_semibold;
                break;
            case 22:
                fontName = R.string.spro_semiboldIt;
                break;
            default:
                fontName = R.string.spro_regular;
                break;
        }

        customFont = getResources().getString(fontName);

        Typeface tf = Typeface.createFromAsset(context.getAssets(),
                "font/" + customFont + ".ttf");
        setTypeface(tf);
        a.recycle();
    }

}