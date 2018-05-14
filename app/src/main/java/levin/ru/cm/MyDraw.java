package levin.ru.cm;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.RectF;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;
import java.util.Iterator;
import java.util.LinkedList;

public class MyDraw extends View {
    public LinkedList<Block> blocks = new LinkedList();
    public Buttons button = new Buttons();
    boolean correct = false;
    public int inputs = 0;
    Level level;
    public String levelName = "";
    public int outputs = 0;
    public LinkedList<Block> outputsBl = new LinkedList();
    private Paint paint;
    private Paint paintMiddleText;
    private Paint paintRightText;
    SharedPreferences sPref;
    String savedText;
    public int simulation = -1;
    public String[] tableReal = new String[24];
    long time = System.currentTimeMillis();
    public LinkedList<String> vars = new LinkedList();
    public float windowHeight = 200.0f;
    public int windowWidth = 0;
    public Vector3 wireF = new Vector3();

    public MyDraw(Context context, Level lvl) {
        super(context);
        this.level = lvl;
        this.paint = new Paint();
        this.paintRightText = new Paint();
        this.paintMiddleText = new Paint();
        String levelStr = this.level.getIntent().getStringExtra("levelStr");
        Log.e("MyDrawConstr", "levelStrV=" + levelStr);
        String[] nameAndTr = levelStr.split(";");
        this.levelName = nameAndTr[0];
        String[] levelNumbers = nameAndTr[1].split("&");
        Log.e("MyDrawConstr", "levelName=" + this.levelName + ";");
        this.inputs = Integer.parseInt(levelNumbers[0]);
        this.outputs = Integer.parseInt(levelNumbers[1]);
        for (int i = 2; i < nameAndTr.length; i++) {
            this.vars.add(nameAndTr[i]);
        }
        loadProgress();
    }

    public void destroy(Block blToD) {
        this.blocks.remove(blToD);
        addNewBlock(0.0f, 0.0f);
    }

