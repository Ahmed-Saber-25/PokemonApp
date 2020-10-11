package com.ahmedsaber.pokemonapp.data.local;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.ahmedsaber.pokemonapp.data.model.Pokemon;

@Database(entities = {Pokemon.class}, version = 2, exportSchema = false)
public abstract class PokemonDB extends RoomDatabase {
    public abstract FavouritePokeDao favouritePokeDao();
}
