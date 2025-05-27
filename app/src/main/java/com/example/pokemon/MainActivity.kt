package com.example.pokemon

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pokemon.api.Api
import com.example.pokemon.databinding.ActivityMainBinding
import com.example.pokemon.model.Pokemon
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import com.bumptech.glide.Glide

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://pokeapi.co/api/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(Api::class.java)

        binding.BuscarPokemon.setOnClickListener {
            val nomePokemon = binding.EditPokemon.text.toString().trim().lowercase()
            if (nomePokemon.isEmpty()) {
                Toast.makeText(this, "Digite um nome", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            retrofit.getPokemon(nomePokemon).enqueue(object : Callback<Pokemon> {
                override fun onResponse(call: Call<Pokemon>, response: Response<Pokemon>) {
                    if (response.isSuccessful) {
                        val pokemon = response.body()
                        val tipos = pokemon?.types?.joinToString(", ") { it.type.name }
                        val resultado = "Nome: ${pokemon?.name}\nAltura: ${pokemon?.height}\nPeso: ${pokemon?.weight}\nTipo(s): $tipos"
                        binding.Resultado.text = resultado

                        // Carregar a imagem
                        val imageUrl = pokemon?.sprites?.front_default
                        Glide.with(this@MainActivity)
                            .load(imageUrl)
                            .into(binding.imgPokemon)
                    } else {
                        Toast.makeText(applicationContext, "Pokémon não encontrado", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Pokemon>, t: Throwable) {
                    Toast.makeText(applicationContext, "Erro de conexão", Toast.LENGTH_SHORT).show()
                    t.printStackTrace()
                }
            })
        }
    }
}
