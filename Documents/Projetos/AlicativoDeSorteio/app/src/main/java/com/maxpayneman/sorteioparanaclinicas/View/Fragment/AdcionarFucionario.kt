package com.maxpayneman.sorteioparanaclinicas.View.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.maxpayneman.sorteioparanaclinicas.Model.Adapter.CostumeAdapter
import com.maxpayneman.sorteioparanaclinicas.Model.Adapter.SpinnerCostum
import com.maxpayneman.sorteioparanaclinicas.Model.Selecionado
import com.maxpayneman.sorteioparanaclinicas.ViewModel.ColaboradorViewModel
import com.maxpayneman.sorteioparanaclinicas.databinding.FragmentAdcionarFucionarioRemoverBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AdcionarFucionario.newInstance] factory method to
 * create an instance of this fragment.
 */
class AdcionarFucionario : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var _binding: FragmentAdcionarFucionarioRemoverBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ColaboradorViewModel by lazy {
        ViewModelProvider(requireActivity()).get(ColaboradorViewModel::class.java)
    }
    private lateinit var adap: CostumeAdapter

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        adap = CostumeAdapter(requireContext(), ArrayList(), viewModel)
    }


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
        _binding = FragmentAdcionarFucionarioRemoverBinding.inflate(inflater, container, false)
        val view = binding.root




        val items = listOf("Manhã", "Tarde", "Noite")
        val adapter = SpinnerCostum(requireContext(), items)
        binding.spinner.adapter = adapter

        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {


                binding.addFun.setOnClickListener {
                    var turno = parent?.getItemAtPosition(position).toString()
                    var nome = binding.nomeColaborador.text.toString()
                    var cracha = binding.cracha.text.toString()
                    if (cracha.isEmpty() || nome == null || nome.isEmpty() || nome == "" || nome.equals(Int) ) Toast.makeText(requireContext(), "Insira o nome do funcionario!", Toast.LENGTH_LONG).show()
                    else {

                        val selecionado = Selecionado(nome, turno,"", cracha)
                        viewModel.addSelecter(selecionado)
                        viewModel.listarColaborador()

                        fragmentManager?.beginTransaction()?.remove(this@AdcionarFucionario)
                            ?.commit()
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }


        binding.close.setOnClickListener {
            fragmentManager?.beginTransaction()?.remove(this@AdcionarFucionario)
                ?.commit()
        }

            // Inflate the layout for this fragment
            return view
        }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AdcionarFucionarioRemover.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AdcionarFucionario().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}