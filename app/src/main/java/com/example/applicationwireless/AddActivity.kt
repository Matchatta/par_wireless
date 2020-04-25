package com.example.applicationwireless

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.fragment.app.Fragment

class AddActivity: Fragment() {
    lateinit var radioGroup: RadioGroup
    lateinit var radioButtonMeal: RadioButton
    lateinit var radioButtonExercises: RadioButton
    lateinit var containerLayout: FrameLayout
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        radioGroup = activity!!.findViewById(R.id.choice)
        radioButtonMeal = activity!!.findViewById(R.id.meal)
        radioButtonExercises = activity!!.findViewById(R.id.exercises)
        openFragment(AddFood())
        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when(activity!!.findViewById<RadioButton>(checkedId)){
                radioButtonExercises -> {
                    openFragment(AddExercises())
                }
                radioButtonMeal->{
                    openFragment(AddFood())
                }

            }
        }
    }
    fun openFragment(fragment: Fragment){
        val fragmentManager = childFragmentManager
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.choice_container, fragment)
        transaction.commit()
    }
}