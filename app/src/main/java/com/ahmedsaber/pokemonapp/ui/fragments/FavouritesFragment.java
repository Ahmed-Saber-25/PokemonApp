package com.ahmedsaber.pokemonapp.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ahmedsaber.pokemonapp.data.model.Pokemon;
import com.ahmedsaber.pokemonapp.databinding.FragmentFavouritesBinding;
import com.ahmedsaber.pokemonapp.ui.adapters.PokemonAdapter;
import com.ahmedsaber.pokemonapp.viewmodel.MainViewModel;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class FavouritesFragment extends Fragment {
    private FragmentFavouritesBinding mFavouritesBinding;
    private MainViewModel viewModel;
    private PokemonAdapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mFavouritesBinding = FragmentFavouritesBinding.inflate(inflater, container, false);
        return mFavouritesBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        initRecyclerView();
        setUpItemTouchHelper();
        observeData();
    }

    private void initRecyclerView() {
        mFavouritesBinding.favouritesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new PokemonAdapter();
        mFavouritesBinding.favouritesRecyclerView.setAdapter(adapter);
    }

    private void setUpItemTouchHelper() {
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int swipedPokemonPosition = viewHolder.getAdapterPosition();
                Pokemon favouritePokemon = adapter.getPokemonAt(swipedPokemonPosition);
                viewModel.deletePokemon(favouritePokemon.getName());
                Toast.makeText(getContext(), "FavouritePokemon deleted from favorites.", Toast.LENGTH_SHORT).show();
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(mFavouritesBinding.favouritesRecyclerView);
    }


    private void observeData() {
        viewModel.getFavoritePokemonList().observe(getViewLifecycleOwner(), new Observer<List<Pokemon>>() {
            @Override
            public void onChanged(List<Pokemon> favouritePokemon) {

                if (favouritePokemon == null || favouritePokemon.size() == 0)
                    mFavouritesBinding.noFavoritesText.setVisibility(View.VISIBLE);
                else {
                    mFavouritesBinding.noFavoritesText.setVisibility(View.GONE);
                    ArrayList<Pokemon> list = new ArrayList<>();
                    list.addAll(favouritePokemon);
                    adapter.setList(list);
                }
            }
        });
    }


}