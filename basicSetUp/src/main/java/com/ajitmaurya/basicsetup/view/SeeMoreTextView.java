package com.ajitmaurya.basicsetup.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.AttributeSet;
import android.view.View;

import com.ajitmaurya.basicsetup.R;

@SuppressLint("AppCompatCustomView")
public class SeeMoreTextView extends CustomTextView {
    private Integer textMaxLength = 200;
    private Integer seeMoreTextColor = R.color.app_color_black;

    private String collapsedTextWithSeeMoreButton;
    private String expandedTextWithSeeMoreButton;
    private String originalContent;

    private SpannableString collapsedTextSpannable;
    private SpannableString expandedTextSpannable;

    private Boolean isExpanded = false;
    ClickableSpan clickableSpan = new ClickableSpan() {
        @Override
        public void onClick(View widget) {
            toggle();
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            ds.setUnderlineText(false);
            ds.setColor(getResources().getColor(seeMoreTextColor));
        }
    };

    public SeeMoreTextView(Context context) {
        super(context);
    }

    public SeeMoreTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    public SeeMoreTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private static int countLines(String str) {
        String[] lines = str.split("\r\n|\r|\n");
        return lines.length;
    }

    //set max length of the string text
    public void setTextMaxLength(Integer maxLength) {
        textMaxLength = maxLength;
    }

    public void setSeeMoreTextColor(Integer color) {
        seeMoreTextColor = color;
    }

    public void expandText(Boolean expand) {
        if (expand) {
            //isExpanded = true;
            setText(expandedTextSpannable);
        } else {
            isExpanded = false;
            setText(collapsedTextSpannable);
        }

    }

    public Boolean isExpanded() {
        return isExpanded;
    }

    //toggle the state
    public void toggle() {
        if (isExpanded) {
            isExpanded = false;
            setText(collapsedTextSpannable);
        } else {
            //isExpanded = true;
            setText(expandedTextSpannable);
        }
    }

    public void setContent(String text) {
        originalContent = text;
        this.setMovementMethod(LinkMovementMethod.getInstance());
        //show see more
        if (originalContent != null && originalContent.length() >= textMaxLength) {
            collapsedTextWithSeeMoreButton = originalContent.substring(0, textMaxLength) + "... Read more";
            expandedTextWithSeeMoreButton = originalContent;// + "... " + seeLess;
            //creating spannable strings
            collapsedTextSpannable = new SpannableString(collapsedTextWithSeeMoreButton);
            expandedTextSpannable = new SpannableString(expandedTextWithSeeMoreButton);

            collapsedTextSpannable.setSpan(clickableSpan, textMaxLength + 4, collapsedTextWithSeeMoreButton.length(), 0);
            collapsedTextSpannable.setSpan(new StyleSpan(Typeface.BOLD), textMaxLength + 4, collapsedTextWithSeeMoreButton.length(), 0);
            collapsedTextSpannable.setSpan(new RelativeSizeSpan(.9f), textMaxLength + 4, collapsedTextWithSeeMoreButton.length(), 0);
            if (isExpanded)
                setText(expandedTextSpannable);
            else
                setText(collapsedTextSpannable);
        } else {
            //to do: don't show see more
            setText(originalContent);
        }
    }
}
