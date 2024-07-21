package com.im.pemkos.network

import com.im.pemkos.network.request.AddKostRequest
import com.im.pemkos.network.request.AddTenantRequest
import com.im.pemkos.network.request.AddRoomRequest
import com.im.pemkos.network.response.*
import io.reactivex.Observable
import retrofit2.http.*

interface ApiInteractor {

    @GET("bill/filter")
    fun filterBill(
        @Query("status") status: String,
        @Query("selectedMonth") selectedMonth: String,
        @Query("selectedYear") selectedYear: String
    ): Observable<BillResponse>

    @GET("pembayaran/filter")
    fun filterPembayaran(
        @Query("metode") metode_pembayaran: String,
        @Query("selectedMonth") selectedMonth: String,
        @Query("selectedYear") selectedYear: String
    ): Observable<PembayaranResponse>

    @FormUrlEncoded
    @POST("bill/update_status")
    fun setStatus(@Field("id_bill") id_bill: String, @Field("metode_pembayaran") metode_pembayaran: String): Observable<StatusResponse>
    @FormUrlEncoded
    @POST("bill/get_snap_token")
    fun payment(@Field("id_bill") id_bill: String,): Observable<CheckoutResponse>
    @FormUrlEncoded
    @POST("auth/login")
    fun login(@Field("username") email: String, @Field("password") password: String): Observable<LoginResponse>

    @GET("kos")
    fun kost(): Observable<KostResponse>

    @POST("kos/add")
    fun addKost(@Body data: AddKostRequest): Observable<AddKostResponse>


    @GET("kos/delete/{id}")
    fun deleteKos(@Path("id") id: String): Observable<GeneralResponse>

    @GET("AnakKos")
    fun tenant(): Observable<TenantResponse>

    @FormUrlEncoded
    @POST("AnakKos/search")
    fun searchTenant(@Field("id") id: String, @Field("keyword") keyword: String): Observable<TenantResponse>

    @POST("AnakKos/add")
    fun addTenant(@Body data: AddTenantRequest): Observable<AddTenantResponse>

    @POST("AnakKos/edit")
    fun editTenant(@Body data: AddTenantRequest): Observable<AddTenantResponse>

    @DELETE("AnakKos/delete/{id}")
    fun deleteTenant(@Path("id") id: String): Observable<DeleteResponse>

    @GET("kamar")
    fun room(): Observable<RoomResponse>

    @FormUrlEncoded
    @POST("kamar/search")
    fun searchRoom(@Field("id") id: String, @Field("keyword") keyword: String): Observable<RoomResponse>

    @GET("kamar/delete/{id}")
    fun deleteRoom(@Path("id") id: String): Observable<GeneralResponse>

    @POST("kamar/add")
    fun addRoom(@Body data: AddRoomRequest): Observable<AddRoomResponse>

    @POST("kamar/edit")
    fun editRoom(@Body data: AddRoomRequest): Observable<AddRoomResponse>

    @GET("bill")
    fun bill(): Observable<BillResponse>

    @GET("bill/user/{id}")
    fun billUser(@Path("id") id: String): Observable<BillResponse>

    @FormUrlEncoded
    @POST("bill/generate")
    fun generateBill(@Field("bulan") month: Int, @Field("tahun") year: Int): Observable<BillResponse>

    @FormUrlEncoded
    @POST("profil/edit")
    fun ubahProfil(
            @Field("nama_sekolah") name: String,
            @Field("email") email: String,
            @Field("id_sekolah") id: String,
            @Field("nama__kepala_sekolah") headName: String,
            @Field("foto") photo: String? = null,
            @Field("alamat") address: String
    ): Observable<UbahProfilResponse>

    @FormUrlEncoded
    @POST("profil/ubahpassword")
    fun ubahPassword(@Field("password_lama") oldPassword: String, @Field("password_baru") newPassword: String, @Field("id") id: String): Observable<GeneralResponse>

}