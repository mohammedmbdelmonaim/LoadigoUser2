package com.mywork.loadigouser.ui.user.history

import androidx.lifecycle.ViewModel
import com.mywork.loadigouser.model.remote.response.user.CategoriesResponse
import com.mywork.loadigouser.model.remote.response.user.Category
import com.mywork.loadigouser.util.Resource
import com.mywork.loadigouser.util.SingleLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(private val repository: HistoryRepository): ViewModel() {
    private val categoriesMutableLiveData = SingleLiveData<Resource<List<Category>>>()
    val categoriesLiveData: SingleLiveData<Resource<List<Category>>> get() = categoriesMutableLiveData

    suspend fun getAllCategories() {
        categoriesMutableLiveData.value = Resource.Loading()
        val response = repository.getAllCategories()
        if (response.statusCode == 200) {
            categoriesMutableLiveData.value = Resource.Success(response.data?.categories!!,response.message!!)
        } else {
            categoriesMutableLiveData.value = Resource.Error(response.message!!)
        }
    }

}