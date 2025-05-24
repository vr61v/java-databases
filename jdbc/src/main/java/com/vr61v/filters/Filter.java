package com.vr61v.filters;

import java.util.Map;

public interface Filter {

    Map<String, Object> toWhereParameters();

}
