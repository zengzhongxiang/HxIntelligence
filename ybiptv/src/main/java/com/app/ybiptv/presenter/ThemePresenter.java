package com.app.ybiptv.presenter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.app.ybiptv.view.ThemeView;
import com.open.leanback.widget.Presenter;

/**
 *
 */
public class ThemePresenter extends Presenter {

    private static final int CARD_WIDTH = 313;
    private static final int CARD_HEIGHT = 176;
    private Context context;

    public ThemePresenter(){}

    public ThemePresenter(Context context){
        this.context = context;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        ThemeView ThemeView = new ThemeView (parent.getContext()) {
            @Override
            public void setSelected(boolean selected) {

                super.setSelected(selected);
            }
        };

        ThemeView.setFocusable(true);
        ThemeView.setFocusableInTouchMode(true);

        return new ViewHolder (ThemeView);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Object item) {
//        Movie movie = (Movie) item;
//        final String title = movie.getTitle();

        ThemeView ThemeView = (ThemeView) viewHolder.view;
        ThemeView.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                Toast.makeText (context,"标题11",Toast.LENGTH_LONG).show ();
            }
        });

    }

    @Override
    public void onUnbindViewHolder(ViewHolder viewHolder) {
        ThemeView ThemeView = (ThemeView) viewHolder.view;
    }
}
