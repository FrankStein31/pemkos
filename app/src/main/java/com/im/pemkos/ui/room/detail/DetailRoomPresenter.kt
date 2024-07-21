package com.im.pemkos.ui.room.detail

import android.app.Activity
import android.util.Log
import com.im.pemkos.network.ApiInteractor
import com.im.pemkos.network.ApiService
import com.im.pemkos.utils.CustomProgress
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers


class DetailTeacherPresenter(private  val activity: Activity, private val view: DetailRoomView) {

    private val apiServices = ApiService.client.create(ApiInteractor::class.java)
    private var subscription: Disposable? = null

    var customProgress: CustomProgress = CustomProgress(activity)



    fun onViewDestroyed() {
        subscription?.dispose()
    }

    fun deleteRoom(id: String){
        try{
            Log.d("DetailAbkPresenter", " id room $id")
            subscription = apiServices.deleteRoom(id)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe {
                        customProgress.showLoading()
                    }
                    .subscribe(
                            {

                                Log.d("DetailAbkPresenter", " response $it")
                                customProgress.hideLoading()
                                if(it.success)
                                {
                                    view.successDelete()
                                }
                                else view.showMessage(it.message)
                            },
                            {

                                customProgress.hideLoading()
                                view.showMessage(it.message.toString())
                                Log.e("MainPresenter","Error ${it.stackTrace}")
                            }
                    )
        } catch (e: Exception) {

            customProgress.hideLoading()
            view.showMessage(e.message.toString())
            Log.e("MainPresenter","Error ${e.message}")
        }
    }

   


}