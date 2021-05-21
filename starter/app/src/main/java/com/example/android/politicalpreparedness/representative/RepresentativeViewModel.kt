package com.example.android.politicalpreparedness.representative

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.CivicsApiService
import com.example.android.politicalpreparedness.network.dtos.representatives
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.network.models.Representative
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class RepresentativeViewModel : ViewModel() {

    var client: CivicsApiService = CivicsApi.retrofitService

    private val _address = MutableLiveData<Address>()
    val address: LiveData<Address>
        get() = _address

    private val _representatives = MutableLiveData<List<Representative>>()
    val representatives: LiveData<List<Representative>>
        get() = _representatives

    fun findMyRepresentative(address: Address) {
        getRepresentatives(address)
        Log.d("selected address", address.toFormattedString())
    }

    private fun getRepresentatives(address: Address) = viewModelScope.launch(Dispatchers.IO) {
        try {
            val networkRepresentativeResponse = client.getRepresentativeInfoByAddress(address.toFormattedString())
            val representatives = networkRepresentativeResponse.representatives
            _representatives.postValue(representatives)
        } catch (e: Exception) {
            Log.d("ExceptionInGetRepresentatives", e.toString())
        }

    }

}
