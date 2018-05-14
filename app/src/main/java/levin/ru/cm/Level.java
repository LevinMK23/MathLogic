package levin.ru.cm;

import android.app.Activity;
import android.os.Bundle;

public class Level extends Activity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        setContentView(new MyDraw(this, this));
    }
}
