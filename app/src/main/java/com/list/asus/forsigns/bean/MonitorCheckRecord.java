package com.list.asus.forsigns.bean;

/*
 * Created by ASUS on 2017/5/22.
 */

import java.util.Date;

public class MonitorCheckRecord {
    /**
     * appKey : a16ec96b707da5d0f2a404b0ce9d755b
     * tableName : CheckRecord
     * objectId :
     * action : updateTable
     * data : {"checkId":"20170522s1","createdAt":"2017-05-22 22:51:50","objectId":"GzrUS116","teaId":"s1","teachingClass":"01","updatedAt":"2017-05-22 22:52:17"}
     */

    private String appKey;
    private String tableName;
    private String objectId;
    private String action;
    private DataBean data;

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * checkId : 20170522s1
         * createdAt : 2017-05-22 22:51:50
         * objectId : GzrUS116
         * teaId : s1
         * teachingClass : 01
         * updatedAt : 2017-05-22 22:52:17
         */

        private String checkId;
        private String createdAt;
        private String objectId;
        private String teaId;
        private String teachingClass;
        private String updatedAt;

        public String getCheckId() {
            return checkId;
        }

        public void setCheckId(String checkId) {
            this.checkId = checkId;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getObjectId() {
            return objectId;
        }

        public void setObjectId(String objectId) {
            this.objectId = objectId;
        }

        public String getTeaId() {
            return teaId;
        }

        public void setTeaId(String teaId) {
            this.teaId = teaId;
        }

        public String getTeachingClass() {
            return teachingClass;
        }

        public void setTeachingClass(String teachingClass) {
            this.teachingClass = teachingClass;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }
    }
}
