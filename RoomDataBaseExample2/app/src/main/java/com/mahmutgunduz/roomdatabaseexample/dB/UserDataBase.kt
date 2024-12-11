package com.mahmutgunduz.roomdatabaseexample.dB

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [UserData::class], version = 3)
abstract class UserDataBase : RoomDatabase() {
    abstract fun userDao(): UserDao

}