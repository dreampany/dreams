package com.dreampany.network;

import com.dreampany.network.data.model.Network;

/**
 * Created by Hawladar Roman on 8/18/2018.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */
public interface BaseNetwork {
    Network getNetwork(boolean hasInternet);
}
