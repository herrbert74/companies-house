import SwiftUI
import shared

struct MainView: View {
    
    private let component: MainComp
    
    @ObservedObject
    private var state: ObservableValue<MainStoreState>
    
    init(_ component: MainComp) {
        self.component = component
        self.state = ObservableValue(component.state)
    }
    
    @State private var searchQuery = ""
    
    let rootParameters = RootParameters()
    
    var body: some View {
        VStack {
            NavigationStack {
                Text("Searching for \(searchQuery)").navigationTitle("Searchable Example")
            }
            .searchable(text: $searchQuery)
            .onChange(of: searchQuery) { newValue in
                component.onSearchQueryChanged(searchQuery:searchQuery)
            }
            Image(systemName: "globe")
                .imageScale(.large)
                .foregroundColor(.accentColor)
            Text("\(state.value.totalResults)")
        }
        .padding()
    }
}

struct MainView_Previews: PreviewProvider {
    static var previews: some View {
        MainView(StubMainComp())
    }
    
    class StubMainComp: MainComp {
        let state: Value<MainStoreState> =
        valueOf(
            MainStoreState(
                searchHistoryItems: [],
                totalResults: 50,
                searchResults: [],
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
