package c.bit.bitscaller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


    }

    public void phone(View view)
    {
        Intent intent = new Intent(home.this,phone.class);
        startActivity(intent);

    }

    public void email(View view)
    {
        Intent intent = new Intent(home.this,email.class);
        startActivity(intent);
        }
}
