package com.example.irrigation;

public class Irrigation {

    private String selectedCrop;
    private float waterFlowRate;
    private String selectedCoverageAreaType;
    private float coverageAreaValue;
    private int numberOfIrrigationWeeks;

    // TODO constructor

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


}
