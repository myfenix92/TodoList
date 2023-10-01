package com.hfad.todolist;

import android.content.Context;
import android.graphics.Paint;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class TodoListAdapter extends RecyclerView.Adapter<TodoListAdapter.ViewHolder> {

    interface Listener{
        void onDoneClick(TodoListModel data, int position);
        void onChangeClick(TodoListModel data, int position);
    }
    private Context context;
    private List<TodoListModel> dataList;
    private Listener listener;
    public TodoListAdapter(Context context, List<TodoListModel> dataList, Listener listener) {
        this.context = context;
        this.dataList = dataList;
        this.listener = listener;
    }
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView v = (CardView)LayoutInflater.from(parent.getContext()).inflate(R.layout.list_todo, parent, false);
        return new ViewHolder(v);
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TodoListModel todoListModel = dataList.get(position);
        CardView cardView = holder.cardView;
        holder.record_id.setText(todoListModel.getRecord_text());
        holder.done_id.setChecked(todoListModel.getIsDone());
        holder.record_date.setText(todoListModel.getDate_create_text());
        if (holder.done_id.isChecked()) {
            holder.record_id.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            holder.record_id.setPaintFlags(0);
        }
        holder.done_id.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                boolean isDone = ((CheckBox) buttonView).isChecked();
                if (isDone) {
                    todoListModel.setDone(true);
                    holder.record_id.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                    listener.onDoneClick(todoListModel, position);
                } else {
                    todoListModel.setDone(false);
                    holder.record_id.setPaintFlags(0);
                    listener.onDoneClick(todoListModel, position);
                }
            }
        });
//
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onChangeClick(todoListModel, position);
                }
            }
        });

    }

    public void updateData(List<TodoListModel> data) {
     //   dataList.clear();
        dataList = data;
        notifyDataSetChanged();
    }

// вот тут вынести отдельно обработчики
//        View.OnClickListener clickListener = new View.OnClickListener() {
//        @Override
//        public void onClick(View view) {
//            if (listener != null) {
//                listener.onChangeClick(todoListModel, position);
//            }
//        }
//    };

    @Override
    public int getItemCount() {
        return dataList.size();
        //return dataList != null ? dataList.size() : 0;
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView record_id;
        TextView record_date;
        CheckBox done_id;
        CardView cardView;

        public ViewHolder(CardView itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.card_view);
            record_id = itemView.findViewById(R.id.text_record);
            record_date = itemView.findViewById(R.id.date_record);
            done_id = itemView.findViewById(R.id.check_record);
        }
    }
}
