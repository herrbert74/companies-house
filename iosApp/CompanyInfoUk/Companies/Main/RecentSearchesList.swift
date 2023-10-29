import SwiftUI
import shared

struct RecentSearchesList: View {
    
    var onItemClicked: (SearchHistoryItem) -> Void
    var onDeleteRecentSearch: () -> Void
    
    @EnvironmentObject var theme : Theme
    
    private let items: [SearchHistoryItem]
    @State private var isClearRecentsDialogVisible = false
    
    init(
        _ items: [SearchHistoryItem],
        onItemClicked: @escaping (SearchHistoryItem) -> Void,
        onDeleteRecentSearch: @escaping () -> Void,
        isClearRecentsDialogVisible: Bool
    ) {
        self.items = items
        self.onItemClicked = onItemClicked
        self.onDeleteRecentSearch = onDeleteRecentSearch
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
                    .listRowBackground(theme.colors.primaryContainer)
                }
                .scrollContentBackground(.hidden)
            }
            HStack {
                Spacer()
                VStack {
                    Spacer()
                    Button(action: {
                        isClearRecentsDialogVisible = true
                    }) {
                        Image(systemName: "trash.fill")
                            .font(.system(size: 36))
                            .foregroundColor(theme.colors.onPrimary)
                            .shadow(color: .gray, radius: 0.2, x: 1, y: 1)
                            .padding(24)
                        
                    }
                    .background(theme.colors.primary)
                    .clipShape(Circle())
                    //.frame(width: 48, height: 48)
                    .padding(24)
                }
            }
        }
        .alert(
            Text("delete_recent_searches"),
            isPresented: $isClearRecentsDialogVisible
        ) {
            Button("Cancel") {
                isClearRecentsDialogVisible = false
            }
            Button("OK") {
                isClearRecentsDialogVisible = false
                onDeleteRecentSearch()
            }
        } message: {
           Text("are_you_sure_you_want_to_delete_all_recent_searches")
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
            onItemClicked: {_ in },
            onDeleteRecentSearch: { },
            isClearRecentsDialogVisible: true
        )
        .environmentObject(themeManager.current)
    }
    
}
