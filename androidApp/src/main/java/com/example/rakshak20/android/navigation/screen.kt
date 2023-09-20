package com.example.rakshak20.android.navigation


// class for navigation objects
sealed class screen(var route : String)
{
    object splash : screen("splash")
    object visualise : screen("visualise")
    object connection : screen("connection")

    object login : screen("login")

    object  registration : screen("registration")

    object main : screen("main")

    object  ipscreen : screen("ipscreen")
}