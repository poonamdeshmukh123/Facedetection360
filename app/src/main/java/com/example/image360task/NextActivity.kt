package com.example.image360task

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Matrix
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.image360task.databinding.ActivityNextBinding

class NextActivity : AppCompatActivity() {
    lateinit var binding: ActivityNextBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNextBinding.inflate(layoutInflater)
        setContentView(binding.root)

        MainActivity.position = -1
        binding.btnsubmit.setOnClickListener {
            if (valid(
                    binding.edtname.text.toString().trim(),
                    binding.edtemail.text.toString().trim()
                )
            ) {
                Toast.makeText(this, "Please wait...", Toast.LENGTH_SHORT).show()

                val imageShareUtil = ImageShareUtil(this)
                val bitmaps = MainActivity.listhead// Replace with your list of Bitmaps
                val subject = "360 pictures of " + binding.edtname.text.toString().trim()
                val message = "360 degree pictures"
                val emailsend = binding.edtemail.text.toString().trim()
                imageShareUtil.shareImagesByEmail(bitmaps, subject, message, emailsend)
                intent.setType("message/rfc822");


            }
        }


    }


    fun valid(name: String, email: String): Boolean {
        return if (name.length == 0) {
            binding.edtname.requestFocus()
            binding.edtname.setError("name is required")
            false
        } else if (!name.matches(Regex("[a-zA-Z]+"))) {
            binding.edtname.requestFocus()
            binding.edtname.setError("enter only character:")
            false
        } else if (email.length == 0) {
            binding.edtname.requestFocus()
            binding.edtname.setError("email is required")
            false
        } else if (!isEmailValid(email)) {
            binding.edtemail.requestFocus()
            binding.edtemail.setError("enter the correct mail")
            false
        } else {
            true
        }

    }

    fun isEmailValid(email: String): Boolean {
        val regex = Regex("^([a-zA-Z0-9_\\.-]+)@([\\da-zA-Z\\.-]+)\\.([a-zA-Z]{2,})$")
        return regex.matches(email)
    }


}