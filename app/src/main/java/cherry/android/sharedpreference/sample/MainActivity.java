package cherry.android.sharedpreference.sample;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

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
                ContentValues values = new ContentValues();
                values.put("app_name", "SharedPreferenceSample");
                getContentResolver().insert(Uri.parse("content://cherry.android/SharedPreference"), values);
                break;
            case R.id.button2:
                startActivity(new Intent(this, SecondActivity.class));
                break;

        }
    }
}
