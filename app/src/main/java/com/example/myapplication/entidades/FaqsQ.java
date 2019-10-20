package com.example.myapplication.entidades;

import com.example.myapplication.entidades.FaqsA;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

public class FaqsQ extends ExpandableGroup<FaqsA> {
    public FaqsQ(String title, List<FaqsA> items) {
        super(title, items);
    }
}
