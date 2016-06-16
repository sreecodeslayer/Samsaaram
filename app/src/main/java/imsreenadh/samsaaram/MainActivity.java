package imsreenadh.samsaaram;

//Android imports

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        messageTextView = (TextView) findViewById(R.id.message);
        resultTextView = (TextView) findViewById(R.id.result_text);
        violetImageButton = (ImageButton) findViewById(R.id.samsaaramVioletIcon);
        redImageButton = (ImageButton) findViewById(R.id.samsaaramRedIcon);

        // initially this needs to be hidden to maintain proper shadow of button
        redImageButton.setVisibility(View.INVISIBLE);

        /*
        ********************************************************************************************************
        IMPLEMENTATION OF ASyncTask to handle intensive Decoder Configuration
        ******************************************************************************************************
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

                toaster("DECODER COFIGURED, YOU MAY BEGIN");
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        recognizer.cancel();
        recognizer.shutdown();
    }

    private void toaster(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }
    public void onSamsaaramStopIconClicked(View v) {
        //Handle stop recognition here
        redImageButton.setVisibility(View.INVISIBLE); // hide the stopper button by showing the starter button
        violetImageButton.setVisibility(View.VISIBLE);
        messageTextView.setText(R.string.start_message);
        recognizer.cancel();
    }

    public void onSamsaaramStartIconClicked(View v) {
        //Handle start recognition here
        violetImageButton.setVisibility(View.INVISIBLE); //hide the starter button by showing the stopper button
        redImageButton.setVisibility(View.VISIBLE);
        messageTextView.setText(R.string.stop_message);
        if(recognizer.startListening(SAMSAARAM)){
            toaster("TRUE");// there is a bug here, but what the hell is it! o.O
        }
        else {
            toaster("FALSE");
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
