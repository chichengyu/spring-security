package com.shiro.constarts;

/**
 * @ClassName: Constant
 */
public interface Constant {


    /**
     * 用户名称 key
     */
    String JWT_USER_NAME="jwt-user-name-key";

    /**
     * 权限key
     */
    String JWT_PERMISSIONS_KEY="jwt-permissions-key_";


    /**
     * 角色key
     */
    String JWT_ROLES_KEY="jwt-roles-key_";

    /**
     * refresh_token 主动退出后加入黑名单 key
     */
    String JWT_REFRESH_TOKEN_BLACKLIST="jwt-refresh-token-blacklist_";

    /**
     * access_token 主动退出后加入黑名单 key
     */
    String JWT_ACCESS_TOKEN_BLACKLIST="jwt-access-token-blacklist_";

    /**
     * 正常token
     */
    String ACCESS_TOKEN="authorization";
    /**
     * 刷新token
     */
    String REFRESH_TOKEN="refresh_token";

    /**
     * 标记用户是否已经被锁定
     */
    String ACCOUNT_LOCK_KEY="account-lock-key_";

    /**
     * 标记用户是否已经删除
     */
    String DELETED_USER_KEY="deleted-user-key_";

    /**
     * 主动去刷新 token key(适用场景 比如修改了用户的角色/权限去刷新token)
     */
    String JWT_REFRESH_KEY="jwt-refresh-key_";
    /**
     * 标记新的 access_token
     */
    String JWT_REFRESH_IDENTIFICATION="jwt-refresh-identification_";

    /**
     * 部门编码key
     */
    String DEPT_CODE_KEY="dept-code-key_";

    /**
     * 用户权鉴缓存 key
     */
    String IDENTIFY_CACHE_KEY="shiro-cache:com.xh.lesson.shiro.CustomRealm.authorizationCache:";

}
