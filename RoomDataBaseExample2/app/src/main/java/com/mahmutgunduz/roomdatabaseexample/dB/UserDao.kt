package com.mahmutgunduz.roomdatabaseexample.dB

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable

@Dao
interface UserDao {

    @Insert
    fun insert(userData: UserData) : Completable

    @Delete
    fun delete (userData: UserData): Completable

    @Query("SELECT * FROM UserData")
    fun getAll(): Flowable< List<UserData>>



}