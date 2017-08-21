package cherry.android.sharedpreference.sample;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class SecondActivity extends AppCompatActivity implements View.OnClickListener {

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        textView = (TextView) findViewById(R.id.textView2);
        findViewById(R.id.button3).setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button3:
                Uri uri = Uri.parse("content://cherry.android/SharedPreference");
                Cursor cursor = getContentResolver().query(uri, new String[]{"app_name"}, null, null, null);
                if (cursor.moveToFirst()) {
                    textView.setText(cursor.getString(0));
                }
                cursor.close();
                break;
        }
    }
}
