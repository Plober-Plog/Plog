package com.plog.backend.domain.neighbor.entity;

import java.util.NoSuchElementException;

public enum NeighborType {
    NEIGHBOR(1),
    MUTUAL_NEIGHBOR(2);

    private int value;

    NeighborType(int value) {this.value =  value; }

    public int getValue() { return value; }

    public static NeighborType neighborType(int value) {
        for(NeighborType neighborType : NeighborType.values()) {
            if(neighborType.getValue() == value) {
                return neighborType;
            }
        }
        throw new NoSuchElementException("일치하는 이웃 종류가 없습니다. : " + value);
    }
}
