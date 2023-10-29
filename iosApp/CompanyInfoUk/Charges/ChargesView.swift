import SwiftUI
import shared

struct ChargesView: View {
    
    @State var initialOffset: CGFloat?
    @State var offset: CGFloat?
    
    @EnvironmentObject var theme : Theme
    @EnvironmentObject var themeManeger : ThemeManager
    
    private let component: ChargesComp
    
    @StateValue
    private var state: ChargesStoreState
    
    init(_ component: ChargesComp) {
        self.component = component
        _state = StateValue(component.state)
    }
    
    private enum CoordinateSpaces {
        case scrollView
    }
    
    var body: some View {
        
        return NavigationView {
            ScrollView(.vertical, showsIndicators: false) {
                ParallaxHeader(
                    coordinateSpace: CoordinateSpaces.scrollView,
                    defaultHeight: 340,
                    title: LocalizedStringKey("charges").stringValue()
                ) {
                    ZStack(alignment: .bottomLeading) {
                        Image("bg_charges")
                            .resizable()
                            .scaledToFill()
                    }
                }
                if (state.isLoading) {
                    Text("loading")
                } else if (state.error != nil) {
                    Text("error")
                } else {
                    ChargesList(state.chargesResponse.items, onItemClicked: component.onItemClicked)
                }
            }
            .coordinateSpace(name: CoordinateSpaces.scrollView)
            .edgesIgnoringSafeArea(.top)
            .navigationBarItems(
                leading: Button(action: { withAnimation { component.onBackClicked() } } ) {
                    HStack {
                        Image(systemName: "chevron.left").foregroundColor(.white)
                        Text("Back").foregroundColor(.white)
                    }
                }
            )
        }
        
    }
    
    private func scrollOffset(_ geometry: GeometryProxy) -> CGFloat {
        let scrollOffset = geometry.frame(in: .global).minY
        return scrollOffset
    }
    
}

struct OffsetKey: PreferenceKey {
    static let defaultValue: CGFloat? = nil
    static func reduce(value: inout CGFloat?,
                       nextValue: () -> CGFloat?) {
        value = value ?? nextValue()
    }
}

//struct ChargesView_Previews: PreviewProvider {
//    static var previews: some View {
//        ChargesView()
//    }
//}
