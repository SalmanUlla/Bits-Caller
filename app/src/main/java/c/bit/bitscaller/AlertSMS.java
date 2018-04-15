package c.bit.bitscaller;

import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AlertSMS extends AppCompatActivity {

    DatabaseHelper db;
    Button dismiss;
    TextView name;
    TextView number;
    TextView message;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.popup_sms, null);
        dialogBuilder.setCancelable(false);
        dialogBuilder.setView(dialogView);
        dismiss = dialogView.findViewById(R.id.dismisssms);
        name = dialogView.findViewById(R.id.smsname);
        number = dialogView.findViewById(R.id.smsnumber);
        message = dialogView.findViewById(R.id.smsbody);
        name.setText(extras.getString("name"));
        number.setText(extras.getString("MessageNumber"));
        message.setText("Message:" + extras.getString("Message"));
        final AlertDialog ad = dialogBuilder.show();
        dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ad.dismiss();
                finish();
            }
        });

    }
}
