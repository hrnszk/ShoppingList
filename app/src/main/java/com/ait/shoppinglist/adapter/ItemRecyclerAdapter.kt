package com.ait.shoppinglist.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ait.shoppinglist.R
import com.ait.shoppinglist.ScrollingActivity
import com.ait.shoppinglist.data.AppDatabase
import com.ait.shoppinglist.data.ShoppingItem
import com.ait.shoppinglist.data.ShoppingItem.Companion.BEVERAGES
import com.ait.shoppinglist.data.ShoppingItem.Companion.BREAD_BAKERY
import com.ait.shoppinglist.data.ShoppingItem.Companion.CANNED_GOODS
import com.ait.shoppinglist.data.ShoppingItem.Companion.CLEANERS
import com.ait.shoppinglist.data.ShoppingItem.Companion.DAIRY
import com.ait.shoppinglist.data.ShoppingItem.Companion.DRY_BAKING_GOODS
import com.ait.shoppinglist.data.ShoppingItem.Companion.FROZEN_FOODS
import com.ait.shoppinglist.data.ShoppingItem.Companion.MEAT
import com.ait.shoppinglist.data.ShoppingItem.Companion.OTHER
import com.ait.shoppinglist.data.ShoppingItem.Companion.PERSONAL_CARE
import com.ait.shoppinglist.data.ShoppingItem.Companion.PRODUCE
import com.ait.shoppinglist.databinding.ShoppingItemRowBinding
import com.ait.shoppinglist.touch.ItemTouchHelperCallback
import kotlin.concurrent.thread
import java.io.Serializable

class ItemRecyclerAdapter: ListAdapter<ShoppingItem, ItemRecyclerAdapter.ViewHolder>,
    ItemTouchHelperCallback {

    val context: Context

    constructor(context: Context) : super(ItemDiffCallback()) {
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemRowBinding = ShoppingItemRowBinding.inflate(
            LayoutInflater.from(context),
            parent, false)
        return ViewHolder(itemRowBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = getItem(holder.adapterPosition)
        holder.bind(currentItem)

        holder.itemRowBinding.btnDelete.setOnClickListener {
            deleteItem(holder.adapterPosition)
        }

        holder.itemRowBinding.btnDescription.setOnClickListener{

                Toast.makeText(this.context,
                    currentItem.details,
                    Toast.LENGTH_LONG).show()
            }

        holder.itemRowBinding.btnEdit.setOnClickListener {
            // Edit...
            (context as ScrollingActivity).showEditDialog(currentItem)
        }

        holder.itemRowBinding.cbHaveBought.setOnClickListener {
            currentItem.found = holder.itemRowBinding.cbHaveBought.isChecked
            thread {
                AppDatabase.getInstance(context).itemDao().updateItem(currentItem)
            }
        }
    }


    fun deleteItem(index: Int) {
        thread {
            AppDatabase.getInstance(context).itemDao().deleteItem(getItem(index))
        }
    }

    override fun onDismissed(position: Int) {
        deleteItem(position)
    }

    override fun onItemMoved(fromPosition: Int, toPosition: Int) {
        notifyItemMoved(fromPosition, toPosition)
    }

    inner class ViewHolder(val itemRowBinding: ShoppingItemRowBinding) : RecyclerView.ViewHolder(itemRowBinding.root) {
        fun bind(item: ShoppingItem) {
            itemRowBinding.tvDate.text = item.createDate
            itemRowBinding.cbHaveBought.isChecked = item.found
            itemRowBinding.tvName.text = item.name
            itemRowBinding.tvPrice.text = item.price.toString()
            if(item.categoryId == BEVERAGES)itemRowBinding.categoryImage.setImageResource(R.drawable.beverages)
            if(item.categoryId == BREAD_BAKERY)itemRowBinding.categoryImage.setImageResource(R.drawable.bread_bakery)
            if(item.categoryId == CANNED_GOODS)itemRowBinding.categoryImage.setImageResource(R.drawable.canned_goods)
            if(item.categoryId == CLEANERS)itemRowBinding.categoryImage.setImageResource(R.drawable.cleaners)
            if(item.categoryId == DAIRY)itemRowBinding.categoryImage.setImageResource(R.drawable.dairy)
            if(item.categoryId == DRY_BAKING_GOODS)itemRowBinding.categoryImage.setImageResource(R.drawable.dry_baking_goods)
            if(item.categoryId == FROZEN_FOODS)itemRowBinding.categoryImage.setImageResource(R.drawable.frozen_foods)
            if(item.categoryId == MEAT)itemRowBinding.categoryImage.setImageResource(R.drawable.meat)
            if(item.categoryId == OTHER)itemRowBinding.categoryImage.setImageResource(R.drawable.other)
            if(item.categoryId == PERSONAL_CARE)itemRowBinding.categoryImage.setImageResource(R.drawable.personal_care)
            if(item.categoryId == PRODUCE)itemRowBinding.categoryImage.setImageResource(R.drawable.produce)
        }
    }
}


class ItemDiffCallback : DiffUtil.ItemCallback<ShoppingItem>() {
    override fun areItemsTheSame(oldItem: ShoppingItem, newItem: ShoppingItem): Boolean {
        return oldItem.shoppingItemId == newItem.shoppingItemId
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: ShoppingItem, newItem: ShoppingItem): Boolean {
        return oldItem == newItem
    }
}