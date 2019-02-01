package com.techapp.james.buybuygo.view.commonFragment

import com.techapp.james.buybuygo.model.data.CountryWrapper

object AreaParameter {
    var countryWrapperList = ArrayList<CountryWrapper>()
    fun findCountryWrapper(s: String): CountryWrapper? {
        return countryWrapperList.find { it ->
            it.country == s
        }
    }
}