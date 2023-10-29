import SwiftUI
import shared

struct ChargesList: View {
    
    var onItemClicked: (ChargesItem) -> Void
    
    @EnvironmentObject var theme : Theme
    
    private let items: [ChargesItem]
    
    init(
        _ items: [ChargesItem],
        onItemClicked: @escaping (ChargesItem) -> Void
    ) {
        self.items = items
        self.onItemClicked = onItemClicked
    }
    
    var body: some View {
        List(items, id: \.self.chargeCode) { item in
            ChargesListItem(item: item)
        }
        .frame(maxWidth: .infinity, minHeight: 1000)
        .background(.white)
        .scrollContentBackground(.hidden)
    }
    
}

struct ChargesList_Previews: PreviewProvider {
    
    static let themeManager = ThemeManager()
    
    static var previews: some View {
        ChargesList(
            [
                ChargesItem(
                    chargeCode: "095699920001",
                    createdOn: "2012-03-12",
                    deliveredOn: "2012-03-12",
                    personsEntitled: "John Doe",
                    resolvedOn: "2012-03-12",
                    satisfiedOn: "",
                    status: "Outstanding",
                    transactions: [],
                    particulars: Particulars(
                        containsFixedCharge: false,
                        containsFloatingCharge: false,
                        containsNegativePledge: false,
                        description: "",
                        floatingChargeCoversAll: false,
                        type: ""
                    )
                ),
                ChargesItem(
                    chargeCode: "095699920002",
                    createdOn: "2012-03-12",
                    deliveredOn: "2012-03-12",
                    personsEntitled: "John Doe",
                    resolvedOn: "2012-03-12",
                    satisfiedOn: "2013-03-12",
                    status: "Satisfied",
                    transactions: [],
                    particulars: Particulars(
                        containsFixedCharge: false,
                        containsFloatingCharge: false,
                        containsNegativePledge: false,
                        description: "",
                        floatingChargeCoversAll: false,
                        type: ""
                    )
                )
            ],
            onItemClicked: {_ in }
        )
        .environmentObject(themeManager.current)
        .padding(.top, 200)
    }
    
}
