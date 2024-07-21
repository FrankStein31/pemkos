package com.im.pemkos.ui.profil

import android.app.Activity
import android.util.Log
import com.google.gson.JsonParser
import com.im.pemkos.network.ApiInteractor
import com.im.pemkos.network.ApiService
import com.im.pemkos.utils.CustomProgress
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException


class ProfilPresenter(private  val activity: Activity, private val view: ProfilView) {


    private val apiServices = ApiService.client.create(ApiInteractor::class.java)
    private var subscription: Disposable? = null
    var customProgress = CustomProgress(activity)


    fun onViewDestroyed() {
        subscription?.dispose()
    }

    fun ubahProfil(id: String, name: String, email: String, address: String, photo: String?, headName: String){
        try{
            subscription = apiServices.ubahProfil(name = name, email = email, id = id, address = address, photo = photo, headName = headName)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    customProgress.showLoading()
                }
                .subscribe(
                    {
                        customProgress.hideLoading()
                        Log.e("ProfilPresenter","${it}")
                        if(it.success)
                        {
                            Log.e("ProfilPresenter","${it.message}")
                            view.successUbahProfil(it.data)
                        }
                        else view.showMessage(it.message)
                    },
                    {

                        customProgress.hideLoading()
                        Log.e("ProfilPresenter","Error ${it.message}")
                        var message = it.message.toString()
                        if(it is HttpException) {
                            val errorJsonString = it.response()?.errorBody()?.string()
                            Log.e("ProfilPresenter","Error ${errorJsonString}")
                            message = JsonParser.parseString(errorJsonString)
                                    .asJsonObject["message"]
                                    .asString
                        }
                        view.showMessage(message)
                    }
                )
        } catch (e: Exception) {

            customProgress.hideLoading()
            view.showMessage(e.message.toString())
            Log.e("ProfilPresenter","Error ${e.message}")
        }
    }


}