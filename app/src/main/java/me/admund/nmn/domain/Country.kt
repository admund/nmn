package me.admund.nmn.domain

data class Country(
    val uid: Long,
    val name: String,
    val population: Long,
    val peopleVaccinated: Long,
    val peoplePartiallyVaccinated: Long,
    val isFavorite: Boolean
)
