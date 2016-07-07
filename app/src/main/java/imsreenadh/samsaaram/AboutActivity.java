package imsreenadh.samsaaram;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        TextView githubHyperlink = (TextView) findViewById(R.id.contributeGithub);
        githubHyperlink.setMovementMethod(LinkMovementMethod.getInstance());
    }
}
