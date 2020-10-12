package com.baramnetworks.skyplayer;

import android.app.Application;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.preference.PreferenceManager;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import org.schabi.newpipe.App;
import org.schabi.newpipe.BuildConfig;
import org.schabi.newpipe.MainActivity;
import org.schabi.newpipe.NewPipeDatabase;
import org.schabi.newpipe.R;
import org.schabi.newpipe.database.AppDatabase;

public class SkyMainActivity extends MainActivity {

    private FirebaseRemoteConfig mFirebaseRemoteConfig;
    private FirebaseAnalytics mFirebaseAnalytics;
    private static final Application APP = App.getApp();
    private AdView adView;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize the Mobile Ads SDK.
        MobileAds.initialize(this);

        // Gets the ad view defined in layout/ad_fragment.xml with ad unit ID set in
        // values/strings.xml.
        adView = findViewById(R.id.ad_view);

        // Create an ad request.
        final AdRequest adRequest = new AdRequest.Builder().build();

        // Start loading the ad in the background.
        adView.loadAd(adRequest);

        final AppDatabase db = NewPipeDatabase.getInstance(this);
        final InitPlayList pl = new InitPlayList(db);
        pl.execute();

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        final FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings
                .Builder()
                .setMinimumFetchIntervalInSeconds(60 * 10)
                .build();
        mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings);
        mFirebaseRemoteConfig.setDefaultsAsync(R.xml.remote_config_defaults);

        mFirebaseRemoteConfig.fetchAndActivate()
                .addOnCompleteListener(this, new OnCompleteListener<Boolean>() {
                    @Override
                    public void onComplete(@NonNull final Task<Boolean> task) {
                        if (task.isSuccessful()) {
                            final long newversion =
                                    mFirebaseRemoteConfig.getLong("version_code");
                            final String versionName =
                                    mFirebaseRemoteConfig.getString("version_name");
                            final String apkLocationUrl =
                                    mFirebaseRemoteConfig.getString("apkLocationUrl");

                            compareAppVersionAndShowNotification(versionName,
                                    apkLocationUrl,
                                    newversion);

                        } else {
                            Toast.makeText(SkyMainActivity.this, "Fetch failed",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void showUpdatePopup(final String apkLocationUrl) {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                new ContextThemeWrapper(this,
                R.style.DarkDialogTheme));

        alertDialogBuilder.setTitle(getString(R.string.app_update_notification_content_title));
        alertDialogBuilder.setMessage(getString(R.string.youAreNotUpdatedMessage));
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton(R.string.yes,
                new DialogInterface.OnClickListener() {
            public void onClick(final DialogInterface dialog, final int id) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse(apkLocationUrl)));
                dialog.cancel();
            }
        });
        alertDialogBuilder.show();
    }

    /**
     * Method to compare the current and latest available app version.
     * If a newer version is available, we show the update notification.
     *
     * @param versionName    Name of new version
     * @param apkLocationUrl Url with the new apk
     * @param versionCode    Code of new version
     */
    private void compareAppVersionAndShowNotification(final String versionName,
                                                      final String apkLocationUrl,
                                                      final long versionCode) {
        final int notificationId = 2000;
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(APP);
        // Check if user has enabled/disabled update checking
        // and if the current apk is a github one or not.
        if (!prefs.getBoolean(APP.getString(R.string.update_app_key), true)) {
            return;
        }

        if (BuildConfig.VERSION_CODE < versionCode) {
            showUpdatePopup(apkLocationUrl);
            // A pending intent to open the apk location url in the browser.
            final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(apkLocationUrl));
            final PendingIntent pendingIntent
                    = PendingIntent.getActivity(this, 0, intent, 0);

            final NotificationCompat.Builder notificationBuilder = new NotificationCompat
                    .Builder(this, getString(R.string.app_update_notification_channel_id))
                    .setSmallIcon(R.drawable.ic_newpipe_update)
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .setContentTitle(getString(R.string.app_update_notification_content_title))
                    .setContentText(getString(R.string.app_update_notification_content_text)
                            + " " + versionName);

            final NotificationManagerCompat notificationManager
                    = NotificationManagerCompat.from(this);
            notificationManager.notify(notificationId, notificationBuilder.build());
        }
    }

    @Override
    public void onPause() {
        if (adView != null) {
            adView.pause();
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adView != null) {
            adView.resume();
        }
    }

    @Override
    public void onDestroy() {
        if (adView != null) {
            adView.destroy();
        }
        super.onDestroy();
    }
}
