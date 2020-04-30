package com.example.imagepicker

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.imagepicker.adapters.ImagePickerAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.custom_dialog.view.*

class MainActivity : AppCompatActivity() {

    var image_uri: Uri? = null
    private val fileListName: MutableList<String>? = null
    lateinit var alertDialog :AlertDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        recycler_id.layoutManager=LinearLayoutManager(this)
        recycler_id.adapter=ImagePickerAdapter(fileListName,this@MainActivity)


//        val dialogBtn=findViewById(R.id.button_id) as Button

        button_id.setOnClickListener(View.OnClickListener {

            val customDialog=LayoutInflater.from(this).inflate(R.layout.custom_dialog,null);

            val dialogBuilder=AlertDialog.Builder(this).setView(customDialog).setTitle("Picker")

           alertDialog=dialogBuilder.show();

            customDialog.gallery_pickerId.setOnClickListener {

                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){

                    if(checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)==PackageManager.PERMISSION_DENIED){

                        val permissionList= arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE);
                        requestPermissions(permissionList,GALLERY_PERMISSION_CODE);
                        
                    }else{
                        pickImageFromGallery();
                    }

                }else{
                    pickImageFromGallery();

                }

            }
            customDialog.camera_pickerId.setOnClickListener {

                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){

                    if(checkSelfPermission(android.Manifest.permission.CAMERA)==PackageManager.PERMISSION_DENIED){

                        val permissionList= arrayOf(android.Manifest.permission.CAMERA);
                        requestPermissions(permissionList, CAMERA_PERMISSION_CODE);

                    }else{
                        openCamera()
                    }

                }else{

                    openCamera()
                }
            }
            //to dismiss the dialog
            customDialog.cancel_Id.setOnClickListener {
                alertDialog.dismiss();
            }
        })
    }
    fun pickImageFromGallery(){

        val intent=Intent(Intent.ACTION_PICK)
        intent.type="image/*"
//        intent.type="video/mp4"
        //for selecting multiple files
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        intent.setAction(Intent.ACTION_GET_CONTENT)
        startActivityForResult(intent, IMAGE_PICK_CODE);

    }

    private fun openCamera() {
//        val values = ContentValues()
//        values.put(MediaStore.Images.Media.TITLE, "New Picture")
//        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera")
//        image_uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        //camera intent
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri)
        startActivityForResult(cameraIntent, CAMERA_PICK_CODE)
    }

    companion object{
        private  val IMAGE_PICK_CODE=1000;
        private  val CAMERA_PICK_CODE=999;
        private  val CAMERA_PERMISSION_CODE=1001;
        private  val GALLERY_PERMISSION_CODE=1002;
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode){
            GALLERY_PERMISSION_CODE ->
                if(grantResults.isNotEmpty() && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    pickImageFromGallery()
                }else{
                    Toast.makeText(this,"Permission Denied",Toast.LENGTH_LONG).show()
                }
            CAMERA_PERMISSION_CODE ->
                if(grantResults.isNotEmpty() && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    openCamera()
                }else{
                    Toast.makeText(this,"Permission Denied",Toast.LENGTH_LONG).show()
                }
        }
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode==Activity.RESULT_OK &&  requestCode == IMAGE_PICK_CODE){

                var totalItem= data?.clipData?.itemCount
                Log.d("totalitem", totalItem.toString())

               for(i in 0..(totalItem?.minus(1))!!){
                   Log.d("image1", data?.clipData?.getItemAt(i).toString())
                   val uri = data!!.clipData!!.getItemAt(i).uri

                   val fileName = getFileName(uri)

                   if (fileName != null) {
                       fileListName?.add(fileName)
                   }

               }

            recycler_id.adapter?.notifyDataSetChanged()
            alertDialog.dismiss();

        }else if(resultCode == Activity.RESULT_OK && requestCode== CAMERA_PICK_CODE){
            Log.d("camera", data?.data.toString())
        }
    }

    //get the image Name
    fun getFileName(uri: Uri): String? {
        var result: String? = null
        if (uri.scheme == "content") {
            val cursor =
                contentResolver.query(uri, null, null, null, null)
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                }
            } finally {
                cursor!!.close()
            }
        } else if (result == null) {
            result = uri.path
            val cut = result!!.lastIndexOf('/')
            if (cut != 1) {
                result = result.substring(cut + 1)
            }
        }
        return result
    }

}
