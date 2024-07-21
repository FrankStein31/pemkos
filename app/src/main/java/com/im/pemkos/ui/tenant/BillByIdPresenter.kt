package com.im.pemkos.ui.tenant

import android.app.Activity
import android.util.Log
import com.im.pemkos.network.ApiInteractor
import com.im.pemkos.network.ApiService
import com.im.pemkos.ui.bill.BillByIdView
import com.im.pemkos.utils.CustomProgress
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class BillByIdPresenter(private  val activity: Activity, private val view: BillByIdView)  {

    private val apiServices = ApiService.client.create(ApiInteractor::class.java)
    private var subscription: Disposable? = null

    var customProgress: CustomProgress = CustomProgress(activity)



//    fun getBillUser(id: String){
//        try{
//            subscription = apiServices.billUser(id)
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
//                        if(it.status!!)
//                        {
//                            view.showBillById(it.data!!)
//                        }
//                        else view.showMessageBill(it.message!!)
//                    },
//                    {
//
//                        customProgress.hideLoading()
//                        view.showMessageBill(it.message!!)
//                        Log.e("MainPresenter","Error ${it.message}")
//                    }
//                )
//        } catch (e: Exception) {
//
//            customProgress.hideLoading()
//            view.showMessageBill(e.message.toString())
//            Log.e("MainPresenter","Error ${e.message}")
//        }
//    }


}