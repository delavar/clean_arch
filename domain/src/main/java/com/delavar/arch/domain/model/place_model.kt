package com.delavar.arch.domain.model

import com.google.gson.annotations.SerializedName

data class PlaceListData(
    @SerializedName("totalResults") val totalResults: Int,
    @SerializedName("groups") val groups: List<PlaceGroupModel>
)

data class PlaceGroupModel(
    @SerializedName("type") val type: String,
    @SerializedName("name") val name: String,
    @SerializedName("items") val items: List<PlaceGroupItemModel>
)

data class PlaceGroupItemModel(
    @SerializedName("venue") val venue: Place
)

data class Place(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("categories") val categories: List<PlaceCategory>? = emptyList(),
    @SerializedName("location") val location: PlaceLocation? = null,
    @SerializedName("referralId") val referralId: String? = null,
    @SerializedName("hasPerk") val hasPerk: Boolean? = null
)


data class PlaceLocation(
    @SerializedName("address") val address: String,
    @SerializedName("lat") val latitude: Double,
    @SerializedName("lng") val longitude: Double,
    @SerializedName("labeledLatLngs") val labeledLocations: List<LabeledLatLng>? = null,
    @SerializedName("cc") val cc: String? = null,
    @SerializedName("city") val city: String? = null,
    @SerializedName("state") val state: String? = null,
    @SerializedName("country") val country: String? = null,
    @SerializedName("formattedAddress") val formattedAddress: List<String>? = null
)

data class LabeledLatLng(
    @SerializedName("label") val label: String,
    @SerializedName("lat") val latitude: Double,
    @SerializedName("lng") val longitude: Double
)

data class PlaceCategory(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("pluralName") val pluralName: String? = null,
    @SerializedName("shortName") val shortName: String? = null,
    @SerializedName("icon") val icon: PlaceIcon,
    @SerializedName("primary") val isPrimary: Boolean = false
)

data class PlaceIcon(
    @SerializedName("prefix") val prefix: String,
    @SerializedName("suffix") val suffix: String
) {

    fun getUrl(size: Int): String {
        return "${prefix}bg_$size$suffix"
    }
}