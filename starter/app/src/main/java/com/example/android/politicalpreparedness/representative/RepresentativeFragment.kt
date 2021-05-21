package com.example.android.politicalpreparedness.representative

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.databinding.FragmentRepresentativeBinding
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.representative.adapter.RepresentativeListAdapter
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.util.Locale

class DetailFragment : Fragment() {
    private lateinit var binding: FragmentRepresentativeBinding
    private val viewModel by viewModels<RepresentativeViewModel>()

    private val FINE_LOCATION_ACCESS_REQUEST_CODE = 1
    private lateinit var lastLocation: Location
    private lateinit var fusedLocationClient: FusedLocationProviderClient


    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_representative, container, false)
        binding.lifecycleOwner = this
        binding.representativeViewModel = viewModel
        binding.myRepresentativeRecycler.adapter = RepresentativeListAdapter()
        binding.address = Address("", "", "", "", "")

        binding.buttonLocation.setOnClickListener {
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
            getLocation()
        }

        binding.state.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                Log.d("spinner selected", p0?.selectedItem.toString())
                binding.listPlaceholder.text = p0?.selectedItem.toString()
            }
        }
        return binding.root
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            FINE_LOCATION_ACCESS_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    getLocation()
                } else {
                    Toast.makeText(context, "Location permission was not granted.", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {

        when {
            (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) -> {
                fusedLocationClient.lastLocation.addOnSuccessListener(requireActivity()) { location ->
                    if (location != null) {
                        lastLocation = location

                        val address = geoCodeLocation(lastLocation)
                        binding.address = address
                        val stateText = address.state

                        if (stateText != "") {
                            val statesForSpinner = requireContext().resources.getStringArray(R.array.states)
                            var statesAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, statesForSpinner)
                            val spinnerPosition: Int = statesAdapter.getPosition(stateText)
                            binding.state.setSelection(spinnerPosition)
                        }

                    }
                }
                Toast.makeText(context, "Location permission is granted.", Toast.LENGTH_LONG).show()
            }
            (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) -> {
                requestPermissions(
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                        FINE_LOCATION_ACCESS_REQUEST_CODE
                )
            }
            else ->
                requestPermissions(
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                        FINE_LOCATION_ACCESS_REQUEST_CODE
                )
        }
    }

    private fun geoCodeLocation(location: Location): Address {
        val geocoder = Geocoder(context, Locale.getDefault())
        return geocoder.getFromLocation(location.latitude, location.longitude, 1)
                .map { address ->
                    Address(address.thoroughfare, address.subThoroughfare, address.locality, address.adminArea, address.postalCode)
                }
                .first()
    }

    private fun hideKeyboard() {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view!!.windowToken, 0)
    }
}