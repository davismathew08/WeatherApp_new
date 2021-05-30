package com.example.weatherappkotlin.ui.main.home

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherappkotlin.R
import com.example.weatherappkotlin.dialogs.CustomProgressDialog
import com.example.weatherappkotlin.model.location_details.AddLocationDetails
import com.example.weatherappkotlin.model.realm_db.BookedMarked
import com.example.weatherappkotlin.ui.main.home.adapter.SelectedLocationsAdapter
import com.example.weatherappkotlin.utils.CommonUtils
import com.example.weatherappkotlin.utils.Status
import com.example.weatherappkotlin.utils.isConnected
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.gson.Gson
import io.realm.Realm
import io.realm.RealmResults
import kotlinx.android.synthetic.main.layout_selected_locations_bottom_sheet.*
import java.util.*


class HomeFragment : Fragment(), OnMapReadyCallback {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var mMap: GoogleMap
    private var locationDetails : ArrayList<AddLocationDetails>? = null
    private lateinit var selectedLocationsAdapter : SelectedLocationsAdapter
    private var locationName=""
    private lateinit var realm: Realm
    private  lateinit var customProgressDialog: CustomProgressDialog
    override fun onAttach(context: Context) {
        super.onAttach(context)
        setupViewModel()
        setupObserver()
    }

    private fun setupViewModel() {
        homeViewModel= HomeViewModel()
    }
    private fun setupObserver() {
        homeViewModel.getSelectedLocationDetailsResponse().observe(this, androidx.lifecycle.Observer {
            when(it.status){

                Status.LOADING->{
                    customProgressDialog.dialog.dismiss()
                Log.e("response",Gson().toJson(it))
                }
                Status.SUCCESS->{
                    customProgressDialog.dialog.dismiss()
                    if(it.data!=null){
                        Log.e("response",Gson().toJson(it))
                        locationDetails?.add(
                                AddLocationDetails(
                                        locationName,it.data.lat.toString(),it.data.lon.toString(),false))
                        Log.e("locationList",Gson().toJson(locationDetails))
                        selectedLocationsAdapter.notifyDataSetChanged()
                    }
                }
                Status.ERROR->{
                    customProgressDialog.dialog.dismiss()
                    Toast.makeText(requireContext(),"Something Wrong", Toast.LENGTH_LONG).show()
                }

                Status.NO_INTERNET->{
                    customProgressDialog.dialog.dismiss()
                    if(requireContext().isConnected){
                        Toast.makeText(requireContext(),"Something Wrong", Toast.LENGTH_LONG).show()
                    }else{
                        Toast.makeText(requireContext(),"No Internet", Toast.LENGTH_LONG).show()
                    }
                }

            }
        })
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        realm = Realm.getDefaultInstance()
        customProgressDialog = CustomProgressDialog()
        locationDetails=ArrayList()
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        Log.e("locationList",Gson().toJson(locationDetails))
        rvSelectedLocationWeatherList.layoutManager = LinearLayoutManager(requireContext())
        selectedLocationsAdapter= SelectedLocationsAdapter(locationDetails!!,
            {functionDelete(it)},{functionSelected(it)},{functionBooked(it)},{functionRemoveBooked(it)})
        rvSelectedLocationWeatherList.adapter = selectedLocationsAdapter
        readData()
    }

    private fun functionRemoveBooked(removebookedPosition: Int) {
        deleteData(locationDetails!![removebookedPosition].latitude,
                locationDetails!![removebookedPosition].longitude)
    }

    private fun functionBooked(posBooked: Int) {
        Log.e("details",Gson().toJson(locationDetails!![posBooked]))
        saveData(locationDetails!![posBooked].latitude,
            locationDetails!![posBooked].longitude,
            locationDetails!![posBooked].location_name,
            locationDetails!![posBooked].bookedmarked)
    }

    private fun functionSelected(selectedItem: Int) {
        Log.e("selected position",selectedItem.toString())
        val action = HomeFragmentDirections.actionHomeFragmentToCityDetailsFragment(
                locationDetails!![selectedItem].latitude,locationDetails!![selectedItem].longitude
        )
        findNavController().navigate(action)
    }

