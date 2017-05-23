package com.example.sxshi.rubbshview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.sxshi.rubbshview.view.RubbshView;

public class MainActivity extends AppCompatActivity {
    private Button mBtnSwitch;
    private RubbshView rubbshView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rubbshView= (RubbshView) findViewById(R.id.rubbsh);
        mBtnSwitch = (Button) findViewById(R.id.btn_switch);
        mBtnSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rubbshView.startAnimation();
            }
        });
    }
}
