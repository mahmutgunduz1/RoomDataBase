package com.mahmutgunduz.roomdatabaseexample.dB

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class UserData   (
    @ColumnInfo(name = "name")
    var name: String,
    @ColumnInfo(name = "surname")
    var surname: String,
    @ColumnInfo(name = "age")
    var age: Int,
    @ColumnInfo(name = "gender")
    var gender: String,




) {

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0


}