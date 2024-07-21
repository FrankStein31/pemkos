package com.im.pemkos.ui.profil.ubahpassword

import android.app.Activity
import android.util.Log
import com.im.pemkos.network.ApiInteractor
import com.im.pemkos.network.ApiService
import com.im.pemkos.utils.CustomProgress
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers


class UbahPasswordPresenter(private  val activity: Activity, private val view: UbahPasswordView) {


    private val apiServices = ApiService.client.create(ApiInteractor::class.java)
    private var subscription: Disposable? = null
    var customProgress = CustomProgress(activity)


    fun onViewDestroyed() {
        subscription?.dispose()
    }

    fun ubahPassword(oldPwd: String, newPwd: String, id: String){
        try{
            subscription = apiServices.ubahPassword(oldPassword = oldPwd, newPassword = newPwd, id = id)
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
                            Log.e("RegisterPresenter","${it.message}")
                            view.successUbahPassword()
                        }
                        else view.showMessage(it.message)
                    },
                    {

                        customProgress.hideLoading()
                        view.showMessage(it.message.toString())
                        Log.e("RegisterPresenter","${it.message} ${it.stackTrace}")
                    }
                )
        } catch (e: Exception) {

            customProgress.hideLoading()
            view.showMessage(e.message.toString())
            Log.e("RegisterPresenter","Error ${e.message}")
        }
    }


}