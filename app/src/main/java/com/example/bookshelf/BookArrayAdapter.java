package com.example.bookshelf;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.w3c.dom.Text;

import java.util.ArrayList;

    public class BookArrayAdapter extends ArrayAdapter<Book> {

        private ArrayList<Book> books;
        private Context context;

        public BookArrayAdapter(Context context, ArrayList<Book> books){
            super(context,0, books);
            this.books = books;
            this.context = context;
        }


        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        return super.getView(position, convertView, parent);
            View view = convertView;

            if(view == null){
                view = LayoutInflater.from(context).inflate(R.layout.content_book, parent,false);
            }

            Book book = books.get(position);

            TextView title = view.findViewById(R.id.TitleView);
            TextView author = view.findViewById(R.id.AuthorView);
            TextView description = view.findViewById(R.id.DescriptionView);
            //TextView status = view.findViewById(R.id.StatusView);
            TextView user = view.findViewById(R.id.UserView);

            title.setText(book.getTitle());
            author.setText(book.getAuthor());
            description.setText(book.getDescription());
            //status.setText(book.getStatus().toString());
            user.setText(book.getOwnerUsername());

            return view;

        }
    }


