package com.mywork.loadigouser.ui.auth.complete

import androidx.lifecycle.ViewModel
import com.mywork.loadigouser.model.remote.request.auth.CompleteRequest
import com.mywork.loadigouser.model.remote.response.auth.CompleteResponse
import com.mywork.loadigouser.util.Resource
import com.mywork.loadigouser.util.SingleLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CompleteViewModel @Inject constructor(private val repository: CompleteRepository): ViewModel() {
    private val completeMutableLiveData = SingleLiveData<Resource<CompleteResponse>>()
    val completeLiveData: SingleLiveData<Resource<CompleteResponse>> get() = completeMutableLiveData



    suspend fun completeUser(completeRequest: CompleteRequest) {

        val response = repository.completeAccount(completeRequest)
        if (response.statusCode == 200) {
            completeMutableLiveData.value = Resource.Success(response.data!!)
        } else {
            completeMutableLiveData.value = Resource.Error(response.message!!)
        }
    }
}