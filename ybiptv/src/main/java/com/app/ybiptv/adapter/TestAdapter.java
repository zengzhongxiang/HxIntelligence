package com.app.ybiptv.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.ybiptv.R;
import com.zhy.autolayout.utils.AutoUtils;

public class TestAdapter extends RecyclerView.Adapter<TestAdapter.ViewHolder> {

    public final static int PAGE_COUNT = 15;

    int size = 0;
    boolean isBookBtn = false;
    int mOffset = 0;

    public TestAdapter(int size, int offset, boolean isBookBtn) {
        this.size = size;
        this.isBookBtn = isBookBtn;
        this.mOffset = offset; // 步长.
    }

    @Override
    public TestAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.item_book_juji_layout, null);
//        view.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                ViewUtils.scaleView(v, hasFocus);
//            }
//        });
        AutoUtils.autoSize(view);
        return new TestAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (isBookBtn) {
            holder.mBtn.setText(String.valueOf(mOffset + position + 1));
        } else {
            holder.mBtn.setVisibility(View.GONE);
            holder.mBtnpart.setVisibility(View.VISIBLE);
            if (size >= 12) {
                holder.mBtnpart.setText((position) * PAGE_COUNT + 1 + " - " + (((position + 1) * PAGE_COUNT) > size ? String.valueOf(size) : ((position + 1) * PAGE_COUNT)));
            } else {
                holder.mBtnpart.setText("1 - " + size);
            }
        }
    }

    @Override
    public int getItemCount() {
        if (isBookBtn) {
            return size;
        } else {
            return size / PAGE_COUNT + ((size % TestAdapter.PAGE_COUNT) > 0 ? 1 : 0);
        }
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mBtn;
        public TextView mBtnpart;

        public ViewHolder(View itemView) {
            super(itemView);
            mBtn = (TextView) itemView.findViewById(R.id.btn);
            mBtnpart = (TextView) itemView.findViewById(R.id.btn_part);
        }
    }
}
