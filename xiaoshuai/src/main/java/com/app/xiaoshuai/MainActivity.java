package com.app.xiaoshuai;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.costar.hotellibrary.HotelSettings;

public class MainActivity extends FragmentActivity {

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_main);
        RadioGroup rg_store = findViewById (R.id.rg_store);
        rg_store.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = (RadioButton) group.findViewById(checkedId);
                String txt = radioButton.getText().toString();
                if("打开".equals (txt)){  //打开
                    HotelSettings.startAdbd (MainActivity.this);
                }else{
                    HotelSettings.stopAdbd (MainActivity.this);
                }

            }
        });

//        System.out.println ("getSystemModel()=="+getSystemModel());

        Button button = findViewById (R.id.myBut);
        button.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                startActivity(new Intent (MainActivity.this, MainActivity2.class));
            }
        });
    }

    public static String getSystemModel() {
        return android.os.Build.MODEL;
    }
}
