package com.logixhunt.shikhaaqua.database;


import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.logixhunt.shikhaaqua.database.dao.AddressDao;
import com.logixhunt.shikhaaqua.database.dao.CartMasterDao;
import com.logixhunt.shikhaaqua.model.database.AddressModel;
import com.logixhunt.shikhaaqua.model.database.ProductCart;

@Database(entities = {
        ProductCart.class,
        AddressModel.class
}, version = 7, exportSchema = true)
public abstract class AppDatabase extends RoomDatabase{
    public abstract CartMasterDao cartMasterDao();
    public abstract AddressDao addressDao();
}
