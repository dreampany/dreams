package com.kddi.htb

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.twitter.sdk.android.core.*
import com.twitter.sdk.android.core.identity.TwitterAuthClient
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val twitter = TwitterAuthClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        login_button.setOnClickListener {
            authorize()
        }

        /*login_button.callback = object : Callback<TwitterSession>() {
            override fun success(result: Result<TwitterSession>?) {
                Log.e("Twitter", result?.data?.userName+"")
            }

            override fun failure(exception: TwitterException?) {
                Log.e("Twitter", exception?.message+"")
            }

        }*/
        //login_button.performClick()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        twitter.onActivityResult(requestCode, resultCode, data)

        if (resultCode != Activity.RESULT_OK) {
            authorize()
        }
        //login_button.onActivityResult(requestCode, resultCode, data)
    }

    private fun authorize() {
        twitter.authorize(this, object : Callback<TwitterSession>() {
            override fun success(result: Result<TwitterSession>?) {
                val session = TwitterCore.getInstance().sessionManager.activeSession
                Log.e("Twitter", result?.data?.userName+"")
                //getEmailFromTwitter(fragment, session, result)
            }

            override fun failure(exception: TwitterException?) {
                Log.e("Twitter", exception?.message+"")
                //_isTwitterSuccessfull.value = false
            }
        })
    }
}
