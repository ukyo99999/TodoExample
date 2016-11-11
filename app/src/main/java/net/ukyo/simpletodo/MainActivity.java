package net.ukyo.simpletodo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ListView mListView;
    private EditText mEditText;
    private Button mButton;
    private ArrayList<String> mItems;
    private ArrayAdapter<String> mItemsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
        setListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        readItems();
        setData();
        mItemsAdapter.notifyDataSetChanged();
    }

    private void findViews() {
        mListView = (ListView) findViewById(R.id.list_items);
        mEditText = (EditText) findViewById(R.id.edit_item);
        mButton = (Button) findViewById(R.id.btn_add_item);
    }

    private void setData() {
        mItemsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mItems);
        mListView.setAdapter(mItemsAdapter);
    }

    private void setListener() {
        mButton.setOnClickListener(onClickListener);
        mListView.setOnItemLongClickListener(itemLongClickListener);
        mListView.setOnItemClickListener(itemClickListener);
    }

    private void readItems() {

        File fileDir = getFilesDir();
        File todoFile = new File(fileDir, "todo.txt");

        try {
            mItems = new ArrayList<>(FileUtils.readLines(todoFile));
        } catch (IOException e) {
            mItems = new ArrayList<>();
        }
    }

    private void writeItems() {

        File fileDir = getFilesDir();
        File todoFile = new File(fileDir, "todo.txt");

        try {
            FileUtils.writeLines(todoFile, mItems);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void gotoEditItemActivity(int position) {
        Intent intent = new Intent();
        intent.setClass(this, EditItemActivity.class);

        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        bundle.putStringArrayList("items", mItems);
        intent.putExtras(bundle);

        startActivity(intent);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            switch (view.getId()) {
                case R.id.btn_add_item:

                    String itemString = mEditText.getText().toString();
                    mItemsAdapter.add(itemString);
                    mEditText.setText("");

                    writeItems();
                    break;
            }

        }
    };

    private AdapterView.OnItemLongClickListener itemLongClickListener = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {

            mItemsAdapter.remove(mItems.get(position));
            mItemsAdapter.notifyDataSetChanged();
            writeItems();

            return true;
        }
    };

    private AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

            gotoEditItemActivity(position);

        }
    };
}
