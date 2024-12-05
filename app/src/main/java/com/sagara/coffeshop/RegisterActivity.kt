package com.sagara.coffeshop

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.auth.User

class RegisterActivity : AppCompatActivity() {

    // Firebase Realtime Database Reference
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Deklarasi Views
        val etName = findViewById<EditText>(R.id.etName)
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val btnRegister = findViewById<Button>(R.id.btnRegister)

        // Inisialisasi Firebase Database
        database = FirebaseDatabase.getInstance("https://coffeshop-22a18-default-rtdb.firebaseio.com/").getReference("User")

        btnRegister.setOnClickListener {
            val name = etName.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            // Validasi input
            when {
                name.isEmpty() -> {
                    etName.error = "Name cannot be empty"
                    etName.requestFocus()
                }
                email.isEmpty() -> {
                    etEmail.error = "Email cannot be empty"
                    etEmail.requestFocus()
                }
                password.isEmpty() -> {
                    etPassword.error = "Password cannot be empty"
                    etPassword.requestFocus()
                }
                password.length < 6 -> {
                    etPassword.error = "Password must be at least 6 characters"
                    etPassword.requestFocus()
                }
                else -> {
                    saveUserToDatabase(name, email, password, etName, etEmail, etPassword)
                    startActivity(Intent(this@RegisterActivity, login::class.java))
                }
            }
        }
    }

    private fun saveUserToDatabase(
        name: String,
        email: String,
        password: String,
        etName: EditText,
        etEmail: EditText,
        etPassword: EditText
    ) {
        // Buat ID unik untuk setiap pengguna
        val userId = database.push().key ?: return

        // Buat objek pengguna menggunakan data class User
        val user = User(name, email, password)

        // Simpan data ke Firebase
        database.child(userId).setValue(user)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show()
                    etName.text.clear()
                    etEmail.text.clear()
                    etPassword.text.clear()
                } else {
                    Toast.makeText(this, "Failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

}