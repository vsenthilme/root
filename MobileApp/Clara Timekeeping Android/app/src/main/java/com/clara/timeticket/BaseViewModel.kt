package com.clara.timeticket

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.clara.timeticket.model.AuthTokenResponse
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

open class BaseViewModel : ViewModel() {
    @Inject
    lateinit var coroutineScope: CoroutineScope
    var authTokenMutableLiveData: MutableLiveData<AuthTokenResponse> = MutableLiveData()
}