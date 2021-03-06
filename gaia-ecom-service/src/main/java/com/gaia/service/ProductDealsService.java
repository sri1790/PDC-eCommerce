package com.gaia.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.gaia.domain.ProductDealsEntity;
import com.gaia.repository.ProductDealsRepo;

@Service
public class ProductDealsService {

	@Autowired
	private ProductDealsRepo productDealsRepo;

	public ProductDealsEntity getProductDeals(Long id) {
		return productDealsRepo.findById(id).orElse(null);
	}

	public Page<ProductDealsEntity> getProductDeals(Map<String, Long> map, String sku, Pageable pageable) {
		Specification<ProductDealsEntity> spec = new Specification<ProductDealsEntity>() {

			@Override
			public Predicate toPredicate(Root<ProductDealsEntity> root, CriteriaQuery<?> query,
					CriteriaBuilder criteriaBuilder) {
				List<Predicate> predicate = new ArrayList<Predicate>();

				map.forEach((k, v) -> {
					predicate.add(criteriaBuilder.equal(root.get(k), v));
				});

				if (sku != null)
					predicate.add(criteriaBuilder.equal(root.get("sku"), sku));

				return criteriaBuilder.and(predicate.stream().toArray(Predicate[]::new));
			}

		};
		return productDealsRepo.findAll(spec, pageable);
	}

}
