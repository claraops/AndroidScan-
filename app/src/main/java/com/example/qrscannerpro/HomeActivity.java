package com.example.qrscannerpro;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.google.android.material.button.MaterialButton;

public class HomeActivity extends AppCompatActivity {

    private MaterialButton btnScan, btnHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setTheme(R.style.Theme_QRScannerPro);

        // Initialiser les boutons
        btnScan = findViewById(R.id.btnScan);
        btnHistory = findViewById(R.id.btnHistory);

        // Configurer les écouteurs
        setupListeners();
    }

    private void setupListeners() {
        // Bouton Scanner -> Page de scan
        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, ScanActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

        // Bouton Historique -> Page historique
        btnHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, HistoryActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
    }

    @Override
    public void onBackPressed() {
        // Quitter l'application depuis la page d'accueil
        super.onBackPressed();
        super.onBackPressed();
        finishAffinity();
    }
}