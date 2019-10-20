package com.example.myapplication.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.R;
import com.example.myapplication.entidades.FaqsA;
import com.example.myapplication.entidades.FaqsQ;
import com.example.myapplication.entidades.FaqsQViewHolder;
import com.example.myapplication.entidades.FaqsViewHolder;
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

public class FaqsAdapter extends ExpandableRecyclerViewAdapter<FaqsQViewHolder, FaqsViewHolder> {
    public FaqsAdapter(List<? extends ExpandableGroup> groups) {
        super(groups);
    }

    @Override
    public FaqsQViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.expandable_faqs,parent, false);

        return new FaqsQViewHolder(v);
    }

    @Override
    public FaqsViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.expandable_faqs_a,parent, false);

        return new FaqsViewHolder(v);
    }

    @Override
    public void onBindChildViewHolder(FaqsViewHolder holder, int flatPosition, ExpandableGroup group, int childIndex) {
        final FaqsA faqsA = (FaqsA) group.getItems().get(childIndex);
        holder.bind(faqsA);
    }

    @Override
    public void onBindGroupViewHolder(FaqsQViewHolder holder, int flatPosition, ExpandableGroup group) {
        final FaqsQ faqsQ = (FaqsQ)group;
        holder.bind(faqsQ);
    }
}
