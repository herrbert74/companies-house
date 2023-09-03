import SwiftUI
import shared

struct RecentSearchesList: View {
    
    var onItemClicked: () -> Void
    
    @EnvironmentObject var theme : Theme
    
    private let items: [SearchHistoryItem]
    @State private var isClearRecentsDialogVisible = true
    
    init(
        _ items: [SearchHistoryItem],
        onItemClicked: @escaping () -> Void,
        isClearRecentsDialogVisible: Bool
    ) {
        self.items = items
        self.onItemClicked = onItemClicked
        self.isClearRecentsDialogVisible = isClearRecentsDialogVisible
    }
    
    var body: some View {
        ZStack {
            VStack{
                Text(StringConstantsKt.RECENT_SEARCHES)
                    .padding([.top, .leading, .trailing], 24)
                    .multilineTextAlignment(.center)
                    .titleMediumStyle()
                List(items, id: \.self) {item in
                    VStack(alignment:.leading){
                        Text(item.companyNumber)
                        Text(item.companyName)
                    }
                }
            }
            HStack {
                Spacer()
                VStack {
                    Spacer()
                    Button(action: {
                        self.onItemClicked()
                    }) {
                        Image(systemName: "trash.circle.fill")
                            .font(.system(size: 100))
                            .foregroundColor(theme.colors.primary)
                            .shadow(color: .gray, radius: 0.2, x: 1, y: 1)
                            .padding(32)
                        
                    }
                }
            }
        }.alert(
            Text("Dialog"),
            isPresented: $isClearRecentsDialogVisible
        ) {
            Button("OK") {
                // Handle the acknowledgement.
            }
        } message: {
           Text("Please check your credentials and try again.")
        }
    }
    
}

struct RecentSearchesList_Previews: PreviewProvider {
    
    static let themeManager = ThemeManager()
    
    static var previews: some View {
        RecentSearchesList(
            [
                SearchHistoryItem(
                    companyName: "PFB Hire",
                    companyNumber: "121212",
                    searchTime: 0
                ),
                SearchHistoryItem(
                    companyName: "Heart Corps",
                    companyNumber: "121213",
                    searchTime: 0
                ),
                SearchHistoryItem(
                    companyName: "Love Ltd.",
                    companyNumber: "121214",
                    searchTime: 0
                )
            ],
            onItemClicked: {},
            isClearRecentsDialogVisible: true
        )
        .environmentObject(themeManager.current)
    }
    
}
