package com.ahmedsaber.pokemonapp.repository;

import androidx.lifecycle.LiveData;

import com.ahmedsaber.pokemonapp.data.local.FavouritePokeDao;
import com.ahmedsaber.pokemonapp.data.model.Pokemon;
import com.ahmedsaber.pokemonapp.data.model.PokemonResponse;
import com.ahmedsaber.pokemonapp.data.remote.PokemonApiService;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Observable;


public class Repository {
    private PokemonApiService mPokemonApiService;
    private FavouritePokeDao mFavouritePokeDao;

    @Inject
    public Repository(PokemonApiService pokemonApiService,
                      FavouritePokeDao FavouritePokeDao) {
        mPokemonApiService = pokemonApiService;
        mFavouritePokeDao = FavouritePokeDao;
    }


    public Observable<PokemonResponse> getPokemon() {
        return mPokemonApiService.getPokemon();
    }

    public void insertFavouritePokemon(Pokemon favouritePokemon) {
        mFavouritePokeDao.insertPokemon(favouritePokemon);
    }

    public void deleteFavouritePokemon(String pokemonName) {
        mFavouritePokeDao.deletePokemon(pokemonName);
    }

    public void deleteAllFavourite() {
        mFavouritePokeDao.deleteAll();
    }

    public LiveData<List<Pokemon>> getFavoritePokemon() {
        return mFavouritePokeDao.getFavoritePokemon();
    }
}
