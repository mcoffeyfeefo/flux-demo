package com.mattcoffey.demo.dto;

import java.util.List;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class SupplyDepot {
  private String name;
  private List<Supply> supplies;
}
