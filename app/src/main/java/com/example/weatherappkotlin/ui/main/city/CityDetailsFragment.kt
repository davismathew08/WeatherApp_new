package com.example.weatherappkotlin.ui.main.city

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.navArgs
import com.example.weatherappkotlin.R
import com.example.weatherappkotlin.model.place_weather_details.PlaceWeatherDetails
import com.example.weatherappkotlin.utils.Status
import com.example.weatherappkotlin.utils.isConnected
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_city_details.*
import kotlinx.android.synthetic.main.fragment_city_details.view.*

class CityDetailsFragment : Fragment() {

    private lateinit var cityDetailsViewModel: CityDetailsViewModel
    private var latPassed=""
    private var lonPassed=""
    //private  lateinit var customProgressDialog: CustomProgressDialog
    val args : CityDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_city_details, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        //customProgressDialog = CustomProgressDialog()
        setupObserver()
        setupViewModel()
    }

    private fun setupObserver() {
        cityDetailsViewModel= CityDetailsViewModel()
    }

    private fun setupViewModel() {
        cityDetailsViewModel.getPlaceDetailsResponse().observe(this, androidx.lifecycle.Observer {
            when(it.status){

                Status.LOADING->{
                   // customProgressDialog.dialog.dismiss()
                    Log.e("response", Gson().toJson(it))
                }
                Status.SUCCESS->{
                    //customProgressDialog.dialog.dismiss()
                    if(it.data!=null){
                        Log.e("response", Gson().toJson(it))
                        if(it.data!=null){
                            displayDetails(it.data)
                        }else{
                            Toast.makeText(requireContext(),"Something Wrong", Toast.LENGTH_LONG).show()
                        }
                    }
                }
                Status.ERROR->{
                    //customProgressDialog.dialog.dismiss()
                    Toast.makeText(requireContext(),"Something Wrong", Toast.LENGTH_LONG).show()
                }

                Status.NO_INTERNET->{
                    //customProgressDialog.dialog.dismiss()
                    if(requireContext().isConnected){
                        Toast.makeText(requireContext(),"Something Wrong", Toast.LENGTH_LONG).show()
                    }else{
                        Toast.makeText(requireContext(),"No Internet", Toast.LENGTH_LONG).show()
                    }
                }

            }
        })
    }

    private fun displayDetails(placeWeatherDetails: PlaceWeatherDetails) {

        if((placeWeatherDetails.main.pressure/33.864).toInt() in 26..29){
            //low pressure rain chance
            if((placeWeatherDetails.main.humidity<=55)){
                //comfort
                Log.e("chance","comfort")
                tvRainValue.text=" Comfort"
            }else if((placeWeatherDetails.main.humidity>=56) &&(placeWeatherDetails.main.humidity<65)){
                //stick
                Log.e("chance","stick")

                tvRainValue.text=" Stick"
            }else if(placeWeatherDetails.main.humidity>=65){
                //moisture
                Log.e("chance","moisture")
                tvRainValue.text=" Moisture"
            }
        }
        else if((placeWeatherDetails.main.pressure/33.864).toInt() in 30..40){
            //high pressure clear sky
            if((placeWeatherDetails.main.humidity<=55)){
                //comfort
                Log.e("chance","comfort")
                tvRainValue.text=" Comfort"
            }else if((placeWeatherDetails.main.humidity>=56) &&(placeWeatherDetails.main.humidity<65)){
                //stick
                Log.e("chance","stick")
                tvRainValue.text=" Stick"
            }else if(placeWeatherDetails.main.humidity>=65){
                //moisture
                Log.e("chance","moisture")
                tvRainValue.text=" Moisture"
            }
        }
        if(placeWeatherDetails.weather[0].id in 200..232){
            //Thunderstorm
            Log.e("weather" ,placeWeatherDetails.weather[0].main)
            mainConstraintCity.background=ContextCompat.getDrawable(requireContext(),R.color.night_background)
            animationViewCity.setAnimation(R.raw.thunderstorm)
            animationViewCity.playAnimation()
            forBlackBackground()
            placeTextDetails(placeWeatherDetails.main.temp.toString(),
                    placeWeatherDetails.main.humidity.toString(),
                    placeWeatherDetails.wind.speed.toString(),
                    placeWeatherDetails.name)
        }
        else if(placeWeatherDetails.weather[0].id in 300..321){
            //Drizzle
            Log.e("weather" ,placeWeatherDetails.weather[0].main)
            mainConstraintCity.background=ContextCompat.getDrawable(requireContext(),R.color.rainy_background)
            animationViewCity.setAnimation(R.raw.rainy)
            animationViewCity.playAnimation()
            forBlackBackground()
            placeTextDetails(placeWeatherDetails.main.temp.toString(),
                placeWeatherDetails.main.humidity.toString(),
                placeWeatherDetails.wind.speed.toString(),
                placeWeatherDetails.name)

        }
        else if(placeWeatherDetails.weather[0].id in 500..531){
            //Rain
            Log.e("weather" ,placeWeatherDetails.weather[0].main)
            mainConstraintCity.background=ContextCompat.getDrawable(requireContext(),R.color.rainy_background)
            animationViewCity.setAnimation(R.raw.rainy)
            animationViewCity.playAnimation()
            forWhiteBackground()
            placeTextDetails(placeWeatherDetails.main.temp.toString(),
                    placeWeatherDetails.main.humidity.toString(),
                    placeWeatherDetails.wind.speed.toString(),
                    placeWeatherDetails.name)
        }
        else if(placeWeatherDetails.weather[0].id in 600..622){
            //snow
            Log.e("weather" ,placeWeatherDetails.weather[0].main)
            mainConstraintCity.background=ContextCompat.getDrawable(requireContext(),R.color.gree_snow)
            animationViewCity.setAnimation(R.raw.snow)
            animationViewCity.playAnimation()
            forWhiteBackground()
            placeTextDetails(placeWeatherDetails.main.temp.toString(),
                    placeWeatherDetails.main.humidity.toString(),
                    placeWeatherDetails.wind.speed.toString(),
                    placeWeatherDetails.name)
        }
        else if(placeWeatherDetails.weather[0].id in 800..804){
            //clear clouds
            Log.e("weather" ,placeWeatherDetails.weather[0].main)
            mainConstraintCity.background=ContextCompat.getDrawable(requireContext(),R.color.morning_background)
            animationViewCity.setAnimation(R.raw.sunny)
            animationViewCity.playAnimation()
            forWhiteBackground()
            placeTextDetails(placeWeatherDetails.main.temp.toString(),
                placeWeatherDetails.main.humidity.toString(),
                placeWeatherDetails.wind.speed.toString(),
                placeWeatherDetails.name)

        }
        else if(placeWeatherDetails.weather[0].id in 701..781){
            //some other
            Log.e("weather" ,placeWeatherDetails.weather[0].main)
            mainConstraintCity.background=ContextCompat.getDrawable(requireContext(),R.color.morning_background)
            animationViewCity.setAnimation(R.raw.sunny)
            animationViewCity.playAnimation()
            forWhiteBackground()
            placeTextDetails(placeWeatherDetails.main.temp.toString(),
                placeWeatherDetails.main.humidity.toString(),
                placeWeatherDetails.wind.speed.toString(),
                placeWeatherDetails.name)

        }



    }
    private fun placeTextDetails(temp: String, humidity: String, wind: String, name: String) {
        var tempTemperature=String.format("%.2f", (temp.toDouble()-272.15))
        tvLocationNameValue.text= " $name"
        tvTemperatureValue.text= "$tempTemperature \u2103"
        tvHumidityValue.text= "$humidity %"
        tvWindValue.text= "$wind m/s"
    }
    private fun forBlackBackground(){

        tvLocationNameTitle.setTextColor(ContextCompat.getColor(requireContext(),R.color.black_gray))
        tvLocationNameValue.setTextColor(ContextCompat.getColor(requireContext(),R.color.black_gray))

        tvTemperatureTitle.setTextColor(ContextCompat.getColor(requireContext(),R.color.white))
        tvTemperatureValue.setTextColor(ContextCompat.getColor(requireContext(),R.color.white))

        tvHumidityTitle.setTextColor(ContextCompat.getColor(requireContext(),R.color.white))
        tvHumidityValue.setTextColor(ContextCompat.getColor(requireContext(),R.color.white))

        tvWindTitle.setTextColor(ContextCompat.getColor(requireContext(),R.color.white))
        tvWindValue.setTextColor(ContextCompat.getColor(requireContext(),R.color.white))

        tvRainTitle.setTextColor(ContextCompat.getColor(requireContext(),R.color.black_gray))
        tvRainValue.setTextColor(ContextCompat.getColor(requireContext(),R.color.black_gray))
    }
    private fun forWhiteBackground(){

        tvLocationNameTitle.setTextColor(ContextCompat.getColor(requireContext(),R.color.black_gray))
        tvLocationNameValue.setTextColor(ContextCompat.getColor(requireContext(),R.color.black_gray))

        tvTemperatureTitle.setTextColor(ContextCompat.getColor(requireContext(),R.color.black_gray))
        tvTemperatureValue.setTextColor(ContextCompat.getColor(requireContext(),R.color.black_gray))

        tvHumidityTitle.setTextColor(ContextCompat.getColor(requireContext(),R.color.black_gray))
        tvHumidityValue.setTextColor(ContextCompat.getColor(requireContext(),R.color.black_gray))

        tvWindTitle.setTextColor(ContextCompat.getColor(requireContext(),R.color.black_gray))
        tvWindValue.setTextColor(ContextCompat.getColor(requireContext(),R.color.black_gray))

        tvRainTitle.setTextColor(ContextCompat.getColor(requireContext(),R.color.black_gray))
        tvRainValue.setTextColor(ContextCompat.getColor(requireContext(),R.color.black_gray))
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        latPassed = args.lat
        lonPassed=args.lon
        Log.e("testing ", "$latPassed--$lonPassed")
        cityDetailsViewModel.fetchPlaceDetails(latPassed,lonPassed,"e7c1f341d343a4476f8fd0be0b125c3c")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

}