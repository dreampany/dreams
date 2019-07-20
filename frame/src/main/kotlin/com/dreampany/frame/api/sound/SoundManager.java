package com.dreampany.frame.api.sound;

import android.annotation.TargetApi;
import android.content.Context;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Build;

import com.dreampany.frame.util.AndroidUtil;
import com.google.common.collect.Maps;

import org.apache.commons.lang3.tuple.MutablePair;

import java.util.Map;

/**
 * Created by Hawladar Roman on 12/28/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
public final class SoundManager {

    private static SoundManager instance;
    private SoundPool pool;
    private Map<Integer, MutablePair<Integer, Integer>> sounds;

    private SoundManager() {
        sounds = Maps.newHashMap();
    }

    public static SoundManager getInstance() {
        if (instance == null) {
            instance = new SoundManager();
        }
        return instance;
    }

    public void playSound(Context context, int soundRes) {
        SoundPool pool = getPool();
        if (!sounds.containsKey(soundRes)) {
            int soundId = pool.load(context, soundRes, 1);
            sounds.put(soundRes, MutablePair.of(soundId, 0));
        }

        MutablePair<Integer, Integer> sound = sounds.get(soundRes);
        if (sound.right != 0) {
            pool.stop(sound.right);
        }
        int streamId = pool.play(sound.left, 1, 1, 0, 0, 1);
        sound.setRight(streamId);
    }

    private SoundPool getPool() {
        if (pool == null) {
            if (AndroidUtil.Companion.hasLollipop()) {
                pool = createNewSoundPool();
            } else {
                pool = createOldSoundPool();
            }
        }
        return pool;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private SoundPool createNewSoundPool() {
        AudioAttributes attributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();
        SoundPool pool = new SoundPool.Builder()
                .setAudioAttributes(attributes)
                .build();
        return pool;
    }

    @SuppressWarnings("deprecation")
    protected SoundPool createOldSoundPool() {
        SoundPool pool = new SoundPool(5, android.media.AudioManager.STREAM_MUSIC, 0);
        return pool;
    }
}
