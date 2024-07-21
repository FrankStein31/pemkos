package com.im.pemkos.ui.login

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


class LoginPresenter(private  val activity: Activity, private val view: LoginView) {

    private val apiServices = ApiService.client.create(ApiInteractor::class.java)

    private var subscription: Disposable? = null
    var customProgress = CustomProgress(activity)


    fun onViewDestroyed() {
        subscription?.dispose()
    }

    fun login(email: String, password: String){
        try{
            subscription = apiServices.login(email, password)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    customProgress.showLoading()
                }
                .subscribe(
                    {
                        customProgress.hideLoading()
                        if(it.success)
                        {
                            view.setData(it.data!!)
                        }
                        else view.showMessage(it.message)
                    },
                    {
                        customProgress.hideLoading()
                        Log.e("LoginPresenter","Error ${it.message}")
                        var message = it.message.toString()
                        if(it is HttpException) {
                            val errorJsonString = it.response()?.errorBody()?.string()
                            Log.e("LoginPresenter","Error ${errorJsonString}")
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
            Log.e("LoginPresenter","Error ${e.message}")
        }
    }


}