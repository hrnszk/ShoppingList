package com.ait.shoppinglist.dialogue

import android.app.Dialog
import android.app.ProgressDialog.show
import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.ait.shoppinglist.R
import com.ait.shoppinglist.ScrollingActivity
import com.ait.shoppinglist.adapter.ItemRecyclerAdapter
import com.ait.shoppinglist.data.AppDatabase
import com.ait.shoppinglist.data.ShoppingItem
import com.ait.shoppinglist.databinding.ActivityScrollingBinding
import com.ait.shoppinglist.databinding.ItemDialogueBinding
import com.ait.shoppinglist.databinding.ShoppingItemRowBinding
import java.lang.Exception
import java.util.*
import kotlin.concurrent.thread

class ShoppingItemDialogue: DialogFragment() , AdapterView.OnItemSelectedListener {


    interface ItemHandler {
        fun itemCreated(newItem: ShoppingItem)

        fun itemUpdated(editedItem: ShoppingItem)
    }

    lateinit var itemHandler: ItemHandler

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is ItemHandler){
            itemHandler = context
        } else {
            throw RuntimeException(
                getString(R.string.not_implementing_interface))
        }
    }

    lateinit var itemDialogBinding: ItemDialogueBinding
    lateinit var shoppingItemRowBinding: ShoppingItemRowBinding


    var isEditMode = false


    override fun onCreateDialog(savedInstanceState: Bundle?) : Dialog {

        val dialogBuilder = AlertDialog.Builder(requireContext())

        if (arguments != null && requireArguments().containsKey(
                ScrollingActivity.KEY_ITEM_EDIT
            )
        ) {
            isEditMode = true
            dialogBuilder.setTitle(getString(R.string.edit_item))
        } else {
            isEditMode = false
            dialogBuilder.setTitle(getString(R.string.new_item))
        }

        itemDialogBinding = ItemDialogueBinding.inflate(layoutInflater)
        dialogBuilder.setView(itemDialogBinding.root)

        shoppingItemRowBinding = ShoppingItemRowBinding.inflate(layoutInflater)


        //

        val categoryAdapter = ArrayAdapter.createFromResource(
            this.requireContext(),
            R.array.category_array, android.R.layout.simple_spinner_item
        )
        categoryAdapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )
        itemDialogBinding.categorySpinner.adapter = categoryAdapter
        itemDialogBinding.categorySpinner.onItemSelectedListener = this



        if (isEditMode) {
            val itemToEdit =
                requireArguments().getSerializable(
                    ScrollingActivity.KEY_ITEM_EDIT
                ) as ShoppingItem

            itemDialogBinding.etItemText.setText(itemToEdit.name)
            itemDialogBinding.etPriceText.setText(itemToEdit.price.toString())
            itemDialogBinding.etDescriptionText.setText(itemToEdit.details.toString())
            itemDialogBinding.cbBought.isChecked = itemToEdit.found
        }

        dialogBuilder.setPositiveButton(getString(R.string.create)) { dialog, which ->
            // leave it empty
        }

        dialogBuilder.setNegativeButton(getString(R.string.cancel)) { dialog, which ->
        }

            return dialogBuilder.create()

    }

    override fun onResume() {
        super.onResume()

        val dialog = dialog as AlertDialog
        val positiveButton = dialog.getButton(Dialog.BUTTON_POSITIVE)

        positiveButton.setOnClickListener {
            if (itemDialogBinding.etPriceText.text.isEmpty() && itemDialogBinding.etItemText.text.isEmpty()){
            itemDialogBinding.etItemText.error = getString(R.string.field_cannot_be_empty)
            itemDialogBinding.etPriceText.error = getString(R.string.Price_empty)
            } else if(itemDialogBinding.etPriceText.text.isEmpty()){
                itemDialogBinding.etPriceText.error = getString(R.string.Price_empty)
            } else if(itemDialogBinding.etItemText.text.isEmpty()){
                itemDialogBinding.etItemText.error = getString(R.string.field_cannot_be_empty)
            } else if (itemDialogBinding.etItemText.text.isNotEmpty() && itemDialogBinding.etItemText.text.isNotEmpty()) {
                if (isEditMode) {
                    handleItemEdit()
                } else {
                    handleItemCreate()
                }
                dialog.dismiss()
            }

        }
    }


    private fun handleItemCreate() {
            itemHandler.itemCreated(
                ShoppingItem(
                    null,
                    Date(System.currentTimeMillis()).toString(),
                    itemDialogBinding.etDescriptionText.text.toString(),
                    itemDialogBinding.cbBought.isChecked,
                    itemDialogBinding.etItemText.text.toString(),
                    itemDialogBinding.etPriceText.text.toString().toFloat(),
                    itemDialogBinding.categorySpinner.selectedItemId.toInt()
                )
            )
    }

    private fun handleItemEdit() {
            val itemToEdit = (arguments?.getSerializable(
                ScrollingActivity.KEY_ITEM_EDIT
            ) as ShoppingItem).copy(
                name = itemDialogBinding.etItemText.text.toString(),
                details = itemDialogBinding.etDescriptionText.text.toString(),
                found = itemDialogBinding.cbBought.isChecked,
                price = itemDialogBinding.etPriceText.text.toString().toFloat(),
                categoryId = itemDialogBinding.categorySpinner.selectedItemId.toInt()
            )
            itemHandler.itemUpdated(itemToEdit)
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }

    override fun onItemSelected(parent: AdapterView<*>?,
                                view: View?, position: Int, id: Long) {
    }

}