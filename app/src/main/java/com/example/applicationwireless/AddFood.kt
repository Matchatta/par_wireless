package com.example.applicationwireless

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.applicationwireless.database.entity.Food
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_add_food.*
import java.text.SimpleDateFormat
import java.util.*

class AddFood: Fragment() {
    val calendar: Calendar = Calendar.getInstance()
    private val REQUEST_IMAGE_CAPTURE = 1
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add_food, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        image.setOnClickListener {
            dispatchTakePictureIntent()
        }
        save.setOnClickListener {
            if(type.text.isEmpty()||name.text.isEmpty()||quantity.text.isEmpty()||cals.text.isEmpty()|| location.text.isEmpty()||note.text.isEmpty()){
                Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
            else{
                addFood()
            }

        }
    }
    private fun addFood(){
        val disposable = MainActivity.disposable
        val dataSource = MainActivity.foodDataSource
        if(view!= null){
            val type = type.text.toString()
            val food = name.text.toString()
            val q = quantity.text.toString().toInt()
            val cals = cals.text.toString().toDouble()
            val location = location.text.toString()
            val note = note.text.toString()
            val date = SimpleDateFormat("dd/MM/yyyy").format(calendar.time)
            val foods = Food(null, food, type, date, cals, note, location, q, MainActivity.getUserInformation()!!.email)
            disposable.add(dataSource.insertFood(foods)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe{
                        Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show()
                        clear()
                    })
        }
    }
    fun clear(){
        type.setText("")
        name.setText("")
        quantity.setText("")
        cals.setText("")
        location.setText("")
        note.setText("")
    }
    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(activity!!.packageManager)?.also {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            val imageBitmap = data!!.extras!!.get("data") as Bitmap
            image.setImageBitmap(imageBitmap)
        }
    }
}