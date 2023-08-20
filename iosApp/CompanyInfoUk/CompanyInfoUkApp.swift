import SwiftUI
import shared

@main
struct CompanyInfoUkApp: App {
    
    init() {
        HelperKt.doInitKoin()
    }
    
    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }

}
