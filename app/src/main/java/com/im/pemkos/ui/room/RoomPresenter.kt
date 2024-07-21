package com.im.pemkos.ui.room

import android.app.Activity
import android.util.Log
import com.im.pemkos.network.ApiInteractor
import com.im.pemkos.network.ApiService
import com.im.pemkos.utils.CustomProgress
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers


class RoomPresenter(private  val activity: Activity, private val view: RoomView) {

    private val apiServices = ApiService.client.create(ApiInteractor::class.java)
    private var subscription: Disposable? = null

    var customProgress: CustomProgress = CustomProgress(activity)



    fun onViewDestroyed() {
        subscription?.dispose()
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

    fun searchTeacher(id: String, keyword: String){
        try{
            subscription = apiServices.searchRoom(id = id, keyword = keyword)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe {
                        customProgress.showLoading()
                    }
                    .subscribe(
                            {

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