package com.chappie.made.projectmovies.adapter;

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
import com.chappie.made.projectmovies.DetailActivity;
import com.chappie.made.projectmovies.R;
import com.chappie.made.projectmovies.parcleable.Movie;

import java.util.ArrayList;

import static com.chappie.made.projectmovies.database.DatabaseContract.CONTENT_MOVIE_URI;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Movie> listMovie = new ArrayList<>();
    private OnItemClickCallback onItemClickCallback;

    public void setOnItemClickCallback(OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    public ArrayList<Movie> getListMovie(){return listMovie;}

    public MovieAdapter(Context context){
        this.context=context;
    }

    public void setListMovie(ArrayList<Movie> movie){
        listMovie.clear();
        listMovie.addAll(movie);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_movies, viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Movie movie = listMovie.get(i);
        Glide.with(context)
                .load(movie.getImg_poster())
                .error(R.drawable.logo_movie)
                .override(viewHolder.img_poster.getWidth(), viewHolder.img_poster.getHeight())
                .into(viewHolder.img_poster);
        Glide.with(context)
                .load(movie.getImg_backdrop())
                .error(R.drawable.logo_movie)
                .override(viewHolder.img_backdrop.getWidth(), viewHolder.img_backdrop.getHeight())
                .into(viewHolder.img_backdrop);
        viewHolder.txt_popularity.setText(String.format("%s : %s", viewHolder.itemView.getResources().getString(R.string.popularity), movie.getPopularity()));
        viewHolder.txt_title.setText(movie.getTitle());

        setAnimation(viewHolder.itemView);

        viewHolder.itemView.setOnClickListener(v -> {
            onItemClickCallback.onItemClicked(listMovie.get(viewHolder.getAdapterPosition()));
            Uri uri = Uri.parse(CONTENT_MOVIE_URI + "/" + listMovie.get(viewHolder.getAdapterPosition()).getId_movie());
            Intent intent = new Intent(viewHolder.itemView.getContext(), DetailActivity.class);
            intent.setData(uri);
            intent.putExtra(DetailActivity.EXTRA_MOVIE, listMovie.get(viewHolder.getAdapterPosition()));
            viewHolder.itemView.getContext().startActivity(intent);
        });
    }

    private void setAnimation(View view){
        Animation animation = AnimationUtils.loadAnimation(view.getContext(),R.anim.fade_transition);
        view.startAnimation(animation);
    }

    @Override
    public int getItemCount() {
        return listMovie.size();
    }

    public interface OnItemClickCallback {
        void onItemClicked(Movie movie);
    }

    class ViewHolder extends RecyclerView.ViewHolder{
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