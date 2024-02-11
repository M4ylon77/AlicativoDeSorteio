package com.maxpayneman.sorteioparanaclinicas.View.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.maxpayneman.sorteioparanaclinicas.Model.Adapter.CostumeAdapter
import com.maxpayneman.sorteioparanaclinicas.Model.Adapter.SpinnerSelect
import com.maxpayneman.sorteioparanaclinicas.Model.Colaborador
import com.maxpayneman.sorteioparanaclinicas.Model.Selecionado
import com.maxpayneman.sorteioparanaclinicas.ViewModel.ColaboradorViewModel
import com.maxpayneman.sorteioparanaclinicas.databinding.FragmentSelecionarFuncionarioBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SelecionarFuncionario.newInstance] factory method to
 * create an instance of this fragment.
 */
class SelecionarFuncionario : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var _binding: FragmentSelecionarFuncionarioBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ColaboradorViewModel by lazy {
        ViewModelProvider(requireActivity()).get(ColaboradorViewModel::class.java)
    }
    private lateinit var adap: CostumeAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSelecionarFuncionarioBinding.inflate(inflater, container, false)
        val view = binding.root
        viewModel.listaSelecionaveis()
        viewModel.minhaListaSelecionado.observe(viewLifecycleOwner, Observer { selecionado ->
            val items = mutableListOf<Selecionado>()

            for (select in selecionado) {

                items.add(select)
            }
            if (selecionado.isEmpty()){
                binding.recado.text = "não ha funcionarios para selecionar!"
            }else {
                val adapter = SpinnerSelect(requireContext(), items)
                binding.spinnerSelect.adapter = adapter
            }
                binding.spinnerSelect.onItemSelectedListener =
                    object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            parent: AdapterView<*>?,
                            view: View?,
                            position: Int,
                            id: Long
                        ) {

                            binding.addFun.setOnClickListener {
                                var selecionado = parent?.getItemAtPosition(position) as Selecionado
                                val colaborador = Colaborador(selecionado)
                                viewModel.cadastrarUsuario(colaborador)


                            }


                        }

                            override fun onNothingSelected(parent: AdapterView<*>?) {
                                TODO("Not yet implemented")
                            }
                        }

        })
        binding.close.setOnClickListener {
            fragmentManager?.beginTransaction()
                ?.remove(this@SelecionarFuncionario)
                ?.commit()

        }
        return view

    }



    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SelecionarFuncionario.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SelecionarFuncionario().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}



