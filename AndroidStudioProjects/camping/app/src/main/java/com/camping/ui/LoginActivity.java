package com.camping.ui;

import android.content.Intent;
import android.os.Bundle;
import android.telecom.Call;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.camping.R;
import com.camping.api.ApiClient;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private EditText etIdentifiant, etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etIdentifiant = findViewById(R.id.etIdentifiant);
        etPassword = findViewById(R.id.etPassword);

        findViewById(R.id.btnLogin).setOnClickListener(v -> {
            Map<String, String> credentials = new HashMap<>();
            credentials.put("identifiant", etIdentifiant.getText().toString());
            credentials.put("mdp", etPassword.getText().toString());

            ApiClient.getApiService().login(credentials).enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(retrofit2.Call<JsonObject> call, Response<JsonObject> response) {

                }

                @Override
                public void onFailure(retrofit2.Call<JsonObject> call, Throwable t) {

                }

                public void onResponse(Call call, Response<JsonObject> response) {
                    if (response.isSuccessful()) {
                        startActivity(new Intent(LoginActivity.this, CreneauxActivity.class));
                    } else {
                        Toast.makeText(LoginActivity.this, "Échec de la connexion", Toast.LENGTH_SHORT).show();
                    }
                }

                public void onFailure(Call call, Throwable t) {
                    Toast.makeText(LoginActivity.this, "Erreur réseau", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}