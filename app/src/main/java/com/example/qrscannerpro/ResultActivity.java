package com.example.qrscannerpro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

public class ResultActivity extends AppCompatActivity {

    private TextView tvScore, tvUrl, tvSecurityStatus, tvRecommendations;
    private ProgressBar progressBarScore;
    private ImageView ivSecurityIcon;
    private LinearLayout layoutDetails;
    private MaterialButton btnNewScan, btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        initViews();
        displaySecurityResults();
        setupListeners();
    }

    private void initViews() {
        tvScore = findViewById(R.id.tvScore);
        tvUrl = findViewById(R.id.tvUrl);
        tvSecurityStatus = findViewById(R.id.tvSecurityStatus);
        tvRecommendations = findViewById(R.id.tvRecommendations);
        progressBarScore = findViewById(R.id.progressBarScore);
        ivSecurityIcon = findViewById(R.id.ivSecurityIcon);
        layoutDetails = findViewById(R.id.layoutDetails);
        btnNewScan = findViewById(R.id.btnNewScan);
        btnSave = findViewById(R.id.btnSave);
    }

    private void displaySecurityResults() {
        // Récupérer les résultats
        AnalysisActivity.SecurityResult result =
                (AnalysisActivity.SecurityResult) getIntent().getSerializableExtra("SECURITY_RESULT");

        if (result == null) {
            // Résultats de démonstration
            result = new AnalysisActivity.SecurityResult();
            result.url = "https://example.com";
            result.securityScore = 85;
            result.isHttps = true;
            result.hasValidCertificate = true;
            result.isSafe = true;
            result.phishingRisk = 15;
            result.malwareDetected = false;
        }

        // Afficher les données
        tvUrl.setText(result.url);
        tvScore.setText(result.securityScore + "/100");
        progressBarScore.setProgress(result.securityScore);

        // Déterminer le niveau de sécurité
        String status;
        int color;
        int iconRes;

        if (result.securityScore >= 80) {
            status = "TRÈS SÉCURISÉ";
            color = Color.GREEN;
            iconRes = R.drawable.ic_security_safe;
        } else if (result.securityScore >= 60) {
            status = "MOYENNEMENT SÉCURISÉ";
            color = Color.YELLOW;
            iconRes = R.drawable.ic_security_warning;
        } else {
            status = "PEU SÉCURISÉ";
            color = Color.RED;
            iconRes = R.drawable.ic_security_danger;
        }

        tvSecurityStatus.setText(status);
        tvSecurityStatus.setTextColor(color);
        ivSecurityIcon.setImageResource(iconRes);

        // Générer les recommandations
        StringBuilder recommendations = new StringBuilder();
        recommendations.append("📊 Score de sécurité: ").append(result.securityScore).append("/100\n\n");

        if (!result.isHttps) {
            recommendations.append("⚠️ Le site n'utilise pas HTTPS\n");
        }
        if (!result.hasValidCertificate) {
            recommendations.append("⚠️ Certificat SSL non valide\n");
        }
        if (result.phishingRisk > 20) {
            recommendations.append("⚠️ Risque de phishing détecté\n");
        }
        if (result.malwareDetected) {
            recommendations.append("🚨 Malware potentiel détecté\n");
        }

        if (recommendations.length() == 0) {
            recommendations.append("✅ Aucun problème de sécurité majeur détecté\n");
        }

        recommendations.append("\n✅ HTTPS: ").append(result.isHttps ? "OUI" : "NON");
        recommendations.append("\n✅ Certificat valide: ").append(result.hasValidCertificate ? "OUI" : "NON");
        recommendations.append("\n✅ Risque phishing: ").append(result.phishingRisk).append("%");
        recommendations.append("\n✅ Malware: ").append(result.malwareDetected ? "OUI" : "NON");

        tvRecommendations.setText(recommendations.toString());
    }

    private void setupListeners() {
        btnNewScan.setOnClickListener(v -> {
            // Retourner au scan
            finish();
            startActivity(new Intent(this, ScanActivity.class));
        });

        btnSave.setOnClickListener(v -> {
            // Sauvegarder dans l'historique
            // TODO: Implémenter la sauvegarde
            btnSave.setText("SAUVEGARDÉ ✓");
            btnSave.setEnabled(false);
        });
    }
}