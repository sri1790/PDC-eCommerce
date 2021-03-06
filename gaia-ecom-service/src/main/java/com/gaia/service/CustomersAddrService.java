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
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.gaia.domain.CustomerAddrRowMapper;
import com.gaia.domain.CustomersAddrEntity;
import com.gaia.repository.CustomersAddrRepo;
import com.gaia.web.rest.vm.CustomerAddrResponse;

@Service
public class CustomersAddrService {

	@Autowired
	private CustomersAddrRepo repo;

	private String query = "SELECT a.`id` addressId,`customer_id` custId,`firstname`,`lastname`,`streetname`,a.`country_id` countryId,a.`region_id` regionId,a.`area_id` areaId,`postcode`,b.`name` country,c.`name` region,d.`name` areaname  FROM `customers_address` a JOIN `countries` b ON a.`country_id` = b.`id` JOIN `countries_regions` c ON c.`id` = a.`region_id` JOIN `countries_regions_areas` d ON a.`area_id` = d.`id` WHERE `customer_id` = '";

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public CustomersAddrEntity addCustomersAddr(CustomersAddrEntity request) {
		return repo.save(request);
	}

	public CustomersAddrEntity getCustomersAddr(Long id) {
		return repo.findById(id).orElse(null);
	}

	public Page<CustomersAddrEntity> getCustomersAddr(Map<String, Long> map, Pageable pageable, String firstname,
			String lastname, String streetname) {
		Specification<CustomersAddrEntity> spec = new Specification<CustomersAddrEntity>() {
			@Override
			public Predicate toPredicate(Root<CustomersAddrEntity> root, CriteriaQuery<?> query,
					CriteriaBuilder criteriaBuilder) {
				List<Predicate> predicate = new ArrayList<Predicate>();
				map.remove("active");

				if (firstname != null)
					predicate.add(criteriaBuilder.equal(root.get("firstname"), firstname));
				if (lastname != null)
					predicate.add(criteriaBuilder.equal(root.get("lastname"), lastname));
				if (streetname != null)
					predicate.add(criteriaBuilder.equal(root.get("streetname"), streetname));
				map.forEach((k, v) -> {
					predicate.add(criteriaBuilder.equal(root.get(k), v));
				});
				return criteriaBuilder.and(predicate.stream().toArray(Predicate[]::new));
			}

		};
		return repo.findAll(spec, pageable);

	}

	public void deleteCustomersAddr(Long id) {
		repo.deleteById(id);
	}

	public List<CustomerAddrResponse> getCustomersAddr(String custId) {
		List<CustomerAddrResponse> response = jdbcTemplate.query(query + custId + "'", new CustomerAddrRowMapper());

		return response;
	}

}
