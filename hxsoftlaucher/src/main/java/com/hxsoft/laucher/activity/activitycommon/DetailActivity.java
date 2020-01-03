package com.hxsoft.laucher.activity.activitycommon;

import android.os.Bundle;
import android.widget.ImageView;

import com.hxsoft.laucher.R;
import com.hxsoft.laucher.activity.BaseActivity;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

@ContentView(R.layout.activity_detail)
public class DetailActivity extends BaseActivity {
    @ViewInject(R.id.bg_iv)
    private ImageView bg_iv;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        x.view().inject(this);

        int type = getIntent ().getIntExtra ("type",1);
        if(type == 1){
            bg_iv.setImageResource (R.mipmap.jsfc);
        }if(type == 2){
            bg_iv.setImageResource (R.mipmap.fsjs);
        }if(type == 3){
            bg_iv.setImageResource (R.mipmap.lbtp);
        }
    }
}
