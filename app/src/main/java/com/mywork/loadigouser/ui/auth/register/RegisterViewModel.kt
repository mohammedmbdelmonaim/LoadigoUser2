package com.mywork.loadigouser.ui.auth.register

import androidx.lifecycle.ViewModel
import com.mywork.loadigouser.model.remote.request.auth.RegisterRequest
import com.mywork.loadigouser.model.remote.response.auth.RegisterResponse
import com.mywork.loadigouser.util.Resource
import com.mywork.loadigouser.util.SingleLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(private val repository: RegisterRepository): ViewModel() {
    private val registerMutableLiveData = SingleLiveData<Resource<RegisterResponse>>()
    val registerLiveData: SingleLiveData<Resource<RegisterResponse>> get() = registerMutableLiveData

    suspend fun registerUser(registerRequest: RegisterRequest) {
        registerMutableLiveData.value = Resource.Loading()
        val response = repository.registerUser(registerRequest)
        if (response.statusCode == 201) {
            registerMutableLiveData.value = Resource.Success(response.data!!,response.message!!)
        } else {
            registerMutableLiveData.value = Resource.Error(response.message!!)
        }
    }

}