package com.example.applicationwireless

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import de.hdodenhof.circleimageview.CircleImageView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_userprofile.*
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class UserActivity : Fragment() {
    private var profileImage: CircleImageView? = null
    var imageUri: Uri? = null
    val dataSource = MainActivity.exercisesDataSource
    val disposable = MainActivity.disposable
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_userprofile, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sign_out.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(activity, LoginActivity::class.java))
        }
        val date = SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().time)
            disposable.add(dataSource.getExercises(MainActivity.getUserInformation()!!.email)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe { it ->
                        val sto = it.filter { it.date == date }
                        val sum = sto.sumByDouble { it.calories }
                        setUI(sum)
                    })
    }
    private fun setUI(burned: Double){
        if(view != null) {
            Height.text = MainActivity.getUserInformation()!!.height.toString()
            Weight.text = MainActivity.getUserInformation()!!.weight.toString()
            name.text = MainActivity.getUserInformation()!!.name
            burn.text = burned.toString()
            profileImage = activity?.findViewById(R.id.Profile_image)

            if (MainActivity.getUserInformation()!!.image != null) {
                val image = Base64.decode(MainActivity.getUserInformation()!!.image, Base64.DEFAULT)
                profileImage?.setImageBitmap(BitmapFactory.decodeByteArray(image, 0, image.size))
            }
            profileImage?.setOnClickListener {
                val gallery = Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(gallery, PICK_IMAGE)
            }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            imageUri = data!!.data
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(activity?.contentResolver, imageUri)
                profileImage!!.setImageBitmap(bitmap)
                val user = MainActivity.getUserInformation()
                val stream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream)
                user!!.image = Base64.encodeToString(stream.toByteArray(), Base64.DEFAULT)
                MainActivity.disposable.add(MainActivity.userDataSource.updateUser(user)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe{
                            Toast.makeText(context, "Image is saved", Toast.LENGTH_SHORT).show()
                        })
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
    companion object {
        private const val PICK_IMAGE = 1


    }

}