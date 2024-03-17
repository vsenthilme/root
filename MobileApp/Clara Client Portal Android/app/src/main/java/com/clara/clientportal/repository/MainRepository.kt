package com.clara.clientportal.repository

import com.clara.clientportal.model.request.CheckListDocumentUploadUpdateRequest
import com.clara.clientportal.model.request.CheckListViewDetailsRequest
import com.clara.clientportal.model.request.DocumentUploadUpdateRequest
import com.clara.clientportal.model.request.FindClientGeneralEmailRequest
import com.clara.clientportal.model.request.FindClientUserNewRequest
import com.clara.clientportal.model.request.FindGeneralClientMobileRequest
import com.clara.clientportal.model.request.MatterRequest
import com.clara.clientportal.model.request.SetupServiceAuthTokenRequest
import com.clara.clientportal.network.ApiService
import com.clara.clientportal.utils.apiCall
import com.clara.clientportal.utils.apiCalls
import okhttp3.MultipartBody
import org.json.JSONObject
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val apiService: ApiService,
    private val isNetworkConnected: Boolean
) {
    suspend fun getSetupServiceAuthToken(
        setupServiceAuthTokenRequest: SetupServiceAuthTokenRequest,
        transactionId: Int
    ) =
        apiCall(transactionId, isNetworkConnected) {
            apiService.setupServiceAuthToken(setupServiceAuthTokenRequest)
        }

    suspend fun sendEmailOtp(authToken: String, email: String) = apiCalls(0, isNetworkConnected) {
        apiService.sendEmailOtp(authToken, email)
    }

    suspend fun verifyEmailOtp(authToken: String, email: String, otp: String) =
        apiCall(0, isNetworkConnected) {
            apiService.verifyEmailOtp(authToken, email, otp)
        }

    suspend fun sendMobileOtp(authToken: String, contactNo: String) =
        apiCalls(0, isNetworkConnected) {
            apiService.sendMobileOtp(authToken, contactNo)
        }

    suspend fun verifyMobileOtp(authToken: String, contactNo: String, otp: String) =
        apiCall(0, isNetworkConnected) {
            apiService.verifyMobileOtp(authToken, contactNo, otp)
        }

    suspend fun findClientGeneralEmail(
        authToken: String,
        findClientGeneralRequest: FindClientGeneralEmailRequest
    ) = apiCall {
        apiService.findGeneralClientEmail(authToken, findClientGeneralRequest)
    }

    suspend fun findClientGeneral(
        authToken: String,
        request: JSONObject
    ) = apiCall(0, isNetworkConnected) {
        apiService.findGeneralClient(authToken, request)
    }

    suspend fun findClientGeneralMobile(
        authToken: String,
        findClientGeneralRequest: FindGeneralClientMobileRequest
    ) = apiCall {
        apiService.findGeneralClientMobile(authToken, findClientGeneralRequest)
    }

    suspend fun findClientUser(
        authToken: String,
        findClientUserNewRequest: FindClientUserNewRequest
    ) = apiCall(0, isNetworkConnected) {
        apiService.findClientUserNew(authToken, findClientUserNewRequest)
    }

    suspend fun findMatter(authToken: String, matterRequest: MatterRequest) =
        apiCall(0, isNetworkConnected) {
            apiService.findMatterGeneralNew(authToken, matterRequest)
        }

    suspend fun matterPopupDetails(url: String, authToken: String) =
        apiCall(0, isNetworkConnected) {
            apiService.matterPopupDetails(url, authToken)
        }

    suspend fun findQuotation(authToken: String, matterRequest: MatterRequest) =
        apiCall(0, isNetworkConnected) {
            apiService.findQuotation(authToken, matterRequest)
        }


    suspend fun findInVoice(authToken: String, matterRequest: MatterRequest) =
        apiCall(0, isNetworkConnected) {
            apiService.findInvoice(authToken, matterRequest)
        }

    suspend fun status(authToken: String) =
        apiCall(0, isNetworkConnected) {
            apiService.getStatus(authToken)
        }

    suspend fun paymentPlan(authToken: String) =
        apiCall(0, isNetworkConnected) {
            apiService.getPaymentPlan(authToken)
        }

    suspend fun paymentPlanDetails(url: String, authToken: String, paymentPlanRevisionNo: Int) =
        apiCall(0, isNetworkConnected) {
            apiService.getPaymentPlanDetails(url, authToken, paymentPlanRevisionNo)
        }

    suspend fun checkList(authToken: String, matterRequest: MatterRequest) =
        apiCall(0, isNetworkConnected) {
            apiService.getCheckList(authToken, matterRequest)
        }

    suspend fun checkListViewDetails(
        authToken: String,
        checkListViewDetailsRequest: CheckListViewDetailsRequest
    ) =
        apiCall(0, isNetworkConnected) {
            apiService.getCheckListViewDetails(authToken, checkListViewDetailsRequest)
        }

    suspend fun receiptNo(authToken: String) =
        apiCall(0, isNetworkConnected) {
            apiService.getReceiptNo(authToken)
        }

    suspend fun uploadedDocument(authToken: String, matterRequest: MatterRequest) =
        apiCall(0, isNetworkConnected) {
            apiService.getUploadedDocument(authToken, matterRequest)
        }

    suspend fun matterId(authToken: String, matterRequest: MatterRequest) =
        apiCall(0, isNetworkConnected) {
            apiService.getMatterId(authToken, matterRequest)
        }

    suspend fun uploadDocument(
        authToken: String,
        location: String,
        classId: Int,
        body: MultipartBody
    ) =
        apiCall(0, isNetworkConnected) {
            apiService.documentUpload(authToken, location, classId, body)
        }

    suspend fun uploadCheckList(
        location: String,
        body: MultipartBody
    ) =
        apiCall(0, isNetworkConnected) {
            apiService.checkListUpload(location, body)
        }

    suspend fun documentUploadUpdate(
        authToken: String,
        clientUserId: String,
        documentUploadUpdateRequest: DocumentUploadUpdateRequest
    ) =
        apiCall(0, isNetworkConnected) {
            apiService.documentUploadUpdate(authToken, clientUserId, documentUploadUpdateRequest)
        }

    suspend fun checkListDocumentUploadUpdate(
        url: String,
        authToken: String,
        clientUserId: String,
        documentUploadUpdateRequest: CheckListDocumentUploadUpdateRequest
    ) =
        apiCall(0, isNetworkConnected) {
            apiService.checkListDocumentUploadUpdate(
                url,
                authToken,
                clientUserId,
                documentUploadUpdateRequest
            )
        }

    suspend fun uploadDocumentMatterDetails(
        url: String,
        authToken: String
    ) =
        apiCall(0, isNetworkConnected) {
            apiService.getUploadDocumentMatterDetails(url, authToken)
        }

    suspend fun checkListDocumentSubmit(
        matterNo: String,
        authToken: String,
        checkListNo: Number,
        clientId: String,
        loginUserId: String,
        matterHeaderId: Number
    ) = apiCall(0, isNetworkConnected) {
        apiService.checkListDocumentSubmit(
            matterNo,
            authToken,
            checkListNo,
            clientId,
            loginUserId,
            matterHeaderId
        )
    }
}