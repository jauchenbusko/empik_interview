package com.yb.empik.task.interview.jpa;

import com.yb.empik.task.interview.entity.UserDataJpa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDataRepository extends JpaRepository<UserDataJpa, String> {
}
