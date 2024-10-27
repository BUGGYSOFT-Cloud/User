package com.buggysoft.user.response;

import java.util.Map;

public class AsyncResponse {
  private final String status;
  private final Map<String, Object> data;

  public AsyncResponse(String status, Map<String, Object> data) {
    this.status = status;
    this.data = data;
  }

  public String getStatus() {
    return status;
  }

  public Map<String, Object> getData() {
    return data;
  }
}