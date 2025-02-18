package com.example.myapplication23

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SuccessMessageActivity : AppCompatActivity() {

    private lateinit var tvStatusMessage: TextView
    private lateinit var tvUserName: TextView
    private lateinit var tvUserPhone: TextView
    private lateinit var tvIncidentDetails: TextView
    private lateinit var tvIncidentDescription: TextView
    private lateinit var btnBack: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_success_message)

        // Initialize views
        tvStatusMessage = findViewById(R.id.tv_status_message)
        tvUserName = findViewById(R.id.tv_user_name)
        tvUserPhone = findViewById(R.id.tv_user_phone)
        tvIncidentDetails = findViewById(R.id.tv_incident_details)
        tvIncidentDescription = findViewById(R.id.tv_incident_description)
        btnBack = findViewById(R.id.btn_back)

        // Retrieve data from Intent
        val status = intent.getStringExtra("status") ?: "unknown"
        val message = intent.getStringExtra("message") ?: "No message available."
        val fullName = intent.getStringExtra("fullName") ?: "N/A"
        val contactNumber = intent.getStringExtra("contactNumber") ?: "N/A"
        val incidentLocation = intent.getStringExtra("incidentLocation") ?: "N/A"
        val incidentDescription = intent.getStringExtra("incidentDescription") ?: "N/A"

        // Display status and message
        tvStatusMessage.text = message

        // Show user details only for success
        if (status == "success") {
            tvUserName.text = "sm: $fullName"
            tvUserPhone.text = "slki kutri: $contactNumber"
            tvIncidentDetails.text = "Incident Location: $incidentLocation"
            tvIncidentDescription.text = "Incident Description: $incidentDescription"
        } else {
            // Hide details for failure
            tvUserName.text = ""
            tvUserPhone.text = ""
            tvIncidentDetails.text = ""
            tvIncidentDescription.text = ""
        }

        // Handle Back Button
        btnBack.setOnClickListener {
            finish() // Close SuccessMessageActivity and return to previous screen
        }
    }
}
