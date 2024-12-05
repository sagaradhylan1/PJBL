package com.sagara.coffeshop

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.sagara.coffeshop.Activity.MainActivity

class login : AppCompatActivity() {

    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)

        // Inisialisasi Firebase Database
        database = FirebaseDatabase.getInstance("https://coffeshop-22a18-default-rtdb.firebaseio.com/").getReference("User")

        // SharedPreferences
        val sharedPreferences = getSharedPreferences("user_session", Context.MODE_PRIVATE)

        btnLogin.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            // Validasi input
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password.length < 6) {
                Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Simpan user ke Firebase dan SharedPreferences
            saveUserToDatabase(email, password, etEmail, etPassword, sharedPreferences)
        }
    }

    private fun saveUserToDatabase(
        email: String,
        password: String,
        etEmail: EditText,
        etPassword: EditText,
        sharedPreferences: SharedPreferences
    ) {
        val userId = database.push().key ?: return
        val user = mapOf("email" to email, "password" to password)

        // Simpan data ke Firebase Realtime Database
        database.child(userId).setValue(user)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "User saved successfully", Toast.LENGTH_SHORT).show()

                    // Simpan status login ke SharedPreferences
                    val editor = sharedPreferences.edit()
                    editor.putBoolean("is_logged_in", true)
                    editor.apply()

                    // Clear input fields
                    etEmail.text.clear()
                    etPassword.text.clear()

                    // Pindah ke MainActivity
                    startActivity(Intent(this, MainActivity::class.java))
                    finish() // Tutup LoginActivity
                } else {
                    Toast.makeText(this, "Failed to save user: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
