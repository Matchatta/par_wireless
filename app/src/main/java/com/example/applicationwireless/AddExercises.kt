package com.example.applicationwireless

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.applicationwireless.database.entity.Exercises
import com.example.applicationwireless.model.ExercisesViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_add_exercises.*
import java.text.SimpleDateFormat
import java.util.*

class AddExercises:  Fragment(){
    private lateinit var dataSource: ExercisesViewModel
    lateinit var act: EditText
    lateinit var calories: EditText
    lateinit var locationAct: EditText
    lateinit var noteAct: EditText
    lateinit var pic : ImageButton
    lateinit var saveBtn: Button
    val calendar: Calendar = Calendar.getInstance()
    val REQUEST_IMAGE_CAPTURE = 1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add_exercises, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataSource = MainActivity.exercisesDataSource
        act = name
        calories = cals
        locationAct = location
        noteAct = note
        pic = image
        saveBtn = save
        setUI()
    }
    private fun setUI(){
        pic.setOnClickListener {
            dispatchTakePictureIntent()
        }
        saveBtn.setOnClickListener {
            if(act.text.toString().isEmpty()||calories.text.isEmpty()||locationAct.text.isEmpty()||noteAct.text.isEmpty()){
                Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
            else{
                saveExercises()
            }

        }
    }
    private fun saveExercises(){
        val df = SimpleDateFormat("dd/MM/yyyy")
        val disposable = MainActivity.disposable
        val exercises= Exercises(null, act.text.toString(), df.format(calendar.time), calories.text.toString().toDouble(), noteAct.text.toString(),locationAct.text.toString(), duration.text.toString().toInt(),MainActivity.getUserInformation()!!.email)
        disposable.add(dataSource.insertExercises(exercises)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show()
                refresh()})
    }
    private fun refresh(){
        act.setText("")
        calories.setText("")
        locationAct.setText("")
        noteAct.setText("")
        pic.setImageDrawable(resources.getDrawable(R.drawable.ic_image_black_24dp))
    }
    //Take a picture
    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(activity!!.packageManager)?.also {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data!!.extras!!.get("data") as Bitmap
            pic.setImageBitmap(imageBitmap)
        }
    }


}