package com.list.asus.forsigns.bean;

import cn.bmob.v3.BmobObject;


//教学班—学生号
public class Class_stuId extends BmobObject {

    private String teachingClass;
    private String stuId;
    private Integer classOrder;

    public Integer getClassOrder() {
        return classOrder;
    }

    public void setClassOrder(Integer classOrder) {
        this.classOrder = classOrder;
    }

    public String getStuId() {
        return stuId;
    }

    public void setStuId(String stuId) {
        this.stuId = stuId;
    }

    public String getTeachingClass() {
        return teachingClass;
    }

    public void setTeachingClass(String teachingClass) {
        this.teachingClass = teachingClass;
    }



}
