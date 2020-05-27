package io.aiico.flight.domain.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class Suggestion(
    @SerializedName("fullname") val fullName: String,
    @SerializedName("location") val location: Location,
    @SerializedName("city") val city: String
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString() as String,
        parcel.readParcelable<Location>(
            Location::class.java.classLoader) as Location,
        parcel.readString() as String
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(fullName)
        parcel.writeParcelable(location, flags)
        parcel.writeString(city)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Suggestion> {
        override fun createFromParcel(parcel: Parcel): Suggestion {
            return Suggestion(parcel)
        }

        override fun newArray(size: Int): Array<Suggestion?> {
            return arrayOfNulls(size)
        }
    }
}
