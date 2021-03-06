package com.epicodus.android_bike_accidents.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.epicodus.android_bike_accidents.Constants;
import com.epicodus.android_bike_accidents.R;
import com.epicodus.android_bike_accidents.models.Accident;
import com.epicodus.android_bike_accidents.ui.AccidentDetailActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.parceler.Parcels;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Guest on 7/26/16.
 */
public class FirebaseAccidentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    View mView;
    Context mContext;

    public FirebaseAccidentViewHolder(View itemView) {
        super(itemView);
        mView = itemView;
        mContext = itemView.getContext();
        itemView.setOnClickListener(this);
    }

    public void bindAccident(Accident accident) {
        TextView collisionTypeTextView = (TextView) mView.findViewById(R.id.collisionTypeTextView);
        TextView severityTextView = (TextView) mView.findViewById(R.id.severityTextView);
        TextView accidentLocationTextView = (TextView) mView.findViewById(R.id.accidentLocationTextView);
        TextView dateTextView = (TextView) mView.findViewById(R.id.dateTextView);

        collisionTypeTextView.setText("Collision with: " + accident.getCollision());
        if(accident.getSeverity() == 0) {
            severityTextView.setText("Severity: Near collision");
        }
        if(accident.getSeverity() == 1) {
            severityTextView.setText("Severity: Minor scrapes & bruises");
        }
        if(accident.getSeverity() == 2) {
            severityTextView.setText("Severity: Injury");
        }
        if(accident.getSeverity() == 3) {
            severityTextView.setText("Severity: Hospitalization");
        }
        if(accident.getSeverity() == 4) {
            severityTextView.setText("Severity: Fatal");
        }

        accidentLocationTextView.setText("Address: " + accident.getLocation());
        dateTextView.setText("Date: " + accident.getDate());
    }

    @Override
    public void onClick(View view) {
        final ArrayList<Accident> accidents = new ArrayList<>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_CHILD_ACCIDENTS);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    accidents.add(snapshot.getValue(Accident.class));
                }

                int itemPosition = getLayoutPosition();

                Intent intent = new Intent(mContext, AccidentDetailActivity.class);
                intent.putExtra("position", itemPosition + "");
                intent.putExtra("accidents", Parcels.wrap(accidents));

                mContext.startActivity(intent);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
