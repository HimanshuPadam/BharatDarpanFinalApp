package com.example.bhartiye_jangannah

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.ContentValues
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask

class EditProfilePage : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseUser: FirebaseUser
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference
    private lateinit var storageReference: StorageReference
    private val storagepath = "Users_Profile_Cover_image/"
    private lateinit var uid: String
    private lateinit var set: ImageView
    private lateinit var profilepic: TextView
    private lateinit var editname: TextView
    private lateinit var editpassword: TextView
    private lateinit var pd: ProgressDialog
    private val CAMERA_REQUEST = 100
    private val STORAGE_REQUEST = 200
    private val IMAGEPICK_GALLERY_REQUEST = 300
    private val IMAGE_PICKCAMERA_REQUEST = 400
    private lateinit var imageuri: Uri
    private lateinit var profileOrCoverPhoto: String
    private lateinit var cameraPermission: Array<String>
    private lateinit var storagePermission: Array<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile_page)

        profilepic = findViewById(R.id.profilepic)
        editname = findViewById(R.id.editname)
        set = findViewById(R.id.setting_profile_image)
        pd = ProgressDialog(this)
        pd.setCanceledOnTouchOutside(false)
        editpassword = findViewById(R.id.changepassword)
        firebaseAuth = FirebaseAuth.getInstance()
        firebaseUser = firebaseAuth.currentUser!!
        firebaseDatabase = FirebaseDatabase.getInstance()
        storageReference = FirebaseStorage.getInstance().reference
        databaseReference = firebaseDatabase.getReference("users")
        cameraPermission = arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        storagePermission = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        val query: Query = databaseReference.orderByChild("email").equalTo(firebaseUser.email)
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (dataSnapshot1 in dataSnapshot.children) {
                    val image = "" + dataSnapshot1.child("image").value
                    try {
                        Glide.with(this@EditProfilePage).load(image).into(set)
                    } catch (e: Exception) {
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })

        editpassword.setOnClickListener {
            pd.setMessage("Changing Password")
            showPasswordChangeDailog()
        }

        profilepic.setOnClickListener {
            pd.setMessage("Updating Profile Picture")
            profileOrCoverPhoto = "image"
            showImagePicDialog()
        }

        editname.setOnClickListener {
            pd.setMessage("Updating Name")
            showNamephoneupdate("name")
        }
    }

    override fun onPause() {
        super.onPause()
        val query: Query = databaseReference.orderByChild("email").equalTo(firebaseUser.email)
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (dataSnapshot1 in dataSnapshot.children) {
                    val image = "" + dataSnapshot1.child("image").value
                    try {
                        Glide.with(this@EditProfilePage).load(image).into(set)
                    } catch (e: Exception) {
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
        editpassword.setOnClickListener {
            pd.setMessage("Changing Password")
            showPasswordChangeDailog()
        }
    }

    override fun onStart() {
        super.onStart()
        val query: Query = databaseReference.orderByChild("email").equalTo(firebaseUser.email)
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (dataSnapshot1 in dataSnapshot.children) {
                    val image = "" + dataSnapshot1.child("image").value
                    try {
                        Glide.with(this@EditProfilePage).load(image).into(set)
                    } catch (e: Exception) {
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
        editpassword.setOnClickListener {
            pd.setMessage("Changing Password")
            showPasswordChangeDailog()
        }
    }

    private fun checkStoragePermission(): Boolean {
        val result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
        return result
    }

    private fun requestStoragePermission() {
        requestPermissions(storagePermission, STORAGE_REQUEST)
    }

    private fun checkCameraPermission(): Boolean {
        val result = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
        val result1 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
        return result && result1
    }

    private fun requestCameraPermission() {
        requestPermissions(cameraPermission, CAMERA_REQUEST)
    }

    private fun showPasswordChangeDailog() {
        val view: View = LayoutInflater.from(this).inflate(R.layout.dialog_update_password, null)
        val oldpass = view.findViewById<EditText>(R.id.oldpasslog)
        val newpass = view.findViewById<EditText>(R.id.newpasslog)
        val editpass = view.findViewById<Button>(R.id.updatepass)
        val builder = AlertDialog.Builder(this)
        builder.setView(view)
        val dialog: AlertDialog = builder.create()
        dialog.show()
        editpass.setOnClickListener {
            val oldp = oldpass.text.toString().trim()
            val newp = newpass.text.toString().trim()
            if (TextUtils.isEmpty(oldp)) {
                Toast.makeText(this@EditProfilePage, "Current Password cant be empty", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(newp)) {
                Toast.makeText(this@EditProfilePage, "New Password cant be empty", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            dialog.dismiss()
            updatePassword(oldp, newp)
        }
    }

    private fun updatePassword(oldp: String, newp: String) {
        pd.show()
        val user = firebaseAuth.currentUser
        val authCredential: AuthCredential = EmailAuthProvider.getCredential(user!!.email!!, oldp)
        user.reauthenticate(authCredential)
            .addOnSuccessListener {
                user.updatePassword(newp)
                    .addOnSuccessListener {
                        pd.dismiss()
                        Toast.makeText(this@EditProfilePage, "Changed Password", Toast.LENGTH_LONG).show()
                    }
                    .addOnFailureListener {
                        pd.dismiss()
                        Toast.makeText(this@EditProfilePage, "Failed", Toast.LENGTH_LONG).show()
                    }
            }
            .addOnFailureListener {
                pd.dismiss()
                Toast.makeText(this@EditProfilePage, "Failed", Toast.LENGTH_LONG).show()
            }
    }

    private fun showNamephoneupdate(key: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Update$key")
        val layout = LinearLayout(this)
        layout.orientation = LinearLayout.VERTICAL
        layout.setPadding(10, 10, 10, 10)
        val editText = EditText(this)
        editText.hint = "Enter$key"
        layout.addView(editText)
        builder.setView(layout)
        builder.setPositiveButton("Update") { dialog, which ->
            val value = editText.text.toString().trim()
            if (!TextUtils.isEmpty(value)) {
                pd.show()
                val result: MutableMap<String, Any> = HashMap()
                result[key] = value
                databaseReference.child(firebaseUser.uid).updateChildren(result)
                    .addOnSuccessListener {
                        pd.dismiss()
                        Toast.makeText(this@EditProfilePage, " updated ", Toast.LENGTH_LONG).show()
                    }
                    .addOnFailureListener {
                        pd.dismiss()
                        Toast.makeText(this@EditProfilePage, "Unable to update", Toast.LENGTH_LONG).show()
                    }
                if (key == "name") {
                    val databaser = FirebaseDatabase.getInstance().getReference("Posts")
                    uid= FirebaseAuth.getInstance().currentUser?.uid.toString()
                    val query: Query = databaser.orderByChild("uid").equalTo(uid)
                    query.addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            for (dataSnapshot1 in dataSnapshot.children) {
                                val child = databaser.key
                                dataSnapshot1.ref.child("uname").setValue(value)
                            }
                        }

                        override fun onCancelled(databaseError: DatabaseError) {}
                    })
                }
            } else {
                Toast.makeText(this@EditProfilePage, "Unable to update", Toast.LENGTH_LONG).show()
            }
        }
        builder.setNegativeButton("Cancel") { dialog, which -> pd.dismiss() }
        builder.create().show()
    }

    private fun showImagePicDialog() {
        val options = arrayOf("Camera", "Gallery")
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Pick Image From")
        builder.setItems(options) { dialog, which ->
            if (which == 0) {
                if (!checkCameraPermission()) {
                    requestCameraPermission()
                } else {
                    pickFromCamera()
                }
            } else if (which == 1) {
                if (!checkStoragePermission()) {
                    requestStoragePermission()
                } else {
                    pickFromGallery()
                }
            }
        }
        builder.create().show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == IMAGEPICK_GALLERY_REQUEST) {
                imageuri = data!!.data!!
                uploadProfileCoverPhoto(imageuri)
            }
            if (requestCode == IMAGE_PICKCAMERA_REQUEST) {
                uploadProfileCoverPhoto(imageuri)
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            CAMERA_REQUEST -> {
                if (grantResults.isNotEmpty()) {
                    val camera_accepted = grantResults[0] == PackageManager.PERMISSION_GRANTED
                    val writeStorageaccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED
//                    if (camera_accepted && writeStorageaccepted) {
                        pickFromCamera()
//                    } else {
//                        Toast.makeText(this, "Please Enable Camera and Storage Permissions", Toast.LENGTH_LONG).show()
//                    }
                }
            }
            STORAGE_REQUEST -> {
                if (grantResults.isNotEmpty()) {
                    val writeStorageaccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED
//                    if (writeStorageaccepted) {
                        pickFromGallery()
//                    } else {
//                        Toast.makeText(this, "Please Enable Storage Permissions", Toast.LENGTH_LONG).show()
//                    }
                }
            }
        }
    }

    private fun pickFromCamera() {
        val contentValues = ContentValues()
        contentValues.put(MediaStore.Images.Media.TITLE, "Temp_pic")
        contentValues.put(MediaStore.Images.Media.DESCRIPTION, "Temp Description")
        imageuri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)!!
        val camerIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        camerIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageuri)
        startActivityForResult(camerIntent, IMAGE_PICKCAMERA_REQUEST)
    }

    private fun pickFromGallery() {
        val galleryIntent = Intent(Intent.ACTION_PICK)
        galleryIntent.type = "image/*"
        startActivityForResult(galleryIntent, IMAGEPICK_GALLERY_REQUEST)
    }

    private fun uploadProfileCoverPhoto(uri: Uri) {
        pd.show()
        val filepathname = storagepath + "" + profileOrCoverPhoto + "_" + firebaseUser.uid
        val storageReference1 = storageReference.child(filepathname)
        storageReference1.putFile(uri)
            .addOnSuccessListener { taskSnapshot ->
                val uriTask = taskSnapshot.storage.downloadUrl
                while (!uriTask.isSuccessful);
                val downloadUri = uriTask.result
                if (uriTask.isSuccessful) {
                    val hashMap: MutableMap<String, Any> = HashMap()
                    hashMap[profileOrCoverPhoto] = downloadUri.toString()
                    databaseReference.child(firebaseUser.uid).updateChildren(hashMap)
                        .addOnSuccessListener {
                            pd.dismiss()
                            Toast.makeText(this@EditProfilePage, "Updated", Toast.LENGTH_LONG).show()
                        }
                        .addOnFailureListener {
                            pd.dismiss()
                            Toast.makeText(this@EditProfilePage, "Error Updating ", Toast.LENGTH_LONG).show()
                        }
                } else {
                    pd.dismiss()
                    Toast.makeText(this@EditProfilePage, "Error", Toast.LENGTH_LONG).show()
                }
            }
            .addOnFailureListener { e ->
                pd.dismiss()
                Toast.makeText(this@EditProfilePage, "Error", Toast.LENGTH_LONG).show()
            }
    }
}
