
package com.example.lrc.view;

import android.content.Context;
import android.widget.TextView;

import com.example.lrc.module.R;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.utils.Utils;

import java.util.ArrayList;

/**
 * Custom implementation of the MarkerView.
 * 
 * @author Philipp Jahoda
 */
public class MyMarkerViewX extends MarkerView {

    private TextView tvContent;
    private ArrayList<String> xVals;

    public MyMarkerViewX(Context context, int layoutResource,ArrayList<String> xVals) {
        super(context, layoutResource);
        this.xVals=xVals;
        tvContent = (TextView) findViewById(R.id.tvContent);
    }

    // callbacks everytime the MarkerView is redrawn, can be used to update the
    // content (user-interface)
    @Override
    public void refreshContent(Entry e, int dataSetIndex) {

        if (e instanceof CandleEntry) {

            CandleEntry ce = (CandleEntry) e;

            tvContent.setText("" + Utils.formatNumber(ce.getHigh(), 0, true)+"%");
        } else {

//            tvContent.setText("" + e.getVal()+"%");
        	tvContent.setText(""+xVals.get(e.getXIndex()));
        }
    }
    
    @Override
    public int getXOffset() {
        // this will center the marker-view horizontally
        return -(getWidth() / 2);
    }

    @Override
    public int getYOffset() {
        // this will cause the marker-view to be above the selected value
    	
        return -(getHeight()*2);
    }
}
