package com.hfad.todolist;

public class TodoListModel {

    private int id_text;
    private String record_text;
    private boolean isDone;

    private String date_create_text;

    public TodoListModel(int id_text, String record_text, boolean isDone, String date_create_text) {
        this.id_text = id_text;
        this.record_text = record_text;
        this.isDone = isDone;
        this.date_create_text = date_create_text;
    }

    public String getRecord_text() {
        return record_text;
    }

    public void setRecord_text(String record_text) {
        this.record_text = record_text;
    }

    public boolean getIsDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

    public int getId_text() {
        return id_text;
    }

    public void setId_text(int id_text) {
        this.id_text = id_text;
    }

    public String getDate_create_text() {
        return date_create_text;
    }

    public void setDate_create_text(String date_create_text) {
        this.date_create_text = date_create_text;
    }
}
