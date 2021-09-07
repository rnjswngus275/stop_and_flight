package com.example.stop_and_flight.models;

import java.io.Serializable;

public class AppInfo implements Serializable {
    private String name = "";
    private boolean checked = false;

    public AppInfo()
    {
    }

    public AppInfo(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isChecked()
    {
        return checked;
    }

    public void setChecked(boolean checked)
    {
        this.checked = checked;
    }

    public String toString()
    {
        return name;
    }
}
