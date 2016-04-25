package com.example.lrc.view;

import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.lrc.broadcast.musicBroadCast;
import com.example.lrc.module.R;
import com.example.lrc.common.ConstantSet;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;


public class ChartFragment extends Fragment {
    @Bind(R.id.LineChart)
    LineChart lineChart;

    private String LineName = "linename";
    private int lineColor = 0;
    private Float lineWidth = 1f;
    Float circleSize = 0.5f;
    //当心率超过一定范围后，提示dialog，切换歌曲，在图表中则为持续n个数值超过范围后，进行提示
    private int unusualNum = 5;

    //判断数值是否正常
    private String TYPE_USUAL = "USUAL";
    private String TYPE_UNUSUAL = "UNUSUAL";

    //生理正常范围值i:正常最低值，j
    private int usualMin = 60;
    private int usualMax = 100;
    //超过正常范围，不正常范围段
    private int unusualScope = 20;

    /**
     * 设置x轴显示的个数
     */
    private int xValueNumber = 20;

    //刷新时间 毫秒
    private int refreshTime = 1000;


    LineData lineData;
    ArrayList<LineDataSet> line;
    LineDataSet lineDataSet;
    ArrayList<String> xValues;//x轴的显示数据
    ArrayList<Entry> yValues;//y轴的显示数据

    MyMarkerViewX myMarkerViewX;
    MyMarkerViewY myMarkerViewY;

    private Typeface tf;

    Runnable runnable;

    android.os.Handler handler;

    Thread thread;

    Object threadLock;

    /**
     * 创建fragment 后续需要传入线条颜色（默认为黑色），线条名字，不正常次数超过n次进行提醒，该生理情况正常范围i~j两个数值,不正常范围段,x轴显示的个数
     *
     * @return
     */
    public ChartFragment newInstance() {
        ChartFragment fragment = new ChartFragment();
        return fragment;
    }

//    public ChartFragment newInstance(int color,){
//
//    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_chart, container, false);

        initView(view);

