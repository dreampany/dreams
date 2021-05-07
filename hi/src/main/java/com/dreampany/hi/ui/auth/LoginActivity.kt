package com.dreampany.hi.ui.auth

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dreampany.hi.databinding.LoginActivityBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: LoginActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = LoginActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

}
