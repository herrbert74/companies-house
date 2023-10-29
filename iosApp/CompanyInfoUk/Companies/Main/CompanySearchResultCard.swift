import SwiftUI
import shared

struct CompanySearchResultCard: View {
    
    @EnvironmentObject var theme : Theme
    
    private let item: CompanySearchResultItem
    
    init(
        item: CompanySearchResultItem
    ) {
        self.item = item
    }
    
    var body: some View {
        
        HStack{
            VStack(alignment: .leading) {
                Text(item.title ?? "")
                    .padding([.top, .leading, .trailing], 8)
                    .multilineTextAlignment(.leading)
                    .titleMediumStyle()
                Text(item.description_ ?? "")
                    .padding([.top, .leading, .trailing], 8)
                    .multilineTextAlignment(.leading)
                    .bodyLargeStyle()
                Text(item.addressSnippet ?? "")
                    .padding([.top, .leading, .trailing], 8)
                    .multilineTextAlignment(.leading)
                    .bodyMediumStyle()
            }
            .frame(maxWidth: .infinity)
            Text(item.companyStatus ?? "")
                .padding([.top, .leading, .trailing], 24)
                .multilineTextAlignment(.leading)
                .bodyLargeStyle()
        }
        .padding()
        .background(Rectangle().fill(Color.white))
        .cornerRadius(10)
        .shadow(color: .gray, radius: 3, x: 2, y: 2)
    }
    
}

struct MainListItem_Previews: PreviewProvider {
    
    static let themeManager = ThemeManager()
    
    static var previews: some View {
        VStack(alignment: .leading) {
            CompanySearchResultCard(
                item: CompanySearchResultItem(
                    descriptionIdentifier: [],
                    dateOfCreation: nil,
                    snippet: nil,
                    companyNumber: nil,
                    title: "ALPHABET ACCOUNTANTS LTD",
                    companyStatus: "active",
                    matches: nil,
                    address: nil,
                    description: "07620277 - Incorporated on  3 May 2011",
                    kind: nil,
                    companyType: nil,
                    addressSnippet: "16  Anchor Street, Chelmsford, CM2 0JY"
                )
            )
        }.padding()
        .environmentObject(themeManager.current)
    }
    
}

struct Dissolved_MainListItem_Previews: PreviewProvider {
    
    static let themeManager = ThemeManager()
    
    static var previews: some View {
        VStack(alignment: .leading) {
            CompanySearchResultCard(
                item: CompanySearchResultItem(
                    descriptionIdentifier: [],
                    dateOfCreation: nil,
                    snippet: nil,
                    companyNumber: nil,
                    title: "ALPHABET ACCOUNTANTS LTD",
                    companyStatus: "dissolved",
                    matches: nil,
                    address: nil,
                    description: "07620277 - Incorporated on  3 May 2011",
                    kind: nil,
                    companyType: nil,
                    addressSnippet: "16  Anchor Street, Chelmsford, CM2 0JY"
                )
            )
        }.padding()
        .environmentObject(themeManager.current)
    }
    
}
