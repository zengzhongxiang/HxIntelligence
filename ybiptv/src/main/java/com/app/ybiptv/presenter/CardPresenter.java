package com.app.ybiptv.presenter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.app.ybiptv.view.CardView;
import com.open.leanback.widget.Presenter;

/**
 *
 */
public class CardPresenter extends Presenter {

    private static final int CARD_WIDTH = 313;
    private static final int CARD_HEIGHT = 176;
    private Context context;

    public CardPresenter(){}

    public CardPresenter(Context context){
        this.context = context;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        CardView cardView = new CardView (parent.getContext()) {
            @Override
            public void setSelected(boolean selected) {

                super.setSelected(selected);
            }
        };
//        ViewGroup.LayoutParams lp = cardView.getLayoutParams();
//        lp.width = CARD_WIDTH;
//        lp.height = CARD_HEIGHT;
//        cardView.setLayoutParams(lp);
//        cardView.setLayoutParams(new ViewGroup.LayoutParams(CARD_WIDTH, CARD_HEIGHT));

        cardView.setFocusable(true);
        cardView.setFocusableInTouchMode(true);

        return new ViewHolder (cardView);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Object item) {
//        Movie movie = (Movie) item;
//        final String title = movie.getTitle();

        CardView cardView = (CardView) viewHolder.view;
        cardView.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                Toast.makeText (context,"标题11",Toast.LENGTH_LONG).show ();
            }
        });

    }

    @Override
    public void onUnbindViewHolder(ViewHolder viewHolder) {
        CardView cardView = (CardView) viewHolder.view;
    }
}
