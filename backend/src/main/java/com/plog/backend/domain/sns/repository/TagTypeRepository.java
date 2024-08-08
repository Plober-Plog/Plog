package com.plog.backend.domain.sns.repository;

import com.plog.backend.domain.sns.entity.TagType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagTypeRepository extends JpaRepository<TagType, Long> {
}
