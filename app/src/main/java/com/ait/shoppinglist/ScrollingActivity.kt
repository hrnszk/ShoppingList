package com.ait.shoppinglist

import android.os.Bundle
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import com.ait.shoppinglist.adapter.ItemRecyclerAdapter
import com.ait.shoppinglist.databinding.ActivityScrollingBinding
import com.ait.shoppinglist.data.AppDatabase
import com.ait.shoppinglist.data.ShoppingItem
import com.ait.shoppinglist.touch.ItemRecyclerTouchCallback
import com.ait.shoppinglist.dialogue.ShoppingItemDialogue
import com.ait.shoppinglist.touch.ItemTouchHelperCallback
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt
import kotlin.concurrent.thread


class ScrollingActivity : AppCompatActivity(), ShoppingItemDialogue.ItemHandler {

    companion object {
        const val KEY_ITEM_EDIT = "KEY_ITEM_EDIT"
        const val PREF_DEFAULT = "PREF_DEFAULT"
        const val KEY_STARTED = "KEY_STARTED"
    }

    private lateinit var binding: ActivityScrollingBinding
    private lateinit var adapter: ItemRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityScrollingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(findViewById(R.id.toolbar))
        binding.toolbarLayout.title = title
        binding.btnAdd.setOnClickListener { view ->
            ShoppingItemDialogue().show(supportFragmentManager, "ITEM_DIALOG")
        }
        binding.btnDeleteAll.setOnClickListener{
            thread {
                AppDatabase.getInstance(this@ScrollingActivity).itemDao().deleteAll()
            }
        }

        initItemRecyclerView()

        if (!wasAlreadyStarted()) {
            MaterialTapTargetPrompt.Builder(this@ScrollingActivity)
                .setTarget(binding.btnAdd)
                .setPrimaryText(getString(R.string.new_shopping_item))
                .setSecondaryText(getString(R.string.initial_instruction))
                .show()

            saveThatAppWasStarted()
        }
    }

    private fun initItemRecyclerView() {
        adapter = ItemRecyclerAdapter(this)
        binding.recyclerItem.adapter = adapter

        var liveDataItems = AppDatabase.getInstance(this).itemDao().getAllItem()
        liveDataItems.observe(this, Observer { items ->
            adapter.submitList(items)
        })

        val touchCallbackList = ItemRecyclerTouchCallback(adapter)
        val itemTouchHelper = ItemTouchHelper(touchCallbackList)
        itemTouchHelper.attachToRecyclerView(binding.recyclerItem)
    }

    private fun saveThatAppWasStarted() {
        val sharedPref = getSharedPreferences(PREF_DEFAULT, MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putBoolean(KEY_STARTED, true)
        editor.commit()
    }

    private fun wasAlreadyStarted() =
        getSharedPreferences(PREF_DEFAULT, MODE_PRIVATE).getBoolean(
            KEY_STARTED, false)




    public fun showEditDialog(itemToEdit: ShoppingItem) {
        val editDialog = ShoppingItemDialogue()

        val bundle = Bundle()
        bundle.putSerializable(KEY_ITEM_EDIT, itemToEdit)
        editDialog.arguments = bundle

        editDialog.show(supportFragmentManager, "TAG_ITEM_EDIT")
    }


    override fun itemCreated(newItem: ShoppingItem) {
        thread {
            AppDatabase.getInstance(this).itemDao().addItem(newItem)
        }
    }

    override fun itemUpdated(editedItem: ShoppingItem) {
        // update the in the Database
        thread {
            AppDatabase.getInstance(this).itemDao().updateItem(editedItem)
        }
    }
}