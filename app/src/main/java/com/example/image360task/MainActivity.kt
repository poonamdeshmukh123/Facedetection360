package com.example.image360task

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.drawable.Drawable
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.image360task.databinding.ActivityMainBinding
import kotlin.random.Random


class MainActivity : AppCompatActivity() {

    private lateinit var cameraManager: CameraManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        btncapture = findViewById(binding.btnSwitch.id)
        supportActionBar?.hide()
        //  imageView=findViewById(R.id.imageview1)
        listrotateface = listOf(
            Pair("right", R.drawable.headright),


            Pair("smile", R.drawable.smile),
            Pair("up", R.drawable.headup),
            Pair("down", R.drawable.headdown),
            Pair("left", R.drawable.headleft)
        )


        faceposition()
        createCameraManager()
        checkForPermission()
        binding.btnSwitch.setOnClickListener {
            MainActivity.storeAllfaceBitmap(this)
        }
    }


    private fun checkForPermission() {
        if (allPermissionsGranted()) {
            cameraManager.startCamera()
        } else {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults:
        IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                cameraManager.startCamera()
            } else {
                Toast.makeText(this, "Permissions not granted by the user.", Toast.LENGTH_SHORT)
                    .show()
                finish()
            }
        }
    }

    private fun createCameraManager() {
        cameraManager = CameraManager(
            this,
            binding.previewViewFinder,
            this,
            binding.graphicOverlayFinder
        )
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED

    }

    companion object {
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(android.Manifest.permission.CAMERA)
        lateinit var binding: ActivityMainBinding
        lateinit var image: Bitmap
        lateinit var newimg: Image
        lateinit var imageView: ImageView
        var position = -1
        var store360img = 0
        lateinit var listrotateface: List<Pair<String, Int>>
        var listhead: MutableList<Bitmap> = mutableListOf()
        lateinit var btncapture: Button
        fun faceposition() {
            position++
            if (position <= listrotateface.size - 1) {
                binding.imgface.setImageResource(listrotateface.get(position).second)
            }
        }

        fun storeAllfaceBitmap(context: MainActivity) {
            faceposition()
            if (position <= listrotateface.size - 1) {
                listhead.add(rotateBitmap(MainActivity.image))


            } else {
                //  position=-1
                listhead.add(rotateBitmap(MainActivity.image))
                val intent = Intent(context, NextActivity::class.java)
                context.startActivity(intent)
                context.finish()
            }
        }

        fun rotateBitmap(bitmap: Bitmap): Bitmap {
            // Rotate the bitmap by 270 degrees
            val matrix = Matrix()
            matrix.postRotate(270f)

            // Create a new rotated bitmap
            val rotatedBitmap = Bitmap.createBitmap(
                bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true
            )

            // Rotate the rotated bitmap by an additional 180 degrees
            val secondMatrix = Matrix()
            secondMatrix.postRotate(0f)

            // Create the final bitmap after the additional rotation
            return Bitmap.createBitmap(
                rotatedBitmap, 0, 0, rotatedBitmap.width, rotatedBitmap.height, secondMatrix, true
            )
        }
    }


}