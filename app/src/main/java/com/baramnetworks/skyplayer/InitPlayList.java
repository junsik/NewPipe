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
    private static final String BUGS_URL =
            "https://www.youtube.com/playlist?list=PL6ikhxBXtaLI3rzIdZnVmkDvQnX8y4tKF";
    public static final List<Tab> FALLBACK_INITIAL_TABS_LIST = Collections.unmodifiableList(
            Arrays.asList(
                    new Tab.PlaylistTab(0,
                            MELON_URL,
                            "Melon Daily Top 100"
                            ),
                    new Tab.PlaylistTab(0,
                            BUGS_URL,
                            "Bugs Daily Top 100"
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
        PlaylistRemoteDAO pl = db.playlistRemoteDAO();

        pl.upsert(new PlaylistRemoteEntity(0, "Melon Daily Top 100",
                "https://www.youtube.com/playlist?list=PL2HEDIx6Li8jGsqCiXUq9fzCqpH99qqHV",
                "https://i.ytimg.com/vi/ESKfHHtiSjs/hqdefault.jpg?sqp="
                        + "-oaymwEWCKgBEF5IWvKriqkDCQgBFQAAiEIYAQ==&rs=AOn4CLAEmFRPUfcQ"
                        + "Phd23TgaitGf_eOBBA",
                "SkyPlayer",
                100L));
        pl.upsert(new PlaylistRemoteEntity(0, "Bugs Daily Top 100",
                BUGS_URL,
                "https://i.ytimg.com/vi/Ht4UGpV3DkE/hqdefault.jpg?"
                        + "sqp=-oaymwEXCNACELwBSFryq4qpAwkIARUAAIhCGAE=&rs="
                        + "AOn4CLD15hDNVgqgy1sPmAGByV6tnxlIvg",
                "SkyPlayer",
                100L));
        return null;
    }
}
