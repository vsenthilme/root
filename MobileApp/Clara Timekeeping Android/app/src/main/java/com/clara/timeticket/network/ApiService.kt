package com.clara.timeticket.network

import com.clara.timeticket.model.ActivityCodeResponse
import com.clara.timeticket.model.AuthTokenResponse
import com.clara.timeticket.model.DeleteTicketResponse
import com.clara.timeticket.model.LoginResponse
import com.clara.timeticket.model.MatterDetails
import com.clara.timeticket.model.MatterIdResponse
import com.clara.timeticket.model.MatterRate
import com.clara.timeticket.model.NewTicketResponse
import com.clara.timeticket.model.SearchExecuteRequest
import com.clara.timeticket.model.SearchStatus
import com.clara.timeticket.model.TaskBasedCodeResponse
import com.clara.timeticket.model.TicketRequest
import com.clara.timeticket.model.TimeKeeperCode
import com.clara.timeticket.model.TimeTicketSummaryResponse
import com.clara.timeticket.model.request.AuthTokenRequest
import com.clara.timeticket.model.request.TimeTicketSummaryRequest
import com.clara.timeticket.utils.Constants
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

interface ApiService {
    @POST(APIConstant.AUTH_TOKEN)
    suspend fun authToken(@Body serviceAuthTokenRequest: AuthTokenRequest): Response<AuthTokenResponse>

    @GET(APIConstant.LOGIN)
    suspend fun login(
        @Query(Constants.AUTH_TOKEN) authToken: String,
        @Query(Constants.USER_ID) userId: String,
        @Query(Constants.PASSWORD) password: String
    ): Response<LoginResponse>

    @GET(APIConstant.VERIFY_EMAIL_OTP)
    suspend fun verifyEmailOtp(
        @Query(Constants.AUTH_TOKEN) authToken: String,
        @Query(Constants.USER_ID) userId: String,
        @Query(Constants.OTP) otp: String
    ): Response<LoginResponse>

    @GET(APIConstant.RESEND_OTP)
    suspend fun resendOtp(
        @Query(Constants.AUTH_TOKEN) authToken: String,
        @Query(Constants.USER_ID) userId: String
    ): Response<Boolean>

    @POST(APIConstant.FIND_MATTER_TIME_TICKET)
    suspend fun timeTicketSummary(
        @Query(Constants.AUTH_TOKEN) authToken: String,
        @Body request: TimeTicketSummaryRequest
    ): Response<List<TimeTicketSummaryResponse>>

    @GET
    suspend fun matterId(
        @Url url: String,
        @Query(Constants.AUTH_TOKEN) authToken: String
    ): Response<MatterIdResponse>

    @GET(APIConstant.TIME_KEEPER_CODE)
    suspend fun timeKeeperCode(
        @Query(Constants.AUTH_TOKEN) authToken: String
    ): Response<List<TimeKeeperCode>>

    @GET(APIConstant.SEARCH_STATUS)
    suspend fun searchStatus(
        @Query(Constants.AUTH_TOKEN) authToken: String
    ): Response<List<SearchStatus>>

    @POST(APIConstant.SEARCH_EXECUTE)
    suspend fun searchExecute(
        @Query(Constants.AUTH_TOKEN) authToken: String,
        @Body request: SearchExecuteRequest
    ): Response<List<TimeTicketSummaryResponse>>

    @GET(APIConstant.MATTER_RATE)
    suspend fun matterRate(
        @Path(Constants.MATTER_NO) matteNumber: String,
        @Query(Constants.AUTH_TOKEN) authToken: String,
        @Query(Constants.QUERY_PARAM_TIMEKEEPER_CODE) timekeeperCode: String,
    ): Response<MatterRate>

    @GET(APIConstant.MATTER_DETAILS)
    suspend fun matterDetails(
        @Path(Constants.MATTER_NO) matteNumber: String,
        @Query(Constants.AUTH_TOKEN) authToken: String
    ): Response<MatterDetails>

    @POST(APIConstant.NEW_TIME_TICKET)
    suspend fun newTimeTicket(
        @Query(Constants.AUTH_TOKEN) authToken: String,
        @Query(Constants.PARAM_LOGIN_USER_ID) loginUserID: String,
        @Body request: TicketRequest
    ): Response<NewTicketResponse>

    @GET(APIConstant.ACTIVITY_CODE)
    suspend fun activityCode(
        @Query(Constants.AUTH_TOKEN) authToken: String
    ): Response<List<ActivityCodeResponse>>

    @GET(APIConstant.TASK_BASED_CODE)
    suspend fun taskBasedCode(
        @Query(Constants.AUTH_TOKEN) authToken: String
    ): Response<List<TaskBasedCodeResponse>>

    @DELETE(APIConstant.DELETE_TICKET)
    suspend fun deleteTicket(
        @Path(Constants.TIME_TICKET_NUMBER) timeTicketNumber: String,
        @Query(Constants.AUTH_TOKEN) authToken: String,
        @Query(Constants.PARAM_LOGIN_USER_ID) loginUserID: String
    ): Response<DeleteTicketResponse>

    @PATCH(APIConstant.EDIT_TICKET)
    suspend fun editTicket(
        @Path(Constants.TIME_TICKET_NUMBER) timeTicketNumber: String,
        @Query(Constants.AUTH_TOKEN) authToken: String,
        @Query(Constants.PARAM_LOGIN_USER_ID) loginUserID: String,
        @Body request: TicketRequest
    ): Response<NewTicketResponse>

    @GET(APIConstant.TIME_TICKET_DETAILS)
    suspend fun timeTicketDetails(
        @Path(Constants.TIME_TICKET_NUMBER) timeTicketNumber: String,
        @Query(Constants.AUTH_TOKEN) authToken: String,
    ): Response<NewTicketResponse>
}