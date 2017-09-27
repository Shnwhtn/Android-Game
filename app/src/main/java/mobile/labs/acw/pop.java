package mobile.labs.acw;


import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.TextView;

public class pop extends Activity{

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.popupwin);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*.8),(int)(height*.6));


        TextView winsign = (TextView)findViewById(R.id.winStatement);
        winsign.setText(R.string.win);

       // TextView currentClick = (TextView)findViewById(R.id.YourClicks);
       // currentClick.setText(R.string.finalscore);

       // TextView currentClickValue = (TextView)findViewById(R.id.currentClicks);
        //currentClick.setText();

      //  TextView history = (TextView)findViewById(R.id.history);
       // history.setText(R.string.lowestScore);

      //  TextView historyCounter = (TextView)findViewById(R.id.historyCounter);
        //

    }
}
