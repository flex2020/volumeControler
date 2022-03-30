package com.example.kotproject

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        getPermission()


    }

    private fun getPermission() {
        val notificationManager = this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if(Build.VERSION.SDK_INT >= 23){
            if(!notificationManager.isNotificationPolicyAccessGranted){
                this.startActivity(Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS))
            }
        }
    }
    private fun volChange(volume: Int){
        val audioManager = this.applicationContext.getSystemService(Context.AUDIO_SERVICE) as AudioManager

        when(volume){
            -1 -> audioManager.ringerMode = AudioManager.RINGER_MODE_SILENT
            else -> {
                audioManager.ringerMode = AudioManager.RINGER_MODE_NORMAL
                audioManager.setStreamVolume(
                        AudioManager.STREAM_RING,
                        (audioManager.getStreamMaxVolume(AudioManager.STREAM_RING) * volume/100.0).toInt(),
                        AudioManager.FLAG_PLAY_SOUND
                )
            }
        }
    }
}