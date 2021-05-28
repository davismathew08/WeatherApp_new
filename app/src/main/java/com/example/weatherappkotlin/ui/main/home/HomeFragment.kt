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
import com.airbnb.lottie.model.Marker
import com.example.weatherappkotlin.R
import com.example.weatherappkotlin.dialogs.CustomProgressDialog
import com.example.weatherappkotlin.model.location_details.AddLocationDetails
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
import kotlinx.android.synthetic.main.layout_selected_locations_bottom_sheet.*
import java.util.*


class HomeFragment : Fragment(), OnMapReadyCallback {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var mMap: GoogleMap
    private  lateinit var customProgressDialog: CustomProgressDialog
    private var locationDetails : ArrayList<AddLocationDetails>? = null
    private lateinit var selectedLocationsAdapter : SelectedLocationsAdapter
    private var locationName=""
    fun dismissLoader() {
        customProgressDialog.dialog.dismiss()
    }

    fun showLoader() {
        customProgressDialog.show(requireContext())
    }
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
        customProgressDialog = CustomProgressDialog()
        locationDetails=ArrayList()
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        Log.e("locationList",Gson().toJson(locationDetails))
        rvSelectedLocationWeatherList.layoutManager = LinearLayoutManager(requireContext())
        selectedLocationsAdapter= SelectedLocationsAdapter(locationDetails!!,{functionDelete(it)},{functionSelected(it)})
        rvSelectedLocationWeatherList.adapter = selectedLocationsAdapter
    }

    private fun functionSelected(selectedItem: Int) {
        Log.e("selected position",selectedItem.toString())
        val action = HomeFragmentDirections.actionHomeFragmentToCityDetailsFragment(
                locationDetails!![selectedItem].latitude,locationDetails!![selectedItem].longitude
        )
        findNavController().navigate(action)
    }

    private fun functionDelete(position: Int) {
        locationDetails?.removeAt(position)
        selectedLocationsAdapter.notifyDataSetChanged()
        if(locationDetails!!.size==0){
            mMap.clear()
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
                    homeViewModel.fetchAgentAssignedPropertyList(tapLocation.latitude.toString(),
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