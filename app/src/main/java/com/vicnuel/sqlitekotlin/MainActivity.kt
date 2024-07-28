package com.vicnuel.sqlitekotlin

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.vicnuel.sqlitekotlin.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: ArrayAdapter<Utilizador>
    private var pos: Int = -1


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val db = DBHelper(this)

        val listaUtilizador = db.utilizadorListSelectAll()

        adapter = ArrayAdapter(
            this, android.R.layout.simple_list_item_1, listaUtilizador
        )

        binding.listView.adapter = adapter

        binding.listView.setOnItemClickListener { _, _, position, _ ->
            binding.textId.text = "ID: ${listaUtilizador[position].id}"
            binding.editUsername.setText(listaUtilizador[position].username)
            binding.editPassword.setText(listaUtilizador[position].password)
            pos = position
        }

        binding.buttonInsert.setOnClickListener {
            val username = binding.editUsername.text.toString()
            val password = binding.editPassword.text.toString()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this@MainActivity, "Preencha todos os campos", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            val res = db.utilizadorInsert(
                username,
                password
            )

            if (res > 0) {
                Toast.makeText(this@MainActivity, "Utilzador adicionado: $res", Toast.LENGTH_SHORT)
                    .show()
                listaUtilizador.add(Utilizador(res.toInt(), username, password))
                adapter.notifyDataSetChanged()
            } else {
                Toast.makeText(this@MainActivity, "Erro ao adicionar: $res", Toast.LENGTH_SHORT)
                    .show()
            }

            binding.textId.text = "ID:"
            binding.editUsername.setText("")
            binding.editPassword.setText("")
            pos = -1
        }

        binding.buttonUpdate.setOnClickListener {
            if (pos < 0) {
                Toast.makeText(this@MainActivity, "Selecine um utilizador:", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            val id = listaUtilizador[pos].id
            val username = binding.editUsername.text.toString()
            val password = binding.editPassword.text.toString()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this@MainActivity, "Preencha todos os campos", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            val res = db.utilizadorUpdate(
                id,
                username,
                password
            )

            if (res > 0) {
                Toast.makeText(this@MainActivity, "Utilzador atualizado: $id", Toast.LENGTH_SHORT)
                    .show()
                listaUtilizador[pos].username = username
                listaUtilizador[pos].password = password
                adapter.notifyDataSetChanged()
            } else {
                Toast.makeText(this@MainActivity, "Erro ao atualizar: $id", Toast.LENGTH_SHORT)
                    .show()
            }

            binding.textId.text = "ID:"
            binding.editUsername.setText("")
            binding.editPassword.setText("")
            pos = -1
        }

        binding.buttonDelete.setOnClickListener {
            if (pos < 0) {
                Toast.makeText(this@MainActivity, "Selecine um utilizador:", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            val id = listaUtilizador[pos].id
            val res = db.utilizadorDelete(id)

            if (res > 0) {
                Toast.makeText(this@MainActivity, "Utilzador removido: $id", Toast.LENGTH_SHORT)
                    .show()
                listaUtilizador.removeAt(pos)
                adapter.notifyDataSetChanged()
            } else {
                Toast.makeText(this@MainActivity, "Erro ao remover: $id", Toast.LENGTH_SHORT)
                    .show()
            }

            binding.textId.text = "ID:"
            binding.editUsername.setText("")
            binding.editPassword.setText("")
            pos = -1
        }


    }

}