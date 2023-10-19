package com.example.image360task
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class ImageShareUtil(private val context: Context) {

    // Function to save a Bitmap to a temporary file
    private fun saveBitmapToFile(bitmap: Bitmap, fileName: String): Uri {
        val file = File(context.cacheDir, fileName)
        try {
            val stream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            stream.flush()
            stream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }


        // Get the content URI using FileProvider
        return FileProvider.getUriForFile(
            context,
            "com.example.image360task.fileprovider",
            file
        )
    }

    // Function to share a list of Bitmaps through email
    fun shareImagesByEmail(bitmaps: List<Bitmap>, subject: String, message: String,email:String) {
        // Create an ArrayList to store the URIs of the image files
        val imageUris = ArrayList<Uri>()

        // Save each Bitmap to a temporary file and get its URI
        for ((index, bitmap) in bitmaps.withIndex()) {
            var angle=MainActivity.listrotateface.get(index).first
            val fileName = "face_$angle.png"
          //  val imageFile = saveBitmapToFile(bitmap, fileName)
            val imageUri = saveBitmapToFile(bitmap, fileName)
            imageUris.add(imageUri)
        }

        // Create an ACTION_SEND intent
        val intent = Intent(Intent.ACTION_SEND_MULTIPLE)
        intent.type = "image/*"

        // Add the image URIs to the intent
        intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUris)

        // Set email subject and message
        intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
        intent.putExtra(Intent.EXTRA_SUBJECT, subject)
        intent.putExtra(Intent.EXTRA_TEXT, message)

        // Choose the email app to share the images
        context.startActivity(Intent.createChooser(intent, "Share images via"))
    }
}
