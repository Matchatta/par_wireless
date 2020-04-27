package com.example.applicationwireless

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.applicationwireless.database.entity.Exercises
import com.example.applicationwireless.database.entity.Food
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_summary.*
import java.text.SimpleDateFormat
import java.util.*

class HomeActivity : Fragment() {
    val food = mutableListOf<Food>()
    val exercises = mutableListOf<Exercises>()
    val calendar = Calendar.getInstance()
    var date = SimpleDateFormat("dd/MM/yyyy").format(calendar.time)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_summary, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadData()
        setDatePicker()
        setClick()
    }

    private fun setClick() {
        daily.setOnClickListener {
            setUI(food.filter { it1->it1.date == date }, exercises.filter { it1->it1.date == date }, 1.0)
        }
        monthly.setOnClickListener {
            val c = Calendar.getInstance().apply {
                set(date.split("/")[0].toInt()
                        , date.split("/")[1].toInt()
                        , date.split("/")[2].toInt())
            }
            val mul = c.getActualMaximum(Calendar.DAY_OF_MONTH)
            setUI(food.filter { it1->it1.date.split("/")[1] == date.split("/")[1] }, exercises.filter { it1->it1.date.split("/")[1] == date.split("/")[1] }, mul.toDouble())
        }
        weekly.setOnClickListener {
            val c : Calendar = Calendar.getInstance()
            val d : Calendar = Calendar.getInstance()
            val date = this.date.split("/")
            c.set(date[2].toInt(), date[1].toInt(), date[0].toInt())
            setUI(food.filter { it1-> d.set(it1.date.split("/")[2].toInt(), it1.date.split("/")[1].toInt(), it1.date.split("/")[0].toInt())
                d.get(Calendar.WEEK_OF_YEAR)== c.get(Calendar.WEEK_OF_YEAR)},
                    exercises.filter { it1-> d.set(it1.date.split("/")[2].toInt(), it1.date.split("/")[1].toInt(), it1.date.split("/")[0].toInt())
                        d.get(Calendar.WEEK_OF_YEAR)== c.get(Calendar.WEEK_OF_YEAR)}
                    , 7.0 )
        }
    }

    private fun setDatePicker() {
        val calendar: Calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DATE)
        date_picker.setOnClickListener {
            context?.let {
                DatePickerDialog(
                        it,
                        DatePickerDialog.OnDateSetListener { _, mYear, mMonth, dayOfMonth ->
                            calendar.set(mYear, mMonth, dayOfMonth)
                            date = SimpleDateFormat("dd/MM/yyyy").format(calendar.time)
                            //setData(ArrayList(filter))
                            setUI(food.filter { it1->it1.date == date }, exercises.filter { it1->it1.date == date }, 1.0)
                        }, year, month, day
                ).show()
            }
        }
    }

    private fun setUI(f: List<Food>, e: List<Exercises>, mul: Double) {
            }
        }
    }

    private fun loadData(){
        val foodDataSource = MainActivity.foodDataSource
        val exercisesDataSource = MainActivity.exercisesDataSource
        val email = MainActivity.getUserInformation()!!.email
        val disposable = MainActivity.disposable
        disposable.add(foodDataSource.getFood(email)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {food.addAll(it)})
        disposable.add(exercisesDataSource.getExercises(email)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {exercises.addAll(it)
                    setUI(food.filter { it1->it1.date == date }, exercises.filter { it1->it1.date == date }, 1.0)})
    }
}