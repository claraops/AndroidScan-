package com.example.qrscannerpro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.Random;

public class AnalysisActivity extends AppCompatActivity {

    private TextView tvUrl, tvStatus, tvDetails;
    private ProgressBar progressBar;
    private MaterialButton btnViewResults;
    private String scannedUrl;

    // Données de test pour l'analyse
    private String[] securityChecks = {
            "Vérification HTTPS...",
            "Analyse du certificat SSL...",
            "Détection de phishing...",
            "Vérification de réputation...",
            "Analyse des en-têtes de sécurité..."
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analysis);

        initViews();
        getScannedData();
        startSecurityAnalysis();
        setupListeners();
    }

    private void initViews() {
        tvUrl = findViewById(R.id.tvUrl);
        tvStatus = findViewById(R.id.tvStatus);
        tvDetails = findViewById(R.id.tvDetails);
        progressBar = findViewById(R.id.progressBar);
        btnViewResults = findViewById(R.id.btnViewResults);

        btnViewResults.setEnabled(false);
    }

    private void getScannedData() {
        scannedUrl = getIntent().getStringExtra("SCANNED_VALUE");
        if (scannedUrl == null || scannedUrl.isEmpty()) {
            scannedUrl = "https://example.com";
        }
        tvUrl.setText("URL: " + scannedUrl);
    }

    private void startSecurityAnalysis() {
        final Handler handler = new Handler();
        final int totalChecks = securityChecks.length;
        final int[] currentCheck = {0};

        Runnable analysisRunnable = new Runnable() {
            @Override
            public void run() {
                if (currentCheck[0] < totalChecks) {
                    // Mettre à jour l'état
                    String currentCheckText = securityChecks[currentCheck[0]];
                    tvStatus.setText("Analyse en cours...");
                    tvDetails.setText(currentCheckText);

                    // Simuler un délai pour chaque vérification
                    progressBar.setProgress(((currentCheck[0] + 1) * 100) / totalChecks);

                    currentCheck[0]++;
                    handler.postDelayed(this, 1500); // 1.5 secondes entre chaque vérification
                } else {
                    // Analyse terminée
                    analysisComplete();
                }
            }
        };

        handler.postDelayed(analysisRunnable, 1000);
    }

    private void analysisComplete() {
        runOnUiThread(() -> {
            tvStatus.setText("✅ Analyse terminée !");
            tvDetails.setText("Tous les tests de sécurité ont été effectués.\nCliquez pour voir les résultats.");

            // Générer un score aléatoire pour la démo
            Random random = new Random();
            int securityScore = 60 + random.nextInt(40); // Score entre 60-100

            // Activer le bouton pour voir les résultats
            btnViewResults.setEnabled(true);
            btnViewResults.setText("VOIR LE SCORE : " + securityScore + "/100");

            // Sauvegarder les résultats pour la page suivante
            SecurityResult result = new SecurityResult();
            result.url = scannedUrl;
            result.securityScore = securityScore;
            result.isHttps = scannedUrl.startsWith("https://");
            result.hasValidCertificate = random.nextBoolean();
            result.isSafe = securityScore > 70;
            result.phishingRisk = random.nextInt(30);
            result.malwareDetected = random.nextBoolean();

            // Passer aux résultats
            btnViewResults.setOnClickListener(v -> {
                Intent intent = new Intent(AnalysisActivity.this, ResultActivity.class);
                intent.putExtra("SECURITY_RESULT", result);
                startActivity(intent);
                finish();
            });
        });
    }

    private void setupListeners() {
        // Bouton retour
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
    }

    // Classe pour stocker les résultats
    public static class SecurityResult implements java.io.Serializable {
        public String url;
        public int securityScore;
        public boolean isHttps;
        public boolean hasValidCertificate;
        public boolean isSafe;
        public int phishingRisk;
        public boolean malwareDetected;
    }
}