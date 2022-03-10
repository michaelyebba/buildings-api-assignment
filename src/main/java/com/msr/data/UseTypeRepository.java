package com.msr.data;

import com.msr.model.UseType;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * A JPA repository for use types
 */
public interface UseTypeRepository extends JpaRepository<UseType, Integer> {
}
