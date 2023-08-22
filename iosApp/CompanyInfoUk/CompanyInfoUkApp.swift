import SwiftUI
import shared

@main
struct CompanyInfoUkApp: App {
    
    @StateObject var themes = ThemeManager()
    
    init() {
        HelperKt.doInitKoin()
    }
    
    var body: some Scene {
        WindowGroup {
            ContentView()
                // This will allow us to manage the current applied theme
                .environmentObject(themes)
                //This will help us to access the members of current theme
                .environmentObject(themes.current)
        }
    }

}
