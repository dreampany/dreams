package com.dreampany.word.data.misc;

import com.dreampany.frame.misc.SmartMap;
import com.dreampany.word.data.enums.ItemState;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Hawladar Roman on 12/5/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
@Singleton
public class StateMapper {

    private final SmartMap<String, ItemState> states;

    @Inject
    StateMapper() {
        states = SmartMap.newMap();
    }

    public ItemState toState(String state) {
        if (!states.contains(state)) {
            states.put(state, ItemState.valueOf(state));
        }
        return states.get(state);
    }
}
