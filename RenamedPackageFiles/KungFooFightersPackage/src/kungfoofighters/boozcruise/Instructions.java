//Default class from the tutorial that displays a text box. This was not modified from the tutorial


package kungfoofighters.boozcruise;

import kungfoofighters.boozcruise.R;

import android.app.Activity;

import android.os.Bundle;
import android.view.View;
import android.view.Window;

public class Instructions extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_dialog);
    }

    /**
     * Callback method defined by the View
     * @param v
     */
    public void finishDialog(View v) {
        Instructions.this.finish();
    }
}
