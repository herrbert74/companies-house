import SwiftUI
import shared

struct EmptySearchList: View {
    
    @EnvironmentObject var theme : Theme
 
    var message: String = LocalizedStringKey("no_search_result").stringValue()
    
    var body: some View {
        VStack {
            Image("ic_business_empty")
                .renderingMode(.original)
                .resizable()
                .aspectRatio(contentMode: .fit)
                .padding([.leading, .trailing], 48)
            Text(message)
                .padding([.top, .leading, .trailing], 24)
                .multilineTextAlignment(.center)
                .headlineLargeStyle()
        }
    }
    
}

struct EmptySearchList_Previews: PreviewProvider {
    
    static let themeManager = ThemeManager()
    
    static var previews: some View {
        EmptySearchList()
            .environmentObject(themeManager.current)
    }
    
}
