package com.sagara.coffeshop.Activity

import ManagmentCart
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.sagara.coffeshop.Adapter.SizeAdapter
import com.sagara.coffeshop.Model.ItemsModel
import com.sagara.coffeshop.R
import com.sagara.coffeshop.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var item: ItemsModel
    private lateinit var managementCart: ManagmentCart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inisialisasi objek cart
        managementCart = ManagmentCart(this)

        // Mengisi data dari Intent
        bundle()
        // Menampilkan list ukuran
        initSizeList()
    }

    private fun initSizeList() {
        // Mendapatkan list ukuran dan harga item
        val sizeList = item.sizePrices.keys.toList()

        // Menampilkan list ukuran menggunakan adapter
        binding.sizeList.adapter = SizeAdapter(sizeList) { selectedSize ->
            updatePriceBasedOnSize(selectedSize)
        }
        binding.sizeList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        // Memuat gambar utama menggunakan Glide
        if (item.picUrl.isNotEmpty()) { // Pastikan picUrl adalah URL valid
            Glide.with(this)
                .load(item.picUrl[0]) // Asumsikan ini URL gambar dalam bentuk String
                .apply(RequestOptions.bitmapTransform(RoundedCorners(100)))
                .into(binding.picMain)
        } else {
            Log.e("DetailActivity", "picUrl is empty or invalid")
        }
    }

    private fun updatePriceBasedOnSize(size: String) {
        // Memperbarui harga berdasarkan ukuran yang dipilih
        val selectedPrice = item.sizePrices[size] ?: item.price
        binding.priceTxt.text = "$${String.format("%.2f", selectedPrice)}"
        item.price = selectedPrice // Memperbarui harga pada model item
    }

    private fun bundle() {
        binding.apply {
            // Mendapatkan data item dari Intent
            item = intent.getParcelableExtra("object") ?: run {
                Log.e("DetailActivity", "Item tidak ditemukan di Intent")
                finish()
                return
            }

            // Mengisi data item ke UI
            titleTxt.text = item.title
            descriptionTxt.text = item.description
            priceTxt.text = "$${String.format("%.2f", item.price)}"
            ratingBar.rating = item.rating.toFloat()

            // Menangani klik tombol "Add to Cart"
            addToCartBtn.setOnClickListener {
                item.numberInCart = numberItemTxt.text.toString().toIntOrNull() ?: 0
                managementCart.insertItems(item)
            }

            // Tombol kembali ke aktivitas utama
            backBtn.setOnClickListener {
                startActivity(Intent(this@DetailActivity, MainActivity::class.java))
            }

            // Tombol tambah jumlah item
            plusCart.setOnClickListener {
                item.numberInCart++
                numberItemTxt.text = item.numberInCart.toString()
            }

            // Tombol kurangi jumlah item
            minusCartBtn.setOnClickListener {
                if (item.numberInCart > 0) {
                    item.numberInCart--
                    numberItemTxt.text = item.numberInCart.toString()
                }
            }
        }
    }
}
