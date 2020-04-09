package com.minbao.wwm.dao.entity;

public class Category {


    private int id;//主键
    private String name;//类型名称
    private String keywords;//关键字
    private String frontDesc;//前端描述
    private int parentId;//父ID
    private int sortOrder; //排序
    private int showIndex;//首页展示
    private int isShow;//是否展示
    private String iconUrl;//类别图标URL
    private String imgUrl;//类别图片URL
    private String level;//级别
    private String frontName;//前端名称
    private int pHeight;//高度
    private int isCategory;//是否是分类
    private int isChannel;//是否是引导

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getFrontDesc() {
        return frontDesc;
    }

    public void setFrontDesc(String frontDesc) {
        this.frontDesc = frontDesc;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public int getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(int sortOrder) {
        this.sortOrder = sortOrder;
    }

    public int getShowIndex() {
        return showIndex;
    }

    public void setShowIndex(int showIndex) {
        this.showIndex = showIndex;
    }

    public int getIsShow() {
        return isShow;
    }

    public void setIsShow(int isShow) {
        this.isShow = isShow;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getFrontName() {
        return frontName;
    }

    public void setFrontName(String frontName) {
        this.frontName = frontName;
    }

    public int getpHeight() {
        return pHeight;
    }

    public void setpHeight(int pHeight) {
        this.pHeight = pHeight;
    }

    public int getIsCategory() {
        return isCategory;
    }

    public void setIsCategory(int isCategory) {
        this.isCategory = isCategory;
    }

    public int getIsChannel() {
        return isChannel;
    }

    public void setIsChannel(int isChannel) {
        this.isChannel = isChannel;
    }

    public Category(int id, String name, String keywords, String frontDesc, int parentId, int sortOrder, int showIndex, int isShow, String iconUrl, String imgUrl, String level, String frontName, int pHeight, int isCategory, int isChannel) {
        this.id = id;
        this.name = name;
        this.keywords = keywords;
        this.frontDesc = frontDesc;
        this.parentId = parentId;
        this.sortOrder = sortOrder;
        this.showIndex = showIndex;
        this.isShow = isShow;
        this.iconUrl = iconUrl;
        this.imgUrl = imgUrl;
        this.level = level;
        this.frontName = frontName;
        this.pHeight = pHeight;
        this.isCategory = isCategory;
        this.isChannel = isChannel;
    }

    public Category(){}

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", keywords='" + keywords + '\'' +
                ", frontDesc='" + frontDesc + '\'' +
                ", parentId=" + parentId +
                ", sortOrder=" + sortOrder +
                ", showIndex=" + showIndex +
                ", isShow=" + isShow +
                ", iconUrl='" + iconUrl + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", level='" + level + '\'' +
                ", frontName='" + frontName + '\'' +
                ", pHeight=" + pHeight +
                ", isCategory=" + isCategory +
                ", isChannel=" + isChannel +
                '}';
    }
}
