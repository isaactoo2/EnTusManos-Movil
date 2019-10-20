package com.example.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.adapter.FaqsAdapter;
import com.example.myapplication.entidades.FaqsA;
import com.example.myapplication.entidades.FaqsQ;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class faqs extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.faqs);
        RecyclerView recyclerView = findViewById(R.id.recyclerFaqs);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ArrayList<FaqsQ> questions = new ArrayList<>();

        ArrayList<FaqsA> answer1 = new ArrayList<>();
        answer1.add(new FaqsA(getString(R.string.a1)));
        FaqsQ question1 = new FaqsQ(getString(R.string.q1), answer1);
        questions.add(question1);

        ArrayList<FaqsA> answer2 = new ArrayList<>();
        answer2.add(new FaqsA(getString(R.string.a2)));
        FaqsQ question2 = new FaqsQ(getString(R.string.q2), answer2);
        questions.add(question2);

        ArrayList<FaqsA> answer3 = new ArrayList<>();
        answer3.add(new FaqsA(getString(R.string.a3)));
        FaqsQ question3 = new FaqsQ(getString(R.string.q3), answer3);
        questions.add(question3);

        ArrayList<FaqsA> answer4 = new ArrayList<>();
        answer4.add(new FaqsA(getString(R.string.a4)));
        FaqsQ question4 = new FaqsQ(getString(R.string.q4), answer4);
        questions.add(question4);

        ArrayList<FaqsA> answer5 = new ArrayList<>();
        answer5.add(new FaqsA(getString(R.string.a5)));
        FaqsQ question5 = new FaqsQ(getString(R.string.q5), answer5);
        questions.add(question5);

        ArrayList<FaqsA> answer6 = new ArrayList<>();
        answer6.add(new FaqsA(getString(R.string.a6)));
        FaqsQ question6 = new FaqsQ(getString(R.string.q6), answer6);
        questions.add(question6);

        ArrayList<FaqsA> answer7 = new ArrayList<>();
        answer7.add(new FaqsA(getString(R.string.a7)));
        FaqsQ question7 = new FaqsQ(getString(R.string.q7), answer7);
        questions.add(question7);

        ArrayList<FaqsA> answer8 = new ArrayList<>();
        answer8.add(new FaqsA(getString(R.string.a8)));
        FaqsQ question8 = new FaqsQ(getString(R.string.q8), answer8);
        questions.add(question8);




        FaqsAdapter adapter = new FaqsAdapter(questions);
        recyclerView.setAdapter(adapter);

        FloatingActionButton fab=findViewById(R.id.fabClose);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}
