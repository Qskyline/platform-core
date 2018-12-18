package com.skyline.platform.core.configure;

import com.skyline.util.OSUtil;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHeaders;
import org.springframework.http.MediaType;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * @ClassName wraper
 * @Description TODO
 * @Author skyline
 * @Date 2018/12/18 18:43
 * Version 1.0
 **/
public class ParameterRequestWrapper extends HttpServletRequestWrapper {
    private Map<String, String[]> params = new HashMap<>();

    public ParameterRequestWrapper(HttpServletRequest request) {
        super(request);
        Map<String, String[]> requestMap = request.getParameterMap();
        this.params.putAll(requestMap);
        this.modifyParameterValues();
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        //非json类型，直接返回
        if (!super.getHeader(HttpHeaders.CONTENT_TYPE).equalsIgnoreCase(MediaType.APPLICATION_JSON_VALUE)) {
            return super.getInputStream();
        }
        //为空，直接返回
        String json = OSUtil.processStdout(super.getInputStream(), "UTF-8");
        if (StringUtils.isEmpty(json)) {
            return super.getInputStream();
        }
        Map<String, Object> map = JSONObject.fromObject(json);
        Set<String> set = map.keySet();
        Iterator<String> it = set.iterator();
        while (it.hasNext()) {
            String key = it.next();
            Object values = map.get(key);
            if (values instanceof String) {
                values = ((String) values).trim();
            }
            map.put(key, values);
        }
        ByteArrayInputStream bis = new ByteArrayInputStream(JSONObject.fromObject(map).toString().getBytes("utf-8"));
        return new MyServletInputStream(bis);
    }

    private void modifyParameterValues() {
        Set<String> set = params.keySet();
        Iterator<String> it = set.iterator();
        while (it.hasNext()) {
            String key = it.next();
            String[] values = params.get(key);
            values[0] = values[0].trim();
            params.put(key, values);
        }
    }

    @Override
    public String getParameter(String name) {
        String[] values = params.get(name);
        if (values == null || values.length == 0) {
            return null;
        }
        return values[0];
    }

    @Override
    public String[] getParameterValues(String name) {
        return params.get(name);
    }

    class MyServletInputStream extends ServletInputStream {
        private ByteArrayInputStream bis;

        public MyServletInputStream(ByteArrayInputStream bis) {
            this.bis = bis;
        }

        @Override
        public boolean isFinished() {
            return true;
        }

        @Override
        public boolean isReady() {
            return true;
        }

        @Override
        public void setReadListener(ReadListener listener) {
        }

        @Override
        public int read() {
            return bis.read();
        }
    }
}