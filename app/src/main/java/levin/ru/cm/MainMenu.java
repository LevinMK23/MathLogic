package levin.ru.cm;

import android.app.Activity;
import android.os.Bundle;

public class MainMenu extends Activity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        setContentView(new MainMenuLoader(this, this));
    }
}
