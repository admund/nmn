package me.admund.nmn.data.api

data class VaccineApiEntity(
    val administered: Long,
    val population: Long,
    val people_vaccinated: Long,
    val people_partially_vaccinated: Long
)
