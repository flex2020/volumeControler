package com.example.kotproject

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.*
import com.example.kotproject.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), SensorEventListener {
    private lateinit var binding: ActivityMainBinding
    private var tmp: Int = 0
    private var prev: Int = 0
    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        getPermission()
        mediaPlayer = MediaPlayer.create(this, R.raw.bgm)
        mediaPlayer.start()
        mediaPlayer.isLooping = false

    }
    private val sensorManager1 by lazy {
        getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }
    override fun onResume() {
        super.onResume()

        sensorManager1.registerListener(this,
            sensorManager1.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
        SensorManager.SENSOR_DELAY_NORMAL)
    }
    private fun getPermission() {
        val notificationManager = this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if(Build.VERSION.SDK_INT >= 23){
            if(!notificationManager.isNotificationPolicyAccessGranted){
                this.startActivity(Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS))
            }
        }
    }
    private fun volChange(volume: Int) {
        val audioManager = this.applicationContext.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        when (volume) {
            -1 -> audioManager.ringerMode = AudioManager.RINGER_MODE_SILENT
            else -> {
                audioManager.setStreamVolume(
                    AudioManager.STREAM_MUSIC,
                    (audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC) * volume / 100.0).toInt(),
                    AudioManager.FLAG_PLAY_SOUND
                )
            }
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            tmp = -event.values[0].toInt()
            binding.seekBar.setProgress(binding.seekBar.getProgress() + tmp, true)
            val volume = binding.seekBar.progress
            Log.e("seekbar", "${binding.seekBar.progress}")
            volChange(volume)
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }
    override fun onPause() {
        super.onPause()
        sensorManager1.unregisterListener(this)
    }

}