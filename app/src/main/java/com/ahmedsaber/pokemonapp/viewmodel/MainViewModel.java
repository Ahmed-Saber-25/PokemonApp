package com.ahmedsaber.pokemonapp.viewmodel;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ahmedsaber.pokemonapp.data.model.Pokemon;
import com.ahmedsaber.pokemonapp.repository.Repository;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainViewModel extends ViewModel {
    private static final String TAG = "MainViewModel";
    MutableLiveData<ArrayList<Pokemon>> pokemonList = new MutableLiveData<>();
    private Repository mRepository;
    private LiveData<List<Pokemon>> favoritePokemonList = null;

    @ViewModelInject
    public MainViewModel(Repository repository) {

        mRepository = repository;
        favoritePokemonList = repository.getFavoritePokemon();

    }
    public MutableLiveData<ArrayList<Pokemon>> getPokemonList() {
        return pokemonList;
    }

    public LiveData<List<Pokemon>> getFavoritePokemonList() {
        return favoritePokemonList;
    }
    @SuppressLint("CheckResult")
    public void getPokemon() {
        mRepository.getPokemon()
                .subscribeOn(Schedulers.io())
                .map(pokemonResponse -> {
                    ArrayList<Pokemon> list = pokemonResponse.getResults();
                    for (Pokemon favouritePokemon : list) {
                        String url = favouritePokemon.getUrl();
                        String[] pokemonIndex = url.split("/");
                        favouritePokemon.setUrl("https://pokeres.bastionbot.org/images/pokemon/" + pokemonIndex[pokemonIndex.length - 1] + ".png");
                    }
                    return list;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> pokemonList.setValue(result),
                        error -> Log.e(TAG, error.getMessage()));

    }

    public void insertPokemon(Pokemon favouritePokemon) {
        mRepository.insertFavouritePokemon(favouritePokemon);
    }

    public void deletePokemon(String pokemonName) {
        mRepository.deleteFavouritePokemon(pokemonName);
    }



}
