package cherry.android.sharedpreference.sample;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import cherry.android.sharedpreference.PreferenceLite;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.textView).setOnClickListener(this);
        findViewById(R.id.button).setOnClickListener(this);
        findViewById(R.id.button2).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button:
                PreferenceLite preference = new PreferenceLite(this, "cherry.android");
                preference.put("current_user", "TomAndJerry");
                preference.put("age", 20);
                break;
            case R.id.button2:
                startActivity(new Intent(this, SecondActivity.class));
                break;

        }
    }
}
