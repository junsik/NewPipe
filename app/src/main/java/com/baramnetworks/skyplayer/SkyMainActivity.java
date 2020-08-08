package com.baramnetworks.skyplayer;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.view.ContextThemeWrapper;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import org.schabi.newpipe.BuildConfig;
import org.schabi.newpipe.MainActivity;
import org.schabi.newpipe.NewPipeDatabase;
import org.schabi.newpipe.R;
import org.schabi.newpipe.database.AppDatabase;
import org.schabi.newpipe.util.NavigationHelper;

public class SkyMainActivity extends MainActivity {
    private AdView mAdView;
    private FirebaseRemoteConfig mFirebaseRemoteConfig;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppDatabase db = NewPipeDatabase.getInstance(this);
        InitPlayList pl = new InitPlayList(db);
        pl.execute();

        MobileAds.initialize(this, "ca-app-pub-7734852415793745~7955951495");

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        InterstitialAd mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-7734852415793745/3028904356");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        NavigationHelper.setInterstitialAd(mInterstitialAd);
//
//        NavigationHelper.openPlaylistFragment(getSupportFragmentManager(),
//                0,
//                "https://www.youtube.com/playlist?list=PL2HEDIx6Li8jGsqCiXUq9fzCqpH99qqHV",
//                "멜론 TOP100");

        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(60 * 60)
                .build();
        mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings);
        mFirebaseRemoteConfig.setDefaultsAsync(R.xml.remote_config_defaults);

        mFirebaseRemoteConfig.fetchAndActivate()
                .addOnCompleteListener(this, new OnCompleteListener<Boolean>() {
                    @Override
                    public void onComplete(@NonNull final Task<Boolean> task) {
                        if (task.isSuccessful()) {
                            long newversion =
                                    mFirebaseRemoteConfig.getLong("skyplayer_version");
                            if (newversion > BuildConfig.VERSION_CODE) {
                                showUpdatePopup();
                            }
                        } else {
                            Toast.makeText(SkyMainActivity.this, "Fetch failed",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void showUpdatePopup() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                new ContextThemeWrapper(this,
                R.style.DarkDialogTheme));

        alertDialogBuilder.setTitle(getString(R.string.youAreNotUpdatedTitle));
        alertDialogBuilder.setMessage(getString(R.string.youAreNotUpdatedMessage));
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton(R.string.update,
                new DialogInterface.OnClickListener() {
            public void onClick(final DialogInterface dialog, final int id) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://skyplayer-app.baramnetworks.com")));
                dialog.cancel();
            }
        });
        alertDialogBuilder.show();
    }

    @Override
    protected void onResume() {
        super.onResume();

        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }
}
