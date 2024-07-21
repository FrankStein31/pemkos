package com.im.pemkos.ui.bill

import android.app.Activity
import android.util.Log
import com.im.pemkos.network.ApiInteractor
import com.im.pemkos.network.ApiService
import com.im.pemkos.utils.CustomProgress
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers


class BillPresenter(private  val activity: Activity, private val view: BillView) {

    private val apiServices = ApiService.client.create(ApiInteractor::class.java)
    private var subscription: Disposable? = null

    var customProgress: CustomProgress = CustomProgress(activity)



    fun onViewDestroyed() {
        subscription?.dispose()
    }

    fun getBill(){
        try{
            subscription = apiServices.bill()
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
                                    view.showBill(it.data)
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


//    fun getFilterBill(status: String, selectedMonth: String, selectedYear: String){
//        try{
//            subscription = apiServices.filterBill(status, selectedMonth, selectedYear)
//                .subscribeOn(Schedulers.newThread())
//                .observeOn(AndroidSchedulers.mainThread())
//                .doOnSubscribe {
//                    customProgress.showLoading()
//                }
//                .subscribe(
//                    {
//
//                        Log.d("AbkPresenter", " response $it")
//                        customProgress.hideLoading()
//                        if(it.status)
//                        {
//                            view.showBill(it.data)
//                        }
//                        else view.showMessage(it.message)
//                    },
//                    {
//
//                        customProgress.hideLoading()
//                        view.showMessage(it.message.toString())
//                        Log.e("MainPresenter","Error ${it.message}")
//                    }
//                )
//        } catch (e: Exception) {
//
//            customProgress.hideLoading()
//            view.showMessage(e.message.toString())
//            Log.e("MainPresenter","Error ${e.message}")
//        }
//    }

    fun getFilterBill(status: String, selectedMonth: String, selectedYear: String) {
        try {
            subscription = apiServices.filterBill(status, selectedMonth, selectedYear)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    customProgress.showLoading()
                }
                .subscribe(
                    { response ->
                        Log.d("AbkPresenter", "response $response")
                        customProgress.hideLoading()
                        if (response.status) {
                            val filteredBills = response.data
                            view.showBill(filteredBills)
                        } else {
                            view.showMessage(response.message)
                        }
                    },
                    { error ->
                        customProgress.hideLoading()
                        view.showMessage(error.message.toString())
                        Log.e("MainPresenter", "Error ${error.message}")
                    }
                )
        } catch (e: Exception) {
            customProgress.hideLoading()
            view.showMessage(e.message.toString())
            Log.e("MainPresenter", "Error ${e.message}")
        }
    }

    fun generateBill(month: Int, year: Int){
        try{
            subscription = apiServices.generateBill(month = month, year = year)
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
                                    view.showBill(it.data)
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

    fun getBillUser(id: String){
        try{
            subscription = apiServices.billUser(id)
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
                            view.showBill(it.data)
                        }
                        else view.showMessage(it.message)
                    },
                    {

                        customProgress.hideLoading()
                        view.showMessage(it.message!!)
                        Log.e("MainPresenter","Error ${it.message}")
                    }
                )
        } catch (e: Exception) {

            customProgress.hideLoading()
            view.showMessage(e.message.toString())
            Log.e("MainPresenter","Error ${e.message}")
        }
    }
//    fun getBillUser(id: String) {
//        try {
//            subscription = apiServices.billUser(id)
//                .subscribeOn(Schedulers.newThread())
//                .observeOn(AndroidSchedulers.mainThread())
//                .doOnSubscribe {
//                    customProgress.showLoading()
//                }
//                .subscribe(
//                    { response ->
//                        Log.d("AbkPresenter", " response $response")
//                        customProgress.hideLoading()
//                        if (response.status) {
//                            view.showBillUser(response.data)
//                        } else view.showMessage(response.message)
//                    },
//                    { error ->
//                        customProgress.hideLoading()
//                        view.showMessage(error.message ?: "Unknown error")
//                        Log.e("MainPresenter", "Error ${error.message}")
//                    }
//                )
//        } catch (e: Exception) {
//            customProgress.hideLoading()
//            view.showMessage(e.message.toString())
//            Log.e("MainPresenter", "Error ${e.message}")
//        }
//    }



   


}