package com.todo.repository;

import com.todo.entity.RefreshToken;
import org.springframework.data.repository.Repository;

public interface RefreshTokenRepository extends Repository<RefreshToken, Integer> {

	RefreshToken save(RefreshToken refreshToken);

	void deleteAll();

	void deleteAllByUsername(String username);

	void delete(RefreshToken refreshToken);

	RefreshToken findByToken(String token);
}
