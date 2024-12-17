package com.logixhunt.shikhaaqua.database.service;

import android.content.Context;
import android.os.AsyncTask;


import com.logixhunt.shikhaaqua.database.DatabaseClient;
import com.logixhunt.shikhaaqua.model.database.AddressModel;
import com.logixhunt.shikhaaqua.model.database.ProductCart;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class DataService {

    private Context context;
    public DataService(Context context) {
        this.context = context;
    }

    public List<ProductCart> getAllCartItem() {
        try {
            return new GetCartDataAsyncTask().execute().get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    private class GetCartDataAsyncTask extends AsyncTask<Void, Void, List<ProductCart>> {
        @Override
        protected List<ProductCart> doInBackground(Void... url) {
            return DatabaseClient.getInstance(context).getAppDatabase().cartMasterDao().getAllCartItem();
        }
    }


    public List<AddressModel> getAllAddress() {
        try {
            return new GetAddressDataAsyncTask().execute().get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    private class GetAddressDataAsyncTask extends AsyncTask<Void, Void, List<AddressModel>> {
        @Override
        protected List<AddressModel> doInBackground(Void... url) {
            return DatabaseClient.getInstance(context).getAppDatabase().addressDao().getAllCars();
        }
    }

}
