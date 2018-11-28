package com.zhuochi.hydream.bathhousekeeper.utils;

/**
 * @author Cuixc
 * @date on  2018/9/10
 */

public interface SelectedList {
    //学校
//    void BackSchoolData(int SchoolID, String SchoolName);
    //学校 校区
//    void BackSchoolOrgData(int SchoolID, String SchoolName, int SchoolOrgId, String SchoolOrgName);
    //学校 校区  浴室
//    void BackSchoolOrgAreaData(int SchoolID, String SchoolName, int SchoolOrgId, String SchoolOrgName, int SchoolAreaId, String SchoolAreaName);
    //学校 校区  浴室 浴位
    String BackSchoolOrgAreaBathData(int SchoolID, String SchoolName, int SchoolOrgId, String SchoolOrgName,
                                   int SchoolAreaId, String SchoolAreaName, int SchoolBathID, String SchoolBathName);

}
