package com.ahmedsaber.pokemonapp.ui.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.ahmedsaber.pokemonapp.databinding.PokemonItemBinding;
import com.ahmedsaber.pokemonapp.data.model.Pokemon;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.annotations.NonNull;

public class PokemonAdapter extends RecyclerView.Adapter<PokemonAdapter.PokemonViewHolder> {
    private PokemonItemBinding mPokemonItemBinding;
    private List<Pokemon> mFavouritePokemonList = new ArrayList<>();

    @NonNull
    @Override
    public PokemonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        mPokemonItemBinding = PokemonItemBinding.inflate(inflater, parent, false);
        return new PokemonViewHolder(mPokemonItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull PokemonViewHolder holder, int position) {
        holder.mPokemonItemBinding.pokemonNameTextView.setText(mFavouritePokemonList.get(position).getName());
        Glide.with(holder.mPokemonItemBinding.pokemonImageView.getContext())
                .load(mFavouritePokemonList.get(position).getUrl())
                .into(holder.mPokemonItemBinding.pokemonImageView);

    }

    @Override
    public int getItemCount() {
        return mFavouritePokemonList == null ? 0 : mFavouritePokemonList.size();
    }

    public void setList(List<Pokemon> favouritePokemonList) {
        this.mFavouritePokemonList = favouritePokemonList;
        notifyDataSetChanged();
    }

    public Pokemon getPokemonAt(int position) {
        return mFavouritePokemonList.get(position);
    }

    public class PokemonViewHolder extends RecyclerView.ViewHolder {
        private PokemonItemBinding mPokemonItemBinding;

        public PokemonViewHolder(PokemonItemBinding mPokemonItemBinding) {
            super(mPokemonItemBinding.getRoot());
            this.mPokemonItemBinding = mPokemonItemBinding;
        }
    }
}