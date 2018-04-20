package c.bit.bitscaller;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class Restore extends AppCompatActivity {

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restore);
        listView = findViewById(R.id.listView);
        ArrayList<String> FilesInFolder = GetFiles(Environment.getExternalStorageDirectory().getPath() + "/BITS Backup");
        listView.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, FilesInFolder));
        listView.setOnItemClickListener((parent, v, position, id) -> {
            Intent intent = new Intent();
            String fname = (String) (listView.getItemAtPosition(position));
            intent.putExtra("filename", fname);
            setResult(RESULT_OK, intent);
            finish();
        });
    }

    public ArrayList<String> GetFiles(String DirectoryPath) {
        ArrayList<String> MyFiles = new ArrayList<>();
        File f = new File(DirectoryPath);
        f.mkdirs();
        File[] files = f.listFiles();
        if (files.length == 0) {
            Toast.makeText(this, "No Backup To Restore", Toast.LENGTH_LONG).show();
            finish();
            return MyFiles;
        }
        else {
            for (int i = 0; i < files.length; i++) {
                if (files[i].getName().endsWith(".db"))
                    MyFiles.add(files[i].getName());
            }
            return MyFiles;
        }
    }
}
