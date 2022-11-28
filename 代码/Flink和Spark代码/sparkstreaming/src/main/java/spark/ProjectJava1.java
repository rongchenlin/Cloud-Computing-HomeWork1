package spark;

import java.io.Serializable;

public class ProjectJava1  implements Serializable {

    private  String  proName;
    private double  pullRequests;
    private  int  stars;
    private int  forks;
    private int  year;

    public String getProName() {
        return proName;
    }

    public void setProName(String proName) {
        this.proName = proName;
    }

    public double getPullRequests() {
        return pullRequests;
    }

    public void setPullRequests(double pullRequests) {
        this.pullRequests = pullRequests;
    }

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

    public int getForks() {
        return forks;
    }

    public void setForks(int forks) {
        this.forks = forks;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public  ProjectJava1  compute(){
        pullRequests =(pullRequests*3+stars*1+forks*2)/6.0;
        return this;
    }
}
