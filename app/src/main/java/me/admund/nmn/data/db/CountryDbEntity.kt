package me.admund.nmn.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import me.admund.nmn.domain.Country

@Entity(tableName = "countries")
data class CountryDbEntity(
    @PrimaryKey(autoGenerate = false) val uid: Long,
    val name: String,
    val population: Long,
    val peopleVaccinated: Long,
    val peoplePartiallyVaccinated: Long,
    val isFavorite: Boolean
) {
    fun toCountry() = Country(
        uid = uid,
        name = name,
        isFavorite = isFavorite,
        population = population,
        peopleVaccinated = peopleVaccinated,
        peoplePartiallyVaccinated = peoplePartiallyVaccinated
    )

    companion object {
        fun fromCountry(country: Country) =
            CountryDbEntity(
                uid = country.uid,
                name = country.name,
                isFavorite = country.isFavorite,
                population = country.population,
                peopleVaccinated = country.peopleVaccinated,
                peoplePartiallyVaccinated = country.peoplePartiallyVaccinated
            )
    }
}