    public void addNewBlock(float x, float y) {
        int type = this.button.getChecked();
        if (type >= 0) {
            if (type != 0) {
                x = ((float) Math.floor((double) (x / (this.windowHeight / 15.0f)))) * (this.windowHeight / 15.0f);
                y = ((float) Math.floor((double) (y / (this.windowHeight / 15.0f)))) * (this.windowHeight / 15.0f);
                this.wireF.set(0.0f, 0.0f);
                this.blocks.add(new Block((this.windowHeight / 10.0f) + x, (this.windowHeight / 10.0f) + y, type, this));
            } else if (this.wireF.f5x == 0.0f) {
                this.wireF.set(x, y);
            } else {
                x = ((float) Math.floor((double) (x / (this.windowHeight / 20.0f)))) * (this.windowHeight / 20.0f);
                y = ((float) Math.floor((double) (y / (this.windowHeight / 20.0f)))) * (this.windowHeight / 20.0f);
                Vector3 vector3 = this.wireF;
                vector3.f5x /= this.windowHeight / 20.0f;
                this.wireF.f5x = (float) Math.floor((double) this.wireF.f5x);
                vector3 = this.wireF;
                vector3.f5x *= this.windowHeight / 20.0f;
                vector3 = this.wireF;
                vector3.f6y /= this.windowHeight / 20.0f;
                this.wireF.f6y = (float) Math.floor((double) this.wireF.f6y);
                vector3 = this.wireF;
                vector3.f6y *= this.windowHeight / 20.0f;
                this.blocks.add(new Block(this.wireF.f5x, this.wireF.f6y, x, y, type, this));
                Log.e("MyDraw", "wire: x=" + this.wireF.f5x + " y=" + this.wireF.f6y + ";x=" + x + " y=" + y);
                this.wireF.set(0.0f, 0.0f);
            }
        }
        if (this.blocks.size() > 0) {
            Block block;
            Iterator it = this.blocks.iterator();
            while (it.hasNext()) {
                block = (Block) it.next();
                block.prevBlock1 = null;
                block.prevBlock2 = null;
            }
            it = this.blocks.iterator();
            while (it.hasNext()) {
                block = (Block) it.next();
                if (block.onTouch(x, y) == 1) {
                    this.wireF.set(0.0f, 0.0f);
                    return;
                }
                Iterator it2 = this.blocks.iterator();
                while (it2.hasNext()) {
                    Block block2 = (Block) it2.next();
                    if (!(block == block2 || block.figType == -6)) {
                        if (block.figType == 0 && Vector3.distance(block.x1, block.y1, block2.f0x, block2.f1y) <= this.windowHeight / 15.0f && block2.prevBlock1 == null) {
                            block2.setPrev1(block);
                        } else if (block.figType == 0 && Vector3.distance(block.x1, block.y1, block2.f0x, block2.f1y) <= this.windowHeight / 15.0f && block2.prevBlock2 == null) {
                            block2.setPrev2(block);
                        } else if (block.figType != 0 && Vector3.distance(block.f0x, block.f1y, block2.f0x, block2.f1y) <= this.windowHeight / 15.0f && block2.prevBlock1 == null) {
                            block2.setPrev1(block);
                        } else if (block.figType != 0 && Vector3.distance(block.f0x, block.f1y, block2.f0x, block2.f1y) <= this.windowHeight / 15.0f && block2.prevBlock2 == null) {
                            block2.setPrev2(block);
                        }
                        Log.e("Block", "candidate=" + block.figType + "distance=" + Vector3.distance(block.f0x, block.f1y, block2.f0x, block2.f1y));
                    }
                }
            }
        }
        invalidate();
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        int i;
        this.windowWidth = w;
        this.windowHeight = (float) h;
        this.button.clear();
        this.button.addButton("Wire", new RectF(0.0f, 0.0f, (float) (this.windowWidth / 10), this.windowHeight / 10.0f), 1);
        this.button.addButton("NO", new RectF(0.0f, (this.windowHeight / 10.0f) * 1.0f, (float) (this.windowWidth / 10), (this.windowHeight / 10.0f) * 2.0f), 2);
        this.button.addButton("AND", new RectF(0.0f, (this.windowHeight / 10.0f) * 2.0f, (float) (this.windowWidth / 10), (this.windowHeight / 10.0f) * 3.0f), 3);
        this.button.addButton("OR", new RectF(0.0f, (this.windowHeight / 10.0f) * 3.0f, (float) (this.windowWidth / 10), (this.windowHeight / 10.0f) * 4.0f), 4);
        this.button.addButton("XOR", new RectF(0.0f, (this.windowHeight / 10.0f) * 4.0f, (float) (this.windowWidth / 10), (this.windowHeight / 10.0f) * 5.0f), 5);
        this.button.addButton("SIM", new RectF(0.0f, (this.windowHeight / 10.0f) * 5.0f, (float) (this.windowWidth / 10), (this.windowHeight / 10.0f) * 6.0f), -10);
        this.button.addButton("EXIT", new RectF(0.0f, (this.windowHeight / 10.0f) * 6.0f, (float) (this.windowWidth / 10), (this.windowHeight / 10.0f) * 7.0f), -15);
        this.button.addButton("Next step", new RectF((float) (this.windowWidth - (this.windowWidth / 8)), this.windowHeight - (this.windowHeight / 10.0f), (float) this.windowWidth, this.windowHeight), -11);
        for (i = 0; i < this.inputs; i++) {
            this.blocks.add(new Block((this.windowHeight / 15.0f) * 5.0f, ((this.windowHeight / 15.0f) * 2.8f) * ((float) (i + 1)), -5, this));
            ((Block) this.blocks.get(i)).on = true;
        }
        for (i = 0; i < this.outputs; i++) {
            this.outputsBl.add(new Block(((float) this.windowWidth) - ((this.windowHeight / 15.0f) * 5.0f), ((this.windowHeight / 15.0f) * 2.8f) * ((float) (i + 1)), -6, this));
            this.blocks.add(new Block(((float) this.windowWidth) - ((this.windowHeight / 15.0f) * 5.0f), ((this.windowHeight / 15.0f) * 2.8f) * ((float) (i + 1)), -6, this));
        }
        this.paint.setTextSize((float) (h / 25));
        this.paintRightText.setTextSize((float) (h / 25));
        this.paintRightText.setTextAlign(Align.RIGHT);
        this.paintMiddleText.setTextSize((float) (h / 25));
        this.paintMiddleText.setTextAlign(Align.CENTER);
    }

    void saveProgress(String msg) {
        this.sPref = this.level.getPreferences(0);
        Editor ed = this.sPref.edit();
        ed.putString("Levels", msg);
        ed.commit();
        Toast.makeText(this.level, "Progress saved", 500).show();
    }

