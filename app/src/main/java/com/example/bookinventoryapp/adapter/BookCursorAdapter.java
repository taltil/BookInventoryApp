package com.example.bookinventoryapp.adapter;


import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bookinventoryapp.R;
import com.example.bookinventoryapp.data.BookContract.BookEntry;

public class BookCursorAdapter extends CursorAdapter {
    public BookCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        final Context c = context;

        ViewHolder holder = new ViewHolder();
        holder.name = view.findViewById(R.id.name);
        holder.price = view.findViewById(R.id.price);
        holder.qunatity = view.findViewById(R.id.quantity);
        holder.sale = view.findViewById(R.id.sale);

        String name = cursor.getString(cursor.getColumnIndexOrThrow(BookEntry.COLUMN_BOOK_NAME));
        String price = cursor.getString(cursor.getColumnIndexOrThrow(BookEntry.COLUMN_BOOk_PRICE));
        String quantity = cursor.getString(cursor.getColumnIndexOrThrow(BookEntry.COLUMN_BOOK_QUANTITY));

        holder.name.setText(name);
        holder.price.setText("Price: " + price + "$");
        holder.qunatity.setText("quantity: " + quantity);


        final long id = cursor.getLong(cursor.getColumnIndexOrThrow(BookEntry._ID));
        int quantityInt = Integer.parseInt(quantity) - 1;
        if (quantityInt <= 0) {
            quantityInt = 0;
        }
        final String newQuantity = String.valueOf(quantityInt);
        holder.sale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues values = new ContentValues();
                values.put(BookEntry.COLUMN_BOOK_QUANTITY, newQuantity);
                updateSale(c, v, values, id);
            }
        });
    }

    private void updateSale(Context context, View view, ContentValues values, Long id) {
        Uri bookUri = ContentUris.withAppendedId(BookEntry.CONTENT_URI, id);
        int rowsAffected = context.getContentResolver().update(bookUri, values, null, null);
        if (rowsAffected == 0) {
            Toast.makeText(view.getContext(), R.string.sale__update_failed, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(view.getContext(), R.string.sale__update_success, Toast.LENGTH_SHORT).show();
        }
    }

    static class ViewHolder {
        TextView name;
        TextView price;
        TextView qunatity;
        Button sale;
    }
}
