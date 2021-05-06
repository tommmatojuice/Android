package com.example.planer.ui.products

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.*
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.planer.R
import com.example.planer.adapters.ProductsAdapter
import com.example.planer.database.entity.ListAndAllProducts
import com.example.planer.database.entity.Product
import com.example.planer.database.viewModel.ProductListViewModel
import com.example.planer.database.viewModel.ProductViewModel
import com.example.planer.util.InfoDialog
import com.example.planer.util.ToastMessages
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.add_product.view.*
import kotlinx.android.synthetic.main.fragment_add_fixed_task.*
import kotlinx.android.synthetic.main.fragment_add_one_time_work_task.*
import kotlinx.android.synthetic.main.fragment_products_recycler.view.*
import kotlin.math.cos


class ProductsRecyclerFragment : Fragment(), ProductsAdapter.OnItemClickListener
{
    private val productViewModel: ProductViewModel by viewModels()
    private val productListViewModel: ProductListViewModel by viewModels()
    private var listAndAllProducts: ListAndAllProducts? = null
    private var adapter: ProductsAdapter? = null
    private lateinit var list: RecyclerView
    private var products: List<Product>? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        val view = inflater.inflate(R.layout.fragment_products_recycler, container, false)

        listAndAllProducts = arguments?.getSerializable("list") as ListAndAllProducts
        products = listAndAllProducts?.products

        (activity as AppCompatActivity).supportActionBar?.title = listAndAllProducts?.list?.title
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)

        adapter = this.context?.let { ProductsAdapter(it, listAndAllProducts?.products, this, productViewModel) }
        list = view.products_recycler_view
        list.adapter = adapter

        val decoration = DividerItemDecoration(this.context, DividerItemDecoration.HORIZONTAL)
        decoration.setDrawable(activity?.applicationContext?.let { ContextCompat.getDrawable(it, R.color.white) }!!)
        list.addItemDecoration(decoration)

        val buttonAddItem: FloatingActionButton = view.button_add_item
        buttonAddItem.setOnClickListener{
            addProduct(null)
        }

        initUi()

        listAndAllProducts?.list?.product_list_id?.let {
            productViewModel.productsByList(it).observe(
                viewLifecycleOwner
            ) { products ->
                if (products != null) {
                    this.products = products
                    view.all_cost.text = "${products.sumOf { it.count * it.cost }.toString()} руб."
                    adapter?.setList(products)
                    list.adapter = adapter
                }
            }
        }

        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, viewHolder2: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDirection: Int) {
                val product = products?.find { product -> product.product_id == products?.get(viewHolder.adapterPosition)?.product_id }

                val myClickListener: DialogInterface.OnClickListener = DialogInterface.OnClickListener { _, which ->
                    when (which) {
                        Dialog.BUTTON_POSITIVE -> {
                            if (product != null) {
                                productViewModel.delete(product)
                            }
                        }
                        Dialog.BUTTON_NEGATIVE -> {
                            products?.let { adapter?.setList(it) }
                            list.adapter = adapter
                        }
                    }
                }

                context?.let { InfoDialog.onCreateConfirmDialog(it, "Удаление", "Удалить продукт \"${product?.name}\"?", R.drawable.delete_red, myClickListener)}
            }
        }

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(list)

        return view
    }

    private fun initUi(){
        val navView = activity?.findViewById<BottomNavigationView>(R.id.nav_view)
        navView?.itemTextColor = this.context?.let { ContextCompat.getColorStateList(it, R.color.red) }
        (activity as AppCompatActivity).supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#D91E18")))
    }

    private fun addProduct(product: Product?)
    {
        val li = LayoutInflater.from(context)
        val addProductView: View = li.inflate(R.layout.add_product, null)

        val mDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(context)

        //Настраиваем prompt.xml для нашего AlertDialog:
        mDialogBuilder.setView(addProductView)

        //Настраиваем отображение поля для ввода текста в открытом диалоге:
        val titleInput = addProductView.input_title
        val countInput = addProductView.input_count
        val costInput = addProductView.input_cost

        if(product != null){
            titleInput.setText(product.name)
            costInput.setText(product.cost.toString())
            countInput.setText(product.count.toString())
        }

        //Настраиваем сообщение в диалоговом окне:
        mDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Сохранить")
                { dialog, _ -> //Вводим текст и отображаем в строке ввода на основном экране:
                    if(titleInput.text.isNullOrEmpty() || countInput.text.isNullOrEmpty()){
                        this.context?.let { ToastMessages.showMessage(it, "Поля \"Название\" и \"Количетво\" являются обазательными!") }
                    } else {
                        val cost = if(costInput.text.isNullOrEmpty()){
                            0.0
                        } else {
                            costInput.text.toString().toDouble()
                        }
                        if(product == null){
                            listAndAllProducts?.list?.product_list_id?.let {
                                Product(titleInput.text.toString(), countInput.text.toString().toDouble(),
                                        cost, false, it)
                            }?.let { productViewModel.insert(it) }
                        } else {
                            product.name = titleInput.text.toString()
                            product.count = countInput.text.toString().toDouble()
                            product.cost = cost
                            productViewModel.update(product)
                        }
                    }
                }
                .setNegativeButton("Отмена") { dialog, _ -> dialog.cancel() }

        val alertDialog: AlertDialog = mDialogBuilder.create()

        alertDialog.show()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater)
    {
        inflater.inflate(R.menu.edit_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean
    {
        return when (item.itemId) {
            R.id.edit_item -> {
                showDialog()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showDialog(){
        val builder: AlertDialog.Builder = AlertDialog.Builder(this.context)
        builder.setTitle("Изменить список")

        val input = EditText(this.context)

        input.inputType = InputType.TYPE_CLASS_TEXT
        input.setText(this.listAndAllProducts?.list?.title)
        builder.setView(input)

        builder.setPositiveButton("Сохранить") { _, _ ->
            this.listAndAllProducts?.list?.title = input.text.toString()
            this.listAndAllProducts?.list?.let { productListViewModel.update(it) }
            (activity as AppCompatActivity).supportActionBar?.title = this.listAndAllProducts?.list?.title
        }
        builder.setNegativeButton("Назад") { dialog, _ -> dialog.cancel() }

        builder.show()
    }

    override fun onItemClick(position: Int) {
        addProduct(products?.get(position))
    }
}