package com.ahmedsaber.pokemonapp.data.local;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.ahmedsaber.pokemonapp.data.model.Pokemon;

import java.util.List;

@Dao
public interface FavouritePokeDao {

    @Insert
    void insertPokemon(Pokemon favouritePokemon);

    @Query("DELETE FROM favorite_table WHERE name = :pokemonName")
    void deletePokemon(String pokemonName);

    @Query("DELETE FROM favorite_table")
    void deleteAll();

    @Query("SELECT * FROM favorite_table")
    LiveData<List<Pokemon>> getFavoritePokemon();

}
