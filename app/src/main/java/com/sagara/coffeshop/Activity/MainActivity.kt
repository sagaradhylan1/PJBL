package com.sagara.coffeshop.Activity

import android.content.Context
import android.content.Intent
import android.media.Image
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.sagara.coffeshop.Adapter.CategoryAdapter
import com.sagara.coffeshop.Adapter.OfferAdapter
import com.sagara.coffeshop.Adapter.PopularAdapter
import com.sagara.coffeshop.Model.ItemsModel
import com.sagara.coffeshop.ViewModel.MainViewModel
import com.sagara.coffeshop.databinding.ActivityMainBinding
import androidx.lifecycle.Observer
import android.widget.ImageView;
import com.sagara.coffeshop.login


class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    private val viewModel = MainViewModel()

    // Fungsi untuk menangani hasil dari EditActivity
    private val editActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val updatedItem = result.data?.getParcelableExtra<ItemsModel>("updatedItem")
            updatedItem?.let {
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initCategory()
        initPopular()
        initOffer()
        bottomMenu()

        binding.btnlogin!!.setOnClickListener {
            // Hapus status login di SharedPreferences
            val sharedPreferences = getSharedPreferences("user_session", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.clear() // Hapus semua data di SharedPreferences
            editor.apply()

            // Arahkan kembali ke IntroActivity
            val intent = Intent(this, IntroActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK // Hapus tumpukan activity sebelumnya
            startActivity(intent)
            finish() // Tutup activity saat ini
        }

    }


    private fun bottomMenu() {
        binding.cartBtn.setOnClickListener { startActivity(Intent(this, CartActivity::class.java)) }
    }

    private fun initOffer() {
        binding.progressBarOffer.visibility = View.VISIBLE
        viewModel.offer.observe(this, Observer {
            binding.recyclerViewOffer.layoutManager =
                LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
            binding.recyclerViewOffer.adapter = OfferAdapter(it)
            binding.progressBarOffer.visibility = View.GONE
        })
        viewModel.loadOffer()
    }

    private fun initPopular() {
        binding.progressBarPopuler.visibility = View.VISIBLE
        viewModel.popular.observe(this, Observer {
            binding.recyclerViewPopuler.layoutManager = LinearLayoutManager(
                this@MainActivity, LinearLayoutManager.HORIZONTAL, false
            )
            binding.recyclerViewPopuler.adapter = PopularAdapter(it)
            binding.progressBarPopuler.visibility = View.GONE
        })
        viewModel.loadPopular()
    }

    private fun initCategory() {
        binding.progressBarCategory.visibility = View.VISIBLE
        viewModel.category.observe(this, Observer {
            binding.recyclerViewCategory.layoutManager =
                LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
            binding.recyclerViewCategory.adapter = CategoryAdapter(it)
            binding.progressBarCategory.visibility = View.GONE
        })
        viewModel.loadCategory()
    }
}
