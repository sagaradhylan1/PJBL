package com.sagara.coffeshop.Activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.sagara.coffeshop.Model.ItemsModel
import com.sagara.coffeshop.R

class EditActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var itemNameEditText: EditText
    private lateinit var itemQuantityEditText: EditText
    private lateinit var itemPriceEditText: EditText
    private lateinit var saveButton: Button
    private lateinit var cancelButton: Button
    private lateinit var editBtn: ImageView

    private lateinit var item: ItemsModel

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        // Inisialisasi UI elements
        toolbar = findViewById(R.id.toolbar)
        itemNameEditText = findViewById(R.id.itemName)
        itemQuantityEditText = findViewById(R.id.itemQuantity)
        itemPriceEditText = findViewById(R.id.itemPrice)
        saveButton = findViewById(R.id.saveBtn)
        cancelButton = findViewById(R.id.cancelBtn)

        // Ambil data item yang dikirim lewat Intent
        item = intent.getParcelableExtra("item") ?: run {
            finish()  // Jika tidak ada item, tutup activity
            return
        }

        // Isi EditText dengan data item yang ada
        itemNameEditText.setText(item.title)
        itemQuantityEditText.setText(item.numberInCart.toString())
        itemPriceEditText.setText(item.price.toString())

        // Set toolbar title
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Edit Item Keranjang"

        // Save Button click listener
        saveButton.setOnClickListener {
            val itemName = itemNameEditText.text.toString()
            val itemQuantity = itemQuantityEditText.text.toString().toIntOrNull() ?: 0
            val itemPrice = itemPriceEditText.text.toString().toDoubleOrNull() ?: 0.0

            // Validasi input
            if (itemName.isEmpty() || itemQuantity <= 0 || itemPrice <= 0.0) {
                Toast.makeText(this, "Harap isi semua kolom dengan nilai yang valid", Toast.LENGTH_SHORT).show()
            } else {
                item.title = itemName
                item.numberInCart = itemQuantity
                item.price = itemPrice

                val resultIntent = Intent()
                resultIntent.putExtra("updatedItem", item)
                setResult(RESULT_OK, resultIntent)

                finish()  // Tutup EditActivity
            }
        }

        // Cancel Button click listener
        cancelButton.setOnClickListener {
            finish()  // Tutup EditActivity tanpa menyimpan
        }
    }
}
