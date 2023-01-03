package com.nextsol.khangbb.model;

import lombok.Data;

import java.util.List;

@Data
public class ScreenDTO {
	private List<String> urls;
	private Long userId;
}
