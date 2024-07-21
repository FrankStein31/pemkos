package com.im.pemkos.ui.tenant.add

import android.app.Activity
import android.util.Log
import com.google.gson.JsonParser
import com.im.pemkos.network.ApiInteractor
import com.im.pemkos.network.ApiService
import com.im.pemkos.network.request.AddTenantRequest
import com.im.pemkos.utils.CustomProgress
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException


class AddTenantPresenter(private  val activity: Activity, private val view: AddTenantView) {

    private val apiServices = ApiService.client.create(ApiInteractor::class.java)
    private var subscription: Disposable? = null

    var customProgress: CustomProgress = CustomProgress(activity)

    fun onViewDestroyed() {
        subscription?.dispose()
    }

    fun addTenant(data: AddTenantRequest){
        try{
            Log.d("AbkPresenter", " id sekolah $data")
            subscription = apiServices.addTenant(data)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe {
                        customProgress.showLoading()
                    }
                    .subscribe(
                            {

                                Log.d("AbkPresenter", " response $it")
                                customProgress.hideLoading()
                                if(it.success)
                                {
                                    view.successAdd()
                                }
                                else view.showMessage(it.message)
                            },
                            {

                                customProgress.hideLoading()
                                var message = it.message.toString()
                                if(it is HttpException) {
                                    val errorJsonString = it.response()?.errorBody()?.string()
                                    message = JsonParser.parseString(errorJsonString)
                                            .asJsonObject["message"]
                                            .asString
                                }
                                view.showMessage(message)
                                Log.e("MainPresenter","Error ${it.message}")
                            }
                    )
        } catch (e: Exception) {

            customProgress.hideLoading()
            view.showMessage(e.message.toString())
            Log.e("MainPresenter","Error ${e.message}")
        }
    }

    fun getRoom(){
        try{
            subscription = apiServices.room()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    customProgress.showLoading()
                }
                .subscribe(
                    {

                        Log.d("AbkPresenter", " response $it")
                        customProgress.hideLoading()
                        if(it.status)
                        {
                            view.showRoom(it.data)
                        }
                        else view.showMessage(it.message)
                    },
                    {

                        customProgress.hideLoading()
                        view.showMessage(it.message.toString())
                        Log.e("MainPresenter","Error ${it.message}")
                    }
                )
        } catch (e: Exception) {

            customProgress.hideLoading()
            view.showMessage(e.message.toString())
            Log.e("MainPresenter","Error ${e.message}")
        }
    }
}