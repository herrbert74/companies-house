import SwiftUI
import shared

struct RootView: View {
    
    private let component: CompaniesRootComp
    
    @StateValue
    private var childStack: ChildStack<Configuration, CompaniesChild>
    
    private var activeChild: CompaniesChild { childStack.active.instance }
    
    init(_ component: CompaniesRootComp) {
        self.component = component
        _childStack = StateValue(component.childStackValue)
    }
    
    var body: some View {
        
        switch activeChild {
        case let main as CompaniesChild.Main: MainView(main.component)
        case let child as CompaniesChild.Company: CompanyView(child.component)
        default: EmptyView()
        }
        //        StackView(
        //            stackValue: StateValue(component.stack),
        //            getTitle: {
        //                switch $0 {
        //                case is CompaniesChild.Main: return "List"
        //                case is CompaniesChild.Company: return "Details"
        //                default: return ""
        //                }
        //            },
        //            onBack: {},//component.onBackClicked,
        //            childContent: {
        //                switch $0 {
        //                case let main as CompaniesChild.Main: MainView(main.component)
        //                case let child as CompaniesChild.Company: CompanyView(child.component)
        //                default: EmptyView()
        //                }
        //            }
        //        )
    }
}

struct RootView_Previews: PreviewProvider {
    static var previews: some View {
        RootView(StubRoot())
    }
    
    class StubRoot : CompaniesRootComp {
        let childStackValue: Value<ChildStack<Configuration, CompaniesChild>> =
        simpleChildStack(.Main(component: MainView_Previews.StubMainComp()))
    }
}
