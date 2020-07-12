package com.pws.mov.sign_up

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import com.pws.mov.HomeScreenActivity
import com.pws.mov.R
import com.pws.mov.utils.*
import kotlinx.android.synthetic.main.activity_sign_up_photoscreen.*
import java.io.File
import java.util.*


class SignUpPhotoScreenActivity : AppCompatActivity(), PermissionListener {

    val REQUEST_IMAGE_CAPTURE = 1
    var statusAdd: Boolean = false
    lateinit var filePath: Uri

    //useStorageOnDatabase
    lateinit var storage: FirebaseStorage
    lateinit var storageReference: StorageReference
    lateinit var preferences: Preferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_photoscreen)

        preferences = Preferences(this)
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        tv_hello.text = "Selamat Datang\n" + intent.getStringExtra("nama")

        btn_uploadLater.setOnClickListener {
            val uploadLater = Intent(this, HomeScreenActivity::class.java)
            startActivity(uploadLater)
            finishAffinity()
        }

        img_Upload.setOnClickListener {
            if (statusAdd) {
                statusAdd = false
                btn_save.visibility = View.VISIBLE
                img_Upload.setImageResource(R.drawable.ic_btn_upload_pict)
                img_Profile.setImageResource(R.drawable.user_pic)
            } else {
                /* Dexter.withContext(this@SignUpPhotoScreenActivity)
                     .withPermission(Manifest.permission.CAMERA)
                     .withListener(this)
                     .check()*/
                ImagePicker.with(this)
                    .cameraOnly() //User can only capture image using Camera
                    .start()
            }
        }

        btn_save.setOnClickListener {
            if (filePath != null) {
                val progressDialog = ProgressDialog(this)
                progressDialog.setTitle("Uploading...")
                progressDialog.show()

                val ref = storageReference.child("images/" + UUID.randomUUID().toString())
                ref.putFile(filePath)
                    .addOnSuccessListener {
                        progressDialog.dismiss()
                        Toast.makeText(
                            this@SignUpPhotoScreenActivity,
                            "Uploaded",
                            Toast.LENGTH_SHORT
                        ).show()

                        ref.downloadUrl.addOnSuccessListener {
                            preferences.setValue("url", it.toString())
                        }

                        finishAffinity()
                        val intent = Intent(
                            this@SignUpPhotoScreenActivity,
                            HomeScreenActivity::class.java
                        )
                        startActivity(intent)

                    }
                    .addOnFailureListener { e ->
                        progressDialog.dismiss()
                        Toast.makeText(
                            this@SignUpPhotoScreenActivity,
                            "Failed " + e.message,
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                    .addOnProgressListener { taskSnapshot ->
                        val progress = 100.0 * taskSnapshot.bytesTransferred / taskSnapshot
                            .totalByteCount
                        progressDialog.setMessage("Uploaded " + progress.toInt() + "%")
                    }
            }

        }
    }

    override fun onPermissionGranted(response: PermissionGrantedResponse?) {
        //To change body of created functions use File | Settings | File Templates.
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }

    }

    override fun onPermissionRationaleShouldBeShown(
        permission: com.karumi.dexter.listener.PermissionRequest?,
        token: PermissionToken?
    ) {
        //To change body of created functions use File | Settings | File Templates.
    }

    override fun onPermissionDenied(response: PermissionDeniedResponse?) {
        Toast.makeText(
            this
            , "Camera Permission Required", Toast.LENGTH_LONG
        ).show()
    }

    //UpdateForOS5(Lolipop)Up
    /*@SuppressLint("MissingSuperCall")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            var bitmap = data?.extras?.get("data") as Bitmap
            statusAdd = true

            filePath = data.getData()!!
            Glide.with(this)
                .load(bitmap)
                .apply(RequestOptions.circleCropTransform())
                .into(img_Profile)

            btn_save.visibility = View.VISIBLE
            img_Upload.setImageResource(R.drawable.ic_btn_remove_pict)
        }
    }*/

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            statusAdd = true
            filePath = data?.data!!
            Glide.with(this)
                .load(filePath)
                .apply(RequestOptions.circleCropTransform())
                .into(img_Profile)
            Log.v("LogUploadPicture", "File Uri Upload" + filePath)

            btn_save.visibility = View.VISIBLE
            img_Upload.setImageResource(R.drawable.ic_btn_remove_pict)

        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onBackPressed() {
        Toast.makeText(this, "In Rush ? Just Click Button Upload Later", Toast.LENGTH_SHORT).show()
    }
}