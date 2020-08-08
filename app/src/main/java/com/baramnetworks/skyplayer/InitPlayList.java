package com.baramnetworks.skyplayer;

import android.os.AsyncTask;

import org.schabi.newpipe.database.AppDatabase;
import org.schabi.newpipe.database.playlist.dao.PlaylistRemoteDAO;
import org.schabi.newpipe.database.playlist.model.PlaylistRemoteEntity;

public class InitPlayList extends AsyncTask<Void, Void, Void> {
    private final AppDatabase db;

    InitPlayList(final AppDatabase db) {
        this.db = db;
    }

    @Override
    protected Void doInBackground(final Void... voids) {
        PlaylistRemoteDAO pl = db.playlistRemoteDAO();

        pl.upsert(new PlaylistRemoteEntity(0, "Melon Daily Top 100",
                "https://www.youtube.com/playlist?list=PL2HEDIx6Li8jGsqCiXUq9fzCqpH99qqHV",
                "https://i.ytimg.com/vi/ESKfHHtiSjs/hqdefault.jpg?sqp="
                        + "-oaymwEWCKgBEF5IWvKriqkDCQgBFQAAiEIYAQ==&rs=AOn4CLAEmFRPUfcQ"
                        + "Phd23TgaitGf_eOBBA",
                "SkyPlayer",
                100L));
        return null;
    }
}
