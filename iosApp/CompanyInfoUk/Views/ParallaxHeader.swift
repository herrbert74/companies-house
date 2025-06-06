import SwiftUI

//https://arturgruchala.com/parallax-header-effect-in-swiftui-using-coordinatespace/

struct ParallaxHeader<Content: View, Space: Hashable>: View {
    let content: () -> Content
    let coordinateSpace: Space
    let defaultHeight: CGFloat
    let title: String
    
    init(
        coordinateSpace: Space,
        defaultHeight: CGFloat,
        title: String,
        @ViewBuilder _ content: @escaping () -> Content
    ) {
        self.content = content
        self.coordinateSpace = coordinateSpace
        self.defaultHeight = defaultHeight
        self.title = title
    }
    
    var body: some View {
        GeometryReader { proxy in
            let offset = offset(for: proxy)
            let heightModifier = heightModifier(for: proxy)
            ZStack(alignment: .bottomLeading) {
                content()
                    .edgesIgnoringSafeArea(.horizontal)
                    .frame(
                        width: proxy.size.width,
                        height: proxy.size.height + heightModifier
                    )
                    .offset(y: offset)
                Color.gray
                    .edgesIgnoringSafeArea(.horizontal)
                    .frame(width: proxy.size.width, height: proxy.size.height + heightModifier)
                    .offset(y: offset)
                    .blendMode(.multiply)
                Text(title).padding([.bottom, .leading], 64)
                    .foregroundColor(.white)
                    .displaySmallStyle()
            }
        }.frame(height: defaultHeight)
    }
    
    private func offset(for proxy: GeometryProxy) -> CGFloat {
        let frame = proxy.frame(in: .named(coordinateSpace))
        if frame.minY < 0 {
            return -frame.minY * 0.8
        }
        return -frame.minY
    }
    
    private func heightModifier(for proxy: GeometryProxy) -> CGFloat {
        let frame = proxy.frame(in: .named(coordinateSpace))
        return max(0, frame.minY)
    }
}
