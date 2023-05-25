package com.aimelodies.services

import com.aimelodies.configurations.settings.JwtSettings
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.AlgorithmMismatchException
import com.auth0.jwt.exceptions.IncorrectClaimException
import com.auth0.jwt.exceptions.JWTVerificationException
import com.auth0.jwt.exceptions.MissingClaimException
import com.auth0.jwt.exceptions.TokenExpiredException
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.security.SignatureException
import java.util.Date

@Service
class JwtService(
    private val jwtSettings: JwtSettings
) {
    private val algorithm = Algorithm.HMAC256(jwtSettings.secretKey)

    fun generate(userDetails: UserDetails): String =
        System.currentTimeMillis().let { currentMillis ->
            JWT.create()
                .withSubject(userDetails.username)
                .withArrayClaim(
                    "roles", arrayOf(userDetails.authorities.joinToString(separator = ",") { it.authority })
                )
                .withIssuedAt(Date(currentMillis))
                .withExpiresAt(Date(currentMillis + jwtSettings.duration.toMillis()))
                .withIssuer(jwtSettings.issuer)
                .sign(algorithm)
        }

    fun getUsername(token: String): String =
        JWT.decode(token).subject

    fun isValid(token: String, allowedExceptions: Array<Class<*>>? = null): Boolean = try {
        JWT
            .require(algorithm)
            .withIssuer(jwtSettings.issuer)
            .build()
            .verify(token)
        true
    } catch (algorithmMismatchException: AlgorithmMismatchException) {
        allowedExceptions?.contains(AlgorithmMismatchException::class.java) ?: false
    } catch (signatureException: SignatureException) {
        allowedExceptions?.contains(SignatureException::class.java) ?: false
    } catch (tokenExpiredException: TokenExpiredException) {
        allowedExceptions?.contains(TokenExpiredException::class.java) ?: false
    } catch (missingClaimException: MissingClaimException) {
        allowedExceptions?.contains(MissingClaimException::class.java) ?: false
    } catch (incorrectClaimException: IncorrectClaimException) {
        allowedExceptions?.contains(IncorrectClaimException::class.java) ?: false
    } catch (jwtVerificationException: JWTVerificationException) {
        allowedExceptions?.contains(JWTVerificationException::class.java) ?: false
    }
}