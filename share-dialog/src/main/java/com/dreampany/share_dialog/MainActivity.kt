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


class MainActivity : AppCompatActivity() {

    val tag = "Share-Dailog"

    lateinit var callbackManager: CallbackManager
    lateinit   var shareDialog: ShareDialog

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
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }

    fun share() {
        if (ShareDialog.canShow(ShareLinkContent::class.java)) {
            val linkContent = ShareLinkContent.Builder()
                .setContentUrl(Uri.parse("http://developers.facebook.com/android"))
                .build()
            shareDialog.show(linkContent)
        }
    }

}
