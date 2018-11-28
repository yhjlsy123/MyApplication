package com.zhuochi.hydream.bathhousekeeper.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.zhuochi.hydream.bathhousekeeper.R;
import com.zhuochi.hydream.bathhousekeeper.base.BaseFragment;
import com.zhuochi.hydream.bathhousekeeper.config.AppManager;
import com.zhuochi.hydream.bathhousekeeper.config.Constants;
import com.zhuochi.hydream.bathhousekeeper.dialog.LoadingTrAnimDialog;
import com.zhuochi.hydream.bathhousekeeper.entity.HomeListEntity;
import com.zhuochi.hydream.bathhousekeeper.entity.SonBaseEntity;
import com.zhuochi.hydream.bathhousekeeper.http.GsonUtils;
import com.zhuochi.hydream.bathhousekeeper.http.XiRequestParams;
import com.zhuochi.hydream.bathhousekeeper.utils.SPUtils;
import com.zhuochi.hydream.bathhousekeeper.utils.Tools;
import com.zhuochi.hydream.bathhousekeeper.view.HeightSelectView;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/*主页*/
public class HomeFragment extends BaseFragment implements HeightSelectView.SelcetCallBack {


    @BindView(R.id.toolbar_back)
    ImageView toolbarBack;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.headViewContaner)
    LinearLayout headViewContaner;
    Unbinder unbinder;
    @BindView(R.id.register_today)
    TextView registerToday;
    @BindView(R.id.register_all)
    TextView registerAll;
    @BindView(R.id.consumeRecord_today)
    TextView consumeRecordToday;
    @BindView(R.id.consumeRecord_all)
    TextView consumeRecordAll;
    @BindView(R.id.use_people_num_today)
    TextView usePeopleNumToday;
    @BindView(R.id.use_people_num_all)
    TextView usePeopleNumAll;
    @BindView(R.id.order_today)
    TextView orderToday;
    @BindView(R.id.order_all)
    TextView orderAll;
    @BindView(R.id.webView)
    WebView webView;
    @BindView(R.id.content_layout)
    LinearLayout content_layout;
    @BindView(R.id.layout_homeone)
    RelativeLayout layoutHomeone;
    @BindView(R.id.layout_home_two)
    RelativeLayout layoutHomeTwo;
    @BindView(R.id.layout_home_three)
    RelativeLayout layoutHomeThree;
    @BindView(R.id.layout_home_four)
    RelativeLayout layoutHomeFour;
    private XiRequestParams params;

    int org_id;
    private int org_area_id;
    private int device_area_id;
    private String device_key;
    private String start_date = "";
    private String end_date = "";
    private View view;
    private View tabView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (null == view) {
            view = inflater.inflate(R.layout.fragment_home, container, false);
        }
        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) {
            parent.removeView(view);
        }
        unbinder = ButterKnife.bind(this, view);
        params = new XiRequestParams(getActivity());
        headViewContaner.addView(new HeightSelectView(getActivity(), this, 3, getResources().getString(R.string.select_school_bathroom_wei)));

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        Bundle bundle = getArguments();
        if (bundle != null) {
            String name = bundle.get("name").toString();
//            tv.setText(name);
        }

    }

    @Override
    public void initView() {
        super.initView();
        toolbarBack.setVisibility(View.GONE);
        toolbarTitle.setText("澡管家");
        tabView = getActivity().findViewById(android.R.id.tabhost);
        initWebView();
    }

    private void initWebView() {
        webView.getSettings().setBlockNetworkImage(false);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);//允许使用js
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);//不使用缓存，只从网络获取数据.
        //支持屏幕缩放
        webSettings.setSupportZoom(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setBuiltInZoomControls(true);

        //不显示webview缩放按钮
        webSettings.setDisplayZoomControls(false);
    }

    public void loadData() {
        params.addCallBack(this);
        params.getHomePage(SPUtils.getInt(Constants.USER_ID, 0), org_id, org_area_id, device_area_id, device_key, start_date, end_date);
    }

    public void loadWebviewData() {
        params.addCallBack(this);
        params.getConsumptionChart(SPUtils.getInt(Constants.USER_ID, 0), org_id, org_area_id, device_area_id, device_key, start_date, end_date, "Statistics/consumptionChart");
    }

    public void loadWebvieRegwData() {
        params.addCallBack(this);
        params.getConsumptionChart(SPUtils.getInt(Constants.USER_ID, 0), org_id, org_area_id, device_area_id, device_key, start_date, end_date, "Statistics/regChart");
    }

    public void loadWebvieOrderData() {
        params.addCallBack(this);
        params.getConsumptionChart(SPUtils.getInt(Constants.USER_ID, 0), org_id, org_area_id, device_area_id, device_key, start_date, end_date, "Statistics/orderChart");
    }


    /**
     * 返回键显示
     *
     * @param layoutId
     */
    public void SetIsVisibile(int layoutId) {
        if (0 == layoutId) {
            toolbarTitle.setText("澡管家");
            webView.setVisibility(View.GONE);
            toolbarBack.setVisibility(View.GONE);
            content_layout.setBackgroundColor(getResources().getColor(R.color.gray_system));
            layoutHomeone.setVisibility(View.VISIBLE);
            layoutHomeTwo.setVisibility(View.VISIBLE);
            layoutHomeThree.setVisibility(View.VISIBLE);
            layoutHomeFour.setVisibility(View.VISIBLE);
            tabView.setVisibility(View.VISIBLE);
        } else {
            webView.setVisibility(View.VISIBLE);
            toolbarBack.setVisibility(View.VISIBLE);
            content_layout.setBackgroundColor(getResources().getColor(R.color.white));
            layoutHomeone.setVisibility(R.id.layout_homeone == layoutId ? View.VISIBLE : View.GONE);
            layoutHomeTwo.setVisibility(R.id.layout_home_two == layoutId ? View.VISIBLE : View.GONE);
            layoutHomeThree.setVisibility(R.id.layout_home_three == layoutId ? View.VISIBLE : View.GONE);
            layoutHomeFour.setVisibility(R.id.layout_home_four == layoutId ? View.VISIBLE : View.GONE);
            if (null != tabView) {
                tabView.setVisibility(View.GONE);
            }
            webView.setWebViewClient(new WebViewClient() {


                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    super.onPageStarted(view, url, favicon);
                    webView.setVisibility(View.GONE);
                    LoadingTrAnimDialog.showLoadingAnimDialog(getActivity());
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    webView.setVisibility(View.VISIBLE);
                    LoadingTrAnimDialog.dismissLoadingAnimDialog();
                }
            });
        }

    }

    @Override
    public void onRequestSuccess(String tag, SonBaseEntity result) {
        LoadingTrAnimDialog.showLoadingAnimDialog(getActivity());
        switch (tag) {
            case "Statisticshome"://获取首页数据列表
                Map map = (Map) result.getData().getData();
                String gson = GsonUtils.parseMapToJson(map);

                HomeListEntity entity = JSON.parseObject(gson, HomeListEntity.class);
                registerToday.setText(String.valueOf(entity.getRegUserNumToday()));
                registerAll.setText(String.valueOf(entity.getRegUserNumAll()));
                consumeRecordToday.setText(String.valueOf(entity.getConsumeMoneyToday()));
                consumeRecordAll.setText(entity.getConsumeMoneyAll());
                usePeopleNumToday.setText(String.valueOf(entity.getValidUserNumTody()));
                usePeopleNumAll.setText(String.valueOf(entity.getRegUserNumAll()));
                orderToday.setText(String.valueOf(entity.getOrderNumToday()));
                orderAll.setText(String.valueOf(entity.getOrderNumAll()));
                break;
            case "Statistics/consumptionChart"://点击消费记录
                Map map1 = (Map) result.getData().getData();
                webView.loadUrl(map1.get("url").toString());
                Log.e("cxt", new DecimalFormat("#0.00.").format("kkkk"));
                consumeRecordToday.setText(new DecimalFormat("#0.00.").format(map1.get("consumeMoneyToday").toString()));
                consumeRecordAll.setText(new DecimalFormat("#0.00").format(map1.get("consumeMoneyAll").toString()));
                break;

            case "Statistics/regChart"://点击注册记录
                map1 = (Map) result.getData().getData();
                webView.loadUrl(map1.get("url").toString());
                consumeRecordToday.setText(map1.get("consumeMoneyToday").toString());
                consumeRecordAll.setText(map1.get("consumeMoneyAll").toString());
                break;
            case "Statistics/orderChart"://点击订单记录数
                map1 = (Map) result.getData().getData();
                webView.loadUrl(map1.get("url").toString());
                consumeRecordToday.setText(map1.get("consumeMoneyToday").toString());
                consumeRecordAll.setText(map1.get("consumeMoneyAll").toString());
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onResume() {
        super.onResume();
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent.getAction() == KeyEvent.ACTION_DOWN && i == KeyEvent.KEYCODE_BACK && webView.getVisibility() == View.VISIBLE) {

                    SetIsVisibile(0);
                    return true;
                } else {
//                    getActivity().onBackPressed();
                    return false;
                }

            }
        });
    }


    @OnClick({R.id.toolbar_back, R.id.layout_homeone, R.id.layout_home_two, R.id.layout_home_three, R.id.layout_home_four})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.toolbar_back:
                SetIsVisibile(0);
                break;
            case R.id.layout_homeone:
                toolbarTitle.setText("注册情况");
                SetIsVisibile(R.id.layout_homeone);
                loadWebvieRegwData();
                break;
            case R.id.layout_home_two:
                toolbarTitle.setText("消费记录");
                SetIsVisibile(R.id.layout_home_two);
                loadWebviewData();
                break;
            case R.id.layout_home_three:

                break;
            case R.id.layout_home_four:
                toolbarTitle.setText("订单");
                SetIsVisibile(R.id.layout_home_four);
                loadWebvieOrderData();
                break;
        }
    }

    @Override //选择完学校校区时间后的回调接口
    public void CallBackSelect(int org_id, int org_area_id, int boothroom_id, String device_key, String StartTime, String EndTime) {
        this.org_id = org_id;
        this.org_area_id = org_area_id;
        this.device_area_id = boothroom_id;
        this.device_key = device_key;
        start_date = StartTime;
        end_date = EndTime;
        if (View.GONE == toolbarBack.getVisibility()) {
            loadData();
        } else {
            switch (toolbarTitle.getText().toString()) {
                case "消费记录":
                    loadWebviewData();
                    break;
                case "注册情况":
                    loadWebvieRegwData();
                    break;
                case "订单":
                    loadWebvieOrderData();
                    break;
            }

        }

    }


}

