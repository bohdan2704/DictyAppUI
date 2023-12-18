package com.example.dictyappui.back.hashing.robin;

public class Entity {
    private int id;
    private int probeLength;
    private String key;
    private String value;
    private String description;

    public Entity(String key, String value, String description) {
        this.probeLength = 0;
        this.key = key;
        this.value = value;
        this.description = description;
    }


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getProbeLength() {
        return probeLength;
    }

    public void setProbeLength(int probeLength) {
        this.probeLength = probeLength;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Entity{" +
                "id=" + id +
                ", key='" + key + '\'' +
                ", value='" + value + '\'' +
                ", probeLength=" + probeLength +
                '}';
    }
}
