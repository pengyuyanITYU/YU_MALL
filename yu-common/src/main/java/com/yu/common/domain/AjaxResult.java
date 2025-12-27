package com.yu.common.domain;

import com.yu.common.constant.HttpStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 通用响应实体类 (泛型版本)
 * @param <T> 数据对象的类型
 */
@Data
@NoArgsConstructor
@ApiModel(description = "通用响应对象")
public class AjaxResult<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 成功 */
    public static final int SUCCESS = HttpStatus.SUCCESS;
    /** 失败 */
    public static final int FAIL = HttpStatus.ERROR;

    @ApiModelProperty(value = "状态码", example = "200")
    private int code;

    @ApiModelProperty(value = "返回消息", example = "操作成功")
    private String msg;

    @ApiModelProperty(value = "数据对象")
    private T data;

    /**
     * 私有构造，强制使用静态方法创建
     */
    private AjaxResult(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    // ============================
    // 成功响应的方法
    // ============================

    public static <T> AjaxResult<T> success() {
        return new AjaxResult<>(SUCCESS, "操作成功", null);
    }

    public static <T> AjaxResult<T> success(T data) {
        return new AjaxResult<>(SUCCESS, "操作成功", data);
    }

    public static <T> AjaxResult<T> success(String msg, T data) {
        return new AjaxResult<>(SUCCESS, msg, data);
    }

    // ============================
    // 警告/错误响应的方法
    // ============================

    public static <T> AjaxResult<T> warn(String msg) {
        return new AjaxResult<>(HttpStatus.WARN, msg, null);
    }

    public static <T> AjaxResult<T> error() {
        return new AjaxResult<>(FAIL, "操作失败", null);
    }

    public static <T> AjaxResult<T> error(String msg) {
        return new AjaxResult<>(FAIL, msg, null);
    }

    public static <T> AjaxResult<T> error(int code, String msg) {
        return new AjaxResult<>(code, msg, null);
    }

    // ============================
    // 辅助方法
    // ============================

    /**
     * 响应返回结果
     * @param rows 影响行数
     * @return 操作结果
     */
    public static <T> AjaxResult<T> toAjax(int rows) {
        return rows > 0 ? AjaxResult.success() : AjaxResult.error();
    }


    /**
     * 响应返回结果
     * @param result 结果
     * @return 操作结果
     */
    public static <T> AjaxResult<T> toAjax(boolean result) {
        return result ? success() : error();
    }

    /**
     * 校验是否成功
     */
    public boolean isSuccess() {
        return this.code == HttpStatus.SUCCESS;
    }
}