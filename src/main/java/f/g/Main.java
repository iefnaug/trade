package f.g;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;

@EnableScheduling
@SpringBootApplication
public class Main {


    public static void calculate(double initial, double rate, LocalDate start, int times) {
        for (int i = 0; i < times; i++) {
            double money = initial * (1 + rate);
            double profit = money - initial;
            System.out.printf("%3d-总计: %-8.0f, 利润: %-8.0f\n", (i+1), money, profit);
            initial = money;
        }
    }

    public static void main(String[] args) {
//        SpringApplication.run(Main.class, args);


//        LocalDate now = LocalDate.of(2025, 1, 12);
//        LocalDate future = LocalDate.of(2025, 6, 26);
//        System.out.println(future.toEpochDay() - now.toEpochDay());

//        calculate(15000, 0.08, LocalDate.of(2025, 1, 20), 40);
//        roll(1, 100, 40, 0.001, 0.1);
        //主流
        roll(80000, 300, 40, 0.005, 0.05);
        roll(80000, 300, 30, 0.005, 0.1);
        //山寨
//        roll(10, 300, 30, 0.01, 0.05);
//        roll(10, 300, 20, 0.01, 0.1);
    }


    /**
     * 计算滚仓
     * @param price 初始币价
     * @param asset 初始资金
     * @param multiple 倍数
     * @param rate 每次获利百分比
     * @param targetRate 目标总获利百分比
     */
    private static void roll(double price, double asset, int multiple, double rate, double targetRate) {
        int times = (int) (targetRate / rate);
        double position = asset * multiple;
        for (int i = 1; i <= times; i++) {
            //当前持币数量
            double amount = position / price;
            //利润
            double profit = position * rate;
            //仓位
            position = position + profit * multiple;
            //币价
            price = price * (1 + rate);
            //持仓均价
            double avgPrice = position / (amount + profit * multiple / price);
            System.out.printf("第%d次获利: %.2f, 当前币价: %.4f, 持仓均价: %.4f，仓位: %.2f\n", i, profit, price, avgPrice, position);
        }
        double finalAsset = position / multiple;
        System.out.printf("滚仓最终盈利: %.2f\n", finalAsset - asset);
        System.out.printf("不滚仓最终盈利: %.2f\n", asset * multiple * targetRate);
    }

}