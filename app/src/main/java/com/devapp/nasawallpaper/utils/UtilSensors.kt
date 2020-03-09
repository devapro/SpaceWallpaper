package com.devapp.nasawallpaper.utils

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log

class UtilSensors private constructor(context: Context){
    val sensorManager : SensorManager
    val accelerometr : Sensor

    var listener : SensorEventListener? = null

    init {
        sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometr = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    }

    fun start(listenerUpdating: SensorChangeListener?){
        listener = object : SensorEventListener{

            var lastX : Float = 0f
            var lastY : Float = 0f
          //  var lastZ : Float = 0f

            var lastUpdate = System.currentTimeMillis()

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

            }

            override fun onSensorChanged(event: SensorEvent?) {
                // get the change of the x,y,z values of the accelerometer
                var deltaX = event?.values!![0]
                var deltaY = event.values[1]
          //      var deltaZ = Math.abs(lastZ - event.values[2])

                // if the change is below 2, it is just plain noise
                if (Math.abs(deltaX) < 2)
                    deltaX = 0f
                if (Math.abs(deltaY) < 2)
                    deltaY = 0f

                if(System.currentTimeMillis() - lastUpdate > 100){
//                    if(Math.abs(lastX - deltaX) > 0.5 || Math.abs(lastY - deltaY) > 0.5) {
//                        listenerUpdating?.onUpdate(deltaX, deltaY)
//                        Log.d("UtilSensors", "deltaX " + deltaX)
//                        Log.d("UtilSensors", "deltaY " + deltaY)
//                    }
                    if(Math.abs(lastX - deltaX) > 0.05) {
                        listenerUpdating?.onUpdate(deltaX, deltaY)
                        Log.d("UtilSensors", "deltaX " + deltaX)
                        Log.d("UtilSensors", "deltaY " + deltaY)
                    }
                    lastX = deltaX
                    lastY = deltaY
                }
                lastUpdate = System.currentTimeMillis()
//                Log.d("UtilSensors", "deltaX " + deltaX)
//                Log.d("UtilSensors", "deltaY " + deltaY)

             //   Log.d("UtilSensors", "deltaZ " + deltaZ)


            }
        }
        sensorManager.registerListener(listener, accelerometr, SensorManager.SENSOR_DELAY_NORMAL)
    }

    fun stop(){
        sensorManager.unregisterListener(listener)
    }

    companion object {
        lateinit var instase : UtilSensors

        fun init(context: Context){
            instase =
                UtilSensors(context)
        }

        fun start(listener: SensorChangeListener){
            instase.start(listener)
        }

        fun stop(){
            instase.stop()
        }

        fun get() : UtilSensors {
            return instase
        }
    }

    interface SensorChangeListener{
        fun onUpdate(deltaX: Float, deltaY: Float)
    }
}