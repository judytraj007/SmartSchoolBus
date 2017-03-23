package Model;

/**
 * Created by Judy T Raj on 05-Jan-17.
 */

public class Student {

    private String name;
    private String busno;
    private String classNo;
    private String id;
    private String stopid;
    private String parentId;
    private int status;

    public String getClassNo() {
        return classNo;
    }

    public void setClassNo(String classNo) {
        this.classNo = classNo;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getId() {
        return id;
    }

    public String getBusno() {
        return busno;
    }

    public void setBusno(String busno) {
        this.busno = busno;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStopid() {
        return stopid;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setStopid(String stopid) {
        this.stopid = stopid;
    }
}
