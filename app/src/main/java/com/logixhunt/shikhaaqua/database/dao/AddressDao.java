package com.logixhunt.shikhaaqua.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.logixhunt.shikhaaqua.model.database.AddressModel;

import java.util.List;

@Dao
public interface AddressDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(AddressModel addressModel);

    @Delete
    void delete(AddressModel addressModel);

    @Query("DELETE FROM address_master")
    void deleteAll();

    @Query("SELECT * from address_master")
    List<AddressModel> getAllCars();

    @Update
    void updateCartItem(AddressModel addressModel);

    @Query("SELECT COUNT(addressId) FROM address_master")
    Integer getCartCount();
}
