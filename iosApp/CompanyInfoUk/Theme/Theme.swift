import SwiftUI

//Used for theme with some modifications:
//https://medium.com/geekculture/adapting-material-theming-from-jetpack-compose-to-swiftui-e1bb3f40651c
//Use this article for switching the theme dynamically as well!

struct Typography{
    
    var displayLarge:Font
    var displayMedium:Font
    var displaySmall:Font
    var headlineLarge:Font
    var headlineMedium:Font
    var headlineSmall:Font
    var titleLarge:Font
    var titleMedium:Font
    var titleSmall:Font
    var bodyLarge:Font
    var bodyMedium:Font
    var bodySmall:Font
    var labelLarge:Font
    var labelMedium:Font
    var labelSmall:Font
    
}

struct Colors {
    
    var primary: Color
    var onPrimary: Color
    var primaryContainer: Color
    var onPrimaryContainer: Color
    var background: Color
    var onBackground: Color
    var surface: Color
    var onSurface : Color
    var error: Color
    var onError: Color
    
}

struct Spacing {
    var largeSpacing:CGFloat
    var mediumSpacing:CGFloat
    var smallSpacing:CGFloat
    var extraLargeSpacing: CGFloat
}

struct Shapes {
    var largeCornerRadius:CGFloat
    var mediumCornerRadius:CGFloat
    var smallCornerRadius:CGFloat
}

class Theme : ObservableObject {
    let colors: Colors
    let shapes: Shapes
    let spacing: Spacing
    let typography: Typography
    
    init(colors: Colors, shapes: Shapes, spacing: Spacing, typography: Typography) {
        self.colors = colors
        self.shapes = shapes
        self.spacing = spacing
        self.typography = typography
    }
}

extension Theme {
    
    static let light = Theme(
        
        colors: Colors(
            primary: Color("Primary"),
            onPrimary: Color("OnPrimary"),
            primaryContainer: Color("PrimaryContainer"),
            onPrimaryContainer: Color("OnPrimaryContainer"),
            background: Color("Background"),
            onBackground: Color("OnBackground"),
            surface: Color("Surface"),
            onSurface: Color("OnSurface"),
            error: Color("Error"),
            onError: Color("OnError")
        ),
        
        shapes: Shapes(largeCornerRadius: 16, mediumCornerRadius: 12, smallCornerRadius: 8),
        
        spacing: Spacing(largeSpacing: 24, mediumSpacing: 16, smallSpacing: 8, extraLargeSpacing: 32),
        
        typography: Typography(
            displayLarge: Font.custom("Raleway-Bold", size: 57),
            displayMedium:Font.custom("Raleway-Bold", size: 45),
            displaySmall: Font.custom("Raleway-Bold", size: 36),
            headlineLarge: Font.custom("Raleway-Bold", size: 32),
            headlineMedium: Font.custom("Raleway-Bold", size: 28),
            headlineSmall: Font.custom("Raleway-Bold", size: 24),
            titleLarge: Font.custom("Raleway-Bold", size: 22),
            titleMedium: Font.custom("Raleway-Bold", size: 16),
            titleSmall: Font.custom("Raleway-Bold", size: 14),
            bodyLarge: Font.custom("Raleway-Regular", size: 16),
            bodyMedium: Font.custom("Raleway-Regular", size: 14),
            bodySmall: Font.custom("Raleway-Regular", size: 12),
            labelLarge: Font.custom("Raleway-Regular", size: 14),
            labelMedium: Font.custom("Raleway-Regular", size: 12),
            labelSmall: Font.custom("Raleway-Regular", size: 11)
        )
    )
    
    static let dark = Theme(
        
        colors: Colors(
            primary: Color("Primary"),
            onPrimary: Color("OnPrimary"),
            primaryContainer: Color("PrimaryContainer"),
            onPrimaryContainer: Color("OnPrimaryContainer"),
            background: Color("Background"),
            onBackground: Color("OnBackground"),
            surface: Color("Surface"),
            onSurface: Color("OnSurface"),
            error: Color("Error"),
            onError: Color("OnError")
        ),
        
        shapes: Shapes(largeCornerRadius: 16, mediumCornerRadius: 12, smallCornerRadius: 8),
        
        spacing: Spacing(largeSpacing: 24, mediumSpacing: 16, smallSpacing: 8, extraLargeSpacing: 32),
        
        typography: Typography(
            displayLarge: Font.custom("Raleway-Bold", size: 57),
            displayMedium:Font.custom("Raleway-Bold", size: 45),
            displaySmall: Font.custom("Raleway-Bold", size: 36),
            headlineLarge: Font.custom("Raleway-Bold", size: 32),
            headlineMedium: Font.custom("Raleway-Bold", size: 28),
            headlineSmall: Font.custom("Raleway-Bold", size: 24),
            titleLarge: Font.custom("Raleway-Bold", size: 22),
            titleMedium: Font.custom("Raleway-Bold", size: 16),
            titleSmall: Font.custom("Raleway-Bold", size: 14),
            bodyLarge: Font.custom("Raleway-Regular", size: 16),
            bodyMedium: Font.custom("Raleway-Regular", size: 14),
            bodySmall: Font.custom("Raleway-Regular", size: 12),
            labelLarge: Font.custom("Raleway-Regular", size: 14),
            labelMedium: Font.custom("Raleway-Regular", size: 12),
            labelSmall: Font.custom("Raleway-Regular", size: 11)
        )
        
    )
    
}

class ThemeManager: ObservableObject {
    @Published var current: Theme = .light
}

struct DisplayLargeStyle : ViewModifier {
    @EnvironmentObject var theme: Theme
    
