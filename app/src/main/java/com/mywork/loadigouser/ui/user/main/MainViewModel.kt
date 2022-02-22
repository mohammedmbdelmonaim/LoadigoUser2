package com.mywork.loadigouser.ui.user.main

import androidx.lifecycle.ViewModel
import com.mywork.loadigouser.model.remote.request.auth.LoginRequest
import com.mywork.loadigouser.model.remote.response.auth.LoginResponse
import com.mywork.loadigouser.util.Resource
import com.mywork.loadigouser.util.SingleLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: MainRepository): ViewModel() {
    private val loginMutableLiveData = SingleLiveData<Resource<LoginResponse>>()
    val loginLiveData: SingleLiveData<Resource<LoginResponse>> get() = loginMutableLiveData

    suspend fun registerUser(loginRequest: LoginRequest) {

        val response = repository.registerUser(loginRequest)
        if (response.statusCode == 200) {
            loginMutableLiveData.value = Resource.Success(response.data!!)
        } else {
            loginMutableLiveData.value = Resource.Error(response.message!!)
        }
    }

}