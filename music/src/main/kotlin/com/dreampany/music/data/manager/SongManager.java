package com.dreampany.music.data.manager;

import android.content.Context;
import android.database.Cursor;
import android.provider.BaseColumns;
import android.provider.MediaStore.Audio.AudioColumns;
import android.provider.MediaStore.Audio.Media;
import android.text.TextUtils;

import com.dreampany.framework.data.source.local.StateDao;
import com.dreampany.music.data.enums.ItemState;
import com.dreampany.music.data.enums.ItemSubtype;
import com.dreampany.music.data.enums.ItemType;
import com.dreampany.music.data.model.Song;
import com.dreampany.music.data.source.local.Pref;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Hawladar Roman on 6/25/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
public class SongManager {
    protected final String BASE_SELECTION = AudioColumns.IS_MUSIC + "=1" + " and " + AudioColumns.TITLE + " != ''";
    protected final String[] PROJECTION = new String[]{
            BaseColumns._ID,// 0
            AudioColumns.TITLE,// 1
            AudioColumns.TRACK,// 2
            AudioColumns.YEAR,// 3
            AudioColumns.DURATION,// 4
            AudioColumns.DATA,// 5
            AudioColumns.DATE_MODIFIED,// 6
            AudioColumns.ALBUM_ID,// 7
            AudioColumns.ALBUM,// 8
            AudioColumns.ARTIST_ID,// 9
            AudioColumns.ARTIST,// 10
    };

    private final Context context;
    private final Pref pref;
    private final StateDao stateDao;
    private final String type;
    private final String subtype;
    private final String stateBlacklist;

    @Inject
    public SongManager(Context context, Pref pref, StateDao stateDao) {
        this.context = context;
        this.pref = pref;
        this.stateDao = stateDao;
        type = ItemType.SONG.name();
        subtype = ItemSubtype.DEFAULT.name();
        stateBlacklist = ItemState.BLACKLIST.name();
    }

    public List<Song> getAllSongs() {
        Cursor cursor = getCursor(null, null);
        return getSongs(cursor);
    }

    public List<Song> getSongs(Cursor cursor) {
        if (cursor == null || !cursor.moveToFirst()) {
            return null;
        }
        List<Song> songs = new ArrayList<>();
        do {
            songs.add(getSong(cursor));
        } while (cursor.moveToNext());
        cursor.close();
        return songs;
    }

    private Song getSong(Cursor cursor) {
        int id = cursor.getInt(0);
        String title = cursor.getString(1);
        int trackNumber = cursor.getInt(2);
        int year = cursor.getInt(3);
        long duration = cursor.getLong(4);
        String data = cursor.getString(5);
        long dateModified = cursor.getLong(6);
        int albumId = cursor.getInt(7);
        String albumName = cursor.getString(8);
        int artistId = cursor.getInt(9);
        String artistName = cursor.getString(10);

        return new Song(id, title, trackNumber, year, duration, data, dateModified, albumId, albumName, artistId, artistName);
    }

    private Cursor getCursor(String selection, String[] selectionArgs) {
        return getCursor(selection, selectionArgs, pref.getSongSortOrder());
    }

    private Cursor getCursor(String selection, String[] selectionArgs, String sortOrder) {
        if (!TextUtils.isEmpty(selection)) {
            selection = BASE_SELECTION + " and " + selection;
        } else {
            selection = BASE_SELECTION;
        }

        //remove black listing items manually
        //List<String> blacklist = stateDao.getStates(type, subtype, stateBlacklist).blockingSingle();

        try {
            return context.getContentResolver().query(
                    Media.EXTERNAL_CONTENT_URI,
                    PROJECTION,
                    selection,
                    selectionArgs,
                    sortOrder);
        } catch (SecurityException ignored) {
            return null;
        }
    }
}
