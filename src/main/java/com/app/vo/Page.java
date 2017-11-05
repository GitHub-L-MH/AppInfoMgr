package com.app.vo;

import java.util.List;

public class Page {

    private int totalPageCount;
    private int pageSize;
    private int totalCount;
    private int currentPageNo;

    public int getTotalPageCount() {
        return totalPageCount;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        if (pageSize > 0) {
            this.pageSize = pageSize;
        } else {
            this.pageSize = 0;
        }
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        if (totalCount > 0) {
            this.totalCount = totalCount;
            this.totalPageCount = this.totalCount % this.pageSize == 0
                    ? (this.totalCount / this.pageSize) : (this.totalCount / this.pageSize + 1);
        }
    }

    public int getCurrentPageNo() {
        return currentPageNo;
    }

    public void setCurrentPageNo(int currentPageNo) {
        if (currentPageNo <= 0) {
            this.currentPageNo = 1;
        } else if (currentPageNo > this.totalPageCount) {
            this.currentPageNo = this.totalPageCount;
        } else {
            this.currentPageNo = currentPageNo;
        }
    }

    @Override
    public String toString() {
        return "Page{" +
                "totalPageCount=" + totalPageCount +
                ", pageSize=" + pageSize +
                ", totalCount=" + totalCount +
                ", currentPageNo=" + currentPageNo +
                '}';
    }
}
