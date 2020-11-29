package com.markiv.core;

public class ClassificationLine {
  String className;
  float probability;

  public ClassificationLine() {
  }

  public ClassificationLine(String className, float probability) {
    this.className = className;
    this.probability = probability;
  }

  public String getClassName() {
    return className;
  }

  public void setClassName(String className) {
    this.className = className;
  }

  public float getProbability() {
    return probability;
  }

  public void setProbability(float probability) {
    this.probability = probability;
  }
}
