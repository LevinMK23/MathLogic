package levin.ru.cm;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.v4.internal.view.SupportMenu;
import android.support.v4.view.InputDeviceCompat;
import android.util.Log;
import java.util.LinkedList;

public class Block {
    private Rect bounds = new Rect();
    public LinkedList<Block> connectedBlocks = new LinkedList();
    private MyDraw draw;
    int figType = 0;
    boolean on = false;
    Paint paint = new Paint();
    private Paint paintText;
    public Block prevBlock1 = null;
    public Block prevBlock2 = null;
    float size = 10.0f;
    private String text;
    float f0x = 0.0f;
    float x1 = 0.0f;
    float f1y = 0.0f;
    float y1 = 0.0f;

    public Block(float x, float y, int figType, MyDraw draw) {
        this.draw = draw;
        this.size = draw.windowHeight / 15.0f;
        Log.e("BlockConst", "size=" + this.size);
        this.paint.setARGB(255, 128, 128, 0);
        this.figType = figType;
        this.f0x = x;
        this.f1y = y;
        this.paintText = new Paint();
        this.paintText.setTextSize(60.0f);
        switch (figType) {
            case -6:
                this.text = "OUT";
                return;
            case -5:
                this.text = "INP";
                return;
            case 1:
                this.text = "NO";
                return;
            case 2:
                this.text = "AND";
                return;
            case 3:
                this.text = "OR";
                return;
            case 4:
                this.text = "XOR";
                return;
            default:
                return;
        }
    }

    public void setPrev1(Block prev) {
        this.prevBlock1 = prev;
        Log.e("Block", "prevBl1=" + prev.figType);
        if (prev.figType != 0) {
            this.f0x = (prev.f0x + this.size) - (this.size / 8.0f);
            this.f1y = prev.f1y;
        } else if (this.figType == 0) {
            prev.x1 = this.f0x;
            prev.y1 = this.f1y;
        } else {
            prev.x1 = (this.f0x - this.size) + (this.size / 6.0f);
            prev.y1 = this.f1y - (this.size / 2.0f);
        }
    }

    public void setPrev2(Block prev) {
        this.prevBlock2 = prev;
        Log.e("Block", "prevBl2=" + prev.figType);
        if (prev.figType != 0) {
            this.f0x = (prev.f0x + this.size) - (this.size / 8.0f);
            this.f1y = prev.f1y;
        } else if (this.figType == 0) {
            prev.x1 = this.f0x;
            prev.y1 = this.f1y;
        } else {
            prev.x1 = (this.f0x - this.size) + (this.size / 6.0f);
            prev.y1 = this.f1y + (this.size / 2.0f);
        }
    }

    public int onTouch(float x, float y) {
        Log.e("Block", "distance=" + Vector3.distance(x, y, this.f0x, this.f1y));
        this.connectedBlocks.clear();
        Log.e("Block", "prevBlock1=" + this.prevBlock1 + ";prevBlock2=" + this.prevBlock2);
        if (this.figType < 0 || this.draw.button.getChecked() != -1 || Vector3.distance(x, y, this.f0x, this.f1y) > 120.0f) {
            return 0;
        }
        this.draw.destroy(this);
        return 1;
    }

    public Block(float x, float y, float x1, float y1, int figType, MyDraw draw) {
        this.draw = draw;
        this.size = draw.windowHeight / 15.0f;
        this.paint.setARGB(255, 128, 128, 0);
        this.figType = figType;
        this.f0x = x;
        this.f1y = y;
        this.x1 = x1;
        this.y1 = y1;
        this.paint.setStrokeWidth(15.0f);
    }

    public boolean check() {
        switch (this.figType) {
            case -6:
                if (this.prevBlock1 != null && this.prevBlock1.on) {
                    this.on = true;
                    break;
                }
                this.on = false;
                break;
            case 0:
                if (this.prevBlock1 != null && this.prevBlock1.on) {
                    this.on = true;
                    break;
                }
                this.on = false;
                break;
            case 1:
                if (this.prevBlock1 != null && this.prevBlock1.on) {
                    this.on = false;
                    break;
                }
                this.on = true;
                break;
            case 2:
                if (this.prevBlock1 != null && this.prevBlock2 != null && this.prevBlock1.on && this.prevBlock2.on) {
                    this.on = true;
                    break;
                }
                this.on = false;
                break;
            case 3:
                if (this.prevBlock1 != null && this.prevBlock2 != null && (this.prevBlock1.on || this.prevBlock2.on)) {
                    this.on = true;
                    break;
                }
                this.on = false;
                break;
            case 4:
                if (this.prevBlock1 != null && this.prevBlock2 != null && (this.prevBlock1.on ^ this.prevBlock2.on)) {
                    this.on = true;
                    break;
                }
                this.on = false;
                break;
        }
        if (this.on) {
            return true;
        }
        return false;
    }

    public void draw(Canvas canvas) {
        check();
        if (this.on) {
            this.paint.setColor(-16711936);
        } else {
            this.paint.setColor(SupportMenu.CATEGORY_MASK);
        }
        if (this.figType == 0) {
            this.paint.setStrokeWidth(10.0f);
            canvas.drawLine(this.f0x, this.f1y, this.x1, this.y1, this.paint);
            canvas.drawCircle(this.f0x, this.f1y, this.size / 3.0f, this.paint);
            this.paint.setStyle(Style.STROKE);
            this.paint.setStrokeWidth(3.0f);
            this.paint.setColor(InputDeviceCompat.SOURCE_ANY);
            canvas.drawCircle(this.f0x, this.f1y, this.size / 3.0f, this.paint);
            this.paint.setStyle(Style.FILL);
            return;
        }
        this.paintText.setTextSize(this.size / 2.0f);
        canvas.drawRoundRect(new RectF(this.f0x - this.size, this.f1y - this.size, this.f0x + this.size, this.f1y + this.size), 20.0f, 20.0f, this.paint);
        if (this.text != null) {
            this.paintText.getTextBounds(this.text, 0, this.text.length(), this.bounds);
            canvas.drawText(this.text, this.f0x - ((float) this.bounds.centerX()), (this.f1y - (this.size / 2.0f)) - ((float) this.bounds.centerY()), this.paintText);
            return;
        }
        Log.e("Block", "figType=" + this.figType + ";");
    }
}
