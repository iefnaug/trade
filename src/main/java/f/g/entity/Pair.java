package f.g.entity;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Pair {

    private String symbol;
    private double high;
    private double low;
    private double open;
    private double close;
    private double rate;

}
