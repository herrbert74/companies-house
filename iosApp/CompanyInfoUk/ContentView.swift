//
//  ContentView.swift
//  CompanyInfoUk
//
//  Created by Zsolt Bertalan on 29/07/2023.
//

import SwiftUI

struct ContentView: View {
    
    @State private var searchText = ""
    
    var body: some View {
        VStack {
            NavigationStack {
                Text("Searching for \(searchText)")
                    .navigationTitle("Searchable Example")
            }
            .searchable(text: $searchText)
            Image(systemName: "globe")
                .imageScale(.large)
                .foregroundColor(.accentColor)
            Text("Hello, world!")
        }
        .padding()
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}
