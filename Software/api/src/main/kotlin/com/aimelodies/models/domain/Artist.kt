package com.aimelodies.models.domain

import com.aimelodies.models.enumerations.Role
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import org.springframework.data.annotation.CreatedDate
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.Date

@Entity
@Table(name = "artist")
class Artist(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long = 0,

    @Column(name = "username", length = 50, unique = true)
    val uname: String,

    @Column(name = "email", length = 70, unique = true)
    var email: String,

    @Column(name = "password", length = 60)
    var passwd: String,

    @Column(name = "first_name", length = 70)
    var firstName: String? = null,

    @Column(name = "last_name", length = 70)
    var lastName: String? = null,

    @Column(name = "bio")
    var bio: String? = null,

    @Column(name = "image", length = 50)
    var image: String? = null,

    @CreatedDate
    @Column(name = "date_created",)
    val dateCreated: Date = Date(),

    @Enumerated(EnumType.STRING)
    @Column(name = "role_name", length = 30)
    val role: Role,

    @OneToMany(mappedBy = "artist")
    val albums: MutableSet<Album> = mutableSetOf()
) : UserDetails {
    override fun getAuthorities() = listOf(SimpleGrantedAuthority("ROLE_${role.name}"))

    override fun getPassword() = passwd

    override fun getUsername() = uname

    override fun isAccountNonExpired() = true

    override fun isAccountNonLocked() = true

    override fun isCredentialsNonExpired() = true

    override fun isEnabled() = true
}