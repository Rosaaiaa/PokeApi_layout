package com.example.pokemon.model


data class Pokemon(
    val name: String,
    val height: Int,
    val weight: Int,
    val types: List<Type>,
    val sprites: Sprites // Novo campo para a imagem
)

data class Type(
    val type: TypeDetail
)

data class TypeDetail(
    val name: String
)

data class Sprites(
    val front_default: String? // URL da imagem do Pokémon
)