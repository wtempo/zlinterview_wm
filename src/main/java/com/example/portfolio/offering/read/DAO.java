package com.example.portfolio.offering.read;

import java.util.List;

public interface DAO<K, D> {
  D getSingle(K customerId);
  List<D> get();
}
