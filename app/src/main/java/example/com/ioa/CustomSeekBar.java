package example.com.ioa;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.SeekBar;

/**
 * Created by Gunjan.K.Kumar on 27-05-2017.
 */
public class CustomSeekBar extends SeekBar {

    private Paint textPaint;

    private Rect textBounds = new Rect();

    private String text = "";


    public CustomSeekBar(Context context) {
        super(context);
        textPaint = new Paint();
        textPaint.setColor(Color.BLACK);

    }

    public CustomSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        textPaint = new Paint();
        textPaint.setColor(Color.BLACK);

    }

    public CustomSeekBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);


        textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        // First draw the regular progress bar, then custom draw our text
        super.onDraw(canvas);

        // Now progress position and convert to text.
        text = Integer.toString(getProgress());

        // Now get size of seek bar.
        float width = getWidth();
        float height = getHeight();

        // Set text size.
        textPaint.setTextSize((height * 3) / 4);
        // Get size of text.
        textPaint.getTextBounds(text, 0, text.length(), textBounds);

        // Calculate where to start printing text.
        float position = (width / getMax()) * getProgress();

        // Get start and end points of where text will be printed.
        float textXStart = position - textBounds.centerX();
        float textXEnd = position + textBounds.centerX();

        // Check does not start drawing outside seek bar.
        if (textXStart < 0) textXStart = 0;

        if (textXEnd > width)
            textXStart -= (textXEnd - width);

        // Calculate y text print position.
        float yPosition = (height / 2) - textBounds.centerY(); //- (textBounds.bottom / 2);

        canvas.drawText(text, textXStart, yPosition, textPaint);
    }

    public synchronized void setTextColor(int color) {
        super.drawableStateChanged();
        textPaint.setColor(color);
        drawableStateChanged();
    }
}
