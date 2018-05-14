package levin.ru.cm;


import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.v4.view.ViewCompat;
import android.util.Log;

public class Buttons {
    private MyButton[] buttons = new MyButton[100];
    private int checked = -1;
    private int drawChecked = -1;
    private int numButtons = 0;

    class MyButton {
        private Rect bounds = new Rect();
        private Paint paint;
        private Paint paintText;
        private RectF pos;
        private int resultCode;
        private String text;

        public MyButton(String text, RectF pos, int resultCode) {
            this.text = text;
            this.pos = pos;
            this.resultCode = resultCode;
            this.paint = new Paint();
            this.paint.setStyle(Style.STROKE);
            this.paint.setStrokeWidth(3.0f);
            this.paintText = new Paint();
            this.paintText.setTextSize(pos.height() / 2.0f);
        }

        public MyButton(String text, RectF pos, int resultCode, float textSize) {
            this.text = text;
            this.pos = pos;
            this.resultCode = resultCode;
            this.paint = new Paint();
            this.paint.setStyle(Style.STROKE);
            this.paint.setStrokeWidth(3.0f);
            this.paintText = new Paint();
            this.paintText.setTextSize(textSize);
        }

        public int checkTouched(float x, float y) {
            if (this.pos.contains(x, y)) {
                return this.resultCode;
            }
            return 0;
        }

        public void draw(int id, Canvas canvas) {
            this.paint.setStyle(Style.FILL);
            if (id == Buttons.this.checked || id == Buttons.this.drawChecked) {
                this.paint.setColor(-16711936);
            } else {
                this.paint.setColor(-1);
            }
            canvas.drawRoundRect(this.pos, 20.0f, 20.0f, this.paint);
            this.paint.setColor(ViewCompat.MEASURED_STATE_MASK);
            this.paint.setStyle(Style.STROKE);
            this.paint.setStrokeWidth(3.0f);
            canvas.drawRoundRect(this.pos, 20.0f, 20.0f, this.paint);
            this.paintText.getTextBounds(this.text, 0, this.text.length(), this.bounds);
            canvas.drawText(this.text, this.pos.centerX() - ((float) this.bounds.centerX()), this.pos.centerY() - ((float) this.bounds.centerY()), this.paintText);
        }

        public void drawWithSolved(int id, Canvas canvas, String solved) {
            this.paint.setStyle(Style.FILL);
            if (solved.contains(this.text)) {
                this.paint.setColor(-16711936);
            } else {
                this.paint.setColor(-1);
            }
            canvas.drawRoundRect(this.pos, 20.0f, 20.0f, this.paint);
            this.paint.setColor(ViewCompat.MEASURED_STATE_MASK);
            this.paint.setStyle(Style.STROKE);
            this.paint.setStrokeWidth(3.0f);
            canvas.drawRoundRect(this.pos, 20.0f, 20.0f, this.paint);
            this.paintText.getTextBounds(this.text, 0, this.text.length(), this.bounds);
            canvas.drawText(this.text, this.pos.centerX() - ((float) this.bounds.centerX()), this.pos.centerY() - ((float) this.bounds.centerY()), this.paintText);
        }
    }

    public void resetPressed() {
        this.checked = -1;
        this.drawChecked = -1;
    }

    public void addButton(String text, RectF pos, int resultCode) {
        if (this.numButtons < this.buttons.length) {
            this.buttons[this.numButtons] = new MyButton(text, pos, resultCode);
            this.numButtons++;
        }
    }

    public void addButton(String text, RectF pos, int resultCode, float textSize) {
        if (this.numButtons < this.buttons.length) {
            this.buttons[this.numButtons] = new MyButton(text, pos, resultCode, textSize);
            this.numButtons++;
        }
    }

    public void setChecked(int id) {
        this.drawChecked = id;
    }

    public int checkTouchedAll(float x, float y, MyDraw draw) {
        for (int i = 0; i < this.numButtons; i++) {
            int res = this.buttons[i].checkTouched(x, y);
            if (res != 0) {
                if (this.checked != i) {
                    if (res >= 0) {
                        resetPressed();
                    }
                    this.checked = i;
                } else {
                    this.checked = -1;
                }
                draw.wireF.set(0.0f, 0.0f);
                Log.e("Button", "checked" + this.checked);
                return res;
            }
        }
        return 0;
    }

    public int checkTouchedAll(float x, float y) {
        for (int i = 0; i < this.numButtons; i++) {
            int res = this.buttons[i].checkTouched(x, y);
            if (res != 0) {
                if (this.checked != i) {
                    this.checked = i;
                } else {
                    this.checked = -1;
                }
                Log.e("Button", "checked" + this.checked);
                return res;
            }
        }
        return 0;
    }

    public void clear() {
        for (int i = 0; i < this.numButtons; i++) {
            this.buttons[i] = null;
        }
        this.numButtons = 0;
    }

    public void drawAll(Canvas canvas) {
        for (int i = 0; i < this.numButtons; i++) {
            this.buttons[i].draw(i, canvas);
        }
    }

    public void drawAllWithSolved(Canvas canvas, String solved) {
        for (int i = 0; i < this.numButtons; i++) {
            this.buttons[i].drawWithSolved(i, canvas, solved);
        }
    }

    public int getChecked() {
        return this.checked;
    }
}
