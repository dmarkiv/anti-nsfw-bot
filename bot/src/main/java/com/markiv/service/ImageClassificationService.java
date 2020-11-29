package com.markiv.service;

import java.io.File;
import java.util.HashMap;
import java.util.List;

public interface ImageClassificationService {
  HashMap<String, Float> classify(File imageFile);

  boolean isSafeForWork(File imageFile);

  default boolean isSafeForWork(List<File> imageFiles) {
    return imageFiles.stream().allMatch(this::isSafeForWork);
  }
}
