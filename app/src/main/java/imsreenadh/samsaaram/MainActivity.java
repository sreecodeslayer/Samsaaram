package imsreenadh.samsaaram;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


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

    public void stopRecognition(View v) {
        Toast.makeText(this, "സംഭാഷണം തിരിച്ചറിയല്‍ സമാപിച്ചു", Toast.LENGTH_SHORT).show();

        ImageButton violetImageButton = (ImageButton) findViewById(R.id.samsaaramIcon);
        violetImageButton.setVisibility(View.VISIBLE); // hide the stopper button by showing the starter button
    }

    public void onSamsaaramIconClick(View v) {
        //Handle recognition here
        v.setVisibility(View.INVISIBLE); //hide the starter button to show stopper button
        Toast.makeText(this, "സംഭാഷണം തിരിച്ചറിയല്‍ ആരംഭിച്ചു", Toast.LENGTH_SHORT).show();
    }

}
