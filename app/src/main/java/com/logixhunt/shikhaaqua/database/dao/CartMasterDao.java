package com.logixhunt.shikhaaqua.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.logixhunt.shikhaaqua.model.database.ProductCart;

import java.util.List;

@Dao
public interface CartMasterDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(ProductCart cartItem);

    @Delete
    void delete(ProductCart cartItem);

    @Query("DELETE FROM product_cart_master")
    void deleteAll();

    @Query("SELECT * from product_cart_master")
    List<ProductCart> getAllCartItem();

    @Update
    void updateCartItem(ProductCart cartItem);

    @Query("SELECT COUNT(bottleId) FROM product_cart_master")
    Integer getCartCount();

}
