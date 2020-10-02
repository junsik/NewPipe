package com.baramnetworks.skyplayer;

import android.os.AsyncTask;

import org.schabi.newpipe.database.AppDatabase;
import org.schabi.newpipe.database.playlist.dao.PlaylistRemoteDAO;
import org.schabi.newpipe.database.playlist.model.PlaylistRemoteEntity;
import org.schabi.newpipe.settings.tabs.Tab;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class InitPlayList extends AsyncTask<Void, Void, Void> {
    private static final String KPOP_URL =
            "https://www.youtube.com/playlist?list=PL6ikhxBXtaLK5sdOP9LnBMvuMrgjtSTyY";
    public static final List<Tab> FALLBACK_INITIAL_TABS_LIST = Collections.unmodifiableList(
            Arrays.asList(
                    new Tab.PlaylistTab(0,
                            KPOP_URL,
                            "K-POP Top 100"
                    )));
    private final AppDatabase db;

    InitPlayList(final AppDatabase db) {
        this.db = db;
    }

    @Override
    protected Void doInBackground(final Void... voids) {
        final PlaylistRemoteDAO pl = db.playlistRemoteDAO();
        pl.upsert(new PlaylistRemoteEntity(0, "K-POP Top 100",
                KPOP_URL,
                "https://i.ytimg.com/vi/gdZLi9oWNZg/hqdefault.jpg?sqp="
                        + "-oaymwEXCNACELwBSFryq4qpAwkIARUAAIhCGAE=&rs=AOn4CLDElRX7_JHRG6K"
                        + "Ttn9LW0uRyDTsMQ",
                "SkyPlayer",
                100L));
        return null;
    }
}
