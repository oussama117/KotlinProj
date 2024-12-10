package com.example.diseasemonitoring.api

import com.example.diseasemonitoring.models.Appointment
import com.example.diseasemonitoring.models.Disease
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path


interface ApiService {

    @GET("api/diseases/user/{userId}/name/{name}")
    fun getDiseaseUserIdAndByName(
        @Path("userId") userId: String,
        @Path("name") name: String
    ): Call<Disease>

    @POST("/api/diseases")
    fun addDisease(@Body disease: Disease): Call<Disease>

    @GET("api/diseases/user/{userId}")
    fun getAllDiseases(@Path("userId") userId: String): Call<List<Disease>>

    @PUT("api/diseases/user/{userId}/name/{name}")
    fun updateDisease(
        @Path("userId") userId: String,
        @Path("name") name: String,
        @Body disease: Disease
    ): Call<Disease>


    @DELETE("api/diseases/user/{userId}/name/{name}")
    fun deleteDisease(
        @Path("userId") userId: String,
        @Path("name") name: String
    ): Call<Void>


    //APPOINTMENTS
    @POST("/api/appointment")
    fun addAppointment(@Body appointment: Appointment): Call<Appointment>

    @GET("/api/appointment/user/{userId}")
    fun getAllAppointments(@Path("userId") userId: String): Call<List<Appointment>>

    @PUT("/api/appointment/user/{userId}/name/{name}")
    fun updateAppointment(
        @Path("userId") userId: String,
        @Path("name") name: String,
        @Body appointment: Appointment
    ): Call<Appointment>

    @DELETE("/api/appointment/user/{userId}/name/{name}")
    fun deleteAppointment(@Path("userId") userId: String, @Path("name") name: String): Call<Void>

}