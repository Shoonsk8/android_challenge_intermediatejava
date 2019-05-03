package com.shoon.android_challenge_intermediatejava;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final TextView tv=findViewById(R.id.textResult);
        final Button bt=findViewById(R.id.buttonDoubleClick);
        bt.setOnClickListener(new DoubleClickHandler(new DoubleClickListener() {

            @Override
            public void onSingleClick(View view) {
                tv.setTextColor(Color.BLACK);
                tv.append("single clickeed! ");
            }

            @Override
            public void onDoubleClick(View view) {

                tv.setTextColor(Color.RED);
                tv.append("double clicked! ");
            }
        }));


        final SymbolSliderView slv=findViewById(R.id.slvInterval);

        slv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction()==MotionEvent.ACTION_UP){
                    tv.setText("");
                    bt.setOnClickListener(new DoubleClickHandler(new DoubleClickListener() {

                        @Override
                        public void onSingleClick(View view) {
                            tv.setTextColor(Color.BLACK);
                            tv.append("single clickeed! ");
                        }

                        @Override
                        public void onDoubleClick(View view) {
                            tv.setTextColor(Color.RED);
                            tv.append("double clicked! ");
                        }
                    },(long)(50*slv.getRating())));
                }

                return false;
            }
        });

    }


}
