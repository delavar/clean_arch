package com.delavar.arch.data.util

interface LocationUtil {

    /**
     * Try to meter distance of two geo locations
     * @param latLngA a comma separated latitude and longitude of point A
     * @param latLngB a comma separated latitude and longitude of point B
     */
    fun distanceBetweenTwoPoints(latLngA: String, latLngB: String): Float
}