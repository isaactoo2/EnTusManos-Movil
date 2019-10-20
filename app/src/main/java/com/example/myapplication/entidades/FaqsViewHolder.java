package com.example.myapplication.entidades;

import android.view.View;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.entidades.FaqsA;
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;

public class FaqsViewHolder extends ChildViewHolder {
    private TextView mTextView;
    public FaqsViewHolder(View itemView) {
        super(itemView);
        mTextView=itemView.findViewById(R.id.txtFaqsA);

    }

    public void bind(FaqsA faqsa){
        mTextView.setText(faqsa.name);
    }
}
