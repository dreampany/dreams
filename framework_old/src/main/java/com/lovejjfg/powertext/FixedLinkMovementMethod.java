package com.lovejjfg.powertext;

import android.text.Layout;
import android.text.Selection;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.text.method.Touch;
import android.text.style.ClickableSpan;
import android.view.MotionEvent;
import android.widget.TextView;

/**
 * Created by Hawladar Roman on 7/6/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
public class FixedLinkMovementMethod extends LinkMovementMethod {
    @Override
    public boolean onTouchEvent(TextView widget, Spannable buffer, MotionEvent event) {
        int action = event.getAction();

        if (action == MotionEvent.ACTION_UP ||
                action == MotionEvent.ACTION_DOWN) {
            int x = (int) event.getX();
            int y = (int) event.getY();

            x -= widget.getTotalPaddingLeft();
            y -= widget.getTotalPaddingTop();

            x += widget.getScrollX();
            y += widget.getScrollY();

            Layout layout = widget.getLayout();
            int line = layout.getLineForVertical(y);
            int off = layout.getOffsetForHorizontal(line, x);

            ClickableSpan[] link = buffer.getSpans(off, off, ClickableSpan.class);

            if (link.length != 0) {
                if (action == MotionEvent.ACTION_UP) {
                    link[0].onClick(widget);

                } else if (action == MotionEvent.ACTION_DOWN) {
                    Selection.setSelection(buffer,
                            buffer.getSpanStart(link[0]),
                            buffer.getSpanEnd(link[0]));
                }

                if (widget instanceof ClickFixedTextView) {
                    ((ClickFixedTextView) widget).linkClicked = true;
                }

                return true;
            } else {
                Selection.removeSelection(buffer);
                super.onTouchEvent(widget, buffer, event);
                if (widget instanceof ClickFixedTextView) {
                    ((ClickFixedTextView) widget).linkClicked = false;
                }
                return false;
            }
        }

        return Touch.onTouchEvent(widget, buffer, event);
    }

    public static FixedLinkMovementMethod getInstance() {
        if (sInstance == null)
            sInstance = new FixedLinkMovementMethod();

        return sInstance;
    }

    private static FixedLinkMovementMethod sInstance;

}
