package com.app.xiaoshuai;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.costar.hotellibrary.HotelSettings;

public class MainActivity2 extends FragmentActivity {

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
                    HotelSettings.startAdbd (MainActivity2.this);
                }else{
                    HotelSettings.stopAdbd (MainActivity2.this);
                }

            }
        });

//        System.out.println ("getSystemModel()=="+getSystemModel());
    }

    public static String getSystemModel() {
        return android.os.Build.MODEL;
    }
}