    private fun functionDelete(position: Int) {
        if(locationDetails!![position].bookedmarked){
            deleteData(locationDetails!![position].latitude,
                locationDetails!![position].longitude)
        }
        locationDetails?.removeAt(position)
        selectedLocationsAdapter.notifyDataSetChanged()
        if(locationDetails!!.size==0){
            mMap.clear()
        }
    }
    private fun saveData(
        latitude: String,
        longitude: String,
        locationName: String,
        bookedmarkedPassed: Boolean
    ) {
        realm.executeTransactionAsync ({
            val booked = it.createObject(BookedMarked::class.java)
            booked.location_name = locationName
            booked.lat = latitude
            booked.lon=longitude
            booked.bookedmarked=bookedmarkedPassed
        },{
            Log.d("db","On Success: Data Written Successfully!")
            //readData()
        },{
            Log.d("db","On Error: Error in saving Data!")
        })
    }

    private fun readData() {
        val booked = realm.where(BookedMarked::class.java).findAll()
        var response=""
        if(booked!=null){
            booked.forEach {
                response = response + "Location Name: ${it.location_name}, latitude: ${it.lat} ,longitude:${it.lon},bookedmarkeddd:${it.bookedmarked}"+"\n"
                locationDetails?.add(
                        AddLocationDetails(
                                it.location_name!!,it.lat!!,it.lon!!,it.bookedmarked!!))
                selectedLocationsAdapter.notifyDataSetChanged()
            }
            Log.e("responseeee",response)
        }

    }
    private fun deleteData(latDelete:String,lonDelete:String){
        val booked: RealmResults<BookedMarked> = realm
                .where(BookedMarked::class.java)
                .findAll()

        val bookeddatabase: BookedMarked? = booked
                .where()
                .equalTo("lat",latDelete )
                .equalTo("lon", lonDelete)
                .findFirst()

        if (bookeddatabase != null) {
            if (!realm.isInTransaction) {
                realm.beginTransaction()
            }
            bookeddatabase.deleteFromRealm()
            realm.commitTransaction()
        }
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        if (googleMap != null) {
            mMap = googleMap
        }
        Log.e("googlemap",googleMap.toString())

        // Set the listener when tapping
        mMap.setOnMapClickListener { tapLocation -> // Latitude and longitude of the tapped position

            if(requireContext().isConnected){
                var location = LatLng(tapLocation.latitude, tapLocation.longitude)
                locationName= CommonUtils.getAddress(tapLocation.latitude.toString(), tapLocation.longitude.toString(),requireContext())
                Log.e("address",locationName)
                if(locationName.isNullOrEmpty()){
                    Toast.makeText(requireContext(),"Some issue in fetching location, please pin location again", Toast.LENGTH_LONG).show()
                }
                else{
                    mMap.clear()
                    mMap.addMarker(MarkerOptions().position(location).title(locationName))
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 14f))
                    homeViewModel.fetchDetails(tapLocation.latitude.toString(),
                            tapLocation.longitude.toString(),"e7c1f341d343a4476f8fd0be0b125c3c")
                    customProgressDialog.show(requireContext())
                }
            }else{
                Toast.makeText(requireContext(),"No Internet", Toast.LENGTH_LONG).show()
            }
        }
        mMap.setOnInfoWindowLongClickListener {marker->
            if(requireContext().isConnected){
                if(marker.isVisible){
                    marker.remove()
                }
            }else{
                Toast.makeText(requireContext(),"No Internet", Toast.LENGTH_LONG).show()
            }
        }
        mMap.setOnInfoWindowClickListener {markerClose->
            if(requireContext().isConnected){
                if(markerClose.isInfoWindowShown){
                    markerClose.hideInfoWindow()
                }else{
                    markerClose.showInfoWindow()
                }
            }else{
                Toast.makeText(requireContext(),"No Internet", Toast.LENGTH_LONG).show()
            }
        }

    }

}