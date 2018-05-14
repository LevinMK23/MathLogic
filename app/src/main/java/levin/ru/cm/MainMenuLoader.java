package levin.ru.cm;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.view.ViewCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Objects;

public class MainMenuLoader extends View {
    //Bitmap f2b = BitmapFactory.decodeResource(getResources(), C0100R.drawable.new_logo);
    private boolean bit = true;
    Bitmap bit2;
    public Buttons button = new Buttons();
    private boolean connected = false;
    Rect dest = new Rect(0, 0, this.f4w, this.f3h);
    public int f3h = 0;
    public LinkedList<String> levelStr = new LinkedList();
    private boolean loading = true;
    MainMenu menu;
    private HTTPwork myThread;
    private Paint paint;
    private Paint paintBig;
    private Paint paintBl;
    private boolean plus = true;
    String savedText;
    int speed = 12;
    long time = System.currentTimeMillis();
    public int f4w = 0;

    public MainMenuLoader(Context context, MainMenu menu) {
        super(context);
        boolean z = true;
       /* @SuppressLint("WrongConstant") NetworkInfo activeNetworkInfo = ((ConnectivityManager) Objects.requireNonNull(context.getSystemService("connectivity"))).getActiveNetworkInfo();
        if (activeNetworkInfo == null || !activeNetworkInfo.isConnected()) {
            z = false;
        }*/
        //this.connected = z;
        this.menu = menu;
        this.paint = new Paint();
        this.paintBl = new Paint();
        this.paintBig = new Paint();
        this.paint.setTextAlign(Align.CENTER);
        this.paintBl.setTextAlign(Align.CENTER);
        this.paintBig.setTextAlign(Align.CENTER);
        /*if (this.connected) {
            this.myThread = new HTTPwork();
            this.myThread.start(this);
        }*/
        this.savedText = menu.getPreferences(0).getString("Levels", "");
        Log.e("MainMenuLoader", "Me");
        this.levelStr.add("And;2&1;0011;1010;0010");
        this.levelStr.add("Or;2&1;0011;1010;1011");
        this.levelStr.add("Xor;2&1;0011;1010;1001");
        this.levelStr.add("No;1&1;01;10");
    }

    public int timePassed() {
        return (int) ((System.currentTimeMillis() - this.time) / 1000);
    }

    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case 0:
                Iterator it = this.levelStr.iterator();
                while (it.hasNext()) {
                    Log.e("MainMenuLoader", "levelStr[]=" + ((String) it.next()));
                }
                int type = this.button.checkTouchedAll(event.getX(), event.getY());
                if (type > 0) {
                    this.button.resetPressed();
                    Intent i = new Intent(getContext(), Level.class);
                    Log.e("MainMenuLoader", "type=" + type);
                    Log.e("MainMenuLoader", "levelStr=" + ((String) this.levelStr.get(type - 1)));
                    i.putExtra("levelStr", (String) this.levelStr.get(type - 1));
                    this.menu.startActivity(i);
                    break;
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    public void end(String msg) {
        if (msg.length() == 0) {
            this.connected = false;
            return;
        }
        this.loading = false;
        String[] allS = new String[255];
        allS = msg.split("\\|");
        float x = 0.0f;
        for (int i = 0; i < allS.length; i++) {
            this.levelStr.add(allS[i]);
            String[] curl = allS[i].split(";");
            x += (float) (this.f4w / 25);
            float y = (float) ((((i / 10) + 1) * this.f3h) / 3);
            Log.e("MainMenuLoader", "newB=" + (this.levelStr.size() - 1));
            this.button.addButton(curl[0], new RectF(x, y, ((float) ((curl[0].length() * this.f4w) / 30)) + x, ((float) (this.f3h / 6)) + y), this.levelStr.size(), (float) (this.f3h / 25));
            x += (float) ((this.f4w / 30) * curl[0].length());
        }
        try {
            this.myThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        this.f4w = w;
        this.f3h = h;
        this.button.addButton("AND", new RectF((float) ((w / 11)), (float) (h / 5), (float) ((w / 12) * 2), (float) (h / 3)), 1, (float) (h / 25));
        this.button.addButton("OR", new RectF((float) ((w / 11) * 2), (float) (h / 5), (float) ((w / 12) * 3), (float) (h / 3)), 2, (float) (h / 25));
        this.button.addButton("XOR", new RectF((float) ((w / 11) * 3), (float) (h / 5), (float) ((w / 12) * 4), (float) (h / 3)), 3, (float) (h / 25));
        this.button.addButton("No", new RectF((float) ((w / 11) * 4), (float) (h / 5), (float) ((w / 12) * 5), (float) (h / 3)), 4, (float) (h / 25));
        //this.bit2 = Bitmap.createScaledBitmap(this.f2b, w / 2, h / 2, false);
        this.paintBl.setColor(ViewCompat.MEASURED_STATE_MASK);
        this.paintBl.setTextSize((float) (h / 25));
        this.paint.setColor(ViewCompat.MEASURED_STATE_MASK);
        this.paint.setTextSize((float) (h / 25));
        this.paintBig.setColor(ViewCompat.MEASURED_STATE_MASK);
        this.paintBig.setTextSize((float) (h / 10));
        super.onSizeChanged(w, h, oldw, oldh);
    }

    protected void onDraw(Canvas canvas) {
        boolean z = false;
        if (this.bit) {
            canvas.drawColor(Color.rgb(46, 204, 113));
            //canvas.drawBitmap(this.bit2, (float) (this.f4w / 4), (float) (this.f4w / 6), this.paint);
            canvas.drawText("MathLogic", canvas.getWidth() / 2, canvas.getHeight()/2, this.paintBig);
            if (timePassed() == 5) {
                this.bit = false;
                Log.e("MainMenuLoader", "timePassed=" + timePassed());
            }
        } else {
            canvas.drawColor(Color.rgb(241, 196, 15));
            canvas.drawText("MathLogic", (float) (this.f4w / 2), (float) (this.f3h / 10), this.paintBig);
            if (this.loading) {
                if ((System.currentTimeMillis() / ((long) this.speed)) % 255 <= 0 || (System.currentTimeMillis() / ((long) this.speed)) % 255 >= 255) {
                    if (!this.plus) {
                        z = true;
                    }
                    this.plus = z;
                }
                if (this.plus) {
                    this.paintBl.setAlpha((int) ((System.currentTimeMillis() / ((long) this.speed)) % 255));
                } else {
                    this.paintBl.setAlpha(255 - ((int) ((System.currentTimeMillis() / ((long) this.speed)) % 255)));
                }
                if (this.connected) {
                    canvas.drawText("Loading..", (float) (this.f4w / 2), (float) (this.f3h / 2), this.paintBl);
                } else {
                    canvas.drawText("Communictaion error", (float) (this.f4w / 2), (float) (this.f3h / 2), this.paintBl);
                }
            }
            this.button.drawAllWithSolved(canvas, this.savedText);
        }
        invalidate();
    }
}
