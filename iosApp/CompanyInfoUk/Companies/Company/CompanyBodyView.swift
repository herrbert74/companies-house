import SwiftUI
import shared

struct CompanyBodyView: View {
    
    @EnvironmentObject var theme : Theme
    
    private let company: Company
    
    init(
        company: Company,
        onMapClicked: @escaping (String) -> Void,
        onChargesClicked: @escaping (String) -> Void,
        onFilingsClicked: @escaping (String) -> Void,
        onInsolvenciesClicked: @escaping (String) -> Void,
        onOfficersClicked: @escaping (String) -> Void,
        onPersonsClicked: @escaping (String) -> Void
    ) {
        self.company = company
    }
    
    var body: some View {
        
        VStack(alignment: .leading) {
            Text("\(company.companyName)").displaySmallStyle().padding()
            Text("incorporated_on \(company.dateOfCreation)").bodyMediumStyle()
            Divider()
            Text("\(company.companyNumber)").bodyMediumStyle()
            Divider()
            Text("Company Address").bodyMediumStyle()
            Text("\(company.registeredOfficeAddress.addressLine1)").bodyLargeStyle()
        }.padding()
    }
    
}

struct CompanyBodyView_Previews: PreviewProvider {
    
    static let themeManager = ThemeManager()
    
    static var previews: some View {
        CompanyBodyView(
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
            onMapClicked: {_ in },
            onChargesClicked: {_ in },
            onFilingsClicked: {_ in },
            onInsolvenciesClicked: {_ in },
            onOfficersClicked: {_ in },
            onPersonsClicked: {_ in }
        )
        .environmentObject(themeManager.current)
    }
    
}
