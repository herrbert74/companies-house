import SwiftUI
import shared

struct RootView: View {
    
    @ObservedObject
    private var childStack: ObservableValue<ChildStack<Configuration, CompaniesChild>>
    
    init(_ component: CompaniesRootComp) {
        self.childStack = ObservableValue(component.childStackValue)
    }
    
    var body: some View {
        
        let child = self.childStack.value.active.instance
        
        switch child {
        case let main as CompaniesChild.Main:
            MainView(main.component)
            
        default: EmptyView()
        }
    }
}

struct RootView_Previews: PreviewProvider {
    static var previews: some View {
        RootView(StubTodoRoot())
    }
    
    class StubTodoRoot : CompaniesRootComp {
        let childStackValue: Value<ChildStack<Configuration, CompaniesChild>> =
        simpleChildStack(.Main(component: MainView_Previews.StubMainComp()))
    }
}
