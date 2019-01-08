package com.dreampany.word.data.misc;

import com.dreampany.frame.misc.SmartMap;
import com.dreampany.word.data.enums.ItemState;
import com.dreampany.word.data.enums.ItemSubstate;

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
    private final SmartMap<String, ItemSubstate> substates;

    @Inject
    StateMapper() {
        states = SmartMap.newMap();
        substates = SmartMap.newMap();
    }

    public ItemState toState(String state) {
        if (!states.contains(state)) {
            states.put(state, ItemState.valueOf(state));
        }
        return states.get(state);
    }

    public ItemSubstate toSubstate(String substate) {
        if (!substates.contains(substate)) {
            substates.put(substate, ItemSubstate.valueOf(substate));
        }
        return substates.get(substate);
    }
}
