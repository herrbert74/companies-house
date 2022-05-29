package com.babestudios.companyinfouk.navigation

sealed class DeepLinkDestination(val address: String) {

	class Map(name: String, address: String) :
		DeepLinkDestination("companyinfo://map?name=${name}&address=${address}")

	class Company(companyNumber: String, companyName: String) :
		DeepLinkDestination("companyinfo://company?number=${companyNumber}&name=${companyName}")

}
