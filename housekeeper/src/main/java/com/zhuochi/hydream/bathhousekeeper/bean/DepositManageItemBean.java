package com.zhuochi.hydream.bathhousekeeper.bean;

public class DepositManageItemBean {

    /**
     * id : 25
     * wallet_id : 25
     * user_nickname : momo
     * mobile : 18854117407
     * deposit : 0.03
     * org_name : 卓驰大学
     * org_area_name : 济南校区
     * create_time : 1.5265468E9
     */

    private int id;
    private int wallet_id;
    private String user_nickname;
    private String mobile;
    private String deposit;
    private String org_name;
    private String org_area_name;
    private Long create_time;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getWallet_id() {
        return wallet_id;
    }

    public void setWallet_id(int wallet_id) {
        this.wallet_id = wallet_id;
    }

    public String getUser_nickname() {
        return user_nickname;
    }

    public void setUser_nickname(String user_nickname) {
        this.user_nickname = user_nickname;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getDeposit() {
        return deposit;
    }

    public void setDeposit(String deposit) {
        this.deposit = deposit;
    }

    public String getOrg_name() {
        return org_name;
    }

    public void setOrg_name(String org_name) {
        this.org_name = org_name;
    }

    public String getOrg_area_name() {
        return org_area_name;
    }

    public void setOrg_area_name(String org_area_name) {
        this.org_area_name = org_area_name;
    }

    public Long getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Long create_time) {
        this.create_time = create_time;
    }
}
