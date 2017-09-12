package com.java.group8;

import java.io.Serializable;

/**
 * @auther Li Yifei
 *
 */

public enum NewsCategory implements Serializable {
    SCIENCE(1), EDUCATION(2), MILITARY(3), DOMESTIC(4),
    SOCIETY(5), CULTURE(6), CAR(7), INTERNATIONAL(8),
    SPORT(9), ECONOMY(10), HEALTH(11), ENTERTAINMENT(12);

    private int index;

    NewsCategory(int _index) {
        index = _index;
    }

    public int getIndex() {
        return index;
    }

    public static NewsCategory valueOf(int code) {
        switch (code){
            case 1:
                return SCIENCE;
            case 2:
                return EDUCATION;
            case 3:
                return MILITARY;
            case 4:
                return DOMESTIC;
            case 5:
                return SOCIETY;
            case 6:
                return CULTURE;
            case 7:
                return CAR;
            case 8:
                return INTERNATIONAL;
            case 9:
                return SPORT;
            case 10:
                return ECONOMY;
            case 11:
                return HEALTH;
            case 12:
                return ENTERTAINMENT;
        }
        return SCIENCE;
    }
}