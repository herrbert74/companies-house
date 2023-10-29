import SwiftUI
import shared

struct AddressCard: View {
    
    @EnvironmentObject var theme : Theme
    
    private let company: Company
    private let onShowMap: () -> Void
    
    init(
        company: Company,
        onShowMap: @escaping () -> Void
    ) {
        self.company = company
        self.onShowMap = onShowMap
    }
    
    var body: some View {
        HStack(alignment: .bottom) {
            VStack(alignment: .leading) {
                Text("Office Address").bodyMediumStyle().padding([.bottom, .leading], 16)
                Text("\(company.registeredOfficeAddress.addressLine1)").titleSmallStyle().padding([.bottom], 8)
                
                let addressLine2: String? = company.registeredOfficeAddress.addressLine2
                if let addressLine2: String = addressLine2 {
                    Text(addressLine2).padding([.bottom], 8)
                }
                
                Text("\(company.registeredOfficeAddress.locality)").titleSmallStyle().padding([.bottom], 8)
                Text("\(company.registeredOfficeAddress.postalCode)").titleSmallStyle().padding([.bottom], 8)
                
                let region: String? = company.registeredOfficeAddress.region
                
                if let region = region {
                    Text(region).titleSmallStyle().padding([.bottom], 8)
                }
                
            }
            Button(action: onShowMap, label: {
                Text("SHOW ON MAP")
            }).padding([.bottom], 8)
        }
    }
    
}

struct AddressCard_Previews: PreviewProvider {
    
    static let themeManager = ThemeManager()
    
    static var previews: some View {
        AddressCard(
            company: Company(
                companyName : "Pbf Hire",
                lastAccountsMadeUpTo : "Last Full account",// made up to 31 July 2022"
                companyNumber : "0234567",
                dateOfCreation : "2012-02-02",
                hasCharges: false,
                hasInsolvencyHistory: false,
                registeredOfficeAddress : Address(
                    addressLine1 : "1 Harewood Street",
                    addressLine2 : nil,
                    country : "England",
                    locality : "Leeds",
                    postalCode : "LS2 7AD",
                    region : "West Yorkshire"
                ),
                natureOfBusiness : "64209 Activities of other holding companies not elsewhere classified"
            ),
            onShowMap: { }
        )
        .environmentObject(themeManager.current)
    }
    
}

