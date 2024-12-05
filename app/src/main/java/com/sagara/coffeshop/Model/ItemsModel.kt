package com.sagara.coffeshop.Model

import android.os.Parcel
import android.os.Parcelable

data class ItemsModel(
    var title: String = "",
    var description: String = "",
    var picUrl: ArrayList<String> = ArrayList(), // Ubah tipe ke ArrayList<String>
    var sizePrices: Map<String, Double> = mapOf(), // Pastikan default-nya kosong
    var name:String="",
    var quantity:Int=0,
    var price: Double = 0.0,
    var rating: Double = 0.0,
    var numberInCart: Int = 0,
    var extra: String = ""
) : Parcelable {
    constructor(parcel: Parcel) : this(
        title = parcel.readString() ?: "",
        description = parcel.readString() ?: "",
        picUrl = parcel.createStringArrayList() ?: ArrayList(), // Baca sebagai ArrayList<String>
        sizePrices = parcel.readHashMap(ClassLoader.getSystemClassLoader()) as? Map<String, Double> ?: mapOf(),
        price = parcel.readDouble(),
        rating = parcel.readDouble(),
        numberInCart = parcel.readInt(),
        extra = parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(description)
        parcel.writeStringList(picUrl) // Tuliskan `picUrl` sebagai List
        parcel.writeMap(sizePrices) // Serialize `sizePrices`
        parcel.writeDouble(price)
        parcel.writeDouble(rating)
        parcel.writeInt(numberInCart)
        parcel.writeString(extra)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<ItemsModel> {
        override fun createFromParcel(parcel: Parcel): ItemsModel {
            return ItemsModel(parcel)
        }

        override fun newArray(size: Int): Array<ItemsModel?> {
            return arrayOfNulls(size)
        }
    }
}
