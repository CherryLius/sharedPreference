package cherry.android.sharedpreference.sample;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import cherry.android.sharedpreference.PreferenceLite;

public class SecondActivity extends AppCompatActivity implements View.OnClickListener {

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        textView = (TextView) findViewById(R.id.textView2);
        findViewById(R.id.button3).setOnClickListener(this);
        findViewById(R.id.button4).setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button3: {
                PreferenceLite preference = new PreferenceLite(this, "cherry.android");
                String user = preference.get("current_user", "");
                int age = preference.get("age", 0);
                textView.setText(user + "::" + age);
                preference.put("current_user", "David");
                Log.i("Test", "current=" + preference.get("current_user", ""));
                preference.delete("app_name");
                preference.put("flag", true);
                Log.i("Test", "flag=" + preference.get("flag", false));

                break;
            }
            case R.id.button4: {
                SharedPreferences preferences = getSharedPreferences("test_name", MODE_PRIVATE);
                Log.i("Test", "preferences=" + preferences.getString("current_user", ""));
                break;
            }
        }
    }
}
