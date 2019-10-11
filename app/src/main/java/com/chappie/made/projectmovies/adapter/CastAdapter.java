package com.chappie.made.projectmovies.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chappie.made.projectmovies.R;
import com.chappie.made.projectmovies.parcleable.Cast;

import java.util.ArrayList;

public class CastAdapter extends RecyclerView.Adapter<CastAdapter.CastViewHolder> {
    private Context context;
    private ArrayList<Cast> cast = new ArrayList<>();

    public CastAdapter(Context context){
        this.context =context;
    }

    public void setCasts(ArrayList<Cast> casts) {
        cast.clear();
        cast.addAll(casts);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CastViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cast, viewGroup, false);
        return new CastViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CastViewHolder castViewHolder, int i) {
        Cast c = cast.get(i);
        Glide.with(context)
                .load(c.getImg_cast())
                .error(R.drawable.logo_movie)
                .override(castViewHolder.img_cast.getWidth(), castViewHolder.img_cast.getHeight())
                .into(castViewHolder.img_cast);
        castViewHolder.txt_Name.setText(c.getName());
        castViewHolder.txt_role.setText(c.getRole());
    }

    @Override
    public int getItemCount() {
        return cast.size();
    }

    class CastViewHolder extends RecyclerView.ViewHolder{
        ImageView img_cast;
        TextView txt_Name, txt_role;

        CastViewHolder(View view) {
            super(view);
            img_cast = view.findViewById(R.id.posterCast);
            txt_Name = view.findViewById(R.id.nameCast);
            txt_role = view.findViewById(R.id.roleCast);
        }
    }
}