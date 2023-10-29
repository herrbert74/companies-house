import SwiftUI
import shared

struct TwoLineView: View {
    
    @EnvironmentObject var theme : Theme
    
    private let firstLineString: String
    private let secondLineString: String
    private let firstLineStyle: Font
    private let secondLineStyle: Font
    private let flipLineStyles: Bool
    
    init(
        firstLineString: String,
        secondLineString: String,
        firstLineStyle: Font? = nil,
        secondLineStyle: Font? = nil,
        /**
         * By default this displays a normal sized title in the first line,
         * and a bold, large title in the second, plus the first line has an extra margin.
         * This can be used for displaying label + data.
         * If we flip them (for example when we want to show data + less important data),
         * the additional margin on the first line is removed.
         */
        flipLineStyles: Bool = false,
        theme : Theme
    ) {
        self.firstLineString = firstLineString
        self.secondLineString = secondLineString
        self.firstLineStyle = firstLineStyle ?? theme.typography.bodyMedium
        self.secondLineStyle = secondLineStyle ?? theme.typography.titleLarge
        self.flipLineStyles = flipLineStyles
    }
    
    var body: some View {
        
        VStack(alignment: .leading) {
//            ZStack(alignment: .bottomLeading) {
//                Image("bg_charges")
//                    .resizable()
//                    .scaledToFill()
//                Color.gray.frame(width: .infinity, height: .infinity)
//                    .blendMode(.multiply)
//                Text("charges").padding([.bottom, .leading], 64)
//                    .foregroundColor(.white)
//                    .titleLargeStyle()
//            }
            Text("\(firstLineString)").font(flipLineStyles ? secondLineStyle : firstLineStyle)
                .frame(maxWidth: .infinity, alignment: .leading).padding(.leading, 8)
            Text("\(secondLineString)").font(flipLineStyles ? firstLineStyle : secondLineStyle)
                .frame(maxWidth: .infinity, alignment: .leading).padding(.leading, 8)
        }
            .frame(maxWidth: .infinity)
    }
    
}

struct TwoLineView_Previews: PreviewProvider {
    
    static let themeManager = ThemeManager()
    
    static var previews: some View {
        VStack(alignment: .leading) {
            TwoLineView(
                firstLineString: "Label", secondLineString: "Information", theme: themeManager.current
            ).padding([.bottom], 24)
            TwoLineView(
                firstLineString: "Important", secondLineString: "Less important", flipLineStyles: true, theme: themeManager.current
            )
        }
        .environmentObject(themeManager.current)
    }
    
}
