package imsreenadh.samsaaram;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    ImageButton violetImageButton;
    ImageButton redImageButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        violetImageButton = (ImageButton) findViewById(R.id.samsaaramVioletIcon);
        redImageButton = (ImageButton) findViewById(R.id.samsaaramRedIcon);
        redImageButton.setVisibility(View.INVISIBLE); //initially this needs to be hidden to maintain proper shadow of button
        /*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        }); */


    }

    public void onSamsaaramStopIconClicked(View v) {

        //Handle stop recognition here
        Toast.makeText(this, R.string.stop_toast_message, Toast.LENGTH_SHORT).show();
        redImageButton.setVisibility(View.INVISIBLE);
        violetImageButton.setVisibility(View.VISIBLE); // hide the stopper button by showing the starter button
    }

    public void onSamsaaramStartIconClicked(View v) {
        //Handle start recognition here
        violetImageButton.setVisibility(View.INVISIBLE); //hide the starter button to show stopper button
        redImageButton.setVisibility(View.VISIBLE);
        TextView messageTextView = (TextView) findViewById(R.id.message);
        messageTextView.setText(R.string.stop_message);
        Toast.makeText(this, R.string.start_toast_message, Toast.LENGTH_SHORT).show();
    }

}
