
package com.express.travel.bean;
/**
 * @author 
 *
 */
public class Goods  {

    /**
     * id
     */
    private Integer id;

    /**
     * 标题
     */
    private String title;

    /**
     * 简单描述
     */
    private String brief_descn;

    /**
     * 详细描述
     */
    private String detail_descn;

    /**
     * 封面图片
     */
    private String coverpath;

    /**
     * 最低价格
     */
    private Float lowest_price;

    /**
     * 分类id
     */
    private Integer category_id;

    /**
     * 区域id
     */
    private Integer region_id;
    
    /**
     * 产品状态 S:上架，X:下架，D删除
     */
    private String state;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDetail_descn() {
		return detail_descn;
	}

	public void setDetail_descn(String detail_descn) {
		this.detail_descn = detail_descn;
	}

	public String getCoverpath() {
		return coverpath;
	}

	public void setCoverpath(String coverpath) {
		this.coverpath = coverpath;
	}

	public Float getLowest_price() {
		return lowest_price;
	}

	public void setLowest_price(Float lowest_price) {
		this.lowest_price = lowest_price;
	}

	public Integer getCategory_id() {
		return category_id;
	}

	public void setCategory_id(Integer category_id) {
		this.category_id = category_id;
	}

	public Integer getRegion_id() {
		return region_id;
	}

	public void setRegion_id(Integer region_id) {
		this.region_id = region_id;
	}

	public String getBrief_descn() {
		return brief_descn;
	}

	public void setBrief_descn(String brief_descn) {
		this.brief_descn = brief_descn;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

}