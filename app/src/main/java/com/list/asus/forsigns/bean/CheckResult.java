package com.list.asus.forsigns.bean;

import cn.bmob.v3.BmobObject;

//考勤结果表
public class CheckResult extends BmobObject {

    private String checkId;
    private String stuId;
    private String stuName;
    private Boolean clockStatus;
    private String remark;


    public Boolean getClockStatus() {
        return clockStatus;
    }

    public void setClockStatus(Boolean clockStatus) {
        this.clockStatus = clockStatus;
    }

    public String getCheckId() {
        return checkId;
    }

    public void setCheckId(String checkId) {
        this.checkId = checkId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getStuId() {
        return stuId;
    }

    public void setStuId(String stuId) {
        this.stuId = stuId;
    }

    public String getStuName() {
        return stuName;
    }

    public void setStuName(String stuName) {
        this.stuName = stuName;
    }


}
