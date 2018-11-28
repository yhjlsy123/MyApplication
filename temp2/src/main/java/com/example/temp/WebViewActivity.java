package com.example.temp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

public class WebViewActivity extends AppCompatActivity {

    private WebView web;
    private TextView titleTv;

    String message = "<!DOCTYPE html>\n" +
            "    <html>\n" +
            "    <head>\n" +
            "    <title>事件办结率</title>\n" +
            "    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />\n" +
            "    <meta content=\"width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0;\" name=\"viewport\" />\n" +
            "    <style type=\"text/css\">\n" +
            "    .warpper {\n" +
            "    \twidth:320px;\n" +
            "    \theight:480px;\n" +
            "    \tmargin: 0 auto;\n" +
            "    }\n" +
            "    \n" +
            "    #main { \n" +
            "    \twidth: 100%;\n" +
            "    \theight: 100%;\n" +
            "    }\n" +
            "    </style>\n" +
            "    </head>\n" +
            "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\" />\n" +
            "    <body>\n" +
            "    \t<div class=\"warpper\">\n" +
            "    \t\t<div id=\"main\" style=\"margin: 0 auto; \"></div>\n" +
            "    \t</div>\n" +
            "    </body>\n" +
            "    <script type=\"text/javascript\"\n" +
            "    \tsrc=\"http://47.104.71.147:80/zzbzbnew/js/jquery.min.js\"></script>\n" +
            "    <script type=\"text/javascript\"\n" +
            "    \tsrc=\"http://47.104.71.147:80/zzbzbnew/js/echarts.min.js\"></script>\n" +
            "    <script type=\"text/javascript\">\n" +
            "    \t$(function() {\n" +
            "    \t\tcityLeft();\n" +
            "    \t});\n" +
            "    \t\n" +
            "    \tvar myChartyuan = echarts.init(document.getElementById('main'));\n" +
            "    \toptionevent = {\n" +
            "            backgroundColor: 'white',\n" +
            "            title: {\n" +
            "                text: '办结率',\n" +
            "                left: 'center',\n" +
            "                top: 20\n" +
            "            },\n" +
            "            tooltip : {\n" +
            "                trigger: 'item',\n" +
            "                formatter: \"{a} <br/>{b} : {c} ({d}%)\"\n" +
            "            },\n" +
            "            legend: {\n" +
            "    \t        orient: '',\n" +
            "    \t        right: 'right'\n" +
            "    \t    },\n" +
            "            series : [\n" +
            "                {\n" +
            "                    name:'',\n" +
            "                    type:'pie',\n" +
            "                    clockwise:'false',\n" +
            "                    center: ['50%', '60%'],\n" +
            "                    label : {\n" +
            "                        normal : {\n" +
            "                          position : \"outside\",\n" +
            "                          formatter: '{d}%'\n" +
            "                        }\n" +
            "                      },\n" +
            "    \t              labelLine : {\n" +
            "    \t                  normal : {\n" +
            "    \t                    length : 5,\n" +
            "    \t                    length2 : 5\n" +
            "    \t                  }\n" +
            "    \t                },\n" +
            "                    itemStyle: {\n" +
            "                      emphasis: {\n" +
            "                          shadowBlur: 10,\n" +
            "                          shadowOffsetX: 0,\n" +
            "                          shadowColor: 'rgba(0, 0, 0, 0.5)'\n" +
            "                      }\n" +
            "                \t}\n" +
            "                }\n" +
            "            ]\n" +
            "        };\n" +
            "    \t\n" +
            "    \tfunction getUrlParms(name) {\n" +
            "    \t    var reg = new RegExp(\"(^|&)\" + name + \"=([^&]*)(&|$)\");\n" +
            "    \t    var r = window.location.search.substr(1).match(reg);\n" +
            "    \t    if (r != null)\n" +
            "    \t        return unescape(r[2]);\n" +
            "    \t    return null;\n" +
            "    \t}\n" +
            "    \t\n" +
            "    \t/*事件办结率Echarts赋值*/\n" +
            "    \tfunction cityLeft() {\n" +
            "    \t\t\n" +
            "    \t\tvar param={\"orgId\":getUrlParms(\"orgId\")};\n" +
            "    \t\tconsole.log(JSON.stringify(param));\n" +
            "    \t\t\n" +
            "    \t\t$.ajax({\n" +
            "    \t\t\t\t\turl : \"http://47.104.71.147:80/zzbzbnew/appindex/default.do?method = eventClosureRate&isMobile=1\",\n" +
            "    \t\t\t\t\tdataType : 'json',\n" +
            "    \t\t\t\t\ttype : \"post\",\n" +
            "    \t\t\t\t\tdata:param,\n" +
            "    \t\t\t\t\tasync : false,\n" +
            "    \t\t\t\t\tsuccess : function(data) {\n" +
            "    \t\t\t\t\t\tvar eventClosureRate = [];\n" +
            "    \t\t\t\t\t\tvar eventTypeNameArray=[];\n" +
            "    \t\t\t\t\t\tvar data = data.tab_list;\n" +
            "    \t\t\t\t\t\tfor (var i = 0; i < data.length; i++) {\n" +
            "    \t\t\t\t\t\t\tvar count = data[i].count;\n" +
            "    \t\t\t\t\t\t\tvar eventTypeName = data[i].eventTypeName;\n" +
            "    \t\t\t\t\t\t\t\n" +
            "    \t\t\t\t\t\t\teventTypeNameArray.push(eventTypeName);\n" +
            "    \t\t\t\t\t\t\t\n" +
            "    \t\t\t\t\t\t\teventClosureRate.push({\n" +
            "    \t\t\t\t\t\t\t\tvalue : count,\n" +
            "    \t\t\t\t\t\t\t\tname : eventTypeName\n" +
            "    \t\t\t\t\t\t\t});\n" +
            "    \t\t\t\t\t\t}\n" +
            "    \t\t\t\t\t\t\n" +
            "    \t\t\t\t\t\toptionevent.legend.data=eventTypeNameArray;\n" +
            "    \t\t\t\t\t\toptionevent.series[0].data = eventClosureRate;\n" +
            "    \t\t\t\t\t\tmyChartyuan.setOption(optionevent);\n" +
            "    \t\t\t\t\t},\n" +
            "    \t\t\t\t\terror : function(data) {\n" +
            "    \t\t\t\t\t\tlayer.msg(\"执行失败o_o请重试...\", {\n" +
            "    \t\t\t\t\t\t\ticon : 2\n" +
            "    \t\t\t\t\t\t});\n" +
            "    \t\t\t\t\t}\n" +
            "    \t\t\t\t})\n" +
            "    \t}\n" +
            "    </script>";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        web = (WebView) findViewById(R.id.web);
        titleTv = (TextView) findViewById(R.id.title);

        WebSettings settings = web.getSettings();
        settings.setUseWideViewPort(true);//设定支持viewport
        settings.setLoadWithOverviewMode(true);   //自适应屏幕
        settings.setBuiltInZoomControls(true);
        settings.setDisplayZoomControls(false);
        settings.setSupportZoom(true);//设定支持缩放
        settings.setJavaScriptEnabled(true);
        settings.setDefaultTextEncodingName("UTF-8");


//        web.loadUrl("http://www.zhihuiducha.com/zzbzbnew/customized/shouye/appIndex/eventClosureRate.jsp?isMobile=1&orgId=37.01.12.002");
        web.setWebChromeClient(new WebChromeClient() {
            //这里设置获取到的网站title
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                titleTv.setText(title);
            }
        });
//        web.loadData(message, "text/html", "UTF-8");
        web.loadDataWithBaseURL("http://47.104.71.147:80/zzbzbnew/js/", message, "text/html", "utf-8", null);


        web.setWebViewClient(new WebViewClient() {
            //在webview里打开新链接
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
    }
}
