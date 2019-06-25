package com.dreampany.match.ui.activity

import android.os.Bundle
import com.dreampany.frame.ui.activity.BaseActivity
import com.dreampany.match.R
import com.firebase.ui.auth.AuthUI


/**
 * Created by Hawladar Roman on 5/22/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
class AuthActivity : BaseActivity() {

    private val RC_SIGN_IN = 101

    override fun isFullScreen(): Boolean {
        return true
    }

    override fun onStartUi(state: Bundle?) {
        val providers = mutableListOf(
            //AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build()
        )

        /*AuthUI.getInstance()
            .signOut(this)
            .addOnCompleteListener { }*/

        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setLogo(R.mipmap.ic_launcher)
                .build(),
            RC_SIGN_IN
        )

    }

    override fun onStopUi() {
    }
}