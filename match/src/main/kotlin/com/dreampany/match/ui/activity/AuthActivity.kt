package com.dreampany.match.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.dreampany.frame.ui.activity.BaseActivity
import com.dreampany.frame.util.AndroidUtil
import com.dreampany.match.R
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_auth.*


/**
 * Created by Hawladar Roman on 5/22/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
class AuthActivity : BaseActivity() {

    companion object {
        private const val RC_SIGN_IN = 9001
    }

    private lateinit var auth: FirebaseAuth

    override fun getLayoutId(): Int {
        return R.layout.activity_auth
    }

    override fun isFullScreen(): Boolean {
        return true
    }

    override fun onStartUi(state: Bundle?) {
        auth = FirebaseAuth.getInstance()
        signInButton.setOnClickListener(this)
        signOutButton.setOnClickListener(this)
    }

    override fun onStopUi() {
    }

    override fun onStart() {
        super.onStart()
        updateUi(auth.currentUser)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.signInButton -> signIn()
            R.id.signOutButton -> signOut()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            if (resultCode == Activity.RESULT_OK) {

                updateUi(auth.currentUser)
            } else {
                //Toast.makeText(this, "Sign In Failed", Toast.LENGTH_SHORT).show()
                updateUi(null)
            }
        }
    }

    private fun signIn() {
        val intent = AuthUI.getInstance().createSignInIntentBuilder()
            .setIsSmartLockEnabled(AndroidUtil.isRelease(this))
            .setAvailableProviders(listOf(AuthUI.IdpConfig.GoogleBuilder().build()))
            .setLogo(R.mipmap.ic_launcher)
            .build()

       // signOut()
        startActivityForResult(intent, RC_SIGN_IN)
    }

    private fun signOut() {
        AuthUI.getInstance().signOut(this)
        updateUi(null)
    }

    private fun updateUi(user: FirebaseUser?) {
        if (user != null) {
            // Signed in
            status.text = getString(R.string.email_fmt, user.email)
            detail.text = getString(R.string.id_fmt, user.uid)

            signInButton.visibility = View.GONE
            signOutButton.visibility = View.VISIBLE
        } else {
            // Signed out
            status.setText(R.string.signed_out)
            detail.text = null

            signInButton.visibility = View.VISIBLE
            signOutButton.visibility = View.GONE
        }
    }
}