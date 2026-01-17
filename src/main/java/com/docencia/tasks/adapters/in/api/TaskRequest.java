package com.docencia.tasks.adapters.in.api;

public class TaskRequest {
  private String title;
  private String description;
  private Boolean completed;

  public TaskRequest() {}

  public String getTitle() { return title; }
  public String getDescription() { return description; }
  public Boolean getCompleted() { return completed; }

  public void setTitle(String title) { this.title = title; }
  public void setDescription(String description) { this.description = description; }
  public void setCompleted(Boolean completed) { this.completed = completed; }
}
