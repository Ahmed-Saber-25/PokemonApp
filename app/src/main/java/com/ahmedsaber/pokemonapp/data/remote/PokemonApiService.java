package com.ahmedsaber.pokemonapp.data.remote;

import com.ahmedsaber.pokemonapp.data.model.PokemonResponse;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.GET;

public interface PokemonApiService {
    @GET("pokemon")
    Observable<PokemonResponse> getPokemon();
}
