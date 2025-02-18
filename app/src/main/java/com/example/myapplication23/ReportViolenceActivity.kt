package com.example.myapplication23

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import java.util.regex.Pattern
import android.telephony.SmsManager
import android.Manifest
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class ReportViolenceActivity : AppCompatActivity() {

    private lateinit var etFullName: EditText
    private lateinit var etContactNumber: EditText
    private lateinit var etIncidentLocation: EditText
    private lateinit var etIncidentDescription: EditText
    private lateinit var btnSubmitReport: Button

    private val firestore by lazy { FirebaseFirestore.getInstance() }
    private val REPORTEE_PHONE_NUMBER = "0962529287" // Number to receive the SMS

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize views
        etFullName = findViewById(R.id.et_full_name)
        etContactNumber = findViewById(R.id.et_contact_number)
        etIncidentLocation = findViewById(R.id.et_incident_location)
        etIncidentDescription = findViewById(R.id.et_incident_description)
        btnSubmitReport = findViewById(R.id.btn_submit_report)

        // Request SMS permission if not granted
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.SEND_SMS), 1)
        }

        // Add TextWatcher to validate phone number
        etContactNumber.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val phonePattern = Pattern.compile("^\\+251\\d{9}$")
                if (!s.isNullOrEmpty() && !phonePattern.matcher(s).matches()) {
                    etContactNumber.error = "Invalid phone number. Format: +251123456789"
                }
            }
        })

        // Handle Submit Button Click
        btnSubmitReport.setOnClickListener {
            val fullName = etFullName.text.toString()
            val contactNumber = etContactNumber.text.toString()
            val incidentLocation = etIncidentLocation.text.toString()
            val incidentDescription = etIncidentDescription.text.toString()

            if (fullName.isEmpty() || contactNumber.isEmpty() || incidentLocation.isEmpty() || incidentDescription.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields before submitting.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val phonePattern = Pattern.compile("^\\+251\\d{9}$")
            if (!phonePattern.matcher(contactNumber).matches()) {
                Toast.makeText(this, "Invalid phone number format.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Create a report object
            val report = hashMapOf(
                "fullName" to fullName,
                "contactNumber" to contactNumber,
                "incidentLocation" to incidentLocation,
                "incidentDescription" to incidentDescription,
                "timestamp" to System.currentTimeMillis()
            )

            // Save to Firestore
            firestore.collection("violence_reports")
                .add(report)
                .addOnSuccessListener {
                    sendSMS(REPORTEE_PHONE_NUMBER, "New Violence Report:\nName: $fullName\nLocation: $incidentLocation\nDescription: $incidentDescription")

                    val intent = Intent(this, SuccessMessageActivity::class.java).apply {
                        putExtra("status", "success")
                        putExtra("message", "Report submitted successfully!")
                        putExtra("fullName", fullName)
                        putExtra("contactNumber", contactNumber)
                        putExtra("incidentLocation", incidentLocation)
                        putExtra("incidentDescription", incidentDescription)
                    }
                    startActivity(intent)
                }
                .addOnFailureListener { e ->
                    val intent = Intent(this, SuccessMessageActivity::class.java).apply {
                        putExtra("status", "failure")
                        putExtra("message", "Failed to submit report: ${e.message}")
                    }
                    startActivity(intent)
                }
        }
    }

    private fun sendSMS(phoneNumber: String, message: String) {
        try {
            val smsManager = SmsManager.getDefault()
            smsManager.sendTextMessage(phoneNumber, null, message, null, null)
            Toast.makeText(this, "Report sent via SMS.", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(this, "Failed to send SMS: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }
}