    String loadProgress() {
        this.sPref = this.level.getPreferences(0);
        this.savedText = this.sPref.getString("Levels", "");
        Toast.makeText(this.level, "Progress loaded", 500).show();
        return this.savedText;
    }

    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case 0:
                int i;
                int res = this.button.checkTouchedAll(event.getX(), event.getY(), this);
                if (res == -15) {
                    Intent intent = new Intent(getContext(), MainMenu.class);
                    this.level.finish();
                } else if (res == -11) {
                    if (this.simulation >= 0) {
                        this.simulation++;
                        if (this.simulation == ((String) this.vars.get(0)).length()) {
                            this.simulation = 0;
                            for (i = 0; i < this.inputs + this.outputs; i++) {
                                if (((Block) this.blocks.get(i)).figType == -5) {
                                    this.tableReal[i] = "";
                                } else {
                                    this.tableReal[i] = "";
                                }
                            }
                        }
                        this.button.resetPressed();
                        this.button.setChecked(5);
                    }
                } else if (res == -10) {
                    if (this.simulation == -1) {
                        this.simulation = 0;
                        for (i = 0; i < this.inputs + this.outputs; i++) {
                            if (((Block) this.blocks.get(i)).figType == -5) {
                                this.tableReal[i] = "";
                            } else {
                                this.tableReal[i] = "";
                            }
                        }
                    } else {
                        this.simulation = -1;
                        this.button.resetPressed();
                        for (i = 0; i < this.tableReal.length; i++) {
                            this.tableReal[i] = "";
                        }
                    }
                } else if (event.getX() >= ((float) (this.windowWidth / 10)) * 1.5f) {
                    addNewBlock(event.getX(), event.getY());
                    this.simulation = -1;
                } else {
                    this.simulation = -1;
                }
                if (this.simulation >= 0) {
                    String[] strArr;
                    for (int j = 0; j < this.inputs; j++) {
                        Log.e("MyDrawSim", "simulation=" + this.simulation);
                        if (((String) this.vars.get(j)).toCharArray()[this.simulation] == '1') {
                            ((Block) this.blocks.get(j)).on = true;
                            strArr = this.tableReal;
                            strArr[j] = strArr[j] + "1";
                        } else {
                            ((Block) this.blocks.get(j)).on = false;
                            strArr = this.tableReal;
                            strArr[j] = strArr[j] + "0";
                        }
                    }
                    this.button.setChecked(5);
                    for (i = 0; i < this.blocks.size(); i++) {
                        Iterator it = this.blocks.iterator();
                        while (it.hasNext()) {
                            ((Block) it.next()).check();
                        }
                    }
                    for (i = this.inputs; i < this.inputs + this.outputs; i++) {
                        ((Block) this.blocks.get(i)).check();
                        if (((Block) this.blocks.get(i)).on) {
                            strArr = this.tableReal;
                            strArr[i] = strArr[i] + "1";
                        } else {
                            strArr = this.tableReal;
                            strArr[i] = strArr[i] + "0";
                        }
                    }
                    this.correct = true;
                    for (i = 0; i < this.vars.size(); i++) {
                        if (!((String) this.vars.get(i)).equals(this.tableReal[i])) {
                            this.correct = false;
                        }
                    }
                    if (this.correct && this.vars.size() > 0) {
                        if (!this.savedText.contains(this.levelName)) {
                            this.savedText += this.levelName;
                            saveProgress(this.savedText + this.levelName + ";");
                        }
                        Toast.makeText(this.level, "Progress saved", 500).show();
                    }
                }
                invalidate();
                break;
        }
        return super.onTouchEvent(event);
    }

    protected void onDraw(Canvas canvas) {
        int i;
        canvas.drawColor(-1);
        this.button.drawAll(canvas);
        Iterator it = this.blocks.iterator();
        while (it.hasNext()) {
            ((Block) it.next()).draw(canvas);
        }
        canvas.drawText("Level name: " + this.levelName, (float) this.windowWidth, this.windowHeight / 25.0f, this.paintRightText);
        canvas.drawText("Time elapsed: " + ((System.currentTimeMillis() - this.time) / 1000) + " s", (float) this.windowWidth, (this.windowHeight * 2.0f) / 25.0f, this.paintRightText);
        canvas.drawText("REAL:", (float) this.windowWidth, this.windowHeight - ((((float) (((String) this.vars.get(0)).length() + 3)) * this.windowHeight) / 25.0f), this.paintRightText);
        canvas.drawText("GIVEN:", (float) (this.windowWidth / 5), this.windowHeight - ((((float) (((String) this.vars.get(0)).length() + 3)) * this.windowHeight) / 25.0f), this.paint);
        for (i = 0; i < this.tableReal.length; i++) {
            if (this.tableReal[i] != null) {
                if (i < this.inputs) {
                    canvas.drawText("INP: " + this.tableReal[i], (float) this.windowWidth, this.windowHeight - ((((float) ((((String) this.vars.get(i)).length() + 2) - i)) * this.windowHeight) / 25.0f), this.paintRightText);
                } else {
                    canvas.drawText("OUT:" + this.tableReal[i], (float) this.windowWidth, this.windowHeight - ((((float) ((((String) this.vars.get(i)).length() + 2) - i)) * this.windowHeight) / 25.0f), this.paintRightText);
                }
            }
        }
        for (i = 0; i < this.vars.size(); i++) {
            if (i < this.inputs) {
                canvas.drawText("INP: " + ((String) this.vars.get(i)), (float) (this.windowWidth / 5), this.windowHeight - ((((float) ((((String) this.vars.get(i)).length() + 2) - i)) * this.windowHeight) / 25.0f), this.paint);
            } else {
                canvas.drawText("OUT:" + ((String) this.vars.get(i)), (float) (this.windowWidth / 5), this.windowHeight - ((((float) ((((String) this.vars.get(i)).length() + 2) - i)) * this.windowHeight) / 25.0f), this.paint);
            }
        }
        if (this.correct) {
            canvas.drawText("CORRECT!", (float) (this.windowWidth / 2), this.windowHeight / 25.0f, this.paintMiddleText);
        }
        invalidate();
    }
}
