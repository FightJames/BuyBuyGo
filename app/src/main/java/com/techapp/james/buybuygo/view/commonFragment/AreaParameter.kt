package com.techapp.james.buybuygo.view.commonFragment

import com.techapp.james.buybuygo.model.data.buyer.AreaWrapper
import com.techapp.james.buybuygo.model.data.buyer.CountryWrapper

object AreaParameter {
    var countryWrapperList = ArrayList<CountryWrapper>()
    var areaWrapperList = ArrayList<AreaWrapper>()
    fun findCountryWrapperByCountry(s: String): CountryWrapper {
        return countryWrapperList.find { it ->
            it.country == s
        }!!
    }

    fun findCountryWrapperByCountryCode(s: String): CountryWrapper {
        return countryWrapperList.find { it ->
            it.countryCode == s
        }!!
    }

    fun findRelationAreaWrappers(s: String): ArrayList<AreaWrapper> {
        var list = ArrayList<AreaWrapper>()
        for (i in areaWrapperList.indices) {
            if (areaWrapperList[i].city.equals(s)) {
                list.add(areaWrapperList[i])
            }
        }
        return list
    }

    fun findAreaWrappers(city: String, area: String): AreaWrapper? {
        for (i in areaWrapperList.indices) {
            if (areaWrapperList[i].city.equals(city) &&
                areaWrapperList[i].area.equals(area)
            ) {
                return areaWrapperList[i]
            }
        }
        return null
    }
}