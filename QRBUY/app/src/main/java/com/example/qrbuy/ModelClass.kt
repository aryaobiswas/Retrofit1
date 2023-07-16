package com.example.qrbuy

import java.io.Serializable

class ModelClass(
    var userId: String,
    var price: Float?,
    var id: Long?,
    var url: String?,
    var transactionDate: String?,
    var transactionID: String?,
    var orderStatus: Boolean?
) : Serializable {

    constructor() : this("", null, null, null, "", "", null)

}
