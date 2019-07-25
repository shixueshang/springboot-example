package cn.ctoedu.miaosha.service;

import cn.ctoedu.miaosha.domain.MiaoshaGoods;
import cn.ctoedu.miaosha.mapper.GoodsMapper;
import cn.ctoedu.miaosha.vo.GoodsVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by xiaomi on 2019/07/23
 */
@Service
public class GoodsService {

    @Resource
    GoodsMapper goodsMapper;

    public List<GoodsVo> listGoodsVo(){
        return goodsMapper.listGoodsVo();
    }

    public GoodsVo getGoodsVoByGoodsId(long goodsId) {
        return goodsMapper.getGoodsVoByGoodsId(goodsId);
    }

    public boolean reduceStock(GoodsVo goods) {
        MiaoshaGoods g = new MiaoshaGoods();
        g.setGoodsId(goods.getId());
        return goodsMapper.reduceStock(g) > 0;
    }

    public void resetStock(List<GoodsVo> goodsList) {
        for(GoodsVo goods : goodsList ) {
            MiaoshaGoods g = new MiaoshaGoods();
            g.setGoodsId(goods.getId());
            g.setStockCount(goods.getStockCount());
            goodsMapper.resetStock(g);
        }
    }
}
