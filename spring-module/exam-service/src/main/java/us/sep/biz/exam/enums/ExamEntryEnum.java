package us.sep.biz.exam.enums;

public enum  ExamEntryEnum {

    Prepare("PREPARE","考试报名未发布"),
    Start("START","考试开始报名"),
    Cancel("CANCEL","考试报名取消"),
    Finish("FINISH","考试报名完成")

            ;

    String state;
    String name;

    ExamEntryEnum(String state, String name) {
        this.state = state;
        this.name = name;
    }

    public String getState() {
        return state;
    }

    public void setState(String action) {
        this.state = state;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
