package pl.maksturzynski.cinemabooking.unit.fakes;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpSession;

import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class FakeHttpSession implements HttpSession {

    private final String id = UUID.randomUUID().toString();
    private final Map<String, Object> attrs = new ConcurrentHashMap<>();

    private final long creationTime = System.currentTimeMillis();
    private long lastAccessedTime = creationTime;
    private int maxInactiveInterval;
    private boolean invalidated;
    private boolean isNew = true;

    @Override public long getCreationTime() { return creationTime; }

    @Override public String getId() { return id; }

    @Override public long getLastAccessedTime() { return lastAccessedTime; }

    @Override public ServletContext getServletContext() { return null; }

    @Override public void setMaxInactiveInterval(int interval) { this.maxInactiveInterval = interval; }

    @Override public int getMaxInactiveInterval() { return maxInactiveInterval; }

    @Override public Object getAttribute(String name) {
        checkValid();
        touch();
        return attrs.get(name);
    }

    @Override public Enumeration<String> getAttributeNames() {
        checkValid();
        touch();
        return Collections.enumeration(attrs.keySet());
    }

    @Override public void setAttribute(String name, Object value) {
        checkValid();
        touch();
        if (value == null) attrs.remove(name);
        else attrs.put(name, value);
    }

    @Override public void removeAttribute(String name) {
        checkValid();
        touch();
        attrs.remove(name);
    }

    @Override public void invalidate() {
        invalidated = true;
        attrs.clear();
    }

    @Override public boolean isNew() {
        checkValid();
        return isNew;
    }

    // helper
    public void markNotNew() { this.isNew = false; }

    private void touch() { lastAccessedTime = System.currentTimeMillis(); }
    private void checkValid() {
        if (invalidated) throw new IllegalStateException("Session invalidated");
    }
}