    func body(content: Content) -> some View {
        return content.font(theme.typography.displayLarge).foregroundColor(theme.colors.onSurface).multilineTextAlignment(.leading)
    }
}

struct DisplayMediumStyle : ViewModifier {
    @EnvironmentObject var theme: Theme
    
    func body(content: Content) -> some View {
        return content.font(theme.typography.displayMedium).foregroundColor(theme.colors.onSurface).multilineTextAlignment(.leading)
    }
}

struct DisplaySmallStyle :  ViewModifier {
    @EnvironmentObject var theme: Theme
    
    func body(content: Content) -> some View {
        return content.font(theme.typography.displaySmall).foregroundColor(theme.colors.onSurface).multilineTextAlignment(.leading)
    }
}

struct HeadlineLargeStyle :  ViewModifier {
    @EnvironmentObject var theme: Theme

    func body(content: Content) -> some View {
        
        return content.font(theme.typography.headlineLarge).foregroundColor(theme.colors.onSurface).multilineTextAlignment(.leading)
    }
}

struct HeadlineMediumStyle :  ViewModifier {
    @EnvironmentObject var theme: Theme
    
    func body(content: Content) -> some View {
        
        return content.font(theme.typography.headlineMedium).foregroundColor(theme.colors.onSurface).multilineTextAlignment(.leading)
    }
}

struct HeadlineSmallStyle :  ViewModifier {
    @EnvironmentObject var theme: Theme
    
    func body(content: Content) -> some View {
        
        return content.font(theme.typography.headlineSmall).foregroundColor(theme.colors.onSurface).multilineTextAlignment(.leading)
    }
}

struct TitleLargeStyle :  ViewModifier {
    @EnvironmentObject var theme: Theme

    func body(content: Content) -> some View {
        
        return content.font(theme.typography.titleLarge).foregroundColor(theme.colors.onSurface).multilineTextAlignment(.leading)
    }
}

struct TitleMediumStyle :  ViewModifier {
    @EnvironmentObject var theme: Theme
    
    func body(content: Content) -> some View {
        
        return content.font(theme.typography.titleMedium).foregroundColor(theme.colors.onSurface).multilineTextAlignment(.leading)
    }
}

struct TitleSmallStyle :  ViewModifier {
    @EnvironmentObject var theme: Theme
    
    func body(content: Content) -> some View {
        
        return content.font(theme.typography.titleSmall).foregroundColor(theme.colors.onSurface).multilineTextAlignment(.leading)
    }
}

struct BodyLargeStyle :  ViewModifier {
    @EnvironmentObject var theme: Theme

    func body(content: Content) -> some View {
        
        return content.font(theme.typography.bodyLarge).foregroundColor(theme.colors.onSurface).multilineTextAlignment(.leading)
    }
}

struct BodyMediumStyle :  ViewModifier {
    @EnvironmentObject var theme: Theme
    
    func body(content: Content) -> some View {
        
        return content.font(theme.typography.bodyMedium).foregroundColor(theme.colors.onSurface).multilineTextAlignment(.leading)
    }
}

struct BodySmallStyle :  ViewModifier {
    @EnvironmentObject var theme: Theme
    
    func body(content: Content) -> some View {
        
        return content.font(theme.typography.bodySmall).foregroundColor(theme.colors.onSurface).multilineTextAlignment(.leading)
    }
}

struct LabelLargeStyle :  ViewModifier {
    @EnvironmentObject var theme: Theme

    func body(content: Content) -> some View {
        
        return content.font(theme.typography.labelLarge).foregroundColor(theme.colors.onSurface).multilineTextAlignment(.leading)
    }
}

struct LabelMediumStyle :  ViewModifier {
    @EnvironmentObject var theme: Theme
    
    func body(content: Content) -> some View {
        
        return content.font(theme.typography.labelMedium).foregroundColor(theme.colors.onSurface).multilineTextAlignment(.leading)
    }
}

struct LabelSmallStyle :  ViewModifier {
    @EnvironmentObject var theme: Theme
    
    func body(content: Content) -> some View {
        
        return content.font(theme.typography.labelSmall).foregroundColor(theme.colors.onSurface).multilineTextAlignment(.leading)
    }
}

extension View {
    func displayLargeStyle() -> some View {
        return modifier(DisplayLargeStyle())
    }
    
    func displayMediumStyle() -> some View {
        return modifier(DisplayMediumStyle())
    }
    
    func displaySmallStyle() -> some View {
        return  modifier(DisplaySmallStyle())
    }
    
    func headlineLargeStyle() -> some View {
        return  modifier(HeadlineLargeStyle())
    }
    
    func headlineMediumStyle() -> some View {
        return  modifier(HeadlineMediumStyle())
    }
    
    func headlineSmallStyle() -> some View {
        return  modifier(HeadlineSmallStyle())
    }
    
    func titleLargeStyle() -> some View {
        return  modifier(TitleLargeStyle())
    }
    
    func titleMediumStyle() -> some View {
        return  modifier(TitleMediumStyle())
    }
    
    func titleSmallStyle() -> some View {
        return  modifier( TitleSmallStyle())
    }
    
    func bodyLargeStyle() -> some View {
        return  modifier( BodyLargeStyle())
    }
    
    func bodyMediumStyle() -> some View {
        return  modifier( BodyMediumStyle())
    }
    
    func bodySmallStyle() -> some View {
        return  modifier( BodySmallStyle())
    }
    
    func labelLargeStyle() -> some View {
        return  modifier( LabelLargeStyle())
    }
    
    func labelMediumStyle() -> some View {
        return  modifier( LabelMediumStyle())
    }
    
    func labelSmallStyle() -> some View {
        return  modifier( LabelSmallStyle())
    }
}




