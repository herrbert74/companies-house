package com.babestudios.companyinfouk.insolvencies.ui.details.list

import android.os.Parcelable
import com.babestudios.companyinfouk.common.model.insolvency.Practitioner
import kotlinx.android.parcel.Parcelize

@Parcelize
data class InsolvencyDetailsTitleItem(val title: String) : Parcelable
@Parcelize
data class InsolvencyDetailsDateItem(val date: String?, val type:String?) : Parcelable
@Parcelize
data class InsolvencyDetailsPractitionerItem(val practitioner: Practitioner) : Parcelable
