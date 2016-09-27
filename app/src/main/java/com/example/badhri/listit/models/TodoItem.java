package com.example.badhri.listit.models;

import com.orm.SugarRecord;

/**
 * Created by badhri on 9/22/16.
 */
public class TodoItem extends SugarRecord {
    public String item;
    public String date;
    public String priority;
    public String status;
    public String comments;

    public TodoItem (){
        super();
    }

    public TodoItem(String item, String date, String priority, String comments, String status) {
        super();
        this.item = item;
        this.date = date;
        this.priority = priority;
        this.status = status;
        this.comments = comments;
    }
}
