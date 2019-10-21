package com.dreampany.share_dialog

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.share.Sharer
import com.facebook.share.model.ShareLinkContent
import com.facebook.share.widget.ShareDialog
import kotlinx.android.synthetic.main.activity_main.*
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.R.attr.data
import com.twitter.sdk.android.core.models.Tweet
import com.twitter.sdk.android.core.services.StatusesService
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import com.twitter.sdk.android.core.*
import kotlin.Result
import com.twitter.sdk.android.core.TwitterException
import com.twitter.sdk.android.core.TwitterSession
import com.twitter.sdk.android.core.identity.TwitterAuthClient
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T




class MainActivity : AppCompatActivity() {

    val tag = "Share-Dailog"

    lateinit var callbackManager: CallbackManager
    lateinit   var shareDialog: ShareDialog

    lateinit var twitterAuthClient: TwitterAuthClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        callbackManager = CallbackManager.Factory.create();
        shareDialog =  ShareDialog(this);
        shareDialog.registerCallback(callbackManager,object : FacebookCallback<Sharer.Result> {
            override fun onSuccess(result: Sharer.Result?) {
                Log.v(tag, "Success");
            }

            override fun onCancel() {
                Log.v(tag, "onCancel");
            }

            override fun onError(error: FacebookException?) {
                Log.v(tag, "onError");
            }

        });
        button.setOnClickListener {
            share()
        }
        twitterAuthClient = TwitterAuthClient()

        val twitterSession = TwitterCore.getInstance().sessionManager.activeSession
        if (twitterSession == null) {
            twitterAuthClient.authorize(this, object : Callback<TwitterSession>() {
                override fun failure(exception: TwitterException) {
                    Log.e("Twitter", "Failed to authenticate user " + exception.message)
                }

                override fun success(result: com.twitter.sdk.android.core.Result<TwitterSession>) {
                    Log.e("Twitter", "success + ")

                }

            })
        }

/*        login_button.callback = object : Callback<TwitterSession>() {
            override fun failure(exception: TwitterException?) {
                Log.d("Error", exception?.toString() ?: "Error");
            }

            override fun success(result: com.twitter.sdk.android.core.Result<TwitterSession>?) {
                share()
            }

        }*/


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (twitterAuthClient != null) {
            twitterAuthClient.onActivityResult(requestCode, resultCode, data);
        }
        callbackManager.onActivityResult(requestCode, resultCode, data)
        //login_button.onActivityResult(requestCode, resultCode, data);
    }

    fun login() {

    }

    fun share() {

/*        if (ShareDialog.canShow(ShareLinkContent::class.java)) {
            val linkContent = ShareLinkContent.Builder()
                .setContentUrl(Uri.parse("http://developers.facebook.com/android"))
                .build()
            shareDialog.show(linkContent)
        }*/
        val statusesService = TwitterCore.getInstance().apiClient.statusesService
        val tweetCall = statusesService.update("HelloDear", null, false, null, null, null, false, false, null)
        tweetCall.enqueue(object : Callback<Tweet>() {
            override fun success(result: com.twitter.sdk.android.core.Result<Tweet>) {
                Log.d("result", result?.data?.idStr ?: "Hee");
            }

            override fun failure(exception: TwitterException) {
                Log.d("Error", exception.toString());
            }

        })
    }

}
