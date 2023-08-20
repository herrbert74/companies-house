import SwiftUI
import shared

struct ContentView: View {
    
    @State private var searchText = ""
    
    let rootParameters = RootParameters()
    //let companiesRepository = rootParameters.companiesRepository
    //let companiesDocumentRepository = rootParameters.companiesDocumentRepository
    //let mainContext = rootParameters.mainContext
    //let ioContext = rootParameters.ioContext
    
    @State private var componentHolder =
    ComponentHolder {
        CompaniesRootComponent(
            componentContext: $0,
            mainContext: RootParameters().mainContext,
            ioContext: RootParameters().ioContext,
            companiesRepository: RootParameters().companiesRepository,
            companiesDocumentRepository: RootParameters().companiesDocumentRepository,
            finishHandler: {}
            //storeFactory: DefaultStoreFactory()
        )
    }
    
    var body: some View {
        RootView(componentHolder.component)
            .onAppear { LifecycleRegistryExtKt.resume(self.componentHolder.lifecycle) }
            .onDisappear { LifecycleRegistryExtKt.stop(self.componentHolder.lifecycle) }
        
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}
