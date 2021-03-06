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

import com.gaia.domain.ProductsStatusEntity;
import com.gaia.repository.ProductsStatusRepo;

@Service
public class ProductsStatusService {

	@Autowired
	private ProductsStatusRepo prodStatusRepo;

	public ProductsStatusEntity getProdStatus(Long id) {
		return prodStatusRepo.findById(id).orElse(null);
	}

	public Page<ProductsStatusEntity> getProdStatus(Map<String, Long> map, Pageable pageable) {
		Specification<ProductsStatusEntity> spec = new Specification<ProductsStatusEntity>() {

			@Override
			public Predicate toPredicate(Root<ProductsStatusEntity> root, CriteriaQuery<?> query,
					CriteriaBuilder criteriaBuilder) {
				List<Predicate> predicate = new ArrayList<Predicate>();

				map.forEach((k, v) -> {
					predicate.add(criteriaBuilder.equal(root.get(k), v));
				});

				return criteriaBuilder.and(predicate.stream().toArray(Predicate[]::new));
			}

		};
		return prodStatusRepo.findAll(spec, pageable);
	}

}
