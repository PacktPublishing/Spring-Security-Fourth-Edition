package com.packtpub.springsecurity.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;


/**
 * {@link CalendarUser} is this applications notion of a user. It is good to use your own objects to interact with a
 * user especially in large applications. This ensures that as you evolve your security requirements (update Spring
 * Security, leverage new Spring Security modules, or even swap out security implementations) you can do so easily.
 *
 * @author bnasslahsen
 *
 */
@Document(collection="calendar_users")
public class CalendarUser implements Persistable<Integer>, Serializable {

    @Id
    private Integer id;

    private String firstName;
    private String lastName;
    private String email;
    private String password;

    @DBRef(lazy = false)
    private Set<Role> roles = new HashSet<>(5);

    public CalendarUser() {}

    @PersistenceConstructor
    public CalendarUser(Integer id, String email, String password, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    /**
     * Gets the email address for this user. When authenticating against this data directly, this is also used as the
     * username.
     *
     * @return
     */
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the first name of the user.
     *
     * @return
     */
    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Gets the id for this user. When creating a new user this should be null, otherwise it will be non-null.
     *
     * @return
     */
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Gets the last name of the user.
     *
     * @return
     */
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Gets the password for this user. In some instances, this password is not actually used. For example, when an in
     * memory authentication is used the password on the spring security User object is used.
     *
     * @return
     */
    @JsonIgnore
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Get the list of Roles for this CalendarUser
     * @return
     */
    public Set<Role> getRoles() {
        return roles;
    }
    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }





    // --- convenience methods ----------------------------------------------//

    /**
     * Gets the full name in a formatted fashion. Note in a real application a formatter may be more appropriate, but in
     * this application simplicity is more important.
     *
     * @return
     */
    @JsonIgnore
    @Transient
    public String getName() {
        return getEmail();
    }

    @JsonIgnore
    @Transient
    public void addRole(Role role){
        this.roles.add(role);
    }

    private Boolean persisted = Boolean.FALSE;

    @Override
    public boolean isNew() {
        return !persisted;
    }

    // --- override Object --------------------------------------------------//

    @Override
    public int hashCode() {
        //return HashCodeBuilder.reflectionHashCode(this);
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        //return EqualsBuilder.reflectionEquals(this, obj);
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        CalendarUser other = (CalendarUser) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }


    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }

    private static final long serialVersionUID = 8433999509932007961L;

}
