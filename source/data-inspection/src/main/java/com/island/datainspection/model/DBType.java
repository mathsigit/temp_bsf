package com.island.datainspection.model;

public enum DBType {

    HIVE (0) ,
    ORACLE (1);

    private int typeValue;

    public int getTypeValue() {
        return this.typeValue;
    }

    public boolean isHive() {
        if(this.typeValue == 0){
            return true;
        };
        return false;
    }

    public boolean isOracle() {
        if(this.typeValue ==1){
            return true;
        };
        return false;
    }

    DBType(int typeValue) {
        this.typeValue = typeValue;
    }
}
