package com.example.lrc.view;

import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Toast;

import com.example.lrc.annotation.ActivityFragmentInject;
import com.example.lrc.base.BaseFragment;
import com.example.lrc.broadcast.musicBroadCast;
import com.example.lrc.common.ConstantSet;
import com.example.lrc.module.R;
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
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;

@ActivityFragmentInject(contentViewId = R.layout.fragment_heart_chart)
public class HeartChartFragmen extends BaseFragment {
    private static boolean isPlayMusic = false;
    private static String LineName = "linename";
    private static int lineColor = 0;
    private Float lineWidth = 1f;
    private Float circleSize = 0.5f;
    private static int heartType = ConstantSet.HEARTTYPE_MOVEMENT;
    private static int time = 1000;
    /**
     * 设置x轴显示的个数
     */
    private static int xValueNumber = 20;


    @Bind(R.id.hearLineChart)
    LineChart lineChart;
    private Typeface tf;
    MyMarkerViewX myMarkerViewX;
    MyMarkerViewY myMarkerViewY;

    LineData lineData;
    ArrayList<LineDataSet> line;
    LineDataSet lineDataSet;
    ArrayList<String> xValues;//x轴的显示数据
    ArrayList<Entry> yValues;//y轴的显示数据

    //心率的本地数据
    String[] data_affable;
    String[] data_movement;

    Timer timer;
    TimerTask timerTask;

    private int NOTIFYDATA = 1;

    Runnable runnable;
    Thread thread;
    Object threadLock;
    musicBroadCast musicBroadCastReceiver;


    public HeartChartFragmen() {
        // Required empty public constructor
    }

    /**
     * @param nofityTime1   刷新时间
     * @param lineColor1    线条颜色
     * @param xValueNumber1 x轴个数
     * @param lineName1     名字
     * @param isPlayMusic1  是否需要监听歌曲播放，ture：播放歌曲开始监测心率；false：打开就开始监测
     * @return
     */
    public static HeartChartFragmen newInstances(int nofityTime1, int lineColor1, int xValueNumber1, String lineName1, boolean isPlayMusic1, int heartType1) {
        HeartChartFragmen fragment = new HeartChartFragmen();

        time = nofityTime1;
        lineColor = lineColor1;
        xValueNumber = xValueNumber1;
        LineName = lineName1;
        isPlayMusic = isPlayMusic1;
        heartType = heartType1;

        return fragment;
    }

    private Handler handler = new Handler() {
        /**
         * Subclasses must implement this to receive messages.
         *
         * @param msg
         */
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == NOTIFYDATA) {

                if (!isPause) {
                    setLine();
                }
            }

            super.handleMessage(msg);

        }
    };

    @Override
    protected void initView(View fragmentRootView) {
        ButterKnife.bind(this, fragmentRootView);

        data_affable = getActivity().getResources().getStringArray(R.array.heart_affable);
        data_movement = getActivity().getResources().getStringArray(R.array.heart_movement);
        xValues = new ArrayList<>();
        yValues = new ArrayList<>();
        line = new ArrayList<>();
        threadLock = new Object();

        initChart();
        setLine();
        initRunnable();
//        timer = new Timer();
//        timerTask = new TimerTask() {
//            @Override
//            public void run() {
//                Log.i("setdata", "---------------------");
//                setData();
//            }
//        };

        thread = new Thread(runnable);
        isPlayMusic();
    }

    public void musicPlay() {
        startDrawChart();
//        if (timer != null) {
//            if (timerTask != null) {
//                timer.schedule(timerTask, 3000, time);
//            }
//        }
    }

    public void musicStop() {
        stopDrawChart();
//        if (timer != null) {
//            if (timerTask != null) {
//                timer.cancel();
//                timerTask.cancel();
//            }
//        }
    }

    public void isPlayMusic() {
        if (isPlayMusic) {
            //注册接收广播
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(ConstantSet.BROADCAST_MUSIC_ACTION);
            musicBroadCastReceiver = new musicBroadCast() {
                @Override
                public void startMusic() {
                    musicPlay();
                    Toast.makeText(getActivity(), "start", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void stopMusic() {
                    musicStop();
                }
            };
            getActivity().registerReceiver(musicBroadCastReceiver, intentFilter);
        } else {
            musicPlay();
        }
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
        if (!isAdded()) return;

        lineDataSet = new LineDataSet(yValues, LineName);
        lineDataSet.setDrawCubic(true);
        lineDataSet.setCubicIntensity(0.2f);
        lineDataSet.setDrawCircles(false);
        lineDataSet.setLineWidth(lineWidth);
        lineDataSet.setCircleSize(circleSize);
        lineDataSet.setHighLightColor(Color.rgb(244, 117, 117));

        if (lineColor == 0) {
            lineDataSet.setColor(getActivity().getResources().getColor(R.color.black));
        } else {
            lineDataSet.setColor(getActivity().getResources().getColor(lineColor));
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

    int curr = 0;

    public void setData() {

        if (heartType == ConstantSet.HEARTTYPE_AFFABLE) {
            addData(data_affable);
        } else if (heartType == ConstantSet.HEARTTYPE_MOVEMENT) {
            addData(data_movement);
        }
        Message msg = handler.obtainMessage();
        msg.what = NOTIFYDATA;
        handler.sendMessage(msg);
    }

    Entry temp_entry;
    String temp_xValue;

    public void addData(String[] strings) {
        curr++;
        if (curr == strings.length) {
            curr = 0;
        }
        temp_xValue = getDate();

        temp_entry = new Entry(getyValue(strings), yValues.size());

        if (xValues.size() < xValueNumber) {
            while (xValues.size() < xValueNumber) {
                xValues.add(temp_xValue);
                yValues.add(temp_entry);
            }
        } else {
            xValues.remove(0);
            xValues.add(xValues.size(), temp_xValue);

            yValues.remove(0);
            for (int i = 0; i < yValues.size(); i++) {
                yValues.get(i).setXIndex(i);
            }
            yValues.add(yValues.size(), temp_entry);
        }

    }

    public float getyValue(String[] strings) {

        return Float.parseFloat(strings[curr]);
    }

    SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
    Date curDate;
    String str;

    //获取当前时间作为x轴的坐标
    public String getDate() {

        curDate = new Date(System.currentTimeMillis());//获取当前时间
        str = formatter.format(curDate);

        return str;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (timer != null) {
            timer.cancel();
            timer = null;
            if (timerTask != null) {
                timerTask.cancel();
                timerTask = null;
            }
        }
    }

    public void initRunnable() {
        runnable = new Runnable() {
            @Override
            public void run() {
                while (true) {

                    setData();
                    if (isPause) {
                        synchronized (threadLock) {
                            try {
                                threadLock.wait();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    try {
                        Thread.sleep(time);
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
        if (!isStart) {
            isStart = true;
            thread.start();
        }

        isPause = false;
        synchronized (threadLock) {
            threadLock.notify();
        }
    }

    public void stopDrawChart() {
        isPause = true;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        stopDrawChart();
        if (musicBroadCastReceiver != null) {
            getActivity().unregisterReceiver(musicBroadCastReceiver);
        }
    }
}
