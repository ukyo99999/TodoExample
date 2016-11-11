package net.ukyo.simpletodo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class EditItemActivity extends AppCompatActivity {

    private EditText mEditText;
    private Button mButton;
    private ArrayList<String> mItems;
    private int mPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        dataInit();
        findViews();
        setData();
        setListener();
    }

    private void dataInit() {
        Bundle bundle = getIntent().getExtras();
        mPosition = bundle.getInt("position");
        mItems = bundle.getStringArrayList("items");
    }

    private void findViews() {
        mEditText = (EditText) findViewById(R.id.edit_item);
        mButton = (Button) findViewById(R.id.btn_save);
    }

    private void setData() {
        mEditText.setText(mItems.get(mPosition));
    }

    private void setListener() {
        mButton.setOnClickListener(onClickListener);
    }


    private void save() {
        File fileDir = getFilesDir();
        File todoFile = new File(fileDir, "todo.txt");

        try {
            FileUtils.writeLines(todoFile, mItems);
        } catch (IOException e) {
            e.printStackTrace();
        }
        finish();
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            switch (view.getId()) {
                case R.id.btn_save:
                    mItems.set(mPosition, mEditText.getText().toString());
                    save();
                    break;
            }
        }
    };

}
