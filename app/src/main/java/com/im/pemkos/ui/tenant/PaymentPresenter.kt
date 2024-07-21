package com.im.pemkos.ui.tenant

import android.app.Activity
import android.util.Log
import com.im.pemkos.network.ApiInteractor
import com.im.pemkos.network.ApiService
import com.im.pemkos.utils.CustomProgress
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class PaymentPresenter(private  val activity: Activity, private val view: PaymentView) {


    private val apiServices = ApiService.client.create(ApiInteractor::class.java)
    private var subscription: Disposable? = null

    var customProgress: CustomProgress = CustomProgress(activity)



    fun onViewDestroyed() {
        subscription?.dispose()
    }

    fun processPayment(idBill: String) {
        try {
            subscription = apiServices.payment(idBill)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    customProgress.showLoading()
                }
                .subscribe(
                    { response ->
                        Log.d("PaymentPresenter", "response $response")
                        customProgress.hideLoading()
                        if (response.status == true) {
                            view.showPaymentSuccess(response, idBill)
                        } else {
                            view.showMessagepayment("Payment failed")
                        }
                    },
                    { throwable ->
                        customProgress.hideLoading()
                        view.showMessagepayment(throwable.message.toString())
                        Log.e("PaymentPresenter", "Error ${throwable.message}")
                    }
                )
        } catch (e: Exception) {
            customProgress.hideLoading()
            view.showMessagepayment(e.message.toString())
            Log.e("PaymentPresenter", "Error ${e.message}")
        }
    }

}