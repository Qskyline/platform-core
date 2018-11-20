package com.skyline.platform.core.configure;

import ch.qos.logback.classic.db.names.DefaultDBNameResolver;

public class MyDBNameResolver extends DefaultDBNameResolver {
    @Override
    public <N extends Enum<?>> String getTableName(N tableName) {
        return "sys_" + super.getTableName(tableName);
    }
}
