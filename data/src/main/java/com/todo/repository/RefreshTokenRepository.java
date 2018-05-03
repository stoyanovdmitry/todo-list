package com.todo.repository;

import com.todo.entity.RefreshToken;
import org.springframework.data.repository.Repository;

import javax.transaction.Transactional;

public interface RefreshTokenRepository extends Repository<RefreshToken, Integer> {

	@Transactional
	RefreshToken save(RefreshToken refreshToken);

	@Transactional
	void deleteAll();

	@Transactional
	void deleteAllByUserUsername(String username);

	@Transactional
	void delete(int id);

	@Transactional
	RefreshToken findByToken(String token);
}
