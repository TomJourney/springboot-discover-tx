package com.tom.springboot.tx.note.appilcation.user.dto;

import lombok.Data;

/**
 * @author tom
 * @version 1.0.0
 * @Description TODO
 * @createTime 2026年01月08日 06:55:00
 */
@Data
public class DataSourceConnNumDTO {

    private int idle;
    private int idleNum;
    private int active;
    private int numActive;

    public static DataSourceConnNumDTO build(int idle, int idleNum, int active, int numActive) {
        DataSourceConnNumDTO dataSourceConnNumDTO = new DataSourceConnNumDTO();
        dataSourceConnNumDTO.idle = idle;
        dataSourceConnNumDTO.idleNum = idleNum;
        dataSourceConnNumDTO.active = active;
        dataSourceConnNumDTO.numActive = numActive;
        return dataSourceConnNumDTO;
    }
}
