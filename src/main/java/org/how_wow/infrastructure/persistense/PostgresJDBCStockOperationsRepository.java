package org.how_wow.infrastructure.persistense;

import org.how_wow.application.dto.repository.PaginatedResult;
import org.how_wow.config.JDBCManager;
import org.how_wow.domain.enums.OperationType;
import org.how_wow.domain.model.StockOperations;
import org.how_wow.domain.repository.StockOperationsRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

public class PostgresJDBCStockOperationsRepository implements StockOperationsRepository {
    @Override
    public PaginatedResult<StockOperations> findByGoodsIdWithPaging(long goodsId, long pageNumber, long pageSize) {
        String sql = "SELECT * FROM stock_operations WHERE goods_id = ? ORDER BY id LIMIT ? OFFSET ?";
        try (Connection connection = JDBCManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, goodsId);
            statement.setLong(2, pageSize);
            statement.setLong(3, (pageNumber - 1) * pageSize);
            ResultSet resultSet = statement.executeQuery();
            return getStockOperationsPaginatedResult(pageNumber, pageSize, resultSet);
        } catch (SQLException e) {
            throw new org.how_wow.exceptions.SQLException(e.getMessage());
        }
    }

    @Override
    public void deleteAllByGoodsId(long goodsId) {
        String sql = "DELETE FROM stock_operations WHERE goods_id = ?";
        try (Connection connection = JDBCManager.getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setLong(1, goodsId);
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

    @Override
    public boolean existsByGoodsId(Long goodsId) {
        String sql = "SELECT COUNT(*) FROM stock_operations WHERE goods_id = ?";
        try (Connection connection = JDBCManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, goodsId);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next() && resultSet.getInt(1) > 0;
        } catch (SQLException e) {
            throw new org.how_wow.exceptions.SQLException(e.getMessage());
        }
    }

    @Override
    public PaginatedResult<StockOperations> findContainsByGoodsIdAndOperationTypeAndBetweenTimeDatesWithPaging(long goodsId, OperationType operationType, LocalDateTime startDateTime, LocalDateTime endDateTime, long pageNumber, long pageSize) {
        String sql = "SELECT * FROM stock_operations WHERE goods_id = ? AND operation_type = ? AND date_time BETWEEN ? AND ? ORDER BY id LIMIT ? OFFSET ?";
        try (Connection connection = JDBCManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, goodsId);
            statement.setString(2, operationType.name());
            statement.setTimestamp(3, java.sql.Timestamp.valueOf(startDateTime));
            statement.setTimestamp(4, java.sql.Timestamp.valueOf(endDateTime));
            statement.setLong(5, pageSize);
            statement.setLong(6, (pageNumber - 1) * pageSize);
            ResultSet resultSet = statement.executeQuery();
            return getStockOperationsPaginatedResult(pageNumber, pageSize, resultSet);
        } catch (SQLException e) {
            throw new org.how_wow.exceptions.SQLException(e.getMessage());
        }
    }

    @Override
    public <S extends StockOperations> S save(S entity) {
        if (entity.getId() == null) {
            return create(entity);
        } else {
            return update(entity);
        }
    }


    @Override
    public Optional<StockOperations> findById(Long aLong) {
        String sql = "SELECT * FROM stock_operations WHERE id = ?";
        try (Connection connection = JDBCManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, aLong);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                StockOperations stockOperation = mapRowToStockOperations(resultSet);
                return Optional.of(stockOperation);
            }
        } catch (SQLException e) {
            throw new org.how_wow.exceptions.SQLException(e.getMessage());
        }
        return Optional.empty();
    }


    @Override
    public boolean existsById(Long aLong) {
        String sql = "SELECT COUNT(*) FROM stock_operations WHERE id = ?";
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
    public PaginatedResult<StockOperations> findAllWithPaging(long pageNumber, long pageSize) {
        String sql = "SELECT * FROM stock_operations ORDER BY id LIMIT ? OFFSET ?";
        try (Connection connection = JDBCManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, pageSize);
            statement.setLong(2, (pageNumber - 1) * pageSize);
            ResultSet resultSet = statement.executeQuery();
            return getStockOperationsPaginatedResult(pageNumber, pageSize, resultSet);
        } catch (SQLException e) {
            throw new org.how_wow.exceptions.SQLException(e.getMessage());
        }
    }

    private PaginatedResult<StockOperations> getStockOperationsPaginatedResult(long pageNumber, long pageSize, ResultSet resultSet) throws SQLException {
        long totalItems = count();
        long totalPages = pageSize > 0 ? (totalItems + pageSize - 1) / pageSize : 0;
        PaginatedResult<StockOperations> paginatedResult = PaginatedResult.<StockOperations>builder()
                .totalPages(totalPages)
                .currentPage(pageNumber)
                .pageSize(pageSize)
                .totalItems(totalItems)
                .content(new ArrayList<>())
                .build();
        while (resultSet.next()) {
            paginatedResult.getContent().add(mapRowToStockOperations(resultSet));
        }
        return paginatedResult;
    }

    @Override
    public long count() {
        String sql = "SELECT COUNT(*) FROM stock_operations";
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
        String sql = "DELETE FROM stock_operations WHERE id = ?";
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

    private static StockOperations mapRowToStockOperations(ResultSet resultSet) throws SQLException {
        return StockOperations.builder()
                .id(resultSet.getLong("id"))
                .goodsId(resultSet.getLong("goods_id"))
                .operationType(OperationType.valueOf(resultSet.getString("operation_type")))
                .quantity(resultSet.getLong("quantity"))
                .operationDateTime(resultSet.getTimestamp("date_time").toLocalDateTime())
                .build();
    }

    private <S extends StockOperations> S update(S entity) {
        String sql = "UPDATE stock_operations SET goods_id = ?, operation_type = ?, quantity = ?, date_time = ? WHERE id = ?";
        try (Connection connection = JDBCManager.getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setLong(1, entity.getGoodsId());
                statement.setString(2, entity.getOperationType().name());
                statement.setLong(3, entity.getQuantity());
                statement.setTimestamp(4, java.sql.Timestamp.valueOf(entity.getOperationDateTime()));
                statement.setLong(5, entity.getId());
                statement.executeUpdate();
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                throw new org.how_wow.exceptions.SQLException(e.getMessage());
            }
        } catch (SQLException e) {
            throw new org.how_wow.exceptions.SQLException(e.getMessage());
        }
        return entity;
    }

    private <S extends StockOperations> S create(S entity) {
        String sql = "INSERT INTO stock_operations (goods_id, operation_type, quantity, date_time) VALUES (?, ?, ?, ?) RETURNING id";
        try (Connection connection = JDBCManager.getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setLong(1, entity.getGoodsId());
                statement.setString(2, entity.getOperationType().name());
                statement.setLong(3, entity.getQuantity());
                statement.setTimestamp(4, java.sql.Timestamp.valueOf(entity.getOperationDateTime()));
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    entity.setId(resultSet.getLong("id"));
                }
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                throw new org.how_wow.exceptions.SQLException(e.getMessage());
            }
        } catch (SQLException e) {
            throw new org.how_wow.exceptions.SQLException(e.getMessage());
        }
        return entity;
    }
}
