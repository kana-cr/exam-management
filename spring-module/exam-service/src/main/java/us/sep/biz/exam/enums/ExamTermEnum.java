package us.sep.biz.exam.enums;

public enum ExamTermEnum {
    FirstHalf("FH","上学期"),
    SecondHalf("SH","下学期")
    ;

    String term;
    String message;

    ExamTermEnum(String term, String message) {
        this.term = term;
        this.message = message;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
