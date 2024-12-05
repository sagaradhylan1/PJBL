package com.sagara.coffeshop.Activity

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.sagara.coffeshop.R

class CheckoutActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout) // Pastikan layout sesuai dengan XML

        // Ambil data dari Intent
        val subtotalValue = intent.getStringExtra("subtotal") ?: "$0.00"
        val deliveryValue = intent.getStringExtra("delivery") ?: "$0.00"
        val taxValue = intent.getStringExtra("tax") ?: "$0.00"
        val totalValue = intent.getStringExtra("total") ?: "$0.00"

        // Set data ke TextView berdasarkan ID
        val subtotalValueTxt = findViewById<TextView>(R.id.subtotalValueTxt)
        val deliveryValueTxt = findViewById<TextView>(R.id.deliveryValueTxt)
        val taxValueTxt = findViewById<TextView>(R.id.taxValueTxt)
        val totalValueTxt = findViewById<TextView>(R.id.totalValueTxt)

        subtotalValueTxt.text = subtotalValue
        deliveryValueTxt.text = deliveryValue
        taxValueTxt.text = taxValue
        totalValueTxt.text = totalValue
    }
}

