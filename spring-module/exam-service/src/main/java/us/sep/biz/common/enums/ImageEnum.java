package us.sep.biz.common.enums;

public enum ImageEnum {
    AVATAR("头像","Avatar" ),
    SHOW("展示","Show"),
    EXAM("考试信息相关","Exam"),

    ;
    //图片用途描述
    String desc;
    //图片用途标签
    String tag;

    ImageEnum(String desc, String tag) {
        this.desc = desc;
        this.tag = tag;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
