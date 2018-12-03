package com.honda.interauto.pojo;

public class ResultMerged {
    private boolean merged;
    private int startRow;
    private int endRow;
    private int startCol;
    private int endCol;
    private String mergedValue;

    public boolean isMerged() {
        return merged;
    }

    public void setMerged(boolean merged) {
        this.merged = merged;
    }

    public int getStartRow() {
        return startRow;
    }

    public void setStartRow(int startRow) {
        this.startRow = startRow;
    }

    public int getEndRow() {
        return endRow;
    }

    public void setEndRow(int endRow) {
        this.endRow = endRow;
    }

    public int getStartCol() {
        return startCol;
    }

    public void setStartCol(int startCol) {
        this.startCol = startCol;
    }

    public int getEndCol() {
        return endCol;
    }

    public void setEndCol(int endCol) {
        this.endCol = endCol;
    }

    public String getMergedValue() {
        return mergedValue;
    }

    public void setMergedValue(String mergedValue) {
        this.mergedValue = mergedValue;
    }

    public ResultMerged(boolean merged, int startRow, int endRow, int startCol, int endCol, String mergedValue) {
        this.merged = merged;
        this.startRow = startRow;
        this.endRow = endRow;
        this.startCol = startCol;
        this.endCol = endCol;
        this.mergedValue = mergedValue;
    }
}
