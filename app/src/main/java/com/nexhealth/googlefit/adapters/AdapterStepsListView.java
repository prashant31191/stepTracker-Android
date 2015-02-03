package com.nexhealth.googlefit.adapters;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.view.View.OnClickListener;
import com.nexhealth.googlefit.R;
import com.nexhealth.googlefit.model.aux.ModelStepList;

import java.util.ArrayList;
import me.grantland.widget.AutofitHelper;




/**
 * Created by waleed on 2/2/15.
 */
public class AdapterStepsListView extends BaseAdapter {
    private final Activity context;
    ArrayList<ModelStepList> stepLists;

    static class  ViewHolderHub {
        static public TextView steps;
        public Button created_at;
        public TextView miles;
    }

    public AdapterStepsListView(Activity context){
        this.context = context;
    }

    public void setArrayListSteps(ArrayList<ModelStepList> stepLists){
        this.stepLists = stepLists;
        ///notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return stepLists.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolderHub viewHolderHub;
        if(convertView == null){
            LayoutInflater inflater = context.getLayoutInflater();
            convertView             = inflater.inflate(R.layout.adapter_step_list_view, null);
            viewHolderHub           = new ViewHolderHub();
            viewHolderHub.created_at  = (Button) convertView.findViewById(R.id.button_step_created_at);
            viewHolderHub.steps = (TextView) convertView.findViewById(R.id.text_steps);
            viewHolderHub.miles      = (TextView) convertView.findViewById(R.id.text_miles);
            //AutofitHelper.create(viewHolderHub.steps);
            convertView.setTag(viewHolderHub);


        }else {
            viewHolderHub = (ViewHolderHub) convertView.getTag();
        }

        viewHolderHub.steps.setText(stepLists.get(position).steps);
        viewHolderHub.miles.setText(String.valueOf(stepLists.get(position).miles));
        String str = stepLists.get(position).created_at;


        viewHolderHub.created_at.setText(str);
        viewHolderHub.created_at.setPaintFlags(viewHolderHub.created_at.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        /*viewHolderHub.created_at.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, mealLists.get(position).typeMal);
                switch (mealLists.get(position).typeMal.toUpperCase()){
                    case "BREAKFAST":
                        Log.i(TAG, "breakfast");
                        context.startActivity(new Intent(context.getApplicationContext(), BreakfastMainActivity.class));
                        break;
                    case "LUNCH":
                        Log.i(TAG, "lunch");
                        context.startActivity(new Intent(context.getApplicationContext(), LunchActivity.class));
                        break;
                    case "DINNER":
                        Log.i(TAG, "dinner");
                        context.startActivity(new Intent(context.getApplicationContext(), DinnerActivity.class));
                        break;
                    case "SNACK":

                        Log.i(TAG, "snack");
                        context.startActivity(new Intent(context.getApplicationContext(), SnackActivity.class));
                        break;
                    case "DRINK":
                        context.startActivity(new Intent(context.getApplicationContext(), DrinkActivity.class));
                        break;
                    case "DRINKS":
                        context.startActivity(new Intent(context.getApplicationContext(), DrinkActivity.class));
                        break;
                    default:
                        Log.i(TAG, "Shit");
                        break;

                }
            }
        });*/


        return convertView;
    }

}
