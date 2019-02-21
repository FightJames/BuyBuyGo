package com.techapp.james.buybuygo.view.choose

import com.techapp.james.buybuygo.model.data.common.UserStatus

interface ChooseView {
    fun isLoad(flag: Boolean)
    fun intentToBuyer()
    fun intentToSeller()
    fun autoIntent(userStatus: UserStatus)
    fun continueOperation(okPress: () -> Unit, cancelPress: () -> Unit)
    fun showRequestMessage(message: String)
}