package com.sagara.coffeshop.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sagara.coffeshop.R
import com.sagara.coffeshop.databinding.ViewholderSizeBinding

class SizeAdapter(
    private val sizeList: List<String>, // Perbaiki deklarasi sizeList
    private val onSizeSelected: (String) -> Unit // Callback untuk ukuran yang dipilih
) : RecyclerView.Adapter<SizeAdapter.Viewholder>() {

    private var selectPosition = -1 // Posisi terpilih saat ini
    private var lastSelectedPosition = -1 // Posisi terakhir yang dipilih

    inner class Viewholder(val binding: ViewholderSizeBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(size: String) {
            binding.sizeText.text = size

            // Set klik listener untuk ukuran
            binding.root.setOnClickListener {
                onSizeSelected(size) // Panggil callback saat ukuran dipilih
                lastSelectedPosition = selectPosition
                selectPosition = adapterPosition
                notifyItemChanged(lastSelectedPosition)
                notifyItemChanged(selectPosition)
            }

            // Ganti warna latar belakang berdasarkan seleksi
            if (selectPosition == adapterPosition) {
                binding.img.setBackgroundResource(R.drawable.orange_bg)
            } else {
                binding.img.setBackgroundResource(R.drawable.size_bg)
            }

            // Menentukan gambar dan ukuran berdasarkan posisi
            val imageSize = when (adapterPosition) {
                0 -> 45.dpToPx(binding.root.context)
                1 -> 50.dpToPx(binding.root.context)
                2 -> 55.dpToPx(binding.root.context)
                3 -> 65.dpToPx(binding.root.context)
                else -> 70.dpToPx(binding.root.context)
            }

            val layoutParams = binding.img.layoutParams
            layoutParams.width = imageSize
            layoutParams.height = imageSize
            binding.img.layoutParams = layoutParams

            // Pastikan gambar terlihat dengan benar sesuai ukuran
            val imageResource = when (size) {
                "Small" -> R.drawable.small_coffee // Gambar kecil
                "Medium" -> R.drawable.medium_coffee // Gambar sedang
                "Large" -> R.drawable.large_coffee // Gambar besar
                else -> R.drawable.coffee // Gambar default
            }

            // Gunakan Glide untuk memuat gambar sesuai dengan ukuran yang dipilih
            Glide.with(binding.root.context)
                .load(imageResource)
                .into(binding.img)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Viewholder {
        val binding = ViewholderSizeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Viewholder(binding)
    }

    override fun onBindViewHolder(holder: Viewholder, position: Int) {
        holder.bind(sizeList[position])
    }

    override fun getItemCount(): Int = sizeList.size

    private fun Int.dpToPx(context: Context): Int {
        return (this * context.resources.displayMetrics.density).toInt()
    }
}

