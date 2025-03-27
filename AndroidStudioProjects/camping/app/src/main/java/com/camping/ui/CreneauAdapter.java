package com.camping.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.camping.R;
import com.camping.api.ApiClient;
import com.camping.api.ApiService;
import com.google.gson.JsonObject;

import java.util.List;

public class CreneauAdapter extends RecyclerView.Adapter<CreneauAdapter.ViewHolder> {
    private List<JsonObject> creneaux;
    private Context context;
    private ApiService apiService;
    private int userId;

    public CreneauAdapter(List<JsonObject> creneaux, Context context, int userId) {
        this.creneaux = creneaux;
        this.context = context;
        this.apiService = ApiClient.getApiService();
        this.userId = userId;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_creneau, parent, false);
        return new ViewHolder(view);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        JsonObject creneau = creneaux.get(position);

        // Extraction des données depuis le JSON
        String date = creneau.get("dateCreneau").getAsString();
        String heure = creneau.get("heureCreneau").getAsString();
        String animation = creneau.getAsJsonObject("animation")
                .get("libelleAnimation").getAsString();
        String lieu = creneau.getAsJsonObject("lieu")
                .get("libelleLieu").getAsString();
        int places = creneau.get("nbPlacesCreneau").getAsInt();

        // Affichage des données
        holder.tvDate.setText(date);
        holder.tvHeure.setText(heure);
        holder.tvAnimation.setText(animation);
        holder.tvLieu.setText(lieu);
        holder.tvPlaces.setText("Places: " + places);

        // Gestion du bouton Participer
        holder.btnParticiper.setOnClickListener(v -> {
            JsonObject participation = new JsonObject();
            participation.addProperty("campeurId", userId);
            participation.addProperty("creneauId", creneau.get("idCreneau").getAsInt());

            apiService.participer(participation).enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    if (response.isSuccessful()) {
                        holder.btnParticiper.setEnabled(false);
                        holder.btnParticiper.setText("Inscrit");
                        Toast.makeText(context, "Inscription réussie!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Erreur: " + response.message(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Toast.makeText(context, "Erreur réseau", Toast.LENGTH_SHORT).show();
                }
            });
        });

        // Désactiver le bouton si plus de places
        if (places <= 0) {
            holder.btnParticiper.setEnabled(false);
            holder.btnParticiper.setText("Complet");
        }
    }

    @Override
    public int getItemCount() {
        return creneaux.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate, tvHeure, tvAnimation, tvLieu, tvPlaces;
        Button btnParticiper;

        public ViewHolder(View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvHeure = itemView.findViewById(R.id.tvHeure);
            tvAnimation = itemView.findViewById(R.id.tvAnimation);
            tvLieu = itemView.findViewById(R.id.tvLieu);
            tvPlaces = itemView.findViewById(R.id.tvPlaces);
            btnParticiper = itemView.findViewById(R.id.btnParticiper);
        }
    }
}