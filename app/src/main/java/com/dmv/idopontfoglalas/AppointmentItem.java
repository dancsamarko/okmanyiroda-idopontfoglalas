package com.dmv.idopontfoglalas;

import java.util.Date;

public class AppointmentItem {

    private String id;
    private String title;
    private Date date;

    public AppointmentItem() {
    }

    public AppointmentItem(String title, Date date) {
        this.title = title;
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public Date getDate() {
        return date;
    }

    public String _getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
