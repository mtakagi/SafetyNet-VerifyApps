package com.abadbabe.safety;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.safetynet.HarmfulAppsData;
import com.google.android.gms.safetynet.SafetyNet;
import com.google.android.gms.safetynet.SafetyNetApi;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView mView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mView = findViewById(R.id.text_view);
        Button isEnabledButton = findViewById(R.id.is_verify_app_button);
        Button enableButton = findViewById(R.id.enable_verify_app_button);
        Button listButton = findViewById(R.id.list_app_button);

        isEnabledButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isVerifyAppsEnabled();
            }
        });

        enableButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enableVerifyApps();
            }
        });

        listButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listHarmfulApps();
            }
        });
    }

    private void isVerifyAppsEnabled() {
        SafetyNet.getClient(this).isVerifyAppsEnabled().addOnCompleteListener(new OnCompleteListener<SafetyNetApi.VerifyAppsUserResponse>() {
            @Override
            public void onComplete(@NonNull Task<SafetyNetApi.VerifyAppsUserResponse> task) {
                if (!task.isSuccessful()) {
                    mView.setText("General Check Error.");
                    return;
                }

                SafetyNetApi.VerifyAppsUserResponse response = task.getResult();
                mView.setText(response.isVerifyAppsEnabled() ? "Verify Apps Enabled." : "Verify Apps Disabled.");
            }
        });
    }

    private void enableVerifyApps() {
        SafetyNet.getClient(this).enableVerifyApps().addOnCompleteListener(new OnCompleteListener<SafetyNetApi.VerifyAppsUserResponse>() {
            @Override
            public void onComplete(@NonNull Task<SafetyNetApi.VerifyAppsUserResponse> task) {
                if (!task.isSuccessful()) {
                    mView.setText("General Enable Error.");
                    return;
                }

                SafetyNetApi.VerifyAppsUserResponse response = task.getResult();
                mView.setText(response.isVerifyAppsEnabled() ? "Enable Verify Apps Success." : "Enable Verify Apps Failed.");
            }
        });
    }

    private void listHarmfulApps() {
        SafetyNet.getClient(this).listHarmfulApps().addOnCompleteListener(new OnCompleteListener<SafetyNetApi.HarmfulAppsResponse>() {
            @Override
            public void onComplete(@NonNull Task<SafetyNetApi.HarmfulAppsResponse> task) {
                if (!task.isSuccessful()) {
                    mView.setText("General Listing Error.");
                    return;
                }

                SafetyNetApi.HarmfulAppsResponse response = task.getResult();
                List<HarmfulAppsData> list = response.getHarmfulAppsList();
                long lastScanMS = response.getLastScanTimeMs();
                String result = "Last Scan Time MS: " + lastScanMS;

                for (HarmfulAppsData data : list) {
                    result += "APK: " + data.apkPackageName + "\n";
                    result += "SHA-256: " + data.apkSha256 + "\n";
                    result += "Category: " + data.apkCategory + "\n";
                }

                mView.setText(result);
            }
        });
    }
}
