package com.lanlengran.badapple;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

public class WelcomeActivity extends AppCompatActivity {
    private EditText tvgap,tvskip;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        tvgap= (EditText) findViewById(R.id.et_gap);
        tvskip= (EditText) findViewById(R.id.et_skip);
        findViewById(R.id.btn_to_main).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strGap=tvgap.getText().toString();
                String strskip=tvskip.getText().toString();
                int gap=5;
                int skip=2;
                if (!strGap.isEmpty()){
                    gap=Integer.valueOf(strGap);
                }
                if (!strskip.isEmpty()){
                    skip=Integer.valueOf(strskip);
                }
                Intent it=new Intent(WelcomeActivity.this,MainActivity.class);
                it.putExtra("gap",gap);
                it.putExtra("skip",skip);
                startActivity(it);
            }
        });
    }
}
