package com.sagara.coffeshop.Activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.sagara.coffeshop.RegisterActivity
import com.sagara.coffeshop.databinding.ActivityIntroBinding
import com.sagara.coffeshop.login
import com.sagara.coffeshop.Activity.MainActivity

class IntroActivity : BaseActivity() {
    private lateinit var binding: ActivityIntroBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIntroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Cek apakah pengguna sudah login
        if (isUserLoggedIn()) {
            // Jika sudah login, langsung pindah ke MainActivity
            startActivity(Intent(this@IntroActivity, MainActivity::class.java))
            finish() // Tutup IntroActivity agar tidak bisa kembali dengan tombol back
        }

        // Tombol Start untuk Register
        binding.startBtn.setOnClickListener {
            startActivity(Intent(this@IntroActivity, RegisterActivity::class.java))
        }

        // Tombol Login untuk Login
        binding.Login.setOnClickListener {
            startActivity(Intent(this@IntroActivity, login::class.java))
        }
    }

    /**
     * Fungsi untuk mendeteksi apakah pengguna sudah login
     */
    private fun isUserLoggedIn(): Boolean {
        val sharedPreferences = getSharedPreferences("user_session", Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean("is_logged_in", false)
    }
}
