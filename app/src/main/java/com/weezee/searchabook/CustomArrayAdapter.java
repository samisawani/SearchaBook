package com.weezee.searchabook;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class CustomArrayAdapter extends ArrayAdapter <Book>{
private Context context;

    CustomArrayAdapter(@NonNull Context context, @NonNull List<Book> objects) {
        super(context, 0, objects);
        this.context = context;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;             final Book currentBookItem= getItem(position);

        if (convertView==null){
            convertView= LayoutInflater.from(context).inflate(R.layout.book_search_result,parent ,false );
            TextView description = convertView.findViewById(R.id.description);
            RatingBar rating = convertView.findViewById(R.id.ratingBar);
            ImageView thumbnail = convertView.findViewById(R.id.thumbnail);
            TextView title = convertView.findViewById(R.id.title);
            Button button= convertView.findViewById(R.id.more_info_button);

            viewHolder= new ViewHolder(description,rating,thumbnail,title,button);
            convertView.setTag(viewHolder);

        }
        else{
            viewHolder=(ViewHolder) convertView.getTag();
        }
            viewHolder.description.setText(currentBookItem.getDescription());
            viewHolder.title.setText(currentBookItem.getTitle());
            viewHolder.rating.setRating(currentBookItem.getRating());
            if (currentBookItem.getThumbnailUrl().equals("No thumbnailUrl available")) viewHolder.thumbnail.setImageResource(R.drawable.thumbnail_not_available);
            else Picasso.get().load(currentBookItem.getThumbnailUrl()).into(viewHolder.thumbnail);



        viewHolder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(currentBookItem.getInfoUrl()));
                context.startActivity(i);
            }
        }) ;






        return convertView;
    }


    private class ViewHolder{
        private TextView description;
        private RatingBar rating;
        private ImageView thumbnail;
        private TextView title;
        private Button button;

        ViewHolder(TextView description, RatingBar rating, ImageView thumbnail, TextView title, Button button) {
            this.description = description;
            this.rating = rating;
            this.thumbnail = thumbnail;
            this.title = title;
            this.button = button;
        }

    }

}

