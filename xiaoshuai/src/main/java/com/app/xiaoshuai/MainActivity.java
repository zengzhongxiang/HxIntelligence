package com.app.xiaoshuai;

import android.content.Context;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

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
    }

    public static String getSystemModel() {
        return android.os.Build.MODEL;
    }
}
