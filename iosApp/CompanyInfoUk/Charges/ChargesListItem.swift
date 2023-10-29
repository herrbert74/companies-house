import SwiftUI
import shared

struct ChargesListItem: View {
    
    @EnvironmentObject var theme : Theme
    
    private let item: ChargesItem
    
    init(
        item: ChargesItem
    ) {
        self.item = item
    }
    
    var body: some View {
        
        HStack{
            VStack(alignment: .leading) {
                TwoLineView(firstLineString: "Charge code", secondLineString: "\(item.chargeCode)", theme: theme).padding(.bottom, 8).frame(maxWidth: .infinity)
                TwoLineView(firstLineString: "Created on", secondLineString: "\(item.createdOn)", theme: theme).padding(.bottom, 8)
                if(item.status == "Satisfied") {
                    TwoLineView(firstLineString: "Satisfied on", secondLineString: "\(item.satisfiedOn)", theme: theme).padding(.bottom, 8)
                }
                TwoLineView(firstLineString: "Persons entitled", secondLineString: "\(item.personsEntitled)", theme: theme)
            }
            .frame(maxWidth: .infinity)
            if(item.status == "Satisfied") {
                Image("ic_baseline_sentiment_satisfied").padding([.leading, .trailing], 8)
            } else {
                Image("ic_baseline_sentiment_very_dissatisfied").padding([.leading, .trailing], 8)
            }
        }
        .padding()
        .background(Rectangle().fill(Color.white))
        .cornerRadius(10)
        .shadow(color: .gray, radius: 3, x: 2, y: 2)
    }
    
}

struct ChargesListItem_Previews: PreviewProvider {
    
    static let themeManager = ThemeManager()
    
    static var previews: some View {
        VStack(alignment: .leading) {
            ChargesListItem(
                item: ChargesItem(
                    chargeCode: "095699920001",
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
            )
        }.padding()
        .environmentObject(themeManager.current)
    }
    
}

struct Dissatisfied_ChargesListItem_Previews: PreviewProvider {
    
    static let themeManager = ThemeManager()
    
    static var previews: some View {
        VStack(alignment: .leading) {
            ChargesListItem(
                item: ChargesItem(
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
                )
            )
        }.padding()
        .environmentObject(themeManager.current)
    }
    
}
