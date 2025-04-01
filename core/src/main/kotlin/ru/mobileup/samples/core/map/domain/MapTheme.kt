package ru.mobileup.samples.core.map.domain

enum class MapTheme(val json: String) {
    Bright(
        """
    [
        {
            "types": ["polyline", "polygon", "point"],
            "tags": {"none": []},
            "stylers": {"saturation": 1}
        }
    ]
"""
    ), Default(
        """
    [
        {
            "types": ["polyline", "polygon", "point"],
            "tags": {"none": []},
            "stylers": {"saturation": 0}
        }
    ]
"""
    ),
    Dark(
        """
    [
        {
            "types": ["polyline", "polygon", "point"],
            "tags": {"none": []},
            "stylers": {"saturation": -1}
        }
    ]
"""
    )
}