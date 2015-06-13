package net.kiwigeeks.spotify;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class ArtistAdapter extends BaseAdapter {

    ArrayList<ArtistListData> artistList = new ArrayList<>();
    LayoutInflater inflater;
    Context context;


    public ArtistAdapter(Context context, ArrayList<ArtistListData> aList) {
        this.artistList = aList;
        this.context = context;
        inflater = LayoutInflater.from(this.context);
    }

    @Override
    public int getCount() {
        return artistList.size();
    }

    @Override
    public ArtistListData getItem(int position) {
        return artistList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyViewHolder mViewHolder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.listview_layout, parent, false);
            mViewHolder = new MyViewHolder(convertView);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (MyViewHolder) convertView.getTag();
        }

        ArtistListData currentListData = getItem(position);

        mViewHolder.nameTextView.setText(currentListData.getArtistName());

        mViewHolder.imageView.setImageResource(currentListData.getArtistId());


        // Trigger the download of the URL asynchronously into the image view.
        Picasso.with(convertView.getContext())
                .load(currentListData.getArtistImageUrl())
                .placeholder(R.drawable.image_placeholder)
                .error(R.drawable.image_placeholder)
                .centerCrop()
                .tag(convertView.getContext())
                .resize(100, 100)
                .into(mViewHolder.imageView);


        return convertView;
    }


    private class MyViewHolder {
        TextView nameTextView;
        ImageView imageView;

        public MyViewHolder(View item) {
            nameTextView = (TextView) item.findViewById(R.id.artist_name);

            imageView = (ImageView) item.findViewById(R.id.artist_thumbnail);
        }
    }


}