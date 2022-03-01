package com.ait.shoppinglist.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ShoppingItemDao {

    @Query("SELECT * FROM shopping_item")
    fun getAllItem() : LiveData<List<ShoppingItem>>

    @Query("DELETE FROM shopping_item")
    fun deleteAll()

    @Insert
    fun addItem(item: ShoppingItem) : Long

    @Delete
    fun deleteItem(item: ShoppingItem)

    @Update
    fun updateItem(item: ShoppingItem)

}