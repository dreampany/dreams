// PlayerService.aidl
package com.dreampany.tools.service;

import com.dreampany.tools.data.model.Station;
// Declare any non-default types here with import statements

interface PlayerService {

    void setStation(in Station station);

    void play();

    void pause();

    void resume();
}
