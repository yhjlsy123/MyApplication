package com.zhuochi.hydream.entity;

/**
 * @author Cuixc
 * @date on  2018/6/26
 */

public class RechargeableCardList {
    private int id;
    private String card_name;
    private int card_type;
    private String denomination;
    private String give;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCard_name() {
        return card_name;
    }

    public void setCard_name(String card_name) {
        this.card_name = card_name;
    }

    public int getCard_type() {
        return card_type;
    }

    public void setCard_type(int card_type) {
        this.card_type = card_type;
    }

    public String getDenomination() {
        return denomination;
    }

    public void setDenomination(String denomination) {
        this.denomination = denomination;
    }

    public String getGive() {
        return give;
    }

    public void setGive(String give) {
        this.give = give;
    }

}
