package com.example.planer.ui.products

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.text.Html
import android.text.InputType
import android.util.Log
import android.view.*
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.planer.R
import com.example.planer.adapters.GroupRecyclerAdapter
import com.example.planer.adapters.ProductsListAdapter
import com.example.planer.database.entity.GroupAndAllTasks
import com.example.planer.database.entity.GroupTask
import com.example.planer.database.entity.ListAndAllProducts
import com.example.planer.database.entity.ProductList
import com.example.planer.database.viewModel.GroupViewModel
import com.example.planer.database.viewModel.ProductListViewModel
import com.example.planer.util.InfoDialog
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_food.view.*
import kotlinx.android.synthetic.main.fragment_group_recycler.view.*
import kotlinx.android.synthetic.main.fragment_group_recycler.view.button_add_item
import kotlinx.android.synthetic.main.fragment_group_recycler.view.group_recycler_view
import kotlinx.android.synthetic.main.fragment_task_recycler.view.*


class FoodFragment : Fragment(), ProductsListAdapter.OnItemClickListener
{
    private val productListViewModel: ProductListViewModel by viewModels()
    private lateinit var foodViewModel: FoodViewModel
    private var lists: List<ListAndAllProducts>? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        foodViewModel = ViewModelProvider(this).get(FoodViewModel::class.java)
        val view = inflater.inflate(R.layout.fragment_food, container, false)

        val adapter = this.context?.let { ProductsListAdapter(it, lists, this) }
        val list = view.product_list_view
        list.adapter = adapter

        val decoration = DividerItemDecoration(this.context, DividerItemDecoration.HORIZONTAL)
        decoration.setDrawable(activity?.applicationContext?.let { ContextCompat.getDrawable(it, R.color.white) }!!)
        list.addItemDecoration(decoration)

        val buttonAddItem: FloatingActionButton = view.button_add_item
        buttonAddItem.setOnClickListener{
            addProductList()
        }

        productListViewModel.listWithProducts().observe(
                viewLifecycleOwner, {
                if (it != null) {
                    this.lists = it
                    adapter?.setList(it)
                    list.adapter = adapter
                }
            }
        )

        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, viewHolder2: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDirection: Int) {
                val id = lists?.get(viewHolder.adapterPosition)?.list?.product_list_id
                val productList =  lists?.find { list -> list.list.product_list_id == id}?.list
                Log.d("productList", productList?.product_list_id.toString())
                val myClickListener: DialogInterface.OnClickListener = DialogInterface.OnClickListener { _, which ->
                    when (which) {
                        Dialog.BUTTON_POSITIVE -> {
                            productList?.let { productListViewModel.delete(it) }
                        }
                        Dialog.BUTTON_NEGATIVE -> {
                            lists?.let { adapter?.setList(it) }
                            list.adapter = adapter
                        }
                    }
                }

                context?.let { InfoDialog.onCreateConfirmDialog(it, "Удаление", "Удалить список покупок \"${productList?.title}\"?", R.drawable.delete_red, myClickListener)}
            }
        }

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(list)

        initUI()

        return view
    }

    private fun addProductList(){
        val builder: AlertDialog.Builder = AlertDialog.Builder(this.context)
        builder.setTitle("Новый список покупок")

        val input = EditText(this.context)

        input.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(input)

        builder.setPositiveButton("Сохранить") { _, _ ->
            productListViewModel.insert(ProductList(input.text.toString()))
        }
        builder.setNegativeButton("Назад") { dialog, _ -> dialog.cancel() }

        builder.show()
    }

    private fun initUI(){
        val navView = activity?.findViewById<BottomNavigationView>(R.id.nav_view)
        navView?.itemTextColor = this.context?.let { ContextCompat.getColorStateList(it, R.color.red) }
        (activity as AppCompatActivity).supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#D91E18")))
        (activity as AppCompatActivity).supportActionBar?.title = Html.fromHtml("<font color=\"#F2F1EF\">" + "Списки покупок" + "</font>")
    }

    override fun onItemClick(position: Int) {
        val bundle = Bundle().apply { putSerializable("list", lists?.get(position)) }
        this.view?.let { Navigation.findNavController(it).navigate(R.id.products_list, bundle) }
    }
}