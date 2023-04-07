package com.example.demo.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.common.entity.Seckill;

public interface SeckillRepository extends JpaRepository<Seckill, Long> {
	
	
}
