package com.example.diseasemonitoring.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.diseasemonitoring.api.RetrofitInstance
import com.example.diseasemonitoring.models.Appointment
import com.example.diseasemonitoring.models.Disease
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DiseaseViewModels : ViewModel() {

    private val _disease = MutableLiveData<Disease>()
    val disease: LiveData<Disease> get() = _disease
    private val _diseaseList = MutableLiveData<List<Disease>>(emptyList())
    val diseaseList: LiveData<List<Disease>> get() = _diseaseList
    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    //new
    private val _appointment = MutableLiveData<Appointment>()
    val appointment: LiveData<Appointment> get() = _appointment

    private val _appointmentList = MutableLiveData<List<Appointment>>(emptyList())

    val appointmentList: LiveData<List<Appointment>> get() = _appointmentList
    private val staticUserId = "64fef678e1b2ec4f112d4f89"
    init {
        fetchAllDiseases(staticUserId)
        fetchAllAppointments(staticUserId)
    }

    fun addDisease(disease: Disease) {
        _isLoading.value = true // Set loading state before API call
        val diseaseWithUserId = disease.copy(userId = staticUserId)
        RetrofitInstance.api.addDisease(diseaseWithUserId).enqueue(object : Callback<Disease> {
            override fun onResponse(call: Call<Disease>, response: Response<Disease>) {
                _isLoading.value = false // Reset loading state
                if (response.isSuccessful) {
                    response.body()?.let {
                        _disease.value = it
                        // Update the diseaseList by adding the new disease
                        val currentList = _diseaseList.value?.toMutableList() ?: mutableListOf()
                        currentList.add(it) // Add the new disease
                        _diseaseList.value = currentList // Update the live data
                        Log.d("DiseaseViewModels", "Disease added successfully: $it")
                    }
                } else {
                    handleError(response)
                }
            }

            override fun onFailure(call: Call<Disease>, t: Throwable) {
                _isLoading.value = false // Reset loading state
                _errorMessage.value = "Error adding disease: ${t.message}"
                Log.e("DiseaseViewModels", "Error adding disease: ${t.message}")
            }
        })
    }

    private fun handleError(response: Response<*>) {
        val errorBody = response.errorBody()?.string()
        val statusCode = response.code()
        _errorMessage.value = "Error: $errorBody"
        Log.e("DiseaseViewModels", "API error (Code: $statusCode): $errorBody")
    }

    fun fetchDiseaseByName(userId:String ,name: String) {
        _isLoading.value = true
        RetrofitInstance.api.getDiseaseUserIdAndByName(userId,name).enqueue(object : Callback<Disease> {
            override fun onResponse(call: Call<Disease>, response: Response<Disease>) {
                _isLoading.value = false // Reset loading state
                if (response.isSuccessful) {
                    _disease.value = response.body()
                    Log.d("DiseaseViewModels", "Fetched disease: ${response.body()}")
                } else {
                    handleError(response)
                }
            }

            override fun onFailure(call: Call<Disease>, t: Throwable) {
                _isLoading.value = false // Reset loading state
                _errorMessage.value = "Error fetching disease by name: ${t.message}"
                Log.e("DiseaseViewModels", "Error fetching disease: ${t.message}")
            }
        })
    }

    fun fetchAllDiseases(userId:String) {
        _isLoading.value = true
        RetrofitInstance.api.getAllDiseases(userId ).enqueue(object : Callback<List<Disease>> {
            override fun onResponse(call: Call<List<Disease>>, response: Response<List<Disease>>) {
                _isLoading.value = false // Reset loading state
                if (response.isSuccessful) {
                    _diseaseList.value = response.body() ?: emptyList()
                    Log.d("DiseaseViewModels", "Fetched all diseases: ${response.body()}")
                } else {
                    handleError(response)
                }
            }

            override fun onFailure(call: Call<List<Disease>>, t: Throwable) {
                _isLoading.value = false // Reset loading state
                _errorMessage.value = "Error fetching all diseases: ${t.message}"
                Log.e("DiseaseViewModels", "Error fetching all diseases: ${t.message}")
            }
        })
    }


    fun updateDisease(diseaseName: String,updatedDisease:Disease) {
        _isLoading.value = true
        val diseaseWithUserId = updatedDisease.copy(userId = staticUserId)
        RetrofitInstance.api.updateDisease(staticUserId , diseaseName, diseaseWithUserId).enqueue(object : Callback<Disease> {
            override fun onResponse(call: Call<Disease>, response: Response<Disease>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    response.body()?.let { updated ->
                        _disease.value = updated
                        _diseaseList.value = _diseaseList.value?.map {
                            if (it.name == diseaseName) updated else it
                        }
                        Log.d("DiseaseViewModels", "Disease updated successfully: $updated")
                    }
                } else {
                    handleError(response)
                }
            }

            override fun onFailure(call: Call<Disease>, t: Throwable) {
                _isLoading.value = false
                _errorMessage.value = "Error updating disease: ${t.message}"
                Log.e("DiseaseViewModels", "Error updating disease: ${t.message}")
            }
        })
    }
    fun deleteDisease(diseaseName: String) {
        _isLoading.value = true
        RetrofitInstance.api.deleteDisease(staticUserId, diseaseName).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    // Remove the deleted disease from the list
                    _diseaseList.value = _diseaseList.value?.filter { it.name != diseaseName }
                    Log.d("DiseaseViewModels", "Disease deleted successfully")
                } else {
                    handleError(response)
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                _isLoading.value = false
                _errorMessage.value = "Error deleting disease: ${t.message}"
                Log.e("DiseaseViewModels", "Error deleting disease: ${t.message}")
            }
        })
    }

    fun addAppointment(appointment: Appointment) {
        _isLoading.value = true // Set loading state before API call
        val appointmentWithUserId = appointment.copy(userId = staticUserId)
        RetrofitInstance.api.addAppointment(appointmentWithUserId).enqueue(object : Callback<Appointment> {
            override fun onResponse(call: Call<Appointment>, response: Response<Appointment>) {
                _isLoading.value = false // Reset loading state
                if (response.isSuccessful) {
                    response.body()?.let {
                        _appointment.value = it
                        // Update the diseaseList by adding the new disease
                        val currentList = _appointmentList.value?.toMutableList() ?: mutableListOf()
                        currentList.add(it) // Add the new disease
                        _appointmentList.value = currentList // Update the live data
                        Log.d("AppointmentViewModels", "appointment added successfully: $it")
                    }
                } else {
                    handleError(response)
                }
            }

            override fun onFailure(call: Call<Appointment>, t: Throwable) {
                _isLoading.value = false // Reset loading state
                _errorMessage.value = "Error adding appointment: ${t.message}"
                Log.e("AppointmentViewModels", "Error adding appointment: ${t.message}")
            }
        })
    }


    fun fetchAllAppointments(userId:String) {
        _isLoading.value = true
        RetrofitInstance.api.getAllAppointments(userId ).enqueue(object : Callback<List<Appointment>> {
            override fun onResponse(call: Call<List<Appointment>>, response: Response<List<Appointment>>) {
                _isLoading.value = false // Reset loading state
                if (response.isSuccessful) {
                    _appointmentList.value = response.body() ?: emptyList()
                    Log.d("AppointmentViewModels", "Fetched all Appointment: ${response.body()}")
                } else {
                    handleError(response)
                }
            }

            override fun onFailure(call: Call<List<Appointment>>, t: Throwable) {
                _isLoading.value = false // Reset loading state
                _errorMessage.value = "Error fetching all Appointment: ${t.message}"
                Log.e("AppointmentViewModels", "Error fetching all Appointments: ${t.message}")
            }
        })
    }


    fun updateAppointment(appointmentName: String,updatedAppointment:Appointment) {
        _isLoading.value = true
        val appointmentWithUserId = updatedAppointment.copy(userId = staticUserId)
        RetrofitInstance.api.updateAppointment(staticUserId , appointmentName, appointmentWithUserId).enqueue(object : Callback<Appointment> {
            override fun onResponse(call: Call<Appointment>, response: Response<Appointment>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    response.body()?.let { updated ->
                        _appointment.value = updated
                        _appointmentList.value = _appointmentList.value?.map {
                            if (it.name == appointmentName) updated else it
                        }
                        Log.d("AppointmentViewModels", "Appointment updated successfully: $updated")
                    }
                } else {
                    handleError(response)
                }
            }

            override fun onFailure(call: Call<Appointment>, t: Throwable) {
                _isLoading.value = false
                _errorMessage.value = "Error updating Appointment: ${t.message}"
                Log.e("AppointmentViewModels", "Error updating Appointment: ${t.message}")
            }
        })
    }
    fun deleteAppointment(appointmentName: String) {
        _isLoading.value = true
        RetrofitInstance.api.deleteAppointment(staticUserId, appointmentName).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    // Remove the deleted disease from the list
                    _diseaseList.value = _diseaseList.value?.filter { it.name != appointmentName }
                    Log.d("AppointmentViewModels", "Appointment deleted successfully")
                } else {
                    handleError(response)
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                _isLoading.value = false
                _errorMessage.value = "Error deleting Appointment: ${t.message}"
                Log.e("AppointmentViewModels", "Error deleting Appointment: ${t.message}")
            }
        })
    }
}
