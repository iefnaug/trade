package f.g.entity;

import lombok.Data;

@Data
public class Pair {

    private String symbol;
    private double high;
    private double low;
    private double open;
    private double close;

}
