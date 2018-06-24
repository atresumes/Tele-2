package com.bignerdranch.expandablerecyclerview.Model;

import java.util.List;

public interface ParentObject {
    List<Object> getChildObjectList();

    void setChildObjectList(List<Object> list);
}
