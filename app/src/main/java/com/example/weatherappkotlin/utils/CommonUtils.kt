package com.example.weatherappkotlin.utils

import android.R
import android.content.Context
import android.database.Cursor
import android.graphics.Color
import android.location.Geocoder
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.util.TypedValue
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.io.IOException
import java.lang.Exception


class CommonUtils {
    companion object {

        fun getAddress(lat: String, lng: String, context: Context): String {
            try {
                val geocoder = Geocoder(context)
                val list = geocoder.getFromLocation(lat.toDouble(), lng.toDouble(), 1)
                return list[0].getAddressLine(0)
            }catch (io:IOException){
                Toast.makeText(context,io.message.toString(),Toast.LENGTH_LONG).show()
                return ""
            }catch (e:Exception){
                Toast.makeText(context,e.message.toString(),Toast.LENGTH_LONG).show()
                return ""
            }
            /*addresses = geocoder.getFromLocation(lat, lon, 1)

        val address = addresses[0].getAddressLine(0)
        val address2 = addresses[0].getAddressLine(1)
        val city = addresses[0].locality
        val state = addresses[0].adminArea
        val country = addresses[0].countryName
        val postalCode = addresses[0].postalCode
        val knownName = addresses[0].featureName*/
        }


    }
}