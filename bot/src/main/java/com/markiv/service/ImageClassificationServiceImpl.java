package com.markiv.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.markiv.core.ClassificationLine;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class ImageClassificationServiceImpl implements ImageClassificationService {
  private final String imageServiceUrl;

  private static final ObjectMapper objectMapper = new ObjectMapper();

  @Autowired
  public ImageClassificationServiceImpl(@Value("${image.service.url}") String imageServiceUrl) {
    this.imageServiceUrl = imageServiceUrl;
  }

  @Override
  public HashMap<String, Float> classify(File imageFile) {
    ResponseEntity<String> responseEntity = postImage(imageFile);
    System.out.println(responseEntity.getBody());
    return jsonClassificationToProbabilityMap(responseEntity.getBody());
  }

  @Override
  public boolean isSafeForWork(File imageFile) {
    HashMap<String, Float> classProbabilityMap = classify(imageFile);
    System.out.println(classProbabilityMap.toString());
    float nsfwProbability = classProbabilityMap.get("Hentai") + classProbabilityMap.get("Porn");
    System.out.println(nsfwProbability);
    return nsfwProbability < 0.5;
  }

  private ResponseEntity<String> postImage(File imageFile) {
    FileSystemResource fileSystemResource = new FileSystemResource(imageFile);
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.MULTIPART_FORM_DATA);
    MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
    body.add("image", fileSystemResource);
    HttpEntity<MultiValueMap<String, Object>> requestEntity
      = new HttpEntity<>(body, headers);

    RestTemplate restTemplate = new RestTemplate();
    return restTemplate.postForEntity(imageServiceUrl, requestEntity, String.class);
  }

  private HashMap<String, Float> jsonClassificationToProbabilityMap(String json) {
    try {
      ClassificationLine[] classificationLines = objectMapper.readValue(json, ClassificationLine[].class);
      return (HashMap<String, Float>) Arrays.stream(classificationLines)
        .collect(Collectors.toMap(ClassificationLine::getClassName, ClassificationLine::getProbability));
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }
}
