package com.sainivik.backgoundlivelocation.database.dao

import androidx.room.*
import com.sainivik.backgoundlivelocation.model.LocationTable

@Dao
interface LocationDao {

    //Adds a contact to the database
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(LocationTable: LocationTable?): Long

    // Removes a contact from the database
    @Delete
    fun delete(person: LocationTable?)

    // Gets all contact in the database
    @Query("select * from locationMaster ORDER BY Loc_Time DESC")
    fun getAllLocations(): List<LocationTable>


    @Update
    fun updateUser(LocationTable: LocationTable?)

    @Update
    fun updateUser(vararg LocationTable: LocationTable?)


    @Query("delete from locationMaster")
    fun deleteAll()

    @Query("DELETE FROM locationMaster")
    fun nukeLocation()


}
