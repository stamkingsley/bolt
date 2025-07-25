package com.cmex.bolt.domain;

import com.cmex.bolt.dto.DepthDto;
import com.google.common.base.Strings;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Symbol {
    private int id;

    private String name;

    private Currency base;

    private Currency quote;

    private boolean quoteSettlement;

    private OrderBook orderBook;

    public void init(){
        orderBook = new OrderBook(this);
    }

    public Currency getPayCurrency(Order.Side side) {
        return side == Order.Side.BID ? quote : base;
    }

    public long formatPrice(String price) {
        if (Strings.isNullOrEmpty(price)) {
            return 0;
        }
        return quote.parse(price);
    }

    public String parsePrice(long price) {
        return quote.format(price);
    }

    public Currency getIncomeCurrency(Order.Side side) {
        return side == Order.Side.BID ? base : quote;
    }

    public long formatQuantity(String quantity) {
        if (Strings.isNullOrEmpty(quantity)) {
            return 0;
        }
        return base.parse(quantity);
    }

    public String parseQuantity(long quantity) {
        return base.format(quantity);
    }

    public Currency getFeeCurrency(Order.Side side) {
        if (quoteSettlement) {
            return quote;
        }
        return getIncomeCurrency(side);
    }

    public long getVolume(long price, long quantity) {
        return Math.multiplyExact(price , quantity) / base.getMultiplier();
    }

    public DepthDto getDepth() {
        return orderBook.getDepth();
    }
}
