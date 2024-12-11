package com.mahmutgunduz.roomdatabaseexample.view



import android.content.Intent

import android.database.sqlite.SQLiteException

import android.net.Uri

import android.os.Bundle

import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher

import androidx.appcompat.app.AppCompatActivity

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog

import com.mahmutgunduz.roomdatabaseexample.adapter.MyAdapter
import com.mahmutgunduz.roomdatabaseexample.dB.UserDao
import com.mahmutgunduz.roomdatabaseexample.dB.UserData
import com.mahmutgunduz.roomdatabaseexample.dB.UserDataBase
import com.mahmutgunduz.roomdatabaseexample.databinding.ActivityMainBinding
import com.mahmutgunduz.roomdatabaseexample.databinding.BottomSheetBinding
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers



class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private lateinit var db: UserDataBase
    private lateinit var dao: UserDao
    private lateinit var userData: UserData
    private lateinit var adapter: MyAdapter

    private lateinit var compositeDisposable: CompositeDisposable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val window = window
        window.statusBarColor =
            ContextCompat.getColor(this, com.mahmutgunduz.roomdatabaseexample.R.color.white)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR





        db = Room.databaseBuilder(
            applicationContext,
            UserDataBase::class.java,
            "userDataBase"
        )  .build()


        // Rxjav
        compositeDisposable = CompositeDisposable()
        dao = db.userDao()

        bottom()
        fetchAllUsers()


    }


    private fun bottom() {

        binding.floatingActionButton.setOnClickListener {
            val bottomSheetDialog = BottomSheetDialog(this@MainActivity)
            val bottomSheetBinding = BottomSheetBinding.inflate(layoutInflater)
            bottomSheetDialog.setContentView(bottomSheetBinding.root)
            BottomSheetBehavior.from(bottomSheetBinding.root.parent as View).state =
                BottomSheetBehavior.STATE_EXPANDED






            bottomSheetBinding.btnSave.setOnClickListener {
                // Verileri alıyoruz
                val name = bottomSheetBinding.etName.text.toString()
                val soyAd = bottomSheetBinding.etSurname.text.toString()
                val yas = bottomSheetBinding.etAge.text.toString()
                val cinsiyet = bottomSheetBinding.etGender.text.toString()



                if (name.isBlank() || soyAd.isBlank() || yas.isBlank() || cinsiyet.isBlank() ) {

                    Toast.makeText(
                        this,
                        "Tüm alanları doldurun",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    // UserData nesnesini oluşturuyoruz
                    val userData = UserData(
                        name,
                        soyAd,
                        yas.toInt(),
                        cinsiyet

                    )

                    // Veritabanına veri ekleme
                    compositeDisposable.add(
                        dao.insert(userData)
                            .subscribeOn(Schedulers.io()) // Veritabanı işlemi arka planda yapılacak
                            .observeOn(AndroidSchedulers.mainThread()) // İşlem tamamlandığında ana thread'de işlem yapılacak
                            .subscribe(
                                {
                                    // Başarı durumunda işlemler
                                    bottomSheetDialog.dismiss()
                                    fetchAllUsers() // Veritabanındaki tüm kullanıcıları göster
                                },
                                { hata ->
                                    // Hata durumunda mesaj göster
                                    when (hata) {
                                        is SQLiteException -> {
                                            Toast.makeText(
                                                this,
                                                "Veritabanı hatası oluştu.",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }


                                    }
                                }
                            )
                    )
                }
            }

            bottomSheetDialog.show()
        }

    }


    private fun fetchAllUsers() {
        compositeDisposable.add(
            dao.getAll()
                .subscribeOn(Schedulers.io()) // Sorgu arka planda çalışacak
                .observeOn(AndroidSchedulers.mainThread()) // Sonuç UI thread'inde işlenecek
                .subscribe(
                    { userList ->
                        handleResponse(userList) // Başarılı yanıt
                    },
                    { error ->
                        println("Hata laaaaan: ${error.message}")
                        println(error.message)
                        Toast.makeText(this, "Hata laaaaan: ${error.message}", Toast.LENGTH_SHORT)
                            .show()
                    }
                )
        )
    }



    private fun handleResponse(list: List<UserData>) {

        adapter = MyAdapter(list)
        binding.Rcv.layoutManager = LinearLayoutManager(this)
        binding.Rcv.adapter = adapter

        adapter.updateData(list)


        Toast.makeText(this, "Veriler güncellendi", Toast.LENGTH_SHORT).show()
    }


    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }



}
