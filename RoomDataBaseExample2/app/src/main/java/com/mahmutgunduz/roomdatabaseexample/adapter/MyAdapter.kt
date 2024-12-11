package com.mahmutgunduz.roomdatabaseexample.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mahmutgunduz.roomdatabaseexample.R
import com.mahmutgunduz.roomdatabaseexample.dB.UserData
import com.mahmutgunduz.roomdatabaseexample.databinding.RecyclerRowBinding
import com.squareup.picasso.Picasso

class MyAdapter(private var mylist: List<UserData>) : RecyclerView.Adapter<MyAdapter.ViewHolder>() {

    // ViewHolder Tanımı
    class ViewHolder(val binding: RecyclerRowBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // RecyclerRowBinding Oluşturuluyor
        val binding = RecyclerRowBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        // Listenin Boyutu Döndürülüyor
        return mylist.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Pozisyona Göre Veri Bağlama
        val currentUser = mylist[position]
        holder.binding.textViewName.text=("Adi :")+currentUser.name
        holder.binding.textViewAge.text=("Yas:")+currentUser.age.toString()
        holder.binding.textViewSurname.text=("Soy adi :")+currentUser.surname
        holder.binding.textGender.text=("Cinsiyet:")+currentUser.gender


        holder.binding.idTxt.text= currentUser.id.toString()







    }
    fun updateData(newList: List<UserData>) {
        this.mylist = newList // Yeni listeyi güncelle
        notifyDataSetChanged()  // RecyclerView'a değişiklikleri bildir
    }

}
