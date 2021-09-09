package com.artemiymatchin.notificationplayer

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    private fun startPlayerService() {
        Intent(this, NotificationPlayerService::class.java).also { intent ->
            startService(intent)
        }
    }

    private fun makeRequestPermissionLauncher(): ActivityResultLauncher<String> {
        return registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                startPlayerService()
            } else {
                Toast.makeText(
                    applicationContext,
                    getString(R.string.ungiven_permission_msg),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun checkForMemoryPermission(requestPermissionLauncher: ActivityResultLauncher<String>) {
        when (PackageManager.PERMISSION_GRANTED) {
            ContextCompat.checkSelfPermission(
                application,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) -> {
                startPlayerService()
            }
            else -> {
                requestPermissionLauncher.launch(
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val requestPermissionLauncher =  makeRequestPermissionLauncher()
        checkForMemoryPermission(requestPermissionLauncher)
    }
}