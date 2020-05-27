package io.aiico.flight.domain.model

import android.os.Parcel
import android.os.Parcelable

data class Destination(
    val fullName: String,
    val location: Location,
    val city: String,
    val iata: String
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString() as String,
        parcel.readParcelable<Location>(Location::class.java.classLoader) as Location,
        parcel.readString() as String,
        parcel.readString() as String
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(fullName)
        parcel.writeParcelable(location, flags)
        parcel.writeString(city)
        parcel.writeString(iata)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Destination> {
        override fun createFromParcel(parcel: Parcel): Destination {
            return Destination(parcel)
        }

        override fun newArray(size: Int): Array<Destination?> {
            return arrayOfNulls(size)
        }
    }
}
