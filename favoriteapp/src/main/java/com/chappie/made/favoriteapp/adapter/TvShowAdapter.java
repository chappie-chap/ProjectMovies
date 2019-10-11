package com.chappie.made.favoriteapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chappie.made.favoriteapp.DetailActivity;
import com.chappie.made.favoriteapp.R;
import com.chappie.made.favoriteapp.parcleable.TvShow;

import java.util.ArrayList;

import static com.chappie.made.favoriteapp.database.DatabaseContract.CONTENT_TVSHOW_URI;


public class TvShowAdapter extends RecyclerView.Adapter<TvShowAdapter.ViewHolder> {
    private Context context;
    private ArrayList<TvShow> listTv = new ArrayList<>();
    private OnItemClickCallback onItemClickCallback;

    public TvShowAdapter(Context context) {
        this.context = context;
    }

    public void setOnItemClickCallback(OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    public ArrayList<TvShow> getlistTv() {
        return listTv;
    }

    public void setListTv(ArrayList<TvShow> tv) {
        listTv.clear();
        listTv.addAll(tv);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_movies, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        TvShow tv = listTv.get(i);
        Glide.with(context)
                .load(tv.getImg_poster())
                .error(R.drawable.logo_movie)
                .override(viewHolder.img_poster.getWidth(), viewHolder.img_poster.getHeight())
                .into(viewHolder.img_poster);
        Glide.with(context)
                .load(tv.getImg_backdrop())
                .error(R.drawable.logo_movie)
                .override(viewHolder.img_backdrop.getWidth(), viewHolder.img_backdrop.getHeight())
                .into(viewHolder.img_backdrop);
        viewHolder.txt_popularity.setText(String.format("%s : %s", viewHolder.itemView.getResources().getString(R.string.popularity), tv.getPopularity()));
        viewHolder.txt_title.setText(tv.getTitle());

        setAnimation(viewHolder.itemView);

        viewHolder.itemView.setOnClickListener(v -> {
            onItemClickCallback.onItemClicked(listTv.get(viewHolder.getAdapterPosition()));
            Uri uri = Uri.parse(CONTENT_TVSHOW_URI + "/" + listTv.get(viewHolder.getAdapterPosition()).getId_tvshow());
            Intent intent = new Intent(viewHolder.itemView.getContext(), DetailActivity.class);
            intent.setData(uri);
            intent.putExtra(DetailActivity.EXTRA_TVSHOW, listTv.get(viewHolder.getAdapterPosition()));
            viewHolder.itemView.getContext().startActivity(intent);
        });
    }

    private void setAnimation(View view){
        Animation animation = AnimationUtils.loadAnimation(view.getContext(),R.anim.fade_transition);
        view.startAnimation(animation);
    }

    @Override
    public int getItemCount() {
        return listTv.size();
    }

    public interface OnItemClickCallback {
        void onItemClicked(TvShow tv);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img_poster, img_backdrop, bgView;
        TextView txt_title, txt_popularity;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_poster = itemView.findViewById(R.id.item_poster);
            img_backdrop = itemView.findViewById(R.id.item_backdrop);
            txt_popularity = itemView.findViewById(R.id.item_popularity);
            txt_title = itemView.findViewById(R.id.item_title);
            bgView = itemView.findViewById(R.id.img_bgView);
        }
    }
}
