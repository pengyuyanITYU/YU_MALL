package com.yu.user.enums;

public enum MemberLevel {
    /** 普通会员 */
    GENERAL(1, "普通会员"),

    /** 白银会员 */
    SILVER(2, "白银会员"),

    /** 黄金会员 */
    GOLD(3, "黄金会员"),

    /** 铂金会员 */
    PLATINUM(4, "铂金会员"),

    /** 钻石会员 */
    DIAMOND(5, "钻石会员"),

    /** 至尊黑卡 */
    SUPREME_BLACK(6, "至尊黑卡");

    private final Integer id;
    private final String name;

    MemberLevel(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

}