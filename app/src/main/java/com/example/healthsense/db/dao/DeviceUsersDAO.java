package com.example.healthsense.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.healthsense.db.entity.DeviceUsers;


@Dao
public interface DeviceUsersDAO {

    @Insert
    void insert(DeviceUsers deviceUser);

    @Update
    void update(DeviceUsers deviceUser);

    @Delete
    void delete(DeviceUsers deviceUser);

    @Query("DELETE FROM Device_Users")
    void deleteAll();

    @Query("UPDATE Device_Users SET upload = upload+1 WHERE id = :id")
    int increaseWorks( int id);

    @Query("UPDATE Device_Users SET upload = upload-1 WHERE id = :id")
    int decreaseWorks( int id);

    @Query("SELECT upload FROM device_users where id = :id")
    int getWorksSaved(int id);
}
