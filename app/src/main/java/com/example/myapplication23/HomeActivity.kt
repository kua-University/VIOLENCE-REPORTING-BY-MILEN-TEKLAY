package com.example.myapplication23

import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home)  // Make sure R.layout.home is correct

        val imageView = findViewById<ImageView>(R.id.imageView)
        val btn1 = findViewById<Button>(R.id.get_started) // Corrected ID

        // Load the animation (make sure fall_from_top.xml is in res/anim)
        val animation = AnimationUtils.loadAnimation(this, R.anim.fall_from_top)
        imageView.startAnimation(animation)

        btn1.setOnClickListener {
            val intent = Intent(this, ReportViolenceActivity::class.java)
            startActivity(intent)
        }
    }
}