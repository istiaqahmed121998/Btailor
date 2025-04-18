package com.backend.productservice.application.category.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CategoryDTO(@JsonProperty("name") String CategoryName,@JsonProperty("parent_name") String ParentCategoryName){}
