package spark;

public class ProjectWordCount {
    String tags1;
    String tags2;

    public String getTags1() {
        if (tags1.equals(""))
            return tags2;
        return tags1;
    }

    public void setTags1(String tags1) {
        this.tags1 = tags1;
    }

    public String getTags2() {
        return tags2;
    }

    public void setTags2(String tags2) {
        this.tags2 = tags2;
    }
}