        return view;
    }

    public void initView(View view) {

        ButterKnife.bind(this, view);//绑定控件

        threadLock=new Object();

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 1) {
                    setLine();
                }
                super.handleMessage(msg);
            }
        };

        //注册接收广播
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConstantSet.BROADCAST_MUSIC_ACTION);
        getActivity().registerReceiver(new musicBroadCast() {
            @Override
            public void startMusic() {
                musicStart();
                Toast.makeText(getActivity(),"start",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void stopMusic() {
                musicStop();
            }
        }, intentFilter);

        //初始化图表
        initChart();

        //初始化x轴和y周的显示数据
        xValues = new ArrayList<>();
        yValues = new ArrayList<>();

        //设置线条
        line = new ArrayList<LineDataSet>();
//          line.add(lineDataSet);


        initRunnable();

        thread = new Thread(runnable);
    }

    public void initChart() {

        lineChart.setDescription("");//
        lineChart.setHighlightEnabled(true);
        lineChart.setTouchEnabled(true);
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(true);
        lineChart.setDrawGridBackground(false);
        lineChart.getAxisRight().setEnabled(false);
        lineChart.setPinchZoom(true);

        lineChart.fitScreen();

        lineChart.setMarker(true, false);// 显示x轴的marker，不显示y轴的marker

        lineChart.setBorderColor(R.color.black);

        tf = Typeface.createFromAsset(getActivity().getAssets(), "OpenSans-Regular.ttf");

        lineChart.animateX(1000);
        //x轴
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTypeface(tf);
        xAxis.setDrawGridLines(false);
        xAxis.setEnabled(true);
        //y轴
        YAxis yAxis = lineChart.getAxisLeft();
        // ChartLineFormatter format = new ChartLineFormatter();// 设置y轴单位为%
        // yAxis.setValueFormatter(format);
        yAxis.setTypeface(tf);
        yAxis.setLabelCount(6);// y轴坐标分6段
        yAxis.setStartAtZero(false);
        yAxis.setEnabled(true);

        lineChart.invalidate();


    }

    public void setLine() {
        lineDataSet = new LineDataSet(yValues, LineName);
        lineDataSet.setDrawCubic(true);
        lineDataSet.setCubicIntensity(0.2f);
        lineDataSet.setDrawCircles(false);
        lineDataSet.setLineWidth(lineWidth);
        lineDataSet.setCircleSize(circleSize);
        lineDataSet.setHighLightColor(Color.rgb(244, 117, 117));

        if (lineColor == 0) {
            lineDataSet.setColor(getResources().getColor(R.color.black));
        } else {
            lineDataSet.setColor(getResources().getColor(lineColor));
        }

        lineDataSet.setFillColor(ColorTemplate.getHoloBlue());


        myMarkerViewX = new MyMarkerViewX(getActivity(), R.layout.custom_marker_view_x, xValues);
        myMarkerViewY = new MyMarkerViewY(getActivity(), R.layout.custom_marker_view_y);

        lineChart.setMarkerViewX(myMarkerViewX);
        lineChart.setMarkerViewY(myMarkerViewY);

        line.clear();
        line.add(lineDataSet);
        lineData = new LineData(xValues, line);
        lineData.setValueTypeface(tf);
        lineData.setValueTextSize(9f);
        lineData.setDrawValues(false);

        lineChart.setData(lineData);
//        lineChart.fitScreen();
        lineChart.invalidate();

    }

    /**
     * 当音乐开始时，图表进行
     */
    public void musicStart() {
        startDrawChart();
    }

    /**
     * 音乐停止时，图表停止
     */
    public void musicStop() {
        stopDrawChart();
    }


    public void initRunnable() {
        runnable = new Runnable() {
            @Override
            public void run() {
                while (true) {

                    setValues();
                    if(isPause){
                        synchronized (threadLock){
                            try {
                                threadLock.wait();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    try {
                        Thread.sleep(refreshTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }

            }
        };
    }

    boolean isPause = false;//线程是否暂停
    boolean isStart = false;

    public void startDrawChart() {
        if (!isStart){
            isStart=true;
            thread.start();
        }

        isPause=false;
        synchronized (threadLock){
            threadLock.notify();
        }
    }

    public void stopDrawChart() {
        isPause = true;
    }


    /**
     * 设置x轴和y轴的值
     */
    public void setValues() {

        String temp_xValue = getDate();
        Entry temp_entry = getyValue();

        /**
         * 可在此处记录数据到数据库中
         */

        if (xValues.size() < xValueNumber) {//刚初始化完成时，还没有完全填充整个图表
            xValues.add(temp_xValue);
            yValues.add(temp_entry);

        } else {
            xValues.remove(0);//移除第一个x值
            xValues.add(xValues.size(), temp_xValue);//把最新的x值放到最后

            yValues.remove(0);//移除第一个y值

            //// TODO: 16/4/2  把entry中的index向前移一位
            for (int i = 0; i < yValues.size(); i++) {
                yValues.get(i).setXIndex(i);
            }

            yValues.add(yValues.size(), temp_entry);//把最新的y值放到最后
        }

        //刷新界面

        handler.sendMessage((handler.obtainMessage(1)));

        //判断是否有一段范围内是不正常值


    }

    Entry entry;

    public Entry getyValue() {
        double y;

        //创建一个随便变量来填充y值

        if (getType().equals(TYPE_USUAL)) {//创建一个正常的范围值
            y = usualMin + (Math.random() * (usualMax - usualMin));
        } else {//创建一个不正常范围的值
            y = usualMax + (Math.random() * unusualScope);
        }


        entry = new Entry((float) y, yValues.size());

        return entry;
    }

    /**
     * //创建一个随机数来表示当前的数值是正常范围还是非正常范围
     *
     * @return
     */
    public String getType() {

        int type = (int) Math.random();

        if (type == 0) {//0为正常
            return TYPE_USUAL;
        } else {//1为不正常
            return TYPE_UNUSUAL;
        }
    }


    SimpleDateFormat formatter;
    Date curDate;
    String str;

    //获取当前时间作为x轴的坐标
    public String getDate() {

        formatter = new SimpleDateFormat("HH:mm:ss");
        curDate = new Date(System.currentTimeMillis());//获取当前时间
        str = formatter.format(curDate);

        return str;
    }


}
