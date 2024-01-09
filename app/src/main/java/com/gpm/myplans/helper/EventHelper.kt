package com.gpm.myplans.helper

sealed class EventHelper {
    data class SaveName(var name: String) : EventHelper()

}