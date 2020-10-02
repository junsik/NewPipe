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
    private static final String BUGS_URL =
            "https://www.youtube.com/playlist?list=PL6ikhxBXtaLK5sdOP9LnBMvuMrgjtSTyY";
    public static final List<Tab> FALLBACK_INITIAL_TABS_LIST = Collections.unmodifiableList(
            Arrays.asList(
                    new Tab.PlaylistTab(0,
                            BUGS_URL,
                            "멜론 인기 순위"
                    ),
                    Tab.Type.DEFAULT_KIOSK.getTab(),
                    Tab.Type.SUBSCRIPTIONS.getTab(),
                    Tab.Type.BOOKMARKS.getTab()));
    private final AppDatabase db;

    InitPlayList(final AppDatabase db) {
        this.db = db;
    }

    @Override
    protected Void doInBackground(final Void... voids) {
        final PlaylistRemoteDAO pl = db.playlistRemoteDAO();
        pl.upsert(new PlaylistRemoteEntity(0, "멜론 인기 순위",
                BUGS_URL,
                "https://i.ytimg.com/vi/gdZLi9oWNZg/hqdefault.jpg?sqp="
                        + "-oaymwEXCNACELwBSFryq4qpAwkIARUAAIhCGAE=&rs=AOn4CLDElRX7_JHRG6K"
                        + "Ttn9LW0uRyDTsMQ",
                "SkyPlayer",
                100L));
        return null;
    }
}
