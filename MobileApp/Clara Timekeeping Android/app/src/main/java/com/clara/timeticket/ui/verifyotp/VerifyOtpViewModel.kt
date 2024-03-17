package com.clara.timeticket.ui.verifyotp

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.clara.timeticket.BaseViewModel
import com.clara.timeticket.model.AuthTokenResponse
import com.clara.timeticket.model.ErrorResponse
import com.clara.timeticket.model.LoginResponse
import com.clara.timeticket.network.APIConstant
import com.clara.timeticket.network.NetworkResult
import com.clara.timeticket.repository.TimeTicketRepository
import com.clara.timeticket.utils.CommonUtils
import com.clara.timeticket.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VerifyOtpViewModel @Inject constructor(
    private val repository: TimeTicketRepository,
    private val commonUtils: CommonUtils
) : BaseViewModel() {
    var verifyOtpMutableLiveData: MutableLiveData<LoginResponse> = MutableLiveData()
    var resendOtpMutableLiveData: MutableLiveData<Boolean> = MutableLiveData()
    var networkMutableLiveDat: MutableLiveData<Boolean> = MutableLiveData()
    val loaderMutableLiveData: MutableLiveData<Boolean> = MutableLiveData()
    var apiFailureMutableLiveData: MutableLiveData<String> = MutableLiveData()

    var userId: String = ""
    var otp: String = ""

    fun getAuthToken(apiId: Int) {
        viewModelScope.launch {
            repository.getAuthToken(
                commonUtils.authTokenRequest(Constants.SETUP_SERVICE),
                0
            )
                .onStart { loaderMutableLiveData.value = true }
                .collectLatest {
                    when (it) {
                        is NetworkResult.Success -> {
                            it.result?.let { result ->
                                authTokenMutableLiveData.value = result as AuthTokenResponse
                            }
                            when (apiId) {
                                APIConstant.VERIFY_EMAIL_OTP_ID -> verifyOtp()
                                APIConstant.RESEND_OTP_ID -> resendOtp()
                            }
                        }

                        is NetworkResult.Failure -> {
                            it.result?.let { error ->
                                apiFailureMutableLiveData.value =
                                    (error as ErrorResponse).error ?: ""
                            }
                            loaderMutableLiveData.value = false
                        }

                        is NetworkResult.Error -> {
                            it.exception?.let { error ->
                                apiFailureMutableLiveData.value = (error as Exception).message
                            }
                            loaderMutableLiveData.value = false
                        }

                        NetworkResult.NoNetwork -> {
                            networkMutableLiveDat.value = true
                            loaderMutableLiveData.value = false
                        }
                    }
                }
        }
    }

    private fun verifyOtp() {
        viewModelScope.launch {
            repository.verifyOtp(authTokenMutableLiveData.value?.accessToken ?: "", userId, otp)
                .onCompletion { loaderMutableLiveData.value = false }
                .collectLatest {
                    when (it) {
                        is NetworkResult.Success -> {
                            it.result?.let { result ->
                                verifyOtpMutableLiveData.value = result as LoginResponse
                            }
                        }

                        is NetworkResult.Failure -> {
                            apiFailureMutableLiveData.value =
                                if (it.result != null) (it.result as ErrorResponse).error else ""
                            loaderMutableLiveData.value = false
                        }

                        is NetworkResult.Error -> {
                            it.exception?.let { error ->
                                apiFailureMutableLiveData.value = (error as Exception).message
                            }
                            loaderMutableLiveData.value = false
                        }

                        NetworkResult.NoNetwork -> {
                            networkMutableLiveDat.value = true
                            loaderMutableLiveData.value = false
                        }
                    }
                }
        }
    }

    private fun resendOtp() {
        viewModelScope.launch {
            repository.resendOtp(authTokenMutableLiveData.value?.accessToken ?: "", userId)
                .onStart { loaderMutableLiveData.value = true }
                .onCompletion { loaderMutableLiveData.value = false }
                .collectLatest {
                    when (it) {
                        is NetworkResult.Success -> {
                            it.result?.let { result ->
                                resendOtpMutableLiveData.value = result as Boolean
                            }
                        }

                        is NetworkResult.Failure -> {
                            it.result?.let { error ->
                                apiFailureMutableLiveData.value =
                                    (error as ErrorResponse).error ?: ""
                            }
                            loaderMutableLiveData.value = false
                        }

                        is NetworkResult.Error -> {
                            it.exception?.let { error ->
                                apiFailureMutableLiveData.value = (error as Exception).message
                            }
                            loaderMutableLiveData.value = false
                        }

                        NetworkResult.NoNetwork -> {
                            networkMutableLiveDat.value = true
                            loaderMutableLiveData.value = false
                        }
                    }
                }
        }
    }
}