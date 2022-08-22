package com.dhu.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class Manager {

    @TableId("m_id")
    private Integer m_id;

    private String m_name;

    private String m_password;

}
