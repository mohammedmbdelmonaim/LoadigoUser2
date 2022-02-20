package com.mywork.loadigouser.ui.auth.login

import androidx.lifecycle.ViewModel
import com.mywork.loadigouser.model.remote.request.auth.LoginRequest
import com.mywork.loadigouser.model.remote.response.auth.LoginResponse
import com.mywork.loadigouser.util.Resource
import com.mywork.loadigouser.util.SingleLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val repository: LoginRepository): ViewModel() {
    private val loginMutableLiveData = SingleLiveData<Resource<LoginResponse>>()
    val loginLiveData: SingleLiveData<Resource<LoginResponse>> get() = loginMutableLiveData



    suspend fun loginUser(loginRequest: LoginRequest) {

        val response = repository.loginUser(loginRequest)
        if (response.statusCode == 200) {
            loginMutableLiveData.value = Resource.Success(response.data!!)
        } else {
            loginMutableLiveData.value = Resource.Error(response.message!!)
        }
    }

//    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
//        if (throwable is SocketException) {
//            //very bad internet
//            loginMutableLiveData.postValue(Resource.Error(throwable.message!!))
//        }
//
//        if (throwable is SocketTimeoutException) {
//            //timeout
//            loginMutableLiveData.postValue(Resource.Error(throwable.message!!))
//        }
//
//        if (throwable is HttpException) {
//            // parse error body message from
//            //throwable.response().errorBody()
//            loginMutableLiveData.postValue(Resource.Error(throwable.message!!))
//        }
//
//        if (throwable is UnknownHostException) {
//            // probably no internet or your base url is wrong
//            loginMutableLiveData.postValue(Resource.Error(throwable.message!!))
//
//        }
//    }
}