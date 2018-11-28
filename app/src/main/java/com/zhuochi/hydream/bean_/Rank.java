package com.zhuochi.hydream.bean_;

import java.io.Serializable;
import java.util.List;

/**
 * 排行榜
 * Created by and on 2016/11/11.
 */

public class Rank extends Base {
    public RankData data;
    public static class RankData implements Serializable {
        public RankItem my;
        public List<RankItem>list;
    }
}
