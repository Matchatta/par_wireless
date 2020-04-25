package com.example.applicationwireless

//import com.google.firebase.auth.FirebaseAuth
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.applicationwireless.database.entity.User
import com.example.applicationwireless.model.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseUser
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MainActivity : AppCompatActivity() {
    private lateinit var userModelFactory: UserModelFactory
    private val userModel: UserViewModel by viewModels { userModelFactory }
    private lateinit var foodModelFactory: FoodModelFactory
    private val foodModel: FoodViewModel by viewModels { foodModelFactory }
    private lateinit var exercisesFactory: ExercisesModelFactory
    private val exercisesModel: ExercisesViewModel by viewModels { exercisesFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val navigation = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        navigation.setOnNavigationItemSelectedListener(onNavigatedItemSelectedListener)
        userModelFactory = Injection.provideUserModelFactory(this)
        foodModelFactory = Injection.provideFoodModelFactory(this)
        exercisesFactory = Injection.provideExercisesModelFactory(this)
        userDataSource = userModel
        exercisesDataSource = exercisesModel
        foodDataSource = foodModel
        loadUser()
    }
    private fun setUI(){
            loadFragment(HomeActivity())
    }
    private val onNavigatedItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { items->
        when(items.itemId){
            R.id.summary->{
                loadFragment(HomeActivity())
                return@OnNavigationItemSelectedListener true
            }
            R.id.exercises->{
                loadFragment(ExercisesActivity())
                return@OnNavigationItemSelectedListener true
            }
            R.id.add->{
                loadFragment(AddActivity())
                return@OnNavigationItemSelectedListener true
            }
            R.id.food->{
                loadFragment(FoodActivity())
                return@OnNavigationItemSelectedListener true
            }
            R.id.profile->{
                loadFragment(UserActivity())
                return@OnNavigationItemSelectedListener true
            }
            else -> false
        }
    }
    private fun loadUser(){
        disposable.add(userModel.getUser(user.email.toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { setUserInformation(it)
                    setUI()})
    }
    private fun loadFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.commit()
    }
    companion object{
        val disposable = CompositeDisposable()
        lateinit var userDataSource : UserViewModel
        lateinit var exercisesDataSource: ExercisesViewModel
        lateinit var foodDataSource: FoodViewModel
        lateinit var user: FirebaseUser
        private var userInformation: User? =null
        fun setFirebaseUser(user: FirebaseUser): Class<MainActivity>{
            this.user = user
            return MainActivity::class.java
        }
        fun setUserInformation(user: User){
            userInformation = user
        }
        fun getUserInformation(): User?{
            return userInformation
        }
    }
}