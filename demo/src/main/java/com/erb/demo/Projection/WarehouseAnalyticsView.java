package com.erb.demo.Projection;

public class WarehouseAnalyticsView {
    private Long totalCount;
    private Long recentCount;

    public WarehouseAnalyticsView(Long totalCount, Long recentCount) {
        this.totalCount = totalCount;
        this.recentCount = recentCount;
    }

    public Long getTotalCount() {
        return totalCount;
    }

    public Long getRecentCount() {
        return recentCount;
    }
}
