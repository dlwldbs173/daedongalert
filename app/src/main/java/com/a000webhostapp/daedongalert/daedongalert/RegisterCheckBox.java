package com.a000webhostapp.daedongalert.daedongalert;

import android.widget.CheckBox;

public class RegisterCheckBox {
    private CheckBox studentCheck;
    private CheckBox teacherCheck;
    private CheckBox parentCheck;
    private int type;

    public RegisterCheckBox (CheckBox studentCheck, CheckBox teacherCheck, CheckBox parentCheck) {
        this.studentCheck = studentCheck;
        this.teacherCheck = teacherCheck;
        this.parentCheck = parentCheck;
    }

    public int errorCheck() {
        int check=0;
        if (studentCheck.isChecked()) {
            check++;
            type = 1;
        }
        if (teacherCheck.isChecked()) {
            check++;
            type = 2;
        }
        if (parentCheck.isChecked()) {
            check++;
            type = 3;
        }
        return check;
    }

    public int getType() {
        return type;
    }
}
