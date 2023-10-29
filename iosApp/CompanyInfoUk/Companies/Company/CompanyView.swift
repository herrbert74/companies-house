import SwiftUI
import shared

struct CompanyView: View {
    
    @EnvironmentObject var theme : Theme
    @EnvironmentObject var themeManeger : ThemeManager
    
    private let component: CompanyComp
    
    @StateValue
    private var state: CompanyStoreState
    
    private enum CoordinateSpaces {
        case scrollView
    }
    
    init(_ component: CompanyComp) {
        self.component = component
        _state = StateValue(component.state)
    }
    
    var body: some View {
             
        return NavigationView {
            ScrollView {
                ParallaxHeader(
                    coordinateSpace: CoordinateSpaces.scrollView,
                    defaultHeight: 340,
                    title: "\(state.company.companyName)"
                ) {
                    ZStack(alignment: .bottomLeading) {
                        Image("bg_company")
                            .resizable()
                            .scaledToFill()
                    }
                }
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
            .coordinateSpace(name: CoordinateSpaces.scrollView)
            .edgesIgnoringSafeArea(.top)
            .navigationBarItems(
                leading: Button(action: { withAnimation { component.onBackClicked(isFavourite: false) } } ) {
                    HStack {
                        Image(systemName: "chevron.left").foregroundColor(.white)
                        Text("Back").foregroundColor(.white)
                    }
                }
            )
        }
    }
    
}
