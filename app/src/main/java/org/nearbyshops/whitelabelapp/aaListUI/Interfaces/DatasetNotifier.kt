package org.nearbyshops.whitelabelapp.aaListUI.Interfaces

interface DatasetNotifier {

//    dataset:List<Any>
    fun notifyDatasetChanged()
    fun notifyRequestFailedWithErrorCode(code:Int)
    fun notifyRequestFailed()
    fun showEmptyScreen()

    fun updateItemCount(itemCount: Int)
    fun showMessage(message:String)
}