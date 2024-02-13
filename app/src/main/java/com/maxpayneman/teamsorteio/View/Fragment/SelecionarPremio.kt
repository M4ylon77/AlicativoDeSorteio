package com.maxpayneman.teamsorteio.View.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.maxpayneman.teamsorteio.Model.Adapter.CostumeAdapter
import com.maxpayneman.teamsorteio.Model.Adapter.SpinnerSelectPremio
import com.maxpayneman.teamsorteio.Model.Premio
import com.maxpayneman.teamsorteio.ViewModel.PremioViewModel
import com.maxpayneman.teamsorteio.databinding.FragmentSelecionarPremioBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SelecionarPremio.newInstance] factory method to
 * create an instance of this fragment.
 */
class SelecionarPremio : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var _binding: FragmentSelecionarPremioBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PremioViewModel by lazy {
        ViewModelProvider(requireActivity()).get(PremioViewModel::class.java)
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
        _binding = FragmentSelecionarPremioBinding.inflate(inflater, container, false)
        val view = binding.root
        // Inflate the layout for this fragment

        viewModel.minhaLista.observe(viewLifecycleOwner, Observer { selecionado ->
            val items = mutableListOf<Premio>()

            for (select in selecionado) {

                items.add(select)
            }
            if (selecionado.isEmpty()) {
                binding.recado.text = "não há premios para selecionar!"
            } else {
                val adapter = SpinnerSelectPremio(requireContext(), items)
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
                            var selecionado = parent?.getItemAtPosition(position) as Premio
                            val premio = Premio(selecionado.nome,selecionado.idPremio)
                            viewModel.selecionarPremio(premio)


                        }
                    }


                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        TODO("Not yet implemented")
                    }
                }
        })
        binding.close.setOnClickListener{
            fragmentManager?.beginTransaction()?.remove(this@SelecionarPremio)?.commit()
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
         * @return A new instance of fragment SelecionarPremio.
         */
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SelecionarPremio.
         */

        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SelecionarPremio().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}