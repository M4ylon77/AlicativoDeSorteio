package com.maxpayneman.teamsorteio.View.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.maxpayneman.teamsorteio.Model.Adapter.CostumeAdapter
import com.maxpayneman.teamsorteio.Model.Premio
import com.maxpayneman.teamsorteio.ViewModel.PremioViewModel
import com.maxpayneman.teamsorteio.databinding.FragmentAdicionarPremioBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AdicionarPremio.newInstance] factory method to
 * create an instance of this fragment.
 */
class AdicionarPremio : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var _binding: FragmentAdicionarPremioBinding? = null
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
        _binding = FragmentAdicionarPremioBinding.inflate(inflater, container, false)
        val view = binding.root

        viewModel.toastMessage.observe(viewLifecycleOwner,Observer{mensagem ->
            Toast.makeText(requireContext(), mensagem, Toast.LENGTH_SHORT).show()
        })


        binding.AddPremio.setOnClickListener {
            var nomePremio = binding.nomePremio.text.toString()
            if (nomePremio.isNotEmpty()){

                val premio = Premio(nomePremio, "")
                viewModel.addPremio(premio)
                fragmentManager?.beginTransaction()?.remove(this@AdicionarPremio)
                    ?.commit()

            }else{
                Toast.makeText(requireContext(), "Campo Vazio adicione o nome do premio", Toast.LENGTH_LONG).show()
            }


        }
        binding.close.setOnClickListener{
            fragmentManager?.beginTransaction()?.remove(this@AdicionarPremio)?.commit()
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
         * @return A new instance of fragment AdicionarPremio.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AdicionarPremio().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}