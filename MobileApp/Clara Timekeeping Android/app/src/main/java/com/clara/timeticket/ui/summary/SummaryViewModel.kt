package com.clara.timeticket.ui.summary

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.clara.timeticket.BaseViewModel
import com.clara.timeticket.model.AuthTokenResponse
import com.clara.timeticket.model.DeleteTicketResponse
import com.clara.timeticket.model.ErrorResponse
import com.clara.timeticket.model.TimeTicketSummaryResponse
import com.clara.timeticket.model.request.TimeTicketSummaryRequest
import com.clara.timeticket.network.APIConstant
import com.clara.timeticket.network.NetworkResult
import com.clara.timeticket.repository.TimeTicketRepository
import com.clara.timeticket.utils.CommonUtils
import com.clara.timeticket.utils.Constants
import com.clara.timeticket.utils.FilterHelperClass
import com.clara.timeticket.utils.PreferenceHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SummaryViewModel @Inject constructor(
    private val repository: TimeTicketRepository,
    private val commonUtils: CommonUtils,
    private val preferenceHelper: PreferenceHelper
) : BaseViewModel() {
    var timeTicketSummaryMutableLiveData: MutableLiveData<List<TimeTicketSummaryResponse>> =
        MutableLiveData()
    var deleteTicketMutableLiveData: MutableLiveData<DeleteTicketResponse> =
        MutableLiveData()
    var networkMutableLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val loaderMutableLiveData: MutableLiveData<Boolean> = MutableLiveData()
    var apiFailureMutableLiveData: MutableLiveData<String> = MutableLiveData()
    var timeTicketNumber: String = ""
    fun getAuthToken(apiId: Int) {
        viewModelScope.launch {
            repository.getAuthToken(
                commonUtils.authTokenRequest(Constants.MANAGEMENT_SERVICE),
                apiId
            )
                .onStart { loaderMutableLiveData.value = true }
                .collectLatest {
                    when (it) {
                        is NetworkResult.Success -> {
                            it.result?.let { result ->
                                authTokenMutableLiveData.value = result as AuthTokenResponse
                            }
                            when (it.id) {
                                APIConstant.FIND_MATTER_TIME_TICKET_ID -> getTimeTicketSummary()
                                APIConstant.DELETE_TICKET_ID -> deleteTicket()
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
                            networkMutableLiveData.value = true
                            loaderMutableLiveData.value = false
                        }
                    }
                }
        }
    }

    private fun getTimeTicketSummary() {
        viewModelScope.launch {
            repository.timeTicketSummary(
                authTokenMutableLiveData.value?.accessToken ?: "",
                timeTicketSummaryRequest()
            )
                .onCompletion { loaderMutableLiveData.value = false }
                .collectLatest {
                    when (it) {
                        is NetworkResult.Success -> {
                            it.result?.let { result ->
                                timeTicketSummaryMutableLiveData.value =
                                    result as List<TimeTicketSummaryResponse>
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
                            networkMutableLiveData.value = true
                            loaderMutableLiveData.value = false
                        }
                    }
                }
        }
    }

    private fun timeTicketSummaryRequest(): TimeTicketSummaryRequest {
        val matterNumberList: MutableList<String> = mutableListOf()
        val billTypeList: ArrayList<String> = arrayListOf()
        val statusList: ArrayList<Int> = arrayListOf()
        val timekeeperList: ArrayList<String> = arrayListOf()
        var startDate = ""
        var endDate = ""

        if (FilterHelperClass.selectedFilterList.isNotEmpty()) {
            for (searchMap in FilterHelperClass.selectedFilterList) {
                for (key in searchMap.keys) {
                    val filterOptionList = searchMap[key]
                    filterOptionList?.let {
                        when (key) {
                            Constants.MATTER_NO -> {
                                for (searchItem in filterOptionList) {
                                    searchItem.id?.let { it1 -> matterNumberList.add(it1) }
                                }
                            }

                            Constants.BILL_TYPE -> {
                                for (searchItem in filterOptionList) {
                                    searchItem.id?.let { it1 -> billTypeList.add(it1) }
                                }
                            }

                            Constants.STATUS -> {
                                for (searchItem in filterOptionList) {
                                    searchItem.id?.toInt()?.let { it1 -> statusList.add(it1) }
                                }
                            }

                            Constants.TIMEKEEPER_CODE -> {
                                for (searchItem in filterOptionList) {
                                    searchItem.id?.let { it1 -> timekeeperList.add(it1) }
                                }
                            }

                            Constants.START_DATE -> {
                                for (searchItem in filterOptionList) {
                                    searchItem.id?.let { it1 -> startDate = it1 }
                                }
                            }

                            Constants.END_DATE -> {
                                for (searchItem in filterOptionList) {
                                    searchItem.id?.let { it1 -> endDate = it1 }
                                }
                            }

                            else -> {}
                        }
                    }
                }
            }
            return TimeTicketSummaryRequest(
                statusList.ifEmpty { null },
                timekeeperList.ifEmpty { null },
                matterNumberList.ifEmpty { null },
                startDate.ifEmpty { null },
                endDate.ifEmpty { null },
                billTypeList.ifEmpty { null }
            )
        } else {
            statusList.add(Constants.STATUS_ID)
            timekeeperList.add(preferenceHelper.getUserId())
            return TimeTicketSummaryRequest(statusList, timekeeperList)
        }
    }

    private fun deleteTicket() {
        viewModelScope.launch {
            repository.deleteTicket(
                timeTicketNumber,
                authTokenMutableLiveData.value?.accessToken ?: "",
                preferenceHelper.getUserId()
            )
                .onCompletion { loaderMutableLiveData.value = false }
                .collectLatest {
                    when (it) {
                        is NetworkResult.Success -> {
                            it.result?.let { result ->
                                deleteTicketMutableLiveData.value =
                                    result as DeleteTicketResponse
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
                            networkMutableLiveData.value = true
                            loaderMutableLiveData.value = false
                        }
                    }
                }
        }
    }
}