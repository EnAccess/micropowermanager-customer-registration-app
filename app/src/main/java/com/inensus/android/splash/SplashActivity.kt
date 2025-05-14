package com.inensus.android.splash

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.inensus.android.login.view.LoginActivity
import com.inensus.android.main.MainActivity
import com.inensus.android.util.SharedPreferenceWrapper
import org.koin.android.ext.android.inject

class SplashActivity : AppCompatActivity() {
    private val preferences: SharedPreferenceWrapper by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (preferences.accessToken.isEmpty()) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        } else {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}
