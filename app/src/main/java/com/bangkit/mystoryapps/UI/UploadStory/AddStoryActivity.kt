package com.bangkit.mystoryapps.UI.UploadStory

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bangkit.mystoryapps.UI.Main.MainActivity
import com.bangkit.mystoryapps.data.Result
import com.bangkit.mystoryapps.data.viewmodels.StoryViewModel
import com.bangkit.mystoryapps.data.viewmodels.ViewModelFactory
import com.bangkit.mystoryapps.databinding.ActivityAddStoryBinding
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class AddStoryActivity : AppCompatActivity() {
    private var binding: ActivityAddStoryBinding? = null
    private var getFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        binding!!.progressBarAddStory.visibility = View.GONE

        val factory: ViewModelFactory = ViewModelFactory.getStoryInstance(this)
        val viewModel: StoryViewModel by viewModels { factory }

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }
        binding!!.btnStartCameraX.setOnClickListener{
            startCameraX()
        }
        binding!!.btnOpenGallery.setOnClickListener{
            startGallery()
        }
        binding!!.btnUpload.setOnClickListener{
            uploadImage(viewModel)
        }
    }

    private fun uploadImage(viewModel: StoryViewModel){
        if(getFile != null){
            val file = reduceFileImage(getFile as File)

            val description = binding!!.txtDescriptionAddStory.text.toString().toRequestBody("text/plain".toMediaType())
            val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val imageMultiPart : MultipartBody.Part = MultipartBody.Part.createFormData(
                "photo",
                file.name,
                requestImageFile
            )
            viewModel.addStory(imageMultiPart, description).observe(this){result ->
                if(result != null){
                    when(result){
                        is Result.Loading -> {
                            binding!!.progressBarAddStory.visibility = View.VISIBLE
                        }
                        is Result.Error -> {
                            Toast.makeText(this, "Error: ${result.error}", Toast.LENGTH_LONG).show()
                            binding!!.progressBarAddStory.visibility = View.GONE
                        }
                        is Result.Success -> {
                            binding!!.progressBarAddStory.visibility = View.GONE
                            Toast.makeText(this, "Berhasil mengupload gambar! Silahkan kembali ke main activity!", Toast.LENGTH_LONG).show()
                            val intent = Intent(this@AddStoryActivity, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    }
                }
            }
        }
        else{
            Toast.makeText(
                this,
                "Silahkan masukkan berkas gambar lebih dahulu.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
    private fun startCameraX(){
        val intent = Intent(this, CameraActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }
    private fun startGallery(){
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {result ->
        val selectedImg = result.data?.data as Uri
        selectedImg.let { uri ->
            val myFile = uriToFile(uri, this@AddStoryActivity)
            getFile = myFile
            binding!!.imgPreviewPhoto.setImageURI(uri)
        }
    }
    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERA_X_RESULT) {
            val myFile = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                it.data?.getSerializableExtra("picture", File::class.java)
            } else {
                @Suppress("DEPRECATION")
                it.data?.getSerializableExtra("picture")
            } as? File
            val isBackCamera = it.data?.getBooleanExtra("isBackCamera", true) as Boolean
            myFile?.let { file ->
                rotateFile(file, isBackCamera)
                getFile = file
                binding!!.imgPreviewPhoto.setImageBitmap(BitmapFactory.decodeFile(file.path))
            }
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS){
            if(!allPermissionsGranted()){
                Toast.makeText(
                    this,
                    "Tidak mendapatkan permission.",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }
    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    companion object{
        const val CAMERA_X_RESULT = 200
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}