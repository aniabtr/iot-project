package com.example.irrigation;

import java.sql.Timestamp;
import java.util.Date;

public class Irrigation {

    private String selectedCrop;
    private float waterFlowRate;
    private String selectedCoverageAreaType;
    private float coverageAreaValue;
    private int numberOfIrrigationWeeks;
    private Date timestamp;
    private Date endDate;
    private long triggerTimeMillis;
    private long intervalMillis;


    public String getSelectedCrop() {
        return selectedCrop;
    }

    public float getWaterFlowRate() {
        return waterFlowRate;
    }

    public String getSelectedCoverageAreaType() {
        return selectedCoverageAreaType;
    }

    public float getCoverageAreaValue() {
        return coverageAreaValue;
    }

    public int getNumberOfIrrigationWeeks() {
        return numberOfIrrigationWeeks;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setSelectedCrop(String selectedCrop) {
        this.selectedCrop = selectedCrop;
    }

    public void setWaterFlowRate(float waterFlowRate) {
        this.waterFlowRate = waterFlowRate;
    }

    public void setSelectedCoverageAreaType(String selectedCoverageAreaType) {
        this.selectedCoverageAreaType = selectedCoverageAreaType;
    }

    public void setCoverageAreaValue(float coverageAreaValue) {
        this.coverageAreaValue = coverageAreaValue;
    }

    public void setNumberOfIrrigationWeeks(int numberOfIrrigationWeeks) {
        this.numberOfIrrigationWeeks = numberOfIrrigationWeeks;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }


    public Object getEndDate() { return endDate;
    }

    public void setEndDate(Date endDate) { this.endDate = endDate;
    }

    public long getTriggerTimeMillis() {
        return triggerTimeMillis;
    }

    public long getIntervalMillis() {
        return intervalMillis;
    }

    public void setTriggerTimeMillis(long triggerTimeMillis) {
        this.triggerTimeMillis = triggerTimeMillis;
    }

    public void setIntervalMillis(long intervalMillis) {
        this.intervalMillis = intervalMillis;
    }
}
