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

public class TracksAdapter extends BaseAdapter {

    ArrayList<TrackListData> trackList = new ArrayList<>();
    LayoutInflater inflater;
    Context context;



    public TracksAdapter(Context context, ArrayList<TrackListData> tracks) {
        this.trackList = tracks;
        this.context = context;
        inflater = LayoutInflater.from(this.context);
    }

    @Override
    public int getCount() {
        return trackList.size();
    }

    @Override
    public TrackListData getItem(int position) {
        return trackList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyViewHolder mViewHolder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.track_listview_layout, parent, false);
            mViewHolder = new MyViewHolder(convertView);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (MyViewHolder) convertView.getTag();
        }

        TrackListData currentListData = getItem(position);

        mViewHolder.nameTextView.setText(currentListData.getTrackName());
      mViewHolder.albumTextView.setText(currentListData.getAlbumName());
        mViewHolder.imageView.setImageResource(currentListData.getTrackId());


        // Trigger the download of the URL asynchronously into the image view.
        Picasso.with(convertView.getContext())
                .load(currentListData.getSmallAlbumThumbnail())
                .placeholder(R.drawable.image_placeholder)
                .error(R.drawable.image_placeholder)
                        // .resizeDimen(100, 100)
                        //.centerInside()
                .tag(convertView.getContext())
                .resize(100, 100)
                .into(mViewHolder.imageView);



        return convertView;
    }


    private class MyViewHolder {
        TextView nameTextView;
        TextView albumTextView;
        ImageView imageView;


        public MyViewHolder(View item) {
            nameTextView = (TextView) item.findViewById(R.id.track_name);
            albumTextView= (TextView) item.findViewById(R.id.track_album);

            imageView = (ImageView) item.findViewById(R.id.track_thumbnail);
        }
    }


}
