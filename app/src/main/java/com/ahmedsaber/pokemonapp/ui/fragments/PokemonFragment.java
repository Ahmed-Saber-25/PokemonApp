package com.ahmedsaber.pokemonapp.ui.fragments;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ahmedsaber.pokemonapp.R;
import com.ahmedsaber.pokemonapp.databinding.FragmentPokemonBinding;
import com.ahmedsaber.pokemonapp.data.model.Pokemon;
import com.ahmedsaber.pokemonapp.ui.adapters.PokemonAdapter;
import com.ahmedsaber.pokemonapp.util.NetworkConnection;
import com.ahmedsaber.pokemonapp.viewmodel.MainViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class PokemonFragment extends Fragment {
    List<Pokemon> mFavouritePokemonList = new ArrayList<>();
    private FragmentPokemonBinding mPokemonBinding;
    private MainViewModel viewModel;
    private PokemonAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mPokemonBinding = FragmentPokemonBinding.inflate(inflater, container, false);
        return mPokemonBinding.getRoot();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        initRecyclerView();
        observeData();
        refreshPokemon();
        observeFavourites();
        setUpItemTouchHelper();
        if (NetworkConnection.isOnline(getActivity())) {
            viewModel.getPokemon();
        } else {
            showNetworkErrorSnackBar();
        }

    }

    private void setUpItemTouchHelper() {
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int swipedPokemonPosition = viewHolder.getAdapterPosition();
                Pokemon favouritePokemon = adapter.getPokemonAt(swipedPokemonPosition);
                if (mFavouritePokemonList == null || mFavouritePokemonList.isEmpty()) {
                    viewModel.insertPokemon(favouritePokemon);
                    adapter.notifyDataSetChanged();
                    Toast.makeText(getContext(), "FavouritePokemon added to favorites.", Toast.LENGTH_SHORT).show();
                } else if (containsPokemon(mFavouritePokemonList, favouritePokemon.getName())) {
                    Toast.makeText(getContext(), "FavouritePokemon already exists in favorites.", Toast.LENGTH_SHORT).show();
                } else {
                    viewModel.insertPokemon(favouritePokemon);
                    adapter.notifyDataSetChanged();
                    Toast.makeText(getContext(), "FavouritePokemon added to favorites.", Toast.LENGTH_SHORT).show();
                }
            }

        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(mPokemonBinding.pokemonRecyclerView);
    }

    private void observeData() {
        viewModel.getPokemonList().observe(getViewLifecycleOwner(), pokemon -> {
            mPokemonBinding.ProgressBar.setVisibility(View.GONE);
            adapter.setList(pokemon);
        });

    }

    private void observeFavourites() {
        viewModel.getFavoritePokemonList().observe(getViewLifecycleOwner(), pokemon -> {
            mFavouritePokemonList.clear();
            mFavouritePokemonList.addAll(pokemon);
        });

    }

    private void initRecyclerView() {
        mPokemonBinding.pokemonRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new PokemonAdapter();
        mPokemonBinding.pokemonRecyclerView.setAdapter(adapter);
    }

    private boolean containsPokemon(List<Pokemon> favouritePokemonList, String name) {
        for (Pokemon poke : favouritePokemonList) {
            if (poke.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void showNetworkErrorSnackBar() {
        Snackbar.make(getView().findViewById(R.id.poke_layout), getResources().getString(R.string.network_error), Snackbar.LENGTH_INDEFINITE)
                .setAction(getResources().getString(R.string.retry), view -> {
                    if (NetworkConnection.isOnline(getContext())) {
                        viewModel.getPokemon();
                    } else {
                        showNetworkErrorSnackBar();
                    }
                }).show();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void refreshPokemon() {
        mPokemonBinding.pokemonRefresh.setColorSchemeColors(Color.WHITE);
        mPokemonBinding.pokemonRefresh.setOnRefreshListener(() -> {
                    if (NetworkConnection.isOnline(requireContext())) {
                        initRecyclerView();
                        viewModel.getPokemon();
                        observeData();
                    } else {
                        mPokemonBinding.pokemonRefresh.setRefreshing(false);
                    }
                    mPokemonBinding.pokemonRefresh.setRefreshing(false);
                }
        );

    }


}