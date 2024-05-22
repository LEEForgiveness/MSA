package com.eurekaclient.userservice.infrastructure;

import com.eurekaclient.userservice.domain.InterestCategory;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InterestCategoryRepository extends JpaRepository<InterestCategory, Long> {

    List<InterestCategory> findByUuid(String uuid);
}
