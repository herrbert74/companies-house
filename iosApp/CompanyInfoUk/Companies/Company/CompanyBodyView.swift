import SwiftUI
import shared

struct CompanyBodyView: View {
    
    @EnvironmentObject var theme : Theme
    
    private let company: Company
    private let onMapClicked: (String) -> Void
    private let onChargesClicked: (String) -> Void
    private let onFilingsClicked: (String) -> Void
    private let onInsolvenciesClicked: (String) -> Void
    private let onOfficersClicked: (String) -> Void
    private let onPersonsClicked: (String) -> Void
    
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
        self.onMapClicked = onMapClicked
        self.onChargesClicked = onChargesClicked
        self.onFilingsClicked = onFilingsClicked
        self.onInsolvenciesClicked = onInsolvenciesClicked
        self.onOfficersClicked = onOfficersClicked
        self.onPersonsClicked = onPersonsClicked
    }
    
    var body: some View {
        
        VStack(alignment: .leading) {
            Text("\(company.companyNumber)").titleLargeStyle()
            Divider()
            Text("incorporated_on \(company.dateOfCreation)").bodyMediumStyle()
            Divider()
            
            AddressCard(company: company, onShowMap: { })
            
            Divider()
            
            TwoLineView(firstLineString: "Nature of business", secondLineString: "\(company.natureOfBusiness)", theme: theme).padding(.bottom, 8).frame(maxWidth: .infinity)
            
            Divider()
            
            TwoLineView(firstLineString: "Accounts", secondLineString: "\(company.lastAccountsMadeUpTo)", theme: theme).padding(.bottom, 8).frame(maxWidth: .infinity)
            
            Divider()
            
            Button(action: { onChargesClicked(company.companyNumber) }) {
                Text("Charges")
            }
            Divider()
            Button(action: { onFilingsClicked(company.companyNumber) }) {
                Text("Filings")
            }
            Divider()
            Button(action: { onInsolvenciesClicked(company.companyNumber) }) {
                Text("Insolvencies")
            }
            Divider()
            Button(action: { onOfficersClicked(company.companyNumber) }) {
                Text("Officers")
            }
            Divider()
            Button(action: { onPersonsClicked(company.companyNumber) }) {
                Text("Persons")
            }
            
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
