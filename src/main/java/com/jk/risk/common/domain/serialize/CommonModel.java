package com.jk.risk.common.domain.serialize;


import com.jk.risk.util.JSONUtil;

import java.io.Serializable;

public class CommonModel implements Serializable {
    protected String json;

    @Override
    public String toString() {
        return toJson();
    }

    public final String toJson() {
        if (this instanceof Immutable) {
            if (json == null) {
                json = JSONUtil.safeToJson(this);
            }
            return json;
        }
        return JSONUtil.safeToJson(this);
    }
}
