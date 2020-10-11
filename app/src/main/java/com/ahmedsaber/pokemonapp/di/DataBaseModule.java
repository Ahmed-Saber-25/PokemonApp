package com.ahmedsaber.pokemonapp.di;

import android.app.Application;

import androidx.room.Room;

import com.ahmedsaber.pokemonapp.data.local.FavouritePokeDao;
import com.ahmedsaber.pokemonapp.data.local.PokemonDB;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ApplicationComponent;

@Module
@InstallIn(ApplicationComponent.class)
public class DataBaseModule {
    @Provides
    @Singleton
    public static PokemonDB providePokemonDB(Application application) {
        return Room.databaseBuilder(application, PokemonDB.class, "Favorite Database")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();
    }

    @Provides
    @Singleton
    public static FavouritePokeDao providePokeDao(PokemonDB pokemonDB) {
        return pokemonDB.favouritePokeDao();
    }
}
