package com.babestudios.companyinfouk.ui.insolvencydetails.list

import android.os.Parcelable
import com.babestudios.companyinfo.data.model.insolvency.Practitioner
import kotlinx.android.parcel.Parcelize

@Parcelize
data class InsolvencyDetailsTitleItem(val title: String) : Parcelable
@Parcelize
data class InsolvencyDetailsDateItem(val date: String?, val type:String?) : Parcelable
@Parcelize
data class InsolvencyDetailsPractitionerItem(val practitioner: Practitioner) : Parcelable