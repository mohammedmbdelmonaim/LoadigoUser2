package com.mywork.loadigouser.ui.user.pickup_payment

import androidx.lifecycle.ViewModel
import com.mywork.loadigouser.model.remote.response.user.Service
import com.mywork.loadigouser.model.remote.response.user.ServiceResponse
import com.mywork.loadigouser.util.Resource
import com.mywork.loadigouser.util.SingleLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PickupAndPaymentViewModel @Inject constructor(private val repository: PickupAndPaymentRepository): ViewModel() {
    private val servicesMutableLiveData = SingleLiveData<Resource<List<Service>>>()
    val servicesLiveData: SingleLiveData<Resource<List<Service>>> get() = servicesMutableLiveData

    suspend fun getServices() {
        servicesMutableLiveData.value = Resource.Loading()
        val response = repository.getAllServices()
        if (response.statusCode == 200) {
            servicesMutableLiveData.value = Resource.Success(response.data?.services!!,response.message!!)
        } else {
            servicesMutableLiveData.value = Resource.Error(response.message!!)
        }
    }

}