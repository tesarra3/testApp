package radek.tesar.ab.Adapter;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.concurrent.TransferQueue;

import radek.tesar.ab.App;
import radek.tesar.ab.Client.entity.Transaction;
import radek.tesar.ab.Enum.Direction;
import radek.tesar.ab.Fragment.ListActivityFragment;
import radek.tesar.ab.R;
import radek.tesar.ab.Utils.Utils;

/**
 * Created by tesar on 28.04.2017.
 */

public class ListTransactionAdapter extends RecyclerView.Adapter<ListTransactionAdapter.ViewHolder> {


    private static List<Transaction> maler;
    private ListTransactionAdapter.OnItemClick mListener;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView price;
        public TextView direct;
        public ImageView image;
        public ImageView move;


        public ViewHolder(View v) {
            super(v);

            price = (TextView) v.findViewById(R.id.price);
            direct = (TextView) v.findViewById(R.id.direct);
            image = (ImageView) v.findViewById(R.id.image);
            move = (ImageView) v.findViewById(R.id.move);
        }
    }

    public static interface OnItemClick {
        public void onItem(Transaction transId);
    }


    public ListTransactionAdapter( List<Transaction> AE, ListActivityFragment fragment) {

        maler = AE;
        mListener = fragment;

    }
    public int getItemCount() {
        return maler.size();
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        final Transaction res = maler.get(position);

        holder.direct.setText(res.getDirection().toString());
        if(res.getDirection().equals(Direction.incoming.toString())){
            holder.image.setImageResource(R.drawable.ic_call_received);
        }else{
            holder.image.setImageResource(R.drawable.ic_call_made);
        }

        holder.price.setText(Utils.convertWithoutRounding(res.getAmountInAccountCurrency()));



        holder.move.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               mListener.onItem(res);

            }
        });

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                         int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list, parent, false);


        // set the view's size, margins, paddings and layout parameters

        return new ViewHolder(v);
    }

    public void changeDataSet(List<Transaction> AE){
        maler = AE;

    }
}
