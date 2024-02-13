package com.maxpayneman.teamsorteio.View.Fragment

import android.app.DatePickerDialog
import android.content.Intent
import android.icu.util.Calendar
import android.media.MediaScannerConnection
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.maxpayneman.teamsorteio.Model.Adapter.ListVencedorCostum
import com.maxpayneman.teamsorteio.Model.Sorteio
import com.maxpayneman.teamsorteio.ViewModel.SorteioViewModel
import com.maxpayneman.teamsorteio.databinding.FragmentListaDeVencedoresBinding
import org.apache.poi.ss.usermodel.BorderStyle
import org.apache.poi.ss.usermodel.FillPatternType
import org.apache.poi.ss.usermodel.HorizontalAlignment
import org.apache.poi.ss.usermodel.IndexedColors
import org.apache.poi.ss.usermodel.WorkbookFactory
import org.apache.poi.ss.util.CellRangeAddress
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ListaDeVencedores.newInstance] factory method to
 * create an instance of this fragment.
 */
class ListaDeVencedores : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var _binding: FragmentListaDeVencedoresBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SorteioViewModel by lazy {
        ViewModelProvider(requireActivity()).get(SorteioViewModel::class.java)
    }
    var Data = ""


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
        _binding = FragmentListaDeVencedoresBinding.inflate(inflater,container,false)
        val view = binding.root


        viewModel.minhaLista.observe(viewLifecycleOwner, Observer {vencedores ->


            val adapter = ListVencedorCostum(requireContext(), vencedores)

            binding.listarVencedorLista.adapter = adapter

        })


        binding.salvarResult.setOnClickListener {


            if (Data.isEmpty()){
                Toast.makeText(requireContext(), "Insira a data do sorteio!", Toast.LENGTH_SHORT).show()
            }else{
                viewModel.minhaLista.observe(viewLifecycleOwner,Observer{Lista ->
                    saveDataToExcel(Lista, Data)
                })
            }
        }


        binding.voltar.setOnClickListener {
            fragmentManager?.beginTransaction()?.remove(this@ListaDeVencedores)?.commit()
        }

        binding.calendarButton.setOnClickListener {
            showDatePickerDialog { selectedDate ->
                Data = selectedDate
            }
        }

        return view
    }

    fun showDatePickerDialog(callback: (String) -> Unit) {
        val editTextDate = binding.editTextDate
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                editTextDate.setText(selectedDate)
                callback("${selectedDay}_${selectedMonth + 1}_$selectedYear") // Chame a função de callback com a data selecionada
            },
            year,
            month,
            day
        )
        datePickerDialog.show()
    }

    private fun saveDataToExcel(dataList: MutableList<Sorteio>, Datas: String) {
        val workbook = WorkbookFactory.create(true)
        val sheet = workbook.createSheet("Sorteio_$Datas")


        val yellowStyle = workbook.createCellStyle().apply {
            fillForegroundColor = IndexedColors.YELLOW.index
            fillPattern = FillPatternType.SOLID_FOREGROUND
            alignment = HorizontalAlignment.CENTER
            val borderStyle = BorderStyle.THIN
            borderTop = borderStyle
            borderBottom = borderStyle
            borderRight = borderStyle
            borderLeft = borderStyle



        }


        val headerRow = sheet.createRow(0)
        val headerStyle = workbook.createCellStyle().apply {
            fillForegroundColor = IndexedColors.DARK_RED.index
            fillPattern = FillPatternType.SOLID_FOREGROUND
            setFont(workbook.createFont().apply {
                color = IndexedColors.WHITE.index
            })
            alignment = HorizontalAlignment.CENTER
            val borderStyle = BorderStyle.THIN

            borderTop = borderStyle
            borderBottom = borderStyle
            borderRight = borderStyle
            borderLeft = borderStyle

        }

        headerRow.createCell(0).apply {
            setCellValue("Nome")
            cellStyle = headerStyle

        }

        headerRow.createCell(1).apply {
            setCellValue("Crachá")
            cellStyle = headerStyle

        }

        headerRow.createCell(2).apply {
            setCellValue("Prêmio")
            cellStyle = headerStyle
            sheet.addMergedRegion(CellRangeAddress(0, 0, 2, 5))
        }
       headerRow.createCell(3).apply {
            cellStyle = yellowStyle
        }
        headerRow.createCell(4).apply {
            cellStyle = yellowStyle
        }
        headerRow.createCell(5).apply {
            cellStyle = yellowStyle
        }

        for (i in dataList.indices) {
            sheet.addMergedRegion(CellRangeAddress(i+1, i+1, 2, 5))

            val row = sheet.createRow(i+1)
            row.createCell(0).apply {
             setCellValue(dataList[i].colaborador.selecionado.nome)
                cellStyle = yellowStyle
            }
            row.createCell(1).apply {
                setCellValue(dataList[i].colaborador.selecionado.cracha)
                cellStyle = yellowStyle

            }
            row.createCell(2).apply {
                setCellValue(dataList[i].premio.nome)
                cellStyle = yellowStyle
            }
            row.createCell(3).apply {
                cellStyle = yellowStyle
            }
            row.createCell(4).apply {
                cellStyle = yellowStyle
            }
            row.createCell(5).apply {
                cellStyle = yellowStyle
            }

        }

        sheet.setColumnWidth(0, 20 * 256) // Definir a largura da primeira coluna em 20 caracteres
        sheet.setColumnWidth(1, 15 * 256)

        // Caminho para a pasta de downloads
        val downloadsFolder =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)

        // Salvar o workbook em um arquivo XLSX na pasta de downloads
        val file = File(downloadsFolder, "Sorteio_$Datas.xlsx")
        try {
            FileOutputStream(file).use { outputStream ->
                workbook.write(outputStream)
            }

            // Notificar o sistema de que um novo arquivo foi criado
            MediaScannerConnection.scanFile(
                requireContext(),
                arrayOf(file.toString()),
                arrayOf("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"),
                null
            )
            val file = File(requireContext().getExternalFilesDir(null), "Sorteio_$Datas.xlsx")
            try {
                FileOutputStream(file).use { outputStream ->
                    workbook.write(outputStream)
                }
                val fileUri = FileProvider.getUriForFile(
                    requireContext(),
                    "${requireContext().packageName}.provider",
                    file
                )

                // Criar uma intenção para compartilhar o arquivo
                val intent = Intent(Intent.ACTION_SEND).apply {
                    type = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
                    putExtra(Intent.EXTRA_STREAM, fileUri)
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                }

                // Iniciar a intenção para compartilhar o arquivo
                startActivity(Intent.createChooser(intent, "Compartilhar Arquivo Excel"))

                Toast.makeText(
                    requireContext(),
                    "Arquivo de Sorteio$Datas salvo com sucesso na pasta de downloads",
                    Toast.LENGTH_LONG
                ).show()

                binding.editTextDate.text.clear()
                Data = ""

            } catch (e: IOException) {
                e.printStackTrace()
                Toast.makeText(
                    requireContext(),
                    "Erro ao salvar arquivo Excel XLSX: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }catch(e: IOException) {
            e.printStackTrace()
            Toast.makeText(
                requireContext(),
                "Erro ao salvar arquivo Excel XLSX: ${e.message}",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ListaDeVencedores.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ListaDeVencedores().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}