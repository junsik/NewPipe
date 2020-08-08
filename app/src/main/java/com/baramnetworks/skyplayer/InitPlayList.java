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
    private static final String MELON_URL =
            "https://www.youtube.com/playlist?list=PL2HEDIx6Li8jGsqCiXUq9fzCqpH99qqHV";
    public static final List<Tab> FALLBACK_INITIAL_TABS_LIST = Collections.unmodifiableList(
            Arrays.asList(
                    new Tab.PlaylistTab(0,
                            MELON_URL,
                            "Melon Daily Top 100"
                            ),
                    new Tab.ChannelTab(0,
                            "https://www.youtube.com/channel/UCskIKQGpjI5sG2ZLtWLYSmw",
                            "미스터 트롯"),
                    Tab.Type.DEFAULT_KIOSK.getTab(),
                    Tab.Type.SUBSCRIPTIONS.getTab(),
                    Tab.Type.BOOKMARKS.getTab()));
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
