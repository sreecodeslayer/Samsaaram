package imsreenadh.samsaaram;

//Android imports

import android.Manifest;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.io.IOException;

import edu.cmu.pocketsphinx.Assets;
import edu.cmu.pocketsphinx.Hypothesis;
import edu.cmu.pocketsphinx.RecognitionListener;
import edu.cmu.pocketsphinx.SpeechRecognizer;

import static edu.cmu.pocketsphinx.SpeechRecognizerSetup.defaultSetup;

// Java imports
// CMU PocketSphinx based Imports

public class MainActivity extends AppCompatActivity implements RecognitionListener {


    private static final String SAMSAARAM = "samsaaram";

    ImageButton violetImageButton;
    ImageButton redImageButton;
    TextView messageTextView;
    TextView resultTextView;

    private SpeechRecognizer recognizer;
    private boolean permission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Dexter.initialize(this);
        messageTextView = (TextView) findViewById(R.id.message);
        resultTextView = (TextView) findViewById(R.id.result_text);
        violetImageButton = (ImageButton) findViewById(R.id.samsaaramVioletIcon);
        redImageButton = (ImageButton) findViewById(R.id.samsaaramRedIcon);

        // initially this needs to be hidden to maintain proper shadow of button
        redImageButton.setVisibility(View.INVISIBLE);

        // PERMISSION

        Dexter.checkPermissionOnSameThread(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse response) {

                /*
                --------------------------------------- GRANTED ------------------------------------
                */

                permission = true;

                /*
                ************************************************************************************
                IMPLEMENTATION OF ASyncTask to handle intensive Decoder Configuration
                ************************************************************************************
                */
                new AsyncTask<Void, Void, Exception>() {
                    @Override
                    protected Exception doInBackground(Void... params) {
                        try {

                            Assets assets = new Assets(MainActivity.this);

                            File assetDir = assets.syncAssets();

                            setupRecognizer(assetDir);

                        } catch (IOException e) {
                            return e;
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(final Exception result) {

                        toaster("DECODER COFIGURED, YOU MAY BEGIN", 100);
                        // show error (if any)
                        if (result != null) {
                            ((TextView) findViewById(R.id.caption_text))
                                    .setText(R.string.failed_init_recognizer);
                        } else {
                            ((TextView) findViewById(R.id.caption_text)).setVisibility(View.INVISIBLE);
                        }
                    }
                }.execute();
            }

            /*
              --------------------------------------- DENIED ------------------------------------
            */
            @Override
            public void onPermissionDenied(PermissionDeniedResponse response) {
                permission = false;
                toaster("Please enable permission under App info and Relaunch the app!", 2500);
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                /* ... */
                token.continuePermissionRequest();
            }
        }, Manifest.permission.RECORD_AUDIO);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    // Menu icons are inflated just as they were with actionbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    // Handle Menu clicks
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_about:
                // User chose the "AboutActivity" item, show the app about UI...
                startActivity(new Intent(this, AboutActivity.class));
                return true;

            case R.id.action_feedback:
                // User chose the "Feedback" item, show the app feedback UI...
                startActivity(new Intent(this, FeedbackActivity.class));
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    private void toaster(String message, int duration) {
        Toast.makeText(this, message, duration).show();
    }

    public void onSamsaaramStopIconClicked(View v) {
        //Handle stop recognition here
        redImageButton.setVisibility(View.INVISIBLE); // hide the stopper button by showing the starter button
        violetImageButton.setVisibility(View.VISIBLE);
        messageTextView.setText(R.string.start_message);
        recognizer.cancel();
    }

    public void onSamsaaramStartIconClicked(View v) {


        if (permission) {
            //Handle start recognition here
            violetImageButton.setVisibility(View.INVISIBLE); //hide the starter button by showing the stopper button
            redImageButton.setVisibility(View.VISIBLE);
            messageTextView.setText(R.string.stop_message);
            recognizer.startListening(SAMSAARAM);
            toaster("TRUE", 1000);// there is a bug here, but what the hell is it! o.O
        } else {
            toaster("Please grant permission first and relaunch App!", 1000);
        }
        // update on the bug : CASE SENSITIVE goto line 31 ( LOL )
    }


    /*
    ********************************************************************************************************
    IMPLEMENTATION OF CMU SPEECH RECOGNITION LISTENER METHODS
    ******************************************************************************************************
    */
    @Override
    public void onBeginningOfSpeech() {

    }

    @Override
    public void onEndOfSpeech() {

    }

    @Override
    public void onPartialResult(Hypothesis hypothesis) {
        if (hypothesis == null) return;
        resultTextView.setText(hypothesis.getHypstr());
    }

    @Override
    public void onResult(Hypothesis hypothesis) {
        ((TextView) findViewById(R.id.result_text)).setText("");
        if (hypothesis != null) resultTextView.setText(hypothesis.getHypstr());
    }

    @Override
    public void onError(Exception e) {
        ((TextView) findViewById(R.id.caption_text)).setText(e.getMessage());
    }

    @Override
    public void onTimeout() {
    }

    public void setupRecognizer(File assetsDir) throws IOException {
        // The recognizer can be configured to perform multiple searches
        // of different kind and switch between them
        recognizer = defaultSetup()
                .setAcousticModel(new File(assetsDir, "ml-in-acoustic"))
                .setDictionary(new File(assetsDir, "samsaaram.dic"))

                // To disable logging of raw audio comment out this call (takes a lot of space on the device)
                // .setRawLogDir(assetsDir)

                // Threshold to tune for keyphrase to balance between false alarms and misses
                // .setKeywordThreshold(1e-45f)

                // Use context-independent phonetic search, context-dependent is too slow for mobile
                .setBoolean("-allphone_ci", true)
                .getRecognizer();
        recognizer.addListener(this);
        // Create language model search
        File languageModel = new File(assetsDir, "samsaaram.arpa");
        recognizer.addNgramSearch(SAMSAARAM, languageModel);
    }
}
