package com.express.travel.service;

import java.util.List;

import com.express.core.service.BaseService;
import com.express.travel.bean.Goods;
import com.express.travel.bean.GoodsAppendixVal;

public interface GoodsService extends BaseService<Goods>{

	/**根据商品id和父参数id 列出参数
	 * @param goods_id
	 * @param pid
	 * @return
	 */
	public List<GoodsAppendixVal> listParamValue(Object param);
}