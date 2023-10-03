package com.hfad.todolist;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class TodoListModel {
    private final String[] monthList = new String[] {
            "Jan", "Feb", "Mar",
            "Apr", "May", "Jun",
            "Jul", "Aug", "Sep",
            "Oct", "Nov", "Dec"};
    private final String[] dayWeekListEn = new String[] {
            "Mon", "Tue", "Wed",
            "Thu", "Fri", "Sat", "Sun"};
    private final String[] dayWeekListRu = new String[] {
            "Пн", "Вт", "Ср",
            "Чт", "Пт", "Сб", "Вс"};
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
        String dateFormat = date_create_text;
        String month = dateFormat.substring(4, 7);
        for (int i = 0; i < monthList.length; i++) {
            if (Objects.equals(monthList[i], month)) {
                month = ((i + 1) < 10 ? "0" + (i + 1) : (i + 1)).toString();
            }
        }
        String dayWeek = dateFormat.substring(0, 3);
        for (int i = 0; i < dayWeekListEn.length; i++) {
            if (Objects.equals(dayWeekListEn[i], dayWeek)) {
                dayWeek = dayWeekListRu[i];
            }
        }
        String day = dateFormat.substring(7, 10);
        String time = dateFormat.substring(11, 19);
        String year = dateFormat.substring(dateFormat.length() - 4);
        return String.format("%s %s-%s-%s, %s", dayWeek, day, month, year, time);
    }

    public void setDate_create_text(String date_create_text) {
        this.date_create_text = date_create_text;
    }
}
