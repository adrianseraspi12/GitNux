package com.adrianseraspi.gitnux.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public abstract class BaseAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    private final List<T> list;

    private int lastPosition = -1;

    private static final int FADE_ANIM_DURATION = 750;

    protected BaseAdapter(List<T> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return onCreateVH(parent, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        T model = list.get(position);
        onBindVH(holder, position, model);
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull VH holder) {
        super.onViewDetachedFromWindow(holder);
        holder.itemView.clearAnimation();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void addItems(List<T> items) {
        list.addAll(items);
        notifyItemInserted(getItemCount() -1);
    }

    protected void setScaleAnimation(View viewToAnimate, int position) {
        if (position > lastPosition) {
            AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
            anim.setDuration(FADE_ANIM_DURATION);
            viewToAnimate.startAnimation(anim);
            lastPosition = position;
        }
    }

    public abstract VH onCreateVH(ViewGroup parent, int viewType);

    public abstract void onBindVH(VH holder, int position, T model);

}
