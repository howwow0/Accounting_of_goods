package org.how_wow.infrastructure.persistense;

import org.how_wow.application.dto.repository.PaginatedResult;
import org.how_wow.config.JDBCManager;
import org.how_wow.domain.model.Goods;
import org.how_wow.domain.repository.GoodsRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.Optional;

public class PostgresJDBCGoodsRepository implements GoodsRepository {

    @Override
    public PaginatedResult<Goods> findContainsByNameAndCategoryWithPaging(long pageNumber, long pageSize, String name, String category) {
        String sql = "SELECT * FROM goods WHERE name ILIKE ? AND category ILIKE ? ORDER BY id LIMIT ? OFFSET ?";
        try (Connection connection = JDBCManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, "%" + name + "%");
            statement.setString(2, "%" + category + "%");
            statement.setLong(3, pageSize);
            statement.setLong(4, (pageNumber - 1) * pageSize);
            ResultSet resultSet = statement.executeQuery();
            return getGoodsPaginatedResult(pageNumber, pageSize, resultSet);
        } catch (SQLException e) {
            throw new org.how_wow.exceptions.SQLException(e.getMessage());
        }
    }

    @Override
    public <S extends Goods> S save(S entity) {
        if (entity.getId() == null) {
            return create(entity);
        } else {
            return update(entity);
        }
    }

    private <S extends Goods> S create(S entity) {
        String sql = "INSERT INTO goods (name, category, quantity, price) VALUES (?, ?, ?, ?) RETURNING id";
        try (Connection connection = JDBCManager.getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, entity.getName());
                statement.setString(2, entity.getCategory());
                statement.setLong(3, entity.getQuantity());
                statement.setBigDecimal(4, entity.getPrice());
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    entity.setId(resultSet.getLong("id"));
                }
                connection.commit();
                return entity;
            } catch (SQLException e) {
                connection.rollback();
                throw new org.how_wow.exceptions.SQLException(e.getMessage());
            }
        } catch (SQLException e) {
            throw new org.how_wow.exceptions.SQLException(e.getMessage());
        }
    }

    private <S extends Goods> S update(S entity) {
        String sql = "UPDATE goods SET name = ?, category = ?, quantity = ?, price = ? WHERE id = ?";
        try (Connection connection = JDBCManager.getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, entity.getName());
                statement.setString(2, entity.getCategory());
                statement.setLong(3, entity.getQuantity());
                statement.setBigDecimal(4, entity.getPrice());
                statement.setLong(5, entity.getId());
                statement.executeUpdate();
                connection.commit();
                return entity;
            } catch (SQLException e) {
                connection.rollback();
                throw new org.how_wow.exceptions.SQLException(e.getMessage());
            }
        } catch (SQLException e) {
            throw new org.how_wow.exceptions.SQLException(e.getMessage());
        }
    }

    @Override
    public Optional<Goods> findById(Long aLong) {
        String sql = "SELECT * FROM goods WHERE id = ?";
        try (Connection connection = JDBCManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, aLong);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(mapRowToGoods(resultSet));
            }
        } catch (SQLException e) {
            throw new org.how_wow.exceptions.SQLException(e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public boolean existsById(Long aLong) {
        String sql = "SELECT COUNT(*) FROM goods WHERE id = ?";
        try (Connection connection = JDBCManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, aLong);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next() && resultSet.getInt(1) > 0;
        } catch (SQLException e) {
            throw new org.how_wow.exceptions.SQLException(e.getMessage());
        }
    }

    @Override
    public PaginatedResult<Goods> findAllWithPaging(long pageNumber, long pageSize) {
        String sql = "SELECT * FROM goods ORDER BY id LIMIT ? OFFSET ?";
        try (Connection connection = JDBCManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, pageSize);
            statement.setLong(2, (pageNumber - 1) * pageSize);
            ResultSet resultSet = statement.executeQuery();
            return getGoodsPaginatedResult(pageNumber, pageSize, resultSet);
        } catch (SQLException e) {
            throw new org.how_wow.exceptions.SQLException(e.getMessage());
        }
    }

    @Override
    public long count() {
        String sql = "SELECT COUNT(*) FROM goods";
        try (Connection connection = JDBCManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            return resultSet.next() ? resultSet.getLong(1) : 0;
        } catch (SQLException e) {
            throw new org.how_wow.exceptions.SQLException(e.getMessage());
        }
    }

    @Override
    public void deleteById(Long aLong) {
        String sql = "DELETE FROM goods WHERE id = ?";
        try (Connection connection = JDBCManager.getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setLong(1, aLong);
                statement.executeUpdate();
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                throw new org.how_wow.exceptions.SQLException(e.getMessage());
            }
        } catch (SQLException e) {
            throw new org.how_wow.exceptions.SQLException(e.getMessage());
        }
    }

    private PaginatedResult<Goods> getGoodsPaginatedResult(long pageNumber, long pageSize, ResultSet resultSet) throws SQLException {
        long totalItems = count();
        long totalPages = pageSize > 0 ? (totalItems + pageSize - 1) / pageSize : 0;
        PaginatedResult<Goods> paginatedResult = PaginatedResult.<Goods>builder()
                .totalPages(totalPages)
                .currentPage(pageNumber)
                .pageSize(pageSize)
                .totalItems(totalItems)
                .content(new ArrayList<>())
                .build();
        while (resultSet.next()) {
            paginatedResult.getContent().add(mapRowToGoods(resultSet));
        }
        return paginatedResult;
    }

    private Goods mapRowToGoods(ResultSet resultSet) throws SQLException {
        return Goods.builder()
                .id(resultSet.getLong("id"))
                .name(resultSet.getString("name"))
                .category(resultSet.getString("category"))
                .quantity(resultSet.getLong("quantity"))
                .price(resultSet.getBigDecimal("price"))
                .build();
    }
}