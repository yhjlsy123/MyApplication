package com.zhuochi.hydream.entity;

/**
 * Created by ztech on 2018/7/5.
 */
/*排名类型*/
public class RankingTypeEntity {

    /**
     * ranking_type : weekly
     * ranking_type_name : 周榜
     */

    private String ranking_type;
    private String ranking_type_name;

    public String getRanking_type() {
        return ranking_type;
    }

    public void setRanking_type(String ranking_type) {
        this.ranking_type = ranking_type;
    }

    public String getRanking_type_name() {
        return ranking_type_name;
    }

    public void setRanking_type_name(String ranking_type_name) {
        this.ranking_type_name = ranking_type_name;
    }
}
