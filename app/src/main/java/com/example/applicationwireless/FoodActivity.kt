package com.example.applicationwireless

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.applicationwireless.database.entity.Food
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.food_item.view.*
import kotlinx.android.synthetic.main.fragment_food.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class FoodActivity: Fragment() {
    private var foodList: List<Food> = listOf()
    private var date = arrayListOf<String>()
    private var month = arrayListOf<String>()
    private var year = arrayListOf<String>()
    private var completedDate = ""
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_food, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val disposable = MainActivity.disposable
        val dataSource = MainActivity.foodDataSource
        val date = SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().time)
        completedDate = date
        disposable.add(dataSource.getFood(MainActivity.getUserInformation()!!.email)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe{ it ->
                    foodList = it
                    setUI()
                    setRecycleView(ArrayList(it.filter { it.date == date }))
                })
    }

    private fun setUI() {
        if(view!= null){
            val format = SimpleDateFormat("dd/MM/yyyy")
            val calendar = Calendar.getInstance()
            calendar.firstDayOfWeek = Calendar.SUNDAY
            calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)
            for(i in 0 until 7){
                val splited = format.format(calendar.time).split("/")
                date.add(splited[0])
                month.add(splited[1])
                year.add(splited[2])
                calendar.add(Calendar.DAY_OF_MONTH, 1)
            }
            sun.text = getString(R.string.sun)+date[0]
            mon.text = getString(R.string.mon)+date[1]
            tue.text = getString(R.string.tue)+date[2]
            wed.text = getString(R.string.wed)+date[3]
            thu.text = getString(R.string.thu)+date[4]
            fri.text = getString(R.string.fri)+date[5]
            sat.text = getString(R.string.sat)+date[6]
            var i =0
            date_group.setOnCheckedChangeListener { _, checkedId ->
                when(checkedId){
                    sun.id->{
                        i =0
                    }
                    mon.id->{
                        i=1
                    }
                    tue.id->{
                        i=2
                    }
                    wed.id->{
                        i=3
                    }
                    thu.id->{
                        i=4
                    }
                    fri.id->{
                        i=5
                    }
                    sat.id->{
                        i=6
                    }
                }
                setRecycleView(ArrayList(foodList.filter { it.date == date[i]+"/"+month[i]+"/"+year[i] }))
                completedDate = date[i]+"/"+month[i]+"/"+year[i]
            }
            search.addTextChangedListener {
                setRecycleView(ArrayList(foodList.filter { it.name.contains(search.text.toString().toLowerCase())&& it.date == completedDate}))
            }
        }
    }

    private fun setRecycleView(foods: ArrayList<Food>){
        val foodAdapter = FoodAdapter(foods)
        if(view!= null){
            recycle_container.apply {
                layoutManager = GridLayoutManager(context,1, GridLayoutManager.VERTICAL, false)
                isNestedScrollingEnabled = false
                adapter = foodAdapter
                onFlingListener = null
            }
        }

    }
    class FoodAdapter(private val items: ArrayList<Food>): RecyclerView.Adapter<FoodAdapter.ViewHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.food_item, parent, false))
        }

        override fun getItemCount() = items.size

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bind(items[position])
        }

        class ViewHolder(itemsView: View): RecyclerView.ViewHolder(itemsView) {
            fun bind(food: Food) {
                itemView.apply {
                    name.text = food.name
                    cal.text = food.calories.toString()
                    setOnClickListener {
                        val builder = AlertDialog.Builder(context)
                        val disposable = MainActivity.disposable
                        val dataSource = MainActivity.foodDataSource
                        builder.setMessage(resources.getString(R.string.delete))
                        builder.setPositiveButton(resources.getString(R.string.yes)) { dialog, _ ->
                            disposable.add(dataSource.deleteFood(food)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe{Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show()
                                        dialog.dismiss()})
                        }
                        builder.setNegativeButton(resources.getString(R.string.no)) { dialog, _ ->
                            dialog.dismiss()
                        }
                        builder.show()
                    }
                }
            }
        }
    }
}


