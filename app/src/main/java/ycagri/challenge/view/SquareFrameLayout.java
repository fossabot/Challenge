package ycagri.challenge.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * Created by YigitCagri on 10.1.2015.
 */
public class SquareFrameLayout extends FrameLayout {

    public SquareFrameLayout(Context context) {
        super(context);
    }

    public SquareFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (getMeasuredWidth() > getMeasuredHeight())
            setMeasuredDimension(getMeasuredHeight(), getMeasuredHeight());
        else
            setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth());
    }
}
