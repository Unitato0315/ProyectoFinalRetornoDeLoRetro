package com.example.elretornodeloretro.io

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm

class GeneralFuntion {
    companion object {
        fun decodeJWT(token: String): Map<String, Any>? {
            try {
                val algorithm = Algorithm.HMAC256("BDPEK@")
                val verifier = JWT.require(algorithm).build()
                val jwt = verifier.verify(token)
                return jwt.claims
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return null
        }
    }
}