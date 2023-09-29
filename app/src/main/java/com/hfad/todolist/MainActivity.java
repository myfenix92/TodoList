package com.hfad.todolist;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.text.Html;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Scroller;
import android.widget.TextView;
import android.widget.Toast;

import com.hfad.todolist.R.layout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
     RecyclerView recyclerView;
     List<TodoListModel> dataList = new ArrayList<>();
     TodoListDBHelper db;
     TodoListAdapter adapter;
     Cursor cursor;
    private String m_Text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recycler_todo);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TodoListAdapter.Listener listener = new TodoListAdapter.Listener() {
            @Override
            public void onDoneClick(TodoListModel data, int position) {
                ContentValues recordValues = new ContentValues();
                recordValues.put("DONE", data.getIsDone());
                try {
                    Boolean checkUpdate = db.updateData(data.getId_text(),
                            data.getRecord_text(), data.getIsDone());
                    if (checkUpdate) {
                        Toast.makeText(MainActivity.this, "updated",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "not updated",
                                Toast.LENGTH_SHORT).show();
                    }
                } catch (SQLiteException e) {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Database unavailable", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }

            @Override
            public void onChangeClick(TodoListModel data, int position) {
                View card = findViewById(R.id.card_view);
                dialogRecord(card, data.getId_text(), data.getRecord_text(), data.getIsDone());

            }
        };
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new TodoListAdapter(this, dataList, listener);
        db = new TodoListDBHelper(this);
        //  adapter.setList(dataList);
        recyclerView.setAdapter(adapter);
        displayData();
      //  adapter.updateData(displayData());

    }

    private List<TodoListModel> displayData() {
        cursor = db.getData();
   //
        if (cursor.getCount() == 0) {
            dataList.clear();
            Toast.makeText(this, "no entry exists", Toast.LENGTH_SHORT).show();
        } else {
            dataList.clear();
            while (cursor.moveToNext()) {
                dataList.add(
                        new TodoListModel(
                                cursor.getInt(0),
                                cursor.getString(1),
                                cursor.getInt(2) == 1,
                                cursor.getString(3))
                );

                //   adapter.setList(dataList);
                //   recyclerView.setAdapter(adapter);
            }

        }

        return dataList;
    }
    public void dialogRecord(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String title = getString(R.string.title_insert);
        builder.setTitle(title);
        builder.setIcon(android.R.drawable.ic_menu_edit);
// Set up the input
        final EditText input = new EditText(this);

// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        input.setSingleLine(false);
        input.setLines(3);
        input.setMaxLines(6);
        input.setFilters(new InputFilter[] {new InputFilter.LengthFilter(250)});
        builder.setMessage("messa");
        builder.setView(input);

// Set up the buttons
        builder.setPositiveButton(R.string.ok_btn, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                m_Text = input.getText().toString();
                Boolean checkInsertData = db.insertRecord(m_Text, false,
                        String.valueOf(Calendar.getInstance().getTime()));
                     adapter.updateData(displayData());
                if (checkInsertData) {
                    Toast.makeText(MainActivity.this, "new entry inserted",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "new entry not inserted",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton(R.string.cancel_btn, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }
    public void dialogRecord(View view, int id_record, String text, boolean isDone) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.title_update);
        builder.setIcon(android.R.drawable.ic_menu_edit);
// Set up the input
        final EditText input = new EditText(this);
        input.setText(text);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        input.setSingleLine(false);
        input.setLines(3);
        input.setMaxLines(6);
        builder.setView(input);
        builder.setView(input);
// Set up the buttons
        builder.setPositiveButton(R.string.ok_btn, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                m_Text = input.getText().toString();
                Boolean checkInsertData = db.updateData(id_record, m_Text, isDone);
                adapter.updateData(displayData());
                if (checkInsertData) {
                    Toast.makeText(MainActivity.this, "new entry inserted",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "new entry not inserted",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNeutralButton(R.string.delete_text, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Boolean checkInsertData = db.deleteData(id_record);
                adapter.updateData(displayData());
                if (checkInsertData) {
                    Toast.makeText(MainActivity.this, "new entry inserted",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "new entry not inserted",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton(R.string.cancel_btn, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }
//    public void dialogRecord(View view, int id_record) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//       // builder.setTitle(R.string.title_delete);
//        builder.setIcon(android.R.drawable.ic_dialog_alert);
//        builder.setMessage(R.string.delete_text);
//// Set up the buttons
//        builder.setPositiveButton(R.string.ok_btn, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                Boolean checkInsertData = db.deleteData(id_record);
//                adapter.updateData(displayData());
//                if (checkInsertData) {
//                    Toast.makeText(MainActivity.this, "new entry inserted",
//                            Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(MainActivity.this, "new entry not inserted",
//                            Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//        builder.setNegativeButton(R.string.cancel_btn, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.cancel();
//            }
//        });
//        builder.show();
//    }
//    @Override
//    public void onResume() {
//        super.onResume();
//        displayData();
//    }
//    @Override
//    public void onPause() {
//        super.onPause();
//        Cursor newCursor = db.getData();
//        if(cursor.getCount() == 0) {
//            Toast.makeText(this, "no entry exists", Toast.LENGTH_SHORT).show();
//            return;
//        } else {
//            while (cursor.moveToNext()) {
//                record.add(cursor.getString(1));
//                done.add(cursor.getInt(2) == 1);
//            }
//        }
//    //    RecyclerView.Adapter cursorAdapter = recyclerView.getAdapter();
//      //  cursorAdapter(newCursor);
//        cursor = newCursor;
//    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        cursor.close();
        db.close();
    }
}