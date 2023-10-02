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
import android.text.Editable;
import android.text.Html;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
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
                    db.updateData(data.getId_text(),
                            data.getRecord_text(), data.getIsDone());
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
        recyclerView.setAdapter(adapter);
        displayData();

    }

    private List<TodoListModel> displayData() {
        cursor = db.getData();
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
            }
        }
        return dataList;
    }

    public void dialogRecord(View view) {
        LayoutInflater layoutInflater = LayoutInflater.from(getApplicationContext());
        View promtView = layoutInflater.inflate(layout.alert_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(promtView);
        final EditText input = promtView.findViewById(R.id.input);
        final TextView textWatcher = promtView.findViewById(R.id.text_watcher);
        final TextView textTitle = promtView.findViewById(R.id.title_alert);
        textTitle.setText(R.string.title_insert);

        textWatcher.setText(String.format("%s %s%s",
                MainActivity.this.getResources().getString(R.string.count_text),
                input.getText().length(),
                MainActivity.this.getResources().getString(R.string.count_symbol)));
            final TextWatcher mTextEditorWatcher = new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                textWatcher.setText(String.format("%s %s%s",
                        MainActivity.this.getResources().getString(R.string.count_text),
                        s.length(),
                        MainActivity.this.getResources().getString(R.string.count_symbol)));
            }

            public void afterTextChanged(Editable s) {
            }
        };
           input.addTextChangedListener(mTextEditorWatcher);

        builder.setPositiveButton(R.string.ok_btn, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.setNegativeButton(R.string.cancel_btn, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (input.length() > 0) {
                    m_Text = input.getText().toString();
                    db.insertRecord(m_Text, false,
                            String.valueOf(Calendar.getInstance().getTime()));
                    adapter.updateData(displayData());
                    alertDialog.dismiss();
                } else {
                    Toast.makeText(MainActivity.this, R.string.empty_error,
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    public void dialogRecord(View view, int id_record, String text, boolean isDone) {
        LayoutInflater layoutInflater = LayoutInflater.from(getApplicationContext());
        View promtView = layoutInflater.inflate(layout.alert_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(promtView);
        final EditText input = promtView.findViewById(R.id.input);
        final TextView textWatcher = promtView.findViewById(R.id.text_watcher);
        final TextView textTitle = promtView.findViewById(R.id.title_alert);
        textTitle.setText(R.string.title_update);
        input.setText(text);
        input.setSelection(input.getText().length());
        textWatcher.setText(String.format("%s %s%s",
                MainActivity.this.getResources().getString(R.string.count_text),
                input.getText().length(),
                MainActivity.this.getResources().getString(R.string.count_symbol)));
        final TextWatcher mTextEditorWatcher = new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                textWatcher.setText(String.format("%s %s%s",
                        MainActivity.this.getResources().getString(R.string.count_text),
                        s.length(),
                        MainActivity.this.getResources().getString(R.string.count_symbol)));
            }

            public void afterTextChanged(Editable s) {
            }
        };
        input.addTextChangedListener(mTextEditorWatcher);

        builder.setPositiveButton(R.string.ok_btn, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
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
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (input.length() > 0) {
                    m_Text = input.getText().toString();
                    db.updateData(id_record, m_Text, isDone);
                    adapter.updateData(displayData());
                    alertDialog.dismiss();
                } else {
                    Toast.makeText(MainActivity.this, R.string.empty_error,
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        cursor.close();
        db.close();
    }
}