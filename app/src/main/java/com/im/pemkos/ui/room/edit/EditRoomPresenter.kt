package com.im.pemkos.ui.room.edit

import android.app.Activity
import android.util.Log
import com.google.gson.JsonParser
import com.im.pemkos.network.ApiInteractor
import com.im.pemkos.network.ApiService
import com.im.pemkos.network.request.AddRoomRequest
import com.im.pemkos.utils.CustomProgress
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException


class EditRoomPresenter(private  val activity: Activity, private val view: EditRoomView) {

    private val apiServices = ApiService.client.create(ApiInteractor::class.java)
    private var subscription: Disposable? = null

    var customProgress: CustomProgress = CustomProgress(activity)



    fun onViewDestroyed() {
        subscription?.dispose()
    }

    fun editRoom(data: AddRoomRequest){
        try{
            Log.d("AbkPresenter", " id room $data")
            subscription = apiServices.editRoom(data)
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
                                    view.successEdit()
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
                                Log.e("EditAbkPresenter","Error ${it.message}")
                                Log.e("EditAbkPresenter","Error ${it.printStackTrace()}")
                            }
                    )
        } catch (e: Exception) {

            customProgress.hideLoading()
            view.showMessage(e.message.toString())
            Log.e("EditAbkPresenter","Error ${e.message}")
        }
    }


    fun getKost(){
        try{
            subscription = apiServices.kost()
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
                            view.showKost(it.data)
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