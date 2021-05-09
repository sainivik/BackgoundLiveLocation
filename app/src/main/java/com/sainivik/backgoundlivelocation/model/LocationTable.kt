package com.sainivik.backgoundlivelocation.model

import androidx.annotation.NonNull
import androidx.databinding.BaseObservable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "locationMaster" + "")
data class LocationTable(

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "id")
    var id: Long,

    @ColumnInfo(name = "Lat")
    var Lat: Double,

    @ColumnInfo(name = "Lng")
    var Lng: Double,

    @ColumnInfo(name = "Loc_Time")
    var Loc_Time: Long,

    @ColumnInfo(name = "bearing")
    var bearing: Double = 0.0,

    @ColumnInfo(name = "accuracy")
    var accuracy: Double = 0.0,

    @ColumnInfo(name = "altitute")
    var altitute: Double = 0.0,
    @ColumnInfo(name = "startTime")
    var startTime: Long,
    @ColumnInfo(name = "stopTime")
    var stopTime: Long,
    @ColumnInfo(name = "type")
    var type: Int,

    ) : Serializable, BaseObservable() {
    constructor() : this(
        0, 0.0, 0.0, 0, 0.0, 0.0, 0.0, 0, 0, 0
    )

}
