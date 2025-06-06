import SwiftUI
import shared

struct MainView: View {
    
    @EnvironmentObject var theme : Theme
    @EnvironmentObject var themeManeger : ThemeManager
    
    private let component: MainComp
    
    @StateValue
    private var state: MainStoreState
    
    init(_ component: MainComp) {
        self.component = component
        _state = StateValue(component.state)
    }
    
    @State private var searchQuery = ""
    
    let rootParameters = RootParameters()
    
    var body: some View {
        VStack {
            NavigationStack {
                //Text("Searching for \(searchQuery)")//.navigationTitle("Search for companies")
                ZStack{
                    if (state.searchHistoryItems.isEmpty) {
                        EmptySearchList(message: LocalizedStringKey("no_recent_searches").stringValue())
                    } else {
                        RecentSearchesList(
                            state.searchHistoryItems,
                            onItemClicked: component.onRecentSearchesItemClicked,
                            onDeleteRecentSearch: component.onClearRecentSearchesClicked,
                            isClearRecentsDialogVisible: true)
                    }
                    if(!searchQuery.isEmpty){
                        if (state.isLoading) {
                            Text("Loading")
                        }
                        else {
                            Image("ic_business_empty")
                                .renderingMode(.original)
                                .resizable()
                                .aspectRatio(contentMode: .fit)
                            Text(StringConstantsKt.RECENT_SEARCHES).displayLargeStyle()
                            Text("no_recent_searches")
                            List(state.searchResults, id: \.self) {item in
                                VStack(alignment:.leading){
                                    Text(item.companyNumber ?? "")
                                    Text(item.title ?? "")
                                }.onTapGesture {
                                    component.onItemClicked(companySearchResultItem: item)
                                }
                            }
                        }
                    }
                }
            }
            .searchable(text: $searchQuery)
            .onChange(of: searchQuery) {
                component.onSearchQueryChanged(searchQuery: searchQuery)
            }
            
        }
        .padding()
    }
}

struct MainView_Previews: PreviewProvider {
    
    static let themeManager = ThemeManager()

    static var previews: some View {
        MainView(StubMainComp())
            .environmentObject(themeManager.current)
    }
    
    class StubMainComp: MainComp {
        let state: Value<MainStoreState> = valueOf(
            MainStoreState(
                searchHistoryItems: [
                    SearchHistoryItem(
                        companyName: "Heart",
                        companyNumber: "1111",
                        searchTime: 0
                    ),
                    SearchHistoryItem(
                        companyName: "Heart2",
                        companyNumber: "1111",
                        searchTime: 0
                    ),
                    SearchHistoryItem(
                        companyName: "Heart3",
                        companyNumber: "1111",
                        searchTime: 0
                    )
                ],
                totalResults: 50,
                searchResults: [
                    CompanySearchResultItem(
                        descriptionIdentifier: [],
                        dateOfCreation: nil,
                        snippet: nil,
                        companyNumber: "1111",
                        title: "Heart",
                        companyStatus:nil,
                        matches: nil,
                        address: nil,
                        description: nil,
                        kind: nil,
                        companyType: nil,
                        addressSnippet: nil
                    ),
                    CompanySearchResultItem(
                        descriptionIdentifier: [],
                        dateOfCreation: nil,
                        snippet: nil,
                        companyNumber: "1112",
                        title: "White Heart",
                        companyStatus:nil,
                        matches: nil,
                        address: nil,
                        description: nil,
                        kind: nil,
                        companyType: nil,
                        addressSnippet: nil
                    ),
                    CompanySearchResultItem(
                        descriptionIdentifier: [],
                        dateOfCreation: nil,
                        snippet: nil,
                        companyNumber: "1113",
                        title: "Purple Heart",
                        companyStatus:nil,
                        matches: nil,
                        address: nil,
                        description: nil,
                        kind: nil,
                        companyType: nil,
                        addressSnippet: nil
                    )
                ],
                filteredSearchResultItems: [],
                error: nil,
                isLoading: false,
                searchQuery: "heart",
                filterState: FilterState.filterShowAll
                
            )
        )
        
        func onItemClicked(companySearchResultItem: CompanySearchResultItem){}
        func onRecentSearchesItemClicked(searchHistoryItem: SearchHistoryItem){}
        func onClearRecentSearchesClicked(){}
        
        func onSearchQueryChanged(searchQuery: String?){}
        
        func showRecentSearches(){}
        func clearRecentSearchesClicked(){}
        func clearRecentSearches(){}
        
        func loadMoreSearch(){}
        
        func setFilterState(filterState: FilterState){}
        func setSearchMenuItemExpanded(){}
        func setSearchMenuItemCollapsed(){}
        
        func onBackClicked(){}
        func onFavoritesClicked(){}
        func onPrivacyClicked(){}
        
    }
}
