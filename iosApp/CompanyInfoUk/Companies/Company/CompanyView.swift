import SwiftUI
import shared

struct CompanyView: View {
    
    @EnvironmentObject var theme : Theme
    @EnvironmentObject var themeManeger : ThemeManager
    
    private let component: CompanyComp
    
    @StateValue
    private var state: CompanyStoreState
    
    init(_ component: CompanyComp) {
        self.component = component
        _state = StateValue(component.state)
    }
    
    var body: some View {
        
        ScrollView {
            CompanyBodyView(
                company: state.company,
                onMapClicked: component.onMapClicked,
                onChargesClicked: component.onChargesClicked,
                onFilingsClicked: component.onFilingsClicked,
                onInsolvenciesClicked: component.onInsolvenciesClicked,
                onOfficersClicked: component.onOfficersClicked,
                onPersonsClicked: component.onPersonsClicked
            )
        }
    }
    
}
