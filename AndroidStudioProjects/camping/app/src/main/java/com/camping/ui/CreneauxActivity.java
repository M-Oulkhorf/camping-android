package com.camping.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import com.camping.R;
import com.camping.api.ApiClient;
import com.google.gson.JsonObject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CreneauxActivity extends AppCompatActivity {
    private RecyclerView recyclerView;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_creneau);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadCreneaux();
    }

    private void loadCreneaux() {
        ApiClient.getApiService().getCreneaux().enqueue(new Callback<List<JsonObject>>() {
            @Override
            public void onResponse(Call<List<JsonObject>> call, Response<List<JsonObject>> response) {
                if (response.isSuccessful()) {
                    List<JsonObject> creneauxJson = response.body();
                    List<Map<String, Object>> creneaux = new ArrayList<>();

                    for (JsonObject json : creneauxJson) {
                        Map<String, Object> creneau = new HashMap<>();
                        creneau.put("date", json.get("dateCreneau").getAsString());
                        creneau.put("heure", json.get("heureCreneau").getAsString());
                        creneau.put("animation", json.getAsJsonObject("animation").get("libelleAnimation").getAsString());
                        creneaux.add(creneau);
                    }

                    CreneauAdapter adapter = new CreneauAdapter(creneaux);
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<JsonObject>> call, Throwable t) {
                Toast.makeText(CreneauxActivity.this, "Erreur de chargement", Toast.LENGTH_SHORT).show();
            }
        });
    }
}