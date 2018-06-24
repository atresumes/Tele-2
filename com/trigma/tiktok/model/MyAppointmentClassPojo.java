package com.trigma.tiktok.model;

import java.util.ArrayList;
import java.util.HashMap;

public class MyAppointmentClassPojo {
    private HashMap<String, ArrayList<Upcoming>> _listDataChild = new HashMap();
    private ArrayList<String> _listDataHeader = new ArrayList();

    public ArrayList<String> get_listDataHeader() {
        return this._listDataHeader;
    }

    public void set_listDataHeader(ArrayList<String> _listDataHeader) {
        this._listDataHeader = _listDataHeader;
    }

    public HashMap<String, ArrayList<Upcoming>> get_listDataChild() {
        return this._listDataChild;
    }

    public void set_listDataChild(HashMap<String, ArrayList<Upcoming>> _listDataChild) {
        this._listDataChild = _listDataChild;
    }
}
