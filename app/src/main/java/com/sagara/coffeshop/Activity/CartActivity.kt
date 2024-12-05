package com.sagara.coffeshop.Activity

import ManagmentCart
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.sagara.coffeshop.Adapter.CartAdapter
import com.sagara.coffeshop.Helper.ChangeNumberItemsListener
import com.sagara.coffeshop.databinding.ActivityCartBinding
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DatabaseReference

class CartActivity : BaseActivity() {
    lateinit var binding: ActivityCartBinding
    lateinit var managment: ManagmentCart
    private var tax: Double = 0.0
    private var delivery: Double = 15.0
    private var itemTotal: Double = 0.0
    private var total: Double = 0.0
    private lateinit var database: DatabaseReference // Tambahkan Firebase Database

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        managment = ManagmentCart(this)
        database = FirebaseDatabase.getInstance().getReference("CartData") // Inisialisasi Database

        calculatorCart()
        setVariable()
        initCartList()
        setupProceedToCheckout()
    }

    private fun initCartList() {
        with(binding) {
            cartView.layoutManager = LinearLayoutManager(this@CartActivity, LinearLayoutManager.VERTICAL, false)
            cartView.adapter = CartAdapter(managment.getListCart(), this@CartActivity, object : ChangeNumberItemsListener {
                override fun onChanged() {
                    calculatorCart()
                }
            })
        }
    }

    private fun setVariable() {
        binding.backBtn.setOnClickListener { finish() }
    }

    private fun calculatorCart() {
        val percentTax = 0.02
        delivery = 15.0
        itemTotal = Math.round(managment.getTotalFee() * 100) / 100.0
        tax = Math.round((managment.getTotalFee() * percentTax) * 100) / 100.0
        total = Math.round((managment.getTotalFee() + tax + delivery) * 100) / 100.0

        with(binding) {
            totalFeeTxt.text = "$$itemTotal"
            taxText.text = "$$tax"
            deliveryTxt.text = "$$delivery"
            totalTxt.text = "$$total"
        }
    }

    private fun setupProceedToCheckout() {
        binding.chechOutBtn.setOnClickListener {
            // Periksa apakah keranjang kosong
            if (managment.getListCart().isEmpty()) {
                showToast("Keranjang Anda kosong. Silakan tambahkan item sebelum melanjutkan.")
                return@setOnClickListener
            }

            saveCartToDatabase() // Simpan data ke database sebelum berpindah halaman

            val intent = Intent(this, CheckoutActivity::class.java)
            intent.putExtra("subtotal", "$itemTotal")
            intent.putExtra("tax", "$tax")
            intent.putExtra("delivery", "$delivery")
            intent.putExtra("total", "$total")
            startActivity(intent)
        }
    }


    private fun saveCartToDatabase() {
        val cartItems = managment.getListCart()
        val cartData = hashMapOf<String, Any>(
            "items" to cartItems.map { item ->
                hashMapOf(
                    "name" to item.name,
                    "price" to item.price,
                    "quantity" to item.quantity
                )
            },
            "subtotal" to itemTotal,
            "tax" to tax,
            "delivery" to delivery,
            "total" to total
        )

        // Buat ID unik untuk setiap keranjang
        val uniqueCartId = database.push().key

        if (uniqueCartId != null) {
            database.child(uniqueCartId).setValue(cartData)
                .addOnSuccessListener {
                    // Berhasil disimpan
                    showToast("Keranjang berhasil disimpan!")
                }
                .addOnFailureListener { e ->
                    // Gagal menyimpan
                    showToast("Failed to save cart: ${e.message}")
                }
        } else {
            showToast("Keranjang berhasil disimpan!")
        }
    }


    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
