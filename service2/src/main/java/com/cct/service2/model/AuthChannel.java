package com.cct.service2.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
* <p>
    * 渠道对应的请求地止
    * </p>
*
* @author hongzp
* @since 2019-06-05
*/
    @Data
    @EqualsAndHashCode(callSuper = false)
    @Accessors(chain = true)
    @TableName("auth_channel")
    public class AuthChannel extends Model<AuthChannel> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 渠道id
     */
    @TableField("app_id")
    private String appId;

    /**
     * 私钥
     */
    @TableField("response_private_key")
    private String responsePrivateKey;

    /**
     * 公钥
     */
    @TableField("public_key")
    private String publicKey;

    /**
     * md5
     */
    @TableField("md5_key")
    private String md5Key;

    /**
     * 验签类型  0 无验签 1 rsa 2 md5
     */
    @TableField("sign_type")
    private Integer signType;




    @TableField("aes_key")
    private String aesKey;

    @TableField("need_in_aes")
    private Integer needInAes;
    @TableField("need_out_aes")
    private Integer needOutAes;

    @TableField("type")
    private Integer type;  //类型

    @TableField("old_card_segment")
    private String oldCardSegment;

    @TableField("old_card_type")
    private String oldCardType;



    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
