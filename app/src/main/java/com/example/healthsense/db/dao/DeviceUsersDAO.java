package com.example.healthsense.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
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
}
