package com.packtpub.springsecurity.dataaccess;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.packtpub.springsecurity.domain.CalendarUser;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * A jdbc implementation of {@link CalendarUserDao}.
 *
 *  @author bnasslahsen
 */
@Repository
public class JdbcCalendarUserDao implements CalendarUserDao {

	// --- members ---

	/**
	 * The constant CALENDAR_USER_QUERY.
	 */
	private static final String CALENDAR_USER_QUERY = "select id, email, password, first_name, last_name from calendar_users where ";

	// --- constructors ---

	/**
	 * The constant CALENDAR_USER_MAPPER.
	 */
	private static final RowMapper<CalendarUser> CALENDAR_USER_MAPPER = new CalendarUserRowMapper("calendar_users.");

	// --- CalendarUserDao methods ---

	/**
	 * The Jdbc operations.
	 */
	private final JdbcOperations jdbcOperations;

	/**
	 * Instantiates a new Jdbc calendar user dao.
	 *
	 * @param jdbcOperations the jdbc operations
	 */
	public JdbcCalendarUserDao(JdbcOperations jdbcOperations) {
		if (jdbcOperations == null) {
			throw new IllegalArgumentException("jdbcOperations cannot be null");
		}
		this.jdbcOperations = jdbcOperations;
	}

	/**
	 * Gets user.
	 *
	 * @param id the id
	 * @return the user
	 */
	@Override
	@Transactional(readOnly = true)
	public CalendarUser getUser(int id) {
		return jdbcOperations.queryForObject(CALENDAR_USER_QUERY + "id = ?", CALENDAR_USER_MAPPER, id);
	}

	/**
	 * Find user by email calendar user.
	 *
	 * @param email the email
	 * @return the calendar user
	 */
	@Override
	@Transactional(readOnly = true)
	public CalendarUser findUserByEmail(String email) {
		if (email == null) {
			throw new IllegalArgumentException("email cannot be null");
		}
		try {
			return jdbcOperations.queryForObject(CALENDAR_USER_QUERY + "email = ?", CALENDAR_USER_MAPPER, email);
		}
		catch (EmptyResultDataAccessException notFound) {
			return null;
		}
	}

	// --- non-public static members ---

	/**
	 * Find users by email list.
	 *
	 * @param email the email
	 * @return the list
	 */
	@Override
	@Transactional(readOnly = true)
	public List<CalendarUser> findUsersByEmail(String email) {
		if (email == null) {
			throw new IllegalArgumentException("email cannot be null");
		}
		if ("".equals(email)) {
			throw new IllegalArgumentException("email cannot be empty string");
		}
		return jdbcOperations.query(CALENDAR_USER_QUERY + "email like ? order by id", CALENDAR_USER_MAPPER, email + "%");
	}

	/**
	 * Create user int.
	 *
	 * @param userToAdd the user to add
	 * @return the int
	 */
	@Override
	public int createUser(final CalendarUser userToAdd) {
		if (userToAdd == null) {
			throw new IllegalArgumentException("userToAdd cannot be null");
		}
		if (userToAdd.getId() != null) {
			throw new IllegalArgumentException("userToAdd.getId() must be null when creating a " + CalendarUser.class.getName());
		}
		KeyHolder keyHolder = new GeneratedKeyHolder();
		this.jdbcOperations.update(connection -> {
			PreparedStatement ps = connection.prepareStatement(
					"insert into calendar_users (email, password, first_name, last_name) values (?, ?, ?, ?)",
					new String[] { "id" });
			ps.setString(1, userToAdd.getEmail());
			ps.setString(2, userToAdd.getPassword());
			ps.setString(3, userToAdd.getFirstName());
			ps.setString(4, userToAdd.getLastName());
			return ps;
		}, keyHolder);
		return keyHolder.getKey().intValue();
	}

	/**
	 * Create a new RowMapper that resolves {@link CalendarUser}'s given a column label prefix. By allowing the prefix
	 * to be specified we can reuse the same {@link RowMapper} for joins in other tables.
	 *
	 * @author bnasslahsen
	 */
	static class CalendarUserRowMapper implements RowMapper<CalendarUser> {
		/**
		 * The Column label prefix.
		 */
		private final String columnLabelPrefix;

		/**
		 * Creates a new instance that allows for a custom prefix for the columnLabel.
		 *
		 * @param columnLabelPrefix the column label prefix
		 */
		public CalendarUserRowMapper(String columnLabelPrefix) {
			this.columnLabelPrefix = columnLabelPrefix;
		}

		/**
		 * Map row calendar user.
		 *
		 * @param rs     the rs
		 * @param rowNum the row num
		 * @return the calendar user
		 * @throws SQLException the sql exception
		 */
		public CalendarUser mapRow(ResultSet rs, int rowNum) throws SQLException {
			return new CalendarUser(rs.getInt(columnLabelPrefix + "id"), rs.getString(columnLabelPrefix + "first_name"),
					rs.getString(columnLabelPrefix + "last_name"), rs.getString(columnLabelPrefix + "email"),
					rs.getString(columnLabelPrefix + "password"));
		}
	}

}