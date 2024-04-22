package com.example.expensenote.navigation

sealed class Graph(val route: String) {

    data object SplashGraph : Graph("splash_graph")
    data object BottomGraph : Graph("bottom_graph")
}