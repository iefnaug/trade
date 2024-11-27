package f.g.job;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import f.g.entity.Pair;
import f.g.socket.DataHandler;
import io.micrometer.common.util.StringUtils;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;

@Component
public class MonitorJob {

    @Resource
    private DataHandler dataHandler;

    public static final List<String> symbolList = new LinkedList<>();

    public static ExecutorService EXECUTOR = Executors.newFixedThreadPool(10);

    @PostConstruct
    public void init() {
        System.out.println("初始化资源");
//        getSymbols();
    }


//    @Scheduled(cron = "0 0/1 * * * ?")
    @Scheduled(cron = "0/5 * * * * ?")
    public void monitorPrice() throws InterruptedException {
//        System.out.println("开始执行");
//        long start = System.currentTimeMillis();
//        CountDownLatch countDownLatch = new CountDownLatch(symbolList.size());
//        OkHttpClient client = new OkHttpClient();
//        for (String symbol : symbolList) {
//            EXECUTOR.execute(() -> {
//                Request request = new Request.Builder()
//                        .url("https://fapi.binance.com/fapi/v1/markPriceKlines?symbol=" + symbol + "&interval=1m&limit=1")
//                        .build();
//                try (Response response = client.newCall(request).execute()) {
//                    String json = response.body().string();
//                    if (StringUtils.isBlank(json)) {
//                        System.err.println("json is null");
//                        return;
//                    }
//                    JSONArray array = JSONArray.parse(json);
//                    JSONArray jsonArray = array.getJSONArray(0);
//                    double open = jsonArray.getDouble(1);
//                    double high = jsonArray.getDouble(2);
//                    double low = jsonArray.getDouble(3);
//                    double close = jsonArray.getDouble(4);
//                    double rate = (high - low) / open;
//                    if (rate >= 0.01) {
//                        System.out.printf("%s波动大于1%%, 开%f, 高%f, 低%f, 收%f \n", symbol, open, high, low, close);
//                    }
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                } finally {
//                    countDownLatch.countDown();
//                }
//            });
//        }
//        countDownLatch.await();
//        System.out.printf("执行结束，耗时%d, 当前时间: %s\n", System.currentTimeMillis() - start, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now()));
        Random random = new Random();
        Pair pair = Pair.builder().symbol("BTCUSDT").open(99500).high(100000 + random.nextInt(1000)).low(99000).close(99800).rate(0.02).build();
        LinkedBlockingDeque<String> list = dataHandler.getQUEUE();
        if (list.size() > 50) {
            list.clear();
        }
        list.add(JSONObject.toJSONString(pair));
    }


    public static void getSymbols() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://fapi.binance.com/fapi/v1/exchangeInfo")
                .build();
        try (Response response = client.newCall(request).execute()) {
            String json = response.body().string();
            if (StringUtils.isBlank(json)) {
                System.err.println("json is null");
                return;
            }
            JSONObject obj = JSONObject.parseObject(json);
            JSONArray symbols = obj.getJSONArray("symbols");
            for (int i = 0; i < symbols.size(); i++) {
                String symbol = symbols.getJSONObject(i).getString("symbol");
                symbolList.add(symbol);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }




//import React from 'react';
//import ReactDOM from 'react-dom/client';
//import { useWebSocket } from 'ahooks';
//
//
//const MyComponent = () => {
//    const [pair, setPair] = React.useState({})
//
//    const { readyState, sendMessage, latestMessage, disconnect, connect } = useWebSocket(
//                'ws://localhost:8080/handler', {
//                        onMessage: function(msg) {
//            console.log(msg.data);
//            setPair(JSON.parse(msg.data))
//        }
//      }
//    );
//
//        function online() {
//            connect()
//        }
//
//        function offline() {
//            disconnect()
//        }
//
//
//        return (
//                <div>
//            <button onClick={online}>连接</button>
//            <button onClick={offline}>断开</button>
//            <p>标的: {pair.symbol} </p>
//            <p>开: {pair.open} </p>
//            <p>高: {pair.high} </p>
//            <p>低: {pair.low} </p>
//            <p>收: {pair.close} </p>
//            <p>波动率: {pair.rate} </p>
//        </div>
//    );
//    };
//
//
//  const root = ReactDOM.createRoot(document.getElementById('root'));
//  root.render(<MyComponent/>)


}
