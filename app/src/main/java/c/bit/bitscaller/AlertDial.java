package c.bit.bitscaller;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AlertDial extends AppCompatActivity {

    Button dismiss;
    TextView name;
    TextView number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.popup, null);
        dialogBuilder.setView(dialogView);
        dismiss =dialogView.findViewById(R.id.dismiss);
        name = dialogView.findViewById(R.id.callername);
        number = dialogView.findViewById(R.id.callernumber);
        name.setText(getIntent().getStringExtra("NAME"));
        number.setText(getIntent().getStringExtra("NUMBER"));
        final AlertDialog ad = dialogBuilder.show();
        dismiss.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ad.dismiss();
                finish();
            }
        });
    }
}
