package cn.imadc.application.xwareman.core.code;

import cn.imadc.application.base.codegenerator.BaseFastAutoGeneratorFaced;

/**
 * <p>
 * 代码生成器
 * </p>
 *
 * @author 杜劲松
 * @since 2021-12-24
 */
public class CodeGenerator {

    // mysql相关信息：连接url、用户名、密码
    static String URL = "jdbc:mysql://10.100.15.50:3306/x_ware_man_dev";
    static String DB_USER_NAME = "root";
    static String DB_PASSWORD = "xsmysql";

    // 代码生成后存放的本地磁盘位置
    static String OUTPUT_DIR = "D://codeGenerate";

    // 指定表名
    static String TABLE_NAME = "trigger";

    // 代码所在的包名（注意这里不要填写最后一级报名）
    static String PACKAGE_NAME = "cn.imadc.application.xwareman.module";
    // 代码所在的包名，最后一级包名
    static String MODULE_NAME = "trigger";
    // 注释上的作者
    static String AUTHOR = "杜劲松";

    public static void main(String[] args) {
        BaseFastAutoGeneratorFaced.generate(URL, DB_USER_NAME, DB_PASSWORD, OUTPUT_DIR, TABLE_NAME, PACKAGE_NAME,
                MODULE_NAME, AUTHOR);
    }
}
