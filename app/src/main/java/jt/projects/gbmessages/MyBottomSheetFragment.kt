package jt.projects.gbmessages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import jt.projects.gbmessages.databinding.FragmentBottomSheetBinding

class MyBottomSheetFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentBottomSheetBinding? = null
    private val binding get() = _binding!!

    private var info: IInformative? = null

    companion object {
        const val TAG = "MyBottomSheetFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        info = context as? IInformative
        _binding = FragmentBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        info?.setBlurEffect(true)
        binding.imageViewBottomSheet.setOnClickListener {
            Toast.makeText(requireContext(), "Cool!", Toast.LENGTH_SHORT).show()
            dismiss()
        }
    }

    override fun onDestroy() {
        info?.setBlurEffect(false)
        _binding = null
        super.onDestroy()
    }
}