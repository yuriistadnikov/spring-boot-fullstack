package com.yuriist.customer;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository("jdbc")
public class CustomerJDBCDataAccessService implements CustomerDao {

    private final JdbcTemplate jdbcTemplate;
    private final CustomerRowMapper customerRowMapper;

    public CustomerJDBCDataAccessService(JdbcTemplate jdbcTemplate, CustomerRowMapper customerRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.customerRowMapper = customerRowMapper;
    }

    @Override
    public List<Customer> getAllCustomers() {
        String sql = """
                SELECT id, name, email, age, gender
                FROM customer;
                """;
        return jdbcTemplate.query(sql, customerRowMapper);
    }

    @Override
    public Optional<Customer> getCustomerById(Long customerId) {
        String sql = """
                SELECT id, name, email, age, gender
                FROM customer
                WHERE id = ?;
                """;
        return jdbcTemplate.query(sql, customerRowMapper, customerId)
                .stream()
                .findFirst();
    }

    @Override
    public void insertCustomer(Customer customer) {
        String sql = """
                INSERT INTO customer (name, email, age, gender)
                VALUES (?, ?, ?, ?)
                """;
        int result = jdbcTemplate.update(
                sql,
                customer.getName(),
                customer.getEmail(),
                customer.getAge(),
                customer.getGender().toString()
        );
        System.out.println("jdbcTemplate.update = " + result);
    }

    @Override
    public boolean existsPersonWithEmail(String email) {
        String sql = """
                SELECT count(id)
                FROM customer
                WHERE email = ?;
                """;
        Integer nubOfCustomers = jdbcTemplate.queryForObject(sql, Integer.class, email);
        return Objects.nonNull(nubOfCustomers) && nubOfCustomers > 0;
    }

    @Override
    public boolean existPersonWithId(Long id) {
        String sql = """
                SELECT count(id)
                FROM customer
                WHERE id = ?;
                """;
        Integer nubOfCustomers = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return Objects.nonNull(nubOfCustomers) && nubOfCustomers > 0;
    }

    @Override
    public void deleteCustomerById(Long id) {
        String sql = """
                DELETE
                FROM customer
                WHERE id = ?;
                """;
        int result = jdbcTemplate.update(sql, id);
        System.out.println("jdbcTemplate.update = " + result);
    }

    @Override
    public void updateCustomer(Customer customer) {
        if (Objects.nonNull(customer.getName())) {
            String sql = """
                    UPDATE customer
                    SET name = ?
                    WHERE id = ?;
                    """;
            jdbcTemplate.update(sql, customer.getName(), customer.getId());
        }

        if (Objects.nonNull(customer.getEmail())) {
            String sql = """
                    UPDATE customer
                    SET email = ?
                    WHERE id = ?;
                    """;
            jdbcTemplate.update(sql, customer.getEmail(), customer.getId());
        }

        if (Objects.nonNull(customer.getAge())) {
            String sql = """
                    UPDATE customer
                    SET age = ?
                    WHERE id = ?;
                    """;
            jdbcTemplate.update(sql, customer.getAge(), customer.getId());
        }

        if (Objects.nonNull(customer.getGender())) {
            String sql = """
                    UPDATE customer
                    SET gender = ?
                    WHERE id = ?;
                    """;
            jdbcTemplate.update(sql, customer.getGender().toString(), customer.getId());
        }
    }
}
