package com.cct.service1.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

@Data
public class TestUser extends Model<TestUser> {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String name;
}
