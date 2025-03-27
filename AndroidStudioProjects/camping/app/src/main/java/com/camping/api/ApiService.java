package com.camping.api;
import com.google.gson.JsonObject;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.*;

public interface ApiService {
    // Login avec Map
    @POST("/api/utilisateurs/login")
    Call<JsonObject> login(@Body Map<String, String> credentials);

    // Récupérer les créneaux (retourne un JsonArray parsé en JsonObject)
    @GET("/api/creneaux")
    Call<List<JsonObject>> getCreneaux();

    // Participer avec un JsonObject
    @POST("/api/creneaux/participer")
    Call<JsonObject> participer(@Body JsonObject participation);
}