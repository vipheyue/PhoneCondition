package com.vipheyue.phonecondition.ui.home

import android.content.ClipData
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.vipheyue.phonecondition.R


import android.content.ClipboardManager
import android.widget.Toast
import com.vipheyue.phonecondition.databinding.FragmentHomeBinding
import com.vipheyue.phonecondition.utils.Config
import com.vipheyue.phonecondition.utils.DataStoreUtils

class HomeFragment : Fragment() {

	private var _binding: FragmentHomeBinding? = null

	// This property is only valid between onCreateView and
	// onDestroyView.
	private val binding get() = _binding!!

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		val homeViewModel =
			ViewModelProvider(this).get(HomeViewModel::class.java)

		_binding = com.vipheyue.phonecondition.databinding.FragmentHomeBinding.inflate(inflater, container, false)
		val root: View = binding.root

		val textView: TextView = binding.textHome
		homeViewModel.text.observe(viewLifecycleOwner) {
			textView.text = it
		}

		root.findViewById<Button>(R.id.button).setOnClickListener {
			copyToClipboard(requireActivity(),textView.text.toString())
			Toast.makeText(requireActivity(), "已复制 去微信吧", Toast.LENGTH_SHORT).show();
		}


//		DataStoreUtils.saveSyncData(Config.LAST_SEND_TIME, 0) // 重置发送

		homeViewModel.getTodayRecord()



		return root
	}


	// 复制文本到剪贴板
	fun copyToClipboard(context: Context, text: String) {
		val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
		val clip = ClipData.newPlainText("label", text)
		clipboard.setPrimaryClip(clip)
	}

	override fun onResume() {
		super.onResume()
	}

	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}
}