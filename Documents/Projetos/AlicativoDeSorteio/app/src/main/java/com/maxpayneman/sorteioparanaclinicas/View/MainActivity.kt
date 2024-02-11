package com.maxpayneman.sorteioparanaclinicas.View

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.maxpayneman.sorteioparanaclinicas.Model.Adapter.CostumeAdapter
import com.maxpayneman.sorteioparanaclinicas.Model.Adapter.CostumeAdapterPremio
import com.maxpayneman.sorteioparanaclinicas.Model.Colaborador
import com.maxpayneman.sorteioparanaclinicas.Model.Sorteio
import com.maxpayneman.sorteioparanaclinicas.R
import com.maxpayneman.sorteioparanaclinicas.View.Fragment.AdcionarFucionario
import com.maxpayneman.sorteioparanaclinicas.View.Fragment.AdicionarPremio
import com.maxpayneman.sorteioparanaclinicas.View.Fragment.ListaDeVencedores
import com.maxpayneman.sorteioparanaclinicas.View.Fragment.SelecionarFuncionario
import com.maxpayneman.sorteioparanaclinicas.View.Fragment.SelecionarPremio
import com.maxpayneman.sorteioparanaclinicas.ViewModel.ColaboradorViewModel
import com.maxpayneman.sorteioparanaclinicas.ViewModel.PremioViewModel
import com.maxpayneman.sorteioparanaclinicas.ViewModel.SorteioViewModel
import com.maxpayneman.sorteioparanaclinicas.databinding.ActivityMainBinding
import java.util.Random
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val db = FirebaseFirestore.getInstance()
    private var sortearChamado = 0
    var boolean = false
    var restore = true
    var colab = mutableListOf<Colaborador>()
    private val colaboradoresCollection = db.collection("colaboradores")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModelPremio = ViewModelProvider(this).get(PremioViewModel::class.java)
        var adapterPremio = CostumeAdapterPremio(this, ArrayList(), viewModelPremio)
        binding.listaPremio.adapter = adapterPremio
        viewModelPremio.minhaListaSelecionada.observe(this, Observer { premio ->
            adapterPremio.updateLista(premio)
        })

        setupObservers()

        val viewModel = ViewModelProvider(this).get(ColaboradorViewModel::class.java)
        var adapter = CostumeAdapter(this, java.util.ArrayList(), viewModel)
        binding.listItem.adapter = adapter

        val viewModelSorteio = ViewModelProvider(this).get(SorteioViewModel::class.java)

        binding.premio.visibility = View.GONE
        binding.listItem.visibility = View.VISIBLE
        binding.listaPremio.visibility = View.VISIBLE
        var valor = 0
        var voltas = 0
        var velocidade = 130

        Thread {
            do {
                runOnUiThread {

                    valor++

                    if (valor == 1) binding.reticiencias.text = "."
                    if (valor == 2) binding.reticiencias.text = ".."
                    if (valor == 3) binding.reticiencias.text = "..."
                    if (valor == 4) valor = 0
                }
                Thread.sleep(velocidade.toLong())
            } while (voltas <= 5)
        }.start()





        viewModel.toastMessage.observe(this, Observer { menssagem ->
            Toast.makeText(this, menssagem, Toast.LENGTH_SHORT).show()
        })

        viewModel.minhaLista.observe(this, Observer { colaborador ->
            adapter.updateLista(colaborador)
            viewModelPremio.minhaListaSelecionada.observe(this, Observer { premios ->


                binding.sortear.setOnClickListener {
                    binding.reticiencias.visibility = View.GONE
                    binding.reticiencias.width = ViewGroup.LayoutParams.WRAP_CONTENT
                    binding.nome.setPadding(0, 0, 0, 0)
                    binding.premio.visibility = View.VISIBLE
                    voltas = 5
                    sortearChamado = 1
                    boolean = true
                    if (restore == false) {
                        Toast.makeText(
                            this,
                            "Sem colaborador suficiente para sorteio ou premios!",
                            Toast.LENGTH_LONG
                        ).show()
                        startActivity(Intent(this, MainActivity::class.java))
                    }
                    if (colaborador.size == null || colaborador.isEmpty() || premios.size == 0 || premios.isEmpty()) {
                        Toast.makeText(
                            this,
                            "Sem colaborador suficiente para sorteio || e premios",
                            Toast.LENGTH_SHORT
                        ).show()
                        binding.nome.setTextSize(TypedValue.COMPLEX_UNIT_PX, 55f)
                        binding.nome.setTextColor(Color.BLUE)
                        binding.nome.text = "Aviso\n[Sem colaborador suficiente para sorteio]"
                        boolean = false
                    } else {
                        binding.nome.setTextSize(TypedValue.COMPLEX_UNIT_PX, 80f)
                        binding.nome.setTextColor(Color.GREEN)
                        Sortear(viewModel, adapter, adapterPremio, viewModelPremio,viewModelSorteio)
                    }
                }

            })
        })
        binding.AddPremio.setOnClickListener {
            replaceFragment(AdicionarPremio())

        }

        binding.selecionarPremio.setOnClickListener {

            replaceFragment(SelecionarPremio())

        }


        binding.addRemove.setOnClickListener {
            replaceFragment(AdcionarFucionario())

        }

        binding.Select.setOnClickListener {

            replaceFragment(SelecionarFuncionario())

        }

        binding.listarVencedor.setOnClickListener {
            replaceFragment(ListaDeVencedores())
        }

    }

    private fun setupObservers() {
        val viewModel = ViewModelProvider(this).get(ColaboradorViewModel::class.java)
        var adapter = CostumeAdapter(this, ArrayList(), viewModel)
        binding.listItem.adapter = adapter

        viewModel.minhaLista.observe(this, Observer { colaborador ->
            adapter.updateLista(colaborador)
        })
        viewModel.listarColaborador()
    }

    private fun setupObserverstest() {
        val viewModel = ViewModelProvider(this).get(PremioViewModel::class.java)
        var adapter = CostumeAdapterPremio(this, ArrayList(), viewModel)
        binding.listaPremio.adapter = adapter

        viewModel.minhaListaSelecionada.observe(this, Observer { colaborador ->
            adapter.updateLista(colaborador)
        })
    }

    // Variável booleana para controlar se a função Sortear já foi chamada

    private fun Sortear(
        viewModel: ColaboradorViewModel,
        adapter: CostumeAdapter,
        adapterPremio: CostumeAdapterPremio,
        viewModelPremio: PremioViewModel,
        viewModelSorteio: SorteioViewModel
    ) {

            viewModel.minhaLista.observe(this, Observer { colaborador ->
                binding.listItem.adapter = adapter
            viewModelPremio.minhaListaSelecionada.observe(this,Observer { premio ->
                binding.listaPremio.adapter = adapterPremio

                var valor = 0
                var qtd = colaborador.size
                var qtdPremio = premio.count()
                var velocidade = 30

                    Thread {
                        if (boolean == true) {
                            if (qtd == 1) {
                                binding.nome.setTextSize(
                                    TypedValue.COMPLEX_UNIT_PX,
                                    65f
                                )
                                do {
                                    runOnUiThread {
                                        var nomeVencedor = "Vencedor: ${colaborador[0].selecionado.nome} Cracha: ${colaborador[0].selecionado.cracha}"
                                        binding.nome.text = nomeVencedor
                                        binding.premio.text = premio[(valor) % premio.size].nome
                                        valor++

                                        if (valor == 101) {

                                            var random = Random()

                                            var premioRandom = random.nextInt(qtdPremio)
                                            var premioSorteado = premio[premioRandom].nome
                                            var premioSorteadoeliminar = premio[premioRandom].idPremio
                                            binding.premio.text = premioSorteado
                                            boolean = false

                                                viewModelPremio.deletarPremio(premioSorteadoeliminar)
                                                viewModel.deletarColaborador(colaborador[0].selecionado.idColaborador)
                                                var sorteio = Sorteio(colaborador[0],premio[premioRandom])
                                                viewModelSorteio.addSorteio(sorteio)

                                            var novalist = ArrayList<Colaborador>()
                                            val adapterBlank = ArrayAdapter(this, android.R.layout.simple_list_item_1, novalist)

                                            binding.listItem.adapter = adapterBlank
                                            restore = false

                                        }
                                    }
                                    Thread.sleep(velocidade.toLong())

                                } while (valor != 101)

                            } else {

                                do {
                                    runOnUiThread {
                                        binding.nome.text =
                                            colaborador[(valor) % colaborador.size].selecionado.nome

                                        binding.premio.text = premio[(valor) % premio.size].nome
                                        valor++

                                        if (valor == 101) { // Se todos os colaboradores foram sorteados e nenhum vencedor foi encontrado
                                            binding.nome.setTextSize(
                                                TypedValue.COMPLEX_UNIT_PX,
                                                65f
                                            )
                                            var random = Random()
                                            var vencedor = random.nextInt(qtd)
                                            var premioRandom = random.nextInt(qtdPremio)
                                            var venc = colaborador[vencedor].selecionado.nome
                                            var vencCracha =
                                                colaborador[vencedor].selecionado.cracha
                                            var premioSelect = premio[premioRandom].nome
                                            var excluir = premio[premioRandom].idPremio
                                            var apagar =
                                                colaborador[vencedor].selecionado.idColaborador
                                            binding.nome.text =
                                                "Vencedor: $venc cracha: $vencCracha"
                                            binding.premio.text = "Premio: $premioSelect"
                                            viewModelPremio.deletarPremio(excluir)
                                            viewModel.deletarColaborador(apagar)
                                            var sorteio = Sorteio(colaborador[vencedor],premio[premioRandom])
                                            viewModelSorteio.addSorteio(sorteio)
                                            setupObservers()
                                            setupObserverstest()

                                            boolean = false

                                        }
                                    }
                                    Thread.sleep(velocidade.toLong())
                                } while (valor != 101) // Continue até que um vencedor seja encontrado
                            }
                        }
                }.start()

            })
            })

    }

    private fun replaceFragment(fragment: Fragment) {
        Log.d("HomeFragment", "Replacing fragment with ${fragment.javaClass.simpleName}")
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout, fragment)
        fragmentTransaction.commit()
    }
    private fun replaceFragmentMaior(fragment: Fragment) {
        Log.d("HomeFragment", "Replacing fragment with ${fragment.javaClass.simpleName}")
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout, fragment)
        fragmentTransaction.commit()
    }
}
