package com.example.springbootwithkotlin.common.entity

import java.io.Serializable
import org.hibernate.engine.spi.SharedSessionContractImplementor
import org.hibernate.id.IdentityGenerator

class IdOrGenerate : IdentityGenerator() {
    override fun generate(sharedSessionContractImplementor: SharedSessionContractImplementor, obj: Any): Serializable {
        val id = sharedSessionContractImplementor.getEntityPersister(null, obj).classMetadata.getIdentifier(obj, sharedSessionContractImplementor)
        return id ?: super.generate(sharedSessionContractImplementor, obj)
    }
}